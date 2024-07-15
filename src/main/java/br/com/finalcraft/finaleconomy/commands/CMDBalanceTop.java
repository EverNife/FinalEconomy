package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.pageviwer.PageViewer;
import br.com.finalcraft.evernifecore.util.FCBukkitUtil;
import br.com.finalcraft.finaleconomy.PermissionNodes;
import br.com.finalcraft.finaleconomy.config.FESettings;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.command.CommandSender;

import java.util.stream.Collectors;

public class CMDBalanceTop {

    public static CMDBalanceTop instance; {instance = this;};

    @FCLocale(lang = LocaleType.EN_US, text = "§e§m-----------------------§6§l[§eBalTop§6§l]§r§e§m-----------------------§r")
    public static LocaleMessage BALTOP_PREFIX;

    @FCLocale(lang = LocaleType.EN_US, text = "§7#  %number%:   §6%player%§r - §a$%money_formatted%")
    public static LocaleMessage BALTOP_LINE;

    @FCLocale(lang = LocaleType.EN_US, text = "")
    public static LocaleMessage BALTOP_FOOTER;

    private PageViewer<FEPlayerData, Double> BAL_TOP;

    public void recalculateBalTop(){
        BAL_TOP = PageViewer.targeting(FEPlayerData.class)
                .withSuplier(() -> PlayerController.getAllPlayerData(FEPlayerData.class).stream().collect(Collectors.toList()))
                .extracting(FEPlayerData::getMoney)
                .setFormatHeader(
                        BALTOP_PREFIX.getDefaultFancyText()
                )
                .setFormatLine(
                        BALTOP_LINE.getDefaultFancyText()
                )
                .setFormatFooter(
                        BALTOP_FOOTER.getDefaultFancyText()
                )
                .addPlaceholder("%money_formatted%", FEPlayerData::getMoneyFormatted)
                .setIncludeDate(FESettings.BALTOP_INCLUDE_DAY_OF_TODAY)
                .setIncludeTotalCount(FESettings.BALTOP_INCLUDE_TOTAL_USERS_COUNT)
                .setLineEnd(FESettings.BALTOP_MAX_PAGES)
                .build();
    }

    @FinalCMD(
            aliases = {"balancetop","baltop","moneytop","febalancetop"},
            permission = PermissionNodes.COMMAND_BALANCETOP
    )
    public void top(CommandSender sender, MultiArgumentos argumentos, @Arg(name = "[page]", context = "[1:*]") Integer page){

        if (page == null && argumentos.get(0).equalsIgnoreCase("all")){
            if (!FCBukkitUtil.hasThePermission(sender, PermissionNodes.COMMAND_BALANCETOP_ALL)){
                return;
            }
            BAL_TOP.send(0, BAL_TOP.getLineStart(), BAL_TOP.getLineEnd(), sender);
            return;
        }

        BAL_TOP.send(page, sender);
    }

}
