package br.com.finalcraft.finaleconomy.common.command;

import br.com.finalcraft.evernifecore.commands.finalcmd.FinalCMDManager;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;

/**
 * Registers every command in one place (both platform bootstraps call this).
 */
public class CommandRegisterer {

    public static void registerCommands(ECPluginData ecPluginData) {
        FinalCMDManager.registerCommand(ecPluginData, CMDFinalEconomy.class);
        FinalCMDManager.registerCommand(ecPluginData, CMDBalance.class);
        FinalCMDManager.registerCommand(ecPluginData, CMDBalanceTop.class);
        FinalCMDManager.registerCommand(ecPluginData, CMDPay.class);
    }
}
