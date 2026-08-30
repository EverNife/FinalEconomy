package br.com.finalcraft.finaleconomy.common.command;

import br.com.finalcraft.evernifecore.api.common.player.FPlayer;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.everylibs.util.FCMathUtil;
import br.com.finalcraft.finaleconomy.common.PermissionNodes;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;

import java.math.BigDecimal;

public class CMDPay {

    @FCLocale(lang = LocaleType.EN_US, text = "§e§l ▶ §cYou do not have enough money. Current balance: §e$${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§e§l ▶ §cVocê não tem dinheiro suficiente. Seu saldo atual: §e$${balance}")
    public static LocaleMessage NOT_ENOUGH_MONEY;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aYou have payed $${amount} to ${receiver}.")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aVocê pagou $${amount} para o jogador ${receiver}.")
    public static LocaleMessage PAY_SUCCESS_SENDER;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §aYou have received $${amount} from ${payer}.")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aVocê recebeu $${amount} do jogador ${payer}.")
    public static LocaleMessage PAY_SUCCESS_RECEIVER;

    @FinalCMD(
            aliases = {"fepay", "pay", "pagar"},
            permission = PermissionNodes.COMMAND_PAY
    )
    public void pay(FPlayer player, FEPlayerData playerData, @Arg("<Player>") FEPlayerData target,
                    @Arg(value = "<Amount>", context = "[0.01:*]") Double amount) {

        BigDecimal value = BigDecimal.valueOf(amount);

        if (!playerData.hasMoney(value)) {
            NOT_ENOUGH_MONEY
                    .addPlaceholder("balance", playerData.getMoneyFormatted())
                    .send(player);
            return;
        }

        playerData.removeMoney(value);
        target.addMoney(value);

        PAY_SUCCESS_SENDER
                .addPlaceholder("amount", FCMathUtil.toString(amount))
                .addPlaceholder("receiver", target.getName())
                .send(player);

        if (target.isPlayerOnline()) {
            PAY_SUCCESS_RECEIVER
                    .addPlaceholder("amount", FCMathUtil.toString(amount))
                    .addPlaceholder("payer", playerData.getName())
                    .send(target.getPlayer());
        }
    }

}
