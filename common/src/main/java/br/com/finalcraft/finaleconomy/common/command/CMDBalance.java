package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.help.HelpLine;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCBukkitUtil;
import br.com.finalcraft.finaleconomy.PermissionNodes;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDBalance {

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aYour current balance is: $%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aSeu saldo atual é: $%balance%")
    public static LocaleMessage SELF_BALANCE;


    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aThe current balance of %target% is: $%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aO saldo atual do jogador %target% é: $%balance%")
    public static LocaleMessage OTHER_BALANCE;

    @FinalCMD(
            aliases = {"febalance","bal","money","balance"},
            permission = PermissionNodes.COMMAND_BALANCE
    )
    public void balance(CommandSender sender, HelpLine helpLine, @Arg(name = "[Player]") FEPlayerData target) {

        if (target != null){

            if (!FCBukkitUtil.hasThePermission(sender,PermissionNodes.COMMAND_BALANCE_OTHER)){
                return;
            }

            OTHER_BALANCE
                    .addPlaceholder("%balance%", target.getMoneyFormatted())
                    .addPlaceholder("%target%", target.getPlayerName())
                    .send(sender);

            return;
        }

        if (!(sender instanceof Player)){ //Console MUST specify a player!
            helpLine.sendTo(sender);
            return;
        }

        FEPlayerData playerData = PlayerController.getPDSection((Player) sender, FEPlayerData.class);

        SELF_BALANCE
                .addPlaceholder("%balance%", playerData.getMoneyFormatted())
                .send(sender);
    }

}
