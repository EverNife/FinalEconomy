package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.commands.finalcmd.FinalCMD;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegisterer {

    public static void registerCommands(JavaPlugin pluginInstance) {

        FinalCMD.registerCommand(pluginInstance, CMDBalance.class);
        FinalCMD.registerCommand(pluginInstance, CMDBalanceTop.class);
        FinalCMD.registerCommand(pluginInstance, CMDEco.class);
        FinalCMD.registerCommand(pluginInstance, CMDPay.class);

    }

}
