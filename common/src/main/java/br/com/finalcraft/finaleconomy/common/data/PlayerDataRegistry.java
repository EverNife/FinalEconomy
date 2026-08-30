package br.com.finalcraft.finaleconomy.common.data;

import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.playerdata.AccountSectionConfiguration;
import br.com.finalcraft.evernifecore.playerdata.PDSectionConfiguration;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;

/**
 * The ONE place this plugin declares its data - called by both platform bootstraps.
 */
public class PlayerDataRegistry {

    /** The balance section's stable storage identity: it, not the class name, names the collection. */
    public static final String SECTION_ID = "finaleconomy";

    /** The landing zone for the 2.x amounts; its own identity, its own collection. */
    public static final String LEGACY_SECTION_ID = "finaleconomylegacy";

    /** The 2.x per-player YAML root key this plugin owned, imported once on the first 3.x boot. */
    private static final String LEGACY_ROOT_KEY = "FinalEconomy";

    public static void registerAll(ECPluginData ecPluginData) {
        // The wallet belongs to the ACCOUNT: two identities linked together spend one balance, which
        // is what a network economy means. The whole account family lives on the single backend
        // configured under `network` in storage.yml - there is no per-section routing to ask for, and
        // no lifecycle to pick: a row is resident while any member of the account is online.
        PlayerController.registerAccountSectionCfg(AccountSectionConfiguration
                .builder(ecPluginData, FEPlayerData.class, SECTION_ID)
                .description("Account-wide balance, shared by every identity linked into the account")
                .build());

        // Only the core can read the 2.x files, and only into a PDSection. This one exists to receive
        // that amount and hand it to the account row (see LegacyBalanceImporter); nothing else writes
        // it, and no balance is ever read from it.
        PlayerController.registerPDSectionCfg(PDSectionConfiguration
                .builder(ecPluginData, FELegacyBalance.class, LEGACY_SECTION_ID)
                .legacyYaml(LEGACY_ROOT_KEY, FELegacyBalance::fromLegacy)
                .build());
    }
}
