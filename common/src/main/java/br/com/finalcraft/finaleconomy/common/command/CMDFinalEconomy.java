package br.com.finalcraft.finaleconomy.common.command;

import br.com.finalcraft.evernifecore.api.common.commandsender.FCommandSender;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginManager;
import br.com.finalcraft.finaleconomy.common.FEBootstrap;
import br.com.finalcraft.finaleconomy.common.PermissionNodes;

/**
 * The plugin's own command. Administering the economy is what {@code /eco} is for; reloading the
 * plugin belongs here.
 */
@FinalCMD(
        aliases = {"finaleconomy"},
        permission = PermissionNodes.COMMAND_RELOAD
)
public class CMDFinalEconomy {

    @FinalCMD.SubCMD(
            subcmd = {"reload"},
            permission = PermissionNodes.COMMAND_RELOAD
    )
    public void reload(FCommandSender sender) {
        ECPluginManager.reloadPlugin(sender, FEBootstrap.get().getPluginData());
    }

}
