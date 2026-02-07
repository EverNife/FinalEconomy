package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.commands.finalcmd.FinalCMDManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;

public class CommandRegisterer {

    public static void registerCommands(JavaPlugin pluginInstance) {

        FinalCMDManager.registerCommand(pluginInstance, CMDBalance.class);
        FinalCMDManager.registerCommand(pluginInstance, CMDBalanceTop.class);
        FinalCMDManager.registerCommand(pluginInstance, CMDEco.class);
        FinalCMDManager.registerCommand(pluginInstance, CMDPay.class);

    }

}
