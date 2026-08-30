package br.com.finalcraft.finaleconomy.common.data;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.playerdata.AccountSection;
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
 * The balance of an ACCOUNT - one wallet, shared by every identity linked into it.
 *
 * <p>The row carries no player identity of its own: two linked players online at once hold the same
 * live instance, so a name or an online state has to come from the {@code PlayerData} the caller
 * already has.</p>
 *
 * <p><b>The balance is a {@link BigDecimal}</b>, so cents add up exactly and a long chain of small
 * transactions cannot drift the way binary floating point does.</p>
 */
public class FEPlayerData extends AccountSection<FEPlayerData> {

    /** Money is counted to the cent; every stored balance is rounded to this scale. */
    public static final int SCALE = 2;

    private BigDecimal money = BigDecimal.ZERO;

    /**
     * The balance as a plain double, kept in step with {@link #money} for one purpose: ordering the
     * ranking inside the backend. The storage index knows only the types in
     * {@code IndexHint.FieldType}, whose widest number is DOUBLE, so a {@code BigDecimal} cannot
     * carry the index itself. Ordering by an approximation is harmless - two balances close enough
     * for the rounding to swap them render the same on screen - while the authoritative value stays
     * exact. Drop this field once the storage layer indexes decimals.
     */
    @Indexed
    private double moneyIndex = 0D;

    public FEPlayerData() {
        // Required no-arg constructor (Jackson + transient default seeding).
    }

    /**
     * Balances ADD UP. It is the only convergence policy that keeps money when two wallets become
     * one: fusing the rows of linked identities has to hand the account what both members had.
     *
     * <p><b>Where it overpays.</b> The framework calls this for a second reason - resolving a
     * concurrent-write conflict, when two servers of the network flushed the same account. There the
     * two inputs are not two wallets but two versions of one, and adding them credits the difference
     * twice. Nothing in a single {@code merge} can tell the two calls apart, so the choice is which
     * way to be wrong: adding overpays a conflict, while keeping one side would silently drop
     * whatever the other server credited. On one writer the case never arises; on several, the
     * optimistic lock the network backend enforces is what keeps it rare.</p>
     */
    @Override
    public FEPlayerData merge(List<FEPlayerData> others) {
        BigDecimal total = this.money;
        for (FEPlayerData other : others) {
            total = total.add(other.money);
        }

        FEPlayerData merged = new FEPlayerData();
        merged.money = normalize(total);
        merged.moneyIndex = merged.money.doubleValue();
        return merged;
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

    /** The balance rendered the way {@code Settings.Money.locale} asks for. */
    public String getMoneyFormatted() {
        return ConfigManager.settings.getMoneyFormat().format(money);
    }

    /**
     * A balance never goes below zero, whatever the caller asked to subtract. Announces the change
     * only when there is one, and only when somebody is listening.
     */
    private void applyBalance(BigDecimal newBalance) {
        BigDecimal rounded = normalize(newBalance);

        if (rounded.compareTo(money) == 0) {
            return;
        }

        BigDecimal previous = money;
        money = rounded;
        moneyIndex = rounded.doubleValue();
        markDirty();

        EverNifeCore.getEventBus().postIfListened(EconomyUpdateEvent.class,
                () -> new EconomyUpdateEvent(getAccountId(), previous, rounded));
    }

    private static BigDecimal normalize(BigDecimal value) {
        return value.setScale(SCALE, RoundingMode.HALF_UP).max(BigDecimal.ZERO);
    }

    /**
     * Every account ordered by balance, richest first, ordered in the BACKEND - no account is loaded
     * into the cache by this call. Runs async; the rows come back DETACHED, so they answer
     * {@code getAccountId()} and nothing else about who they belong to.
     *
     * @param limit how many rows to bring back, or zero/less for all of them
     */
    public static CompletableFuture<List<FEPlayerData>> rankedByBalance(int limit) {
        QueryOptions.Builder options = QueryOptions.builder().descending("moneyIndex");
        if (limit > 0) {
            options.limit(limit);
        }
        return PlayerController.get().queryAccountSection(FEPlayerData.class, Query.all(), options.build());
    }
}
