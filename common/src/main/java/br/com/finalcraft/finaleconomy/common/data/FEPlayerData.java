package br.com.finalcraft.finaleconomy.common.data;

import br.com.finalcraft.everyconfig.config.section.ConfigSection;
import br.com.finalcraft.evernifecore.playerdata.PDSection;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.everydatabase.query.Indexed;
import br.com.finalcraft.everydatabase.query.Query;
import br.com.finalcraft.everydatabase.query.QueryOptions;
import br.com.finalcraft.everylibs.util.FCMathUtil;
import br.com.finalcraft.finaleconomy.common.config.ConfigManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A player's balance - the only data this plugin owns.
 *
 * <p>Registered {@code PRELOADED} (see {@code PlayerDataRegistry}), because the Vault economy this
 * plugin implements is a synchronous third-party interface: it must answer for an OFFLINE player,
 * on the server thread, without touching storage.</p>
 */
public class FEPlayerData extends PDSection {

    /** Indexed so the ranking is one ordered backend query instead of a scan over every player. */
    @Indexed
    private double money = 0D;

    public FEPlayerData() {
        // Required no-arg constructor (Jackson + transient default seeding).
    }

    public double getMoney() {
        return money;
    }

    public boolean hasMoney(double amount) {
        return money >= amount;
    }

    public void addMoney(double amount) {
        applyBalance(money + amount);
    }

    public void removeMoney(double amount) {
        applyBalance(money - amount);
    }

    public void setMoney(double amount) {
        applyBalance(amount);
    }

    /** The balance rendered with the format {@code Settings.moneyFormatLocale} asks for. */
    public String getMoneyFormatted() {
        return ConfigManager.settings.getMoneyFormatter().format(FCMathUtil.normalizeDouble(money));
    }

    /** A balance never goes below zero, whatever the caller asked to subtract. */
    private void applyBalance(double newBalance) {
        this.money = Math.max(0D, newBalance);
        markDirty();
    }

    /**
     * Reads the balance out of the 2.x per-player YAML subtree this plugin used to own
     * ({@code FinalEconomy: money: <n>}), for the one-time import of a first boot.
     */
    public static FEPlayerData fromLegacy(ConfigSection legacy) {
        FEPlayerData section = new FEPlayerData();
        section.money = Math.max(0D, legacy.getDouble("money", 0D));
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
        QueryOptions.Builder options = QueryOptions.builder().descending("money");
        if (limit > 0) {
            options.limit(limit);
        }
        return PlayerController.get().querySection(FEPlayerData.class, Query.all(), options.build());
    }
}
