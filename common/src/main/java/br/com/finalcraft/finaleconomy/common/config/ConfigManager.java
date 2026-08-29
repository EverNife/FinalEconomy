package br.com.finalcraft.finaleconomy.common.config;

import br.com.finalcraft.everyconfig.config.Config;
import br.com.finalcraft.evernifecore.config.ConfigFactory;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.locale.scanner.FCLocaleScanner;
import br.com.finalcraft.finaleconomy.common.command.CMDBalance;
import br.com.finalcraft.finaleconomy.common.command.CMDBalanceTop;
import br.com.finalcraft.finaleconomy.common.command.CMDEco;
import br.com.finalcraft.finaleconomy.common.command.CMDPay;

/**
 * Owns the plugin's config file and the order it loads in. Reading a block is not its job:
 * {@link FESettings} owns the keys.
 */
public final class ConfigManager {

    public static Config mainConfig;
    public static FESettings settings;

    private ConfigManager() {
    }

    public static void initialize(ECPluginData plugin) {
        mainConfig = ConfigFactory.open(plugin, "config.yml");
        loadState();

        // Populate the static LocaleMessage fields of every holder class. The command classes are
        // scanned by the framework too when they are registered; listing them here is what guarantees
        // their keys reach lang_XX.yml even for a command that ends up not being registered.
        FCLocaleScanner.scanForLocale(plugin, false, CMDBalance.class);
        FCLocaleScanner.scanForLocale(plugin, false, CMDBalanceTop.class);
        FCLocaleScanner.scanForLocale(plugin, false, CMDEco.class);
        FCLocaleScanner.scanForLocale(plugin, false, CMDPay.class);
    }

    /** Re-read config.yml from disk. Safe to call from the reload hook or a command. */
    public static void reload() {
        mainConfig.reload();
        loadState();
    }

    private static void loadState() {
        settings = FESettings.load(mainConfig);
        if (mainConfig.hasNewSeededDefaults()) {
            mainConfig.save();
            mainConfig.clearNewSeededDefaults();
        }
    }
}
