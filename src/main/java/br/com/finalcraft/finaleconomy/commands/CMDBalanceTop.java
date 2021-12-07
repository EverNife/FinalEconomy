package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.util.FCTextUtil;
import br.com.finalcraft.evernifecore.util.numberwrapper.NumberWrapper;
import br.com.finalcraft.evernifecore.util.pageviwer.PageViwer;
import br.com.finalcraft.finaleconomy.PermissionNodes;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.command.CommandSender;

import java.util.stream.Collectors;

public class CMDBalanceTop {

    private final PageViwer<FEPlayerData, Double> BAL_TOP = PageViwer.builder(
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
            .setLineEnd(100)
            .build();

    @FinalCMD(
            aliases = {"balancetop","baltop","moneytop","febalancetop"},
            usage = "<Player>",
            permission = PermissionNodes.COMMAND_BALANCETOP
    )
    public void top(CommandSender sender, MultiArgumentos argumentos){
        NumberWrapper<Integer> page = argumentos.get(0)
                .getNumberWrapper(Integer.class, 1)
                .bound(1, 10);

        BAL_TOP.send(page.get(), sender);
    }

}
