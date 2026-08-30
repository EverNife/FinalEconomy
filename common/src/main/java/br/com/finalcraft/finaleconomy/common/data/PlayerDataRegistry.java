package br.com.finalcraft.finaleconomy.common.data;

import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.playerdata.PDSectionConfiguration;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.playerdata.storage.SectionLifecycle;

/**
 * The ONE place this plugin declares its player data - called by both platform bootstraps.
 */
public class PlayerDataRegistry {

    /** The section's stable storage identity: it, not the class name, names the collection. */
    public static final String SECTION_ID = "finaleconomy";

    /** The 2.x per-player YAML root key this plugin owned, imported once on the first 3.x boot. */
    private static final String LEGACY_ROOT_KEY = "FinalEconomy";


    public static void registerAll(ECPluginData ecPluginData) {
        PlayerController.registerPDSectionCfg(PDSectionConfiguration
                .builder(ecPluginData, FEPlayerData.class, SECTION_ID)
                // PRELOADED: the whole collection enters memory at bind and never leaves. It is the only
                // lifecycle that lets the Vault economy answer for an offline player without an I/O
                // round-trip on the server thread - see EconomyService. Ceiling to know about:
                // SectionLifecycle.NEVER_RELEASED_WARN_THRESHOLD (100k players, one double each).
                .lifecycle(SectionLifecycle.PRELOADED)
                // Money is the classic multi-writer entity: two servers of a network can debit the same
                // player. Only these backends enforce the optimistic lock; the admin still has the final
                // say in storage.yml and an outside choice only warns at boot.
                .suggestedBackends("mysql", "postgresql", "mongo")
                // The balances of every 2.x server that ever ran this plugin. The core drives the import
                // on the first tick of the first 3.x boot, archives the old files instead of deleting
                // them, and prints a report; this adapter is the plugin's whole share of it.
                .legacyYaml(LEGACY_ROOT_KEY, FEPlayerData::fromLegacy)
                .build());
    }
}
