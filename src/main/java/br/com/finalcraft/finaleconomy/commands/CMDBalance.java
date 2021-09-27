package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.help.HelpLine;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDBalance {

    @FCLocale(lang = LocaleType.EN_US, text = "§aYour current balance is: $%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§aSeu saldo atual é: $%balance%")
    public static LocaleMessage SELF_BALANCE;

    @FCLocale(lang = LocaleType.EN_US, text = "§aThe current balance of %target% is : $%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§aO saldo atual do(a) %target% é: $%balance%")
    public static LocaleMessage OTHER_BALANCE;


    @FinalCMD(
            aliases = {"balance","bal","money","febalance"},
            usage = "<Player>"
    )
    public void balance(CommandSender sender, String label, MultiArgumentos argumentos, HelpLine helpLine) {

        if (argumentos.get(0).isEmpty()){
            if (sender instanceof Player == false){
                helpLine.sendTo(sender);
                return;
            }

            FEPlayerData playerData = PlayerController.getPDSection((Player) sender, FEPlayerData.class);

            OTHER_BALANCE
                    .addPlaceholder("%balance%", playerData.getMoneyFormatted())
                    .addPlaceholder("%target%", playerData.getPlayerName())
                    .send(sender);
            return;
        }

        FEPlayerData playerData = argumentos.get(0).getPDSection(FEPlayerData.class);

        if (playerData == null){
            FCMessageUtil.playerDataNotFound(sender, argumentos.getStringArg(0));
            return;
        }

        SELF_BALANCE
                .addPlaceholder("%balance%", playerData.getMoneyFormatted())
                .send(sender);
    }

}
