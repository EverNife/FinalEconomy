package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.commands.finalcmd.FinalCMDManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegisterer {

    public static void registerCommands(JavaPlugin pluginInstance) {

        FinalCMDManager.registerCommand(pluginInstance, CMDBalance.class);
        FinalCMDManager.registerCommand(pluginInstance, CMDBalanceTop.class);
        FinalCMDManager.registerCommand(pluginInstance, CMDEco.class);
        FinalCMDManager.registerCommand(pluginInstance, CMDPay.class);

    }

}
