package br.com.finalcraft.finaleconomy.common.data;

import br.com.finalcraft.everyconfig.config.section.ConfigSection;
import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.playerdata.PDSection;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.everydatabase.query.Indexed;
import br.com.finalcraft.everydatabase.query.Query;
import br.com.finalcraft.everydatabase.query.QueryOptions;
import br.com.finalcraft.finaleconomy.common.config.ConfigManager;
import br.com.finalcraft.finaleconomy.common.event.EconomyUpdateEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A player's balance - the only data this plugin owns.
 *
 * <p>Registered {@code PRELOADED} (see {@code PlayerDataRegistry}), because the Vault economy this
 * plugin implements is a synchronous third-party interface: it must answer for an OFFLINE player,
 * on the server thread, without touching storage.</p>
 *
 * <p><b>The balance is a {@link BigDecimal}</b>, so cents add up exactly and a long chain of small
 * transactions cannot drift the way binary floating point does.</p>
 */
public class FEPlayerData extends PDSection {

    /** Money is counted to the cent; every stored balance is rounded to this scale. */
    public static final int SCALE = 2;

    private BigDecimal money = BigDecimal.ZERO;

    /**
     * The balance as a plain double, kept in step with {@link #money} for one purpose: ordering the
     * ranking inside the backend. The storage index knows only the types in
     * {@code IndexHint.FieldType}, whose widest number is DOUBLE, so a {@code BigDecimal} cannot
     * carry the index itself. Ordering by an approximation is harmless - two balances close enough
     * for the rounding to swap them are close enough for either order to read the same - while the
     * authoritative value stays exact. Drop this field once the storage layer indexes decimals.
     */
    @Indexed
    private double moneyIndex = 0D;

    public FEPlayerData() {
        // Required no-arg constructor (Jackson + transient default seeding).
    }

    public BigDecimal getMoney() {
        return money;
    }

    public boolean hasMoney(BigDecimal amount) {
        return money.compareTo(amount) >= 0;
    }

    public void addMoney(BigDecimal amount) {
        applyBalance(money.add(amount));
    }

    public void removeMoney(BigDecimal amount) {
        applyBalance(money.subtract(amount));
    }

    public void setMoney(BigDecimal amount) {
        applyBalance(amount);
    }

    /** The balance rendered the way {@code Settings.MoneyFormat.locale} asks for. */
    public String getMoneyFormatted() {
        return ConfigManager.settings.getMoneyFormat().format(money);
    }

    /**
     * A balance never goes below zero, whatever the caller asked to subtract. Announces the change
     * only when there is one, and only when somebody is listening.
     */
    private void applyBalance(BigDecimal newBalance) {
        BigDecimal rounded = newBalance.setScale(SCALE, RoundingMode.HALF_UP).max(BigDecimal.ZERO);

        if (rounded.compareTo(money) == 0) {
            return;
        }

        BigDecimal previous = money;
        money = rounded;
        moneyIndex = rounded.doubleValue();
        markDirty();

        EverNifeCore.getEventBus().postIfListened(EconomyUpdateEvent.class,
                () -> new EconomyUpdateEvent(getUniqueId(), previous, rounded));
    }

    /**
     * Reads the balance out of the 2.x per-player YAML subtree this plugin used to own
     * ({@code FinalEconomy: money: <n>}), for the one-time import of a first boot.
     */
    public static FEPlayerData fromLegacy(ConfigSection legacy) {
        FEPlayerData section = new FEPlayerData();
        section.money = BigDecimal.valueOf(Math.max(0D, legacy.getDouble("money", 0D)))
                .setScale(SCALE, RoundingMode.HALF_UP);
        section.moneyIndex = section.money.doubleValue();
        return section;
    }

    /**
     * Every player ordered by balance, richest first, ordered in the BACKEND - no player is loaded
     * into the cache by this call. Runs async; the rows come back DETACHED, so they answer
     * {@code getUniqueId()} but not {@code getName()}.
     *
     * @param limit how many rows to bring back, or zero/less for all of them
     */
    public static CompletableFuture<List<FEPlayerData>> rankedByBalance(int limit) {
        QueryOptions.Builder options = QueryOptions.builder().descending("moneyIndex");
        if (limit > 0) {
            options.limit(limit);
        }
        return PlayerController.get().querySection(FEPlayerData.class, Query.all(), options.build());
    }
}
