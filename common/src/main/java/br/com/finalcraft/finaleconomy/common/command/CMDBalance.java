package br.com.finalcraft.finaleconomy.common.command;

import br.com.finalcraft.evernifecore.api.common.commandsender.FCommandSender;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.help.HelpLine;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.finaleconomy.common.PermissionNodes;
import br.com.finalcraft.evernifecore.playerdata.PlayerData;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;
import br.com.finalcraft.finaleconomy.common.economy.EconomyService;

public class CMDBalance {

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aYour current balance is: $${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aSeu saldo atual é: $${balance}")
    public static LocaleMessage SELF_BALANCE;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aThe current balance of ${target} is: $${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aO saldo atual do jogador ${target} é: $${balance}")
    public static LocaleMessage OTHER_BALANCE;

    @FinalCMD(
            aliases = {"febalance", "bal", "money", "balance"},
            permission = PermissionNodes.COMMAND_BALANCE
    )
    public void balance(FCommandSender sender, HelpLine helpLine, @Arg("[Player]") PlayerData target) {

        if (target != null) {

            if (!FCMessageUtil.hasThePermission(sender, PermissionNodes.COMMAND_BALANCE_OTHER)) {
                return;
            }

            //the token names a player and the message says their name, so the wallet is read off them
            OTHER_BALANCE
                    .addPlaceholder("balance", target.getAccountSection(FEPlayerData.class).join().getMoneyFormatted())
                    .addPlaceholder("target", target.getName())
                    .send(sender);

            return;
        }

        if (!sender.isPlayer()) { //Console MUST specify a player!
            helpLine.sendTo(sender);
            return;
        }

        FEPlayerData playerData = EconomyService.sectionOf(sender.getUniqueId());

        SELF_BALANCE
                .addPlaceholder("balance", playerData.getMoneyFormatted())
                .send(sender);
    }

}
