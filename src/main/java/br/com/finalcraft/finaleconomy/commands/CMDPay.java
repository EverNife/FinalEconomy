package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.help.HelpLine;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCMathUtil;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.entity.Player;

public class CMDPay {

    @FCLocale(lang = LocaleType.EN_US, text = "§aYou do not have enough money. Current balance: $%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§aVocê não tem money suficiente. Seu saldo atual: $%balance%")
    public static LocaleMessage NOT_ENOUGH_MONEY;

    @FCLocale(lang = LocaleType.EN_US, text = "§aYou have payed $%amount% to %receiver%.")
    @FCLocale(lang = LocaleType.PT_BR, text = "§aVocê pagou $%amount% para %receiver%.")
    public static LocaleMessage PAY_SUCESS_SENDER;

    @FCLocale(lang = LocaleType.EN_US, text = "§aYou have received $%amount% from %payer%.")
    @FCLocale(lang = LocaleType.PT_BR, text = "§aVocê recebeu $%amount% do(a) %payer%.")
    public static LocaleMessage PAY_SUCESS_RECEIVER;


    @FinalCMD(
            aliases = {"pay","pagar"},
            usage = "%label% <Player> <Amount>"
    )
    public void pay(Player player, FEPlayerData playerData, String label, MultiArgumentos argumentos, HelpLine helpLine) {

        if (argumentos.emptyArgs(0,1)){
            helpLine.sendTo(player);
            return;
        }

        FEPlayerData targetData = argumentos.get(0).getPDSection(FEPlayerData.class);

        if (targetData == null){
            FCMessageUtil.playerDataNotFound(player, argumentos.getStringArg(0));
            return;
        }

        Double amount = argumentos.get(1).getDouble();

        if (amount == null){
            helpLine.sendTo(player);
            return;
        }

        if (amount <= 0){
            FCMessageUtil.notBoundedLower(player, amount, 0);
            return;
        }

        if (!playerData.hasMoney(amount)){
            NOT_ENOUGH_MONEY
                    .addPlaceholder("%balance%", targetData.getMoneyFormatted())
                    .send(player);
            return;
        }

        playerData.removeMoney(amount);
        targetData.addMoney(amount);

        playerData.forceSavePlayerDataOnYML();
        targetData.forceSavePlayerDataOnYML();

        PAY_SUCESS_SENDER
                .addPlaceholder("%amount%", FCMathUtil.toString(amount))
                .addPlaceholder("%receiver%", targetData.getPlayerName())
                .send(player);

        if (targetData.isPlayerOnline()){
            PAY_SUCESS_RECEIVER
                    .addPlaceholder("%amount%", FCMathUtil.toString(amount))
                    .addPlaceholder("%payer%", playerData.getPlayerName())
                    .send(player);
        }
    }

}
