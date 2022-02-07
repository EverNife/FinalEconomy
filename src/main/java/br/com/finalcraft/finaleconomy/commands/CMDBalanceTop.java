package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.util.FCBukkitUtil;
import br.com.finalcraft.evernifecore.util.FCTextUtil;
import br.com.finalcraft.evernifecore.util.pageviwer.PageViewer;
import br.com.finalcraft.finaleconomy.PermissionNodes;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.command.CommandSender;

import java.util.stream.Collectors;

public class CMDBalanceTop {

    private final PageViewer<FEPlayerData, Double> BAL_TOP = PageViewer.builder(
            () -> PlayerController.getAllPlayerData(FEPlayerData.class).stream().collect(Collectors.toList()),
            playerData -> playerData.getMoney())
            .setFormatHeader(
                    FCTextUtil.alignCenter("§6 §l[§eBalTop§6§l]§6 §r","§e§m-")
            )
            .setFormatLine(
                    "§7#  %number%:   §e%player%§r - §6$%money_formatted%"
            )
            .addPlaceholder("%money_formatted%", FEPlayerData::getMoneyFormatted)
            .setIncludeDate(true)
            .setIncludeTotalPlayers(true)
            .setLineEnd(-1)
            .build();

    @FinalCMD(
            aliases = {"balancetop","baltop","moneytop","febalancetop"},
            usage = "[-all]",
            permission = PermissionNodes.COMMAND_BALANCETOP
    )
    public void top(CommandSender sender, MultiArgumentos argumentos){

        if (argumentos.get(0).equalsIgnoreCase("-all")){
            if (!FCBukkitUtil.hasThePermission(sender, PermissionNodes.COMMAND_BALANCETOP_ALL)){
                return;
            }
            BAL_TOP.send(BAL_TOP.getLineStart(), BAL_TOP.getLineEnd(), sender);
            return;
        }

        int page = argumentos.get(0).getNumberWrapper(Integer.class, 1)
                .boundLower(1) //0 page to Infinity
                .intValue();

        BAL_TOP.send(page, sender);
    }

}
