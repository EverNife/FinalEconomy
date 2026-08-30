package br.com.finalcraft.finaleconomy.common.data;

import br.com.finalcraft.everyconfig.config.section.ConfigSection;
import br.com.finalcraft.evernifecore.playerdata.PDSection;
import br.com.finalcraft.everydatabase.query.Indexed;

/**
 * Where a balance written by the 2.x line lands, and nothing else.
 *
 * <p>The balance itself lives on an {@link FEPlayerData}, which belongs to the account - and
 * {@code AccountSectionConfiguration} has no {@code legacyYaml} hook, so the core's importer (which
 * sweeps {@code playerdata/*.yml}, archives what it read and prints a report) cannot write there
 * directly. This per-player section is that hook: the importer fills it, and
 * {@link LegacyBalanceImporter} moves each amount into the account row exactly once.</p>
 *
 * <p>It is transitional by design. The day an account section can declare its own legacy adapter,
 * this class and its claim pass are deleted together and the collection is dropped.</p>
 */
public class FELegacyBalance extends PDSection {

    /** The 2.x value, as it was stored: a plain double under {@code FinalEconomy: money}. */
    private double money = 0D;

    /** Indexed so the claim pass asks the backend for what is left, instead of reading every player. */
    @Indexed
    private boolean claimed = false;

    public FELegacyBalance() {
        // Required no-arg constructor (Jackson + transient default seeding).
    }

    public double getMoney() {
        return money;
    }

    public boolean isClaimed() {
        return claimed;
    }

    /** Marks this amount as already folded into the account, so a repeated pass never adds it twice. */
    public void markClaimed() {
        if (!claimed) {
            claimed = true;
            markDirty();
        }
    }

    /**
     * Reads the balance out of the 2.x per-player YAML subtree this plugin used to own
     * ({@code FinalEconomy: money: <n>}).
     */
    public static FELegacyBalance fromLegacy(ConfigSection legacy) {
        FELegacyBalance section = new FELegacyBalance();
        section.money = Math.max(0D, legacy.getDouble("money", 0D));
        return section;
    }
}
