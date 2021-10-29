package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.commands.finalcmd.help.HelpLine;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.util.FCMathUtil;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.finaleconomy.PermissionNodes;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.entity.Player;

public class CMDPay {

    @FCLocale(lang = LocaleType.EN_US, text = "§e§l ▶ §cYou do not have enough money. Current balance: §e$%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§e§l ▶ §cVocê não tem money suficiente. Seu saldo atual: §e$%balance%")
    public static LocaleMessage NOT_ENOUGH_MONEY;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aYou have payed $%amount% to %receiver%.")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aVocê pagou $%amount% para o jogador %receiver%.")
    public static LocaleMessage PAY_SUCCESS_SENDER;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aYou have received $%amount% from %payer%.")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aVocê recebeu $%amount% do jogador %payer%.")
    public static LocaleMessage PAY_SUCCESS_RECEIVER;


    @FinalCMD(
            aliases = {"pay","pagar"},
            usage = "%label% <Player> <Amount>",
            permission = PermissionNodes.COMMAND_PAY
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
            FCMessageUtil.needsToBeDouble(player, argumentos.getStringArg(1));
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

        PAY_SUCCESS_SENDER
                .addPlaceholder("%amount%", FCMathUtil.toString(amount))
                .addPlaceholder("%receiver%", targetData.getPlayerName())
                .send(player);

        if (targetData.isPlayerOnline()){
            PAY_SUCCESS_RECEIVER
                    .addPlaceholder("%amount%", FCMathUtil.toString(amount))
                    .addPlaceholder("%payer%", playerData.getPlayerName())
                    .send(targetData.getPlayer());
        }
    }

}
