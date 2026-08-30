package br.com.finalcraft.finaleconomy.common.command;

import br.com.finalcraft.evernifecore.api.common.commandsender.FCommandSender;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginManager;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.everylibs.util.FCMathUtil;
import br.com.finalcraft.finaleconomy.common.FEBootstrap;
import br.com.finalcraft.finaleconomy.common.PermissionNodes;
import br.com.finalcraft.finaleconomy.common.config.ConfigManager;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;

import java.math.BigDecimal;

/**
 * The plugin's command: administering balances, and reloading it.
 *
 * <p>Amounts arrive as {@code Double} because that is what a human types and what the argument
 * parser produces; they become a {@link BigDecimal} at this boundary, so the exactness the balance is
 * kept with starts here instead of after a round of binary arithmetic.</p>
 */
@FinalCMD(
        aliases = {"finaleconomy", "feeco", "eco", "economy"},
        permission = PermissionNodes.COMMAND_ECO
)
public class CMDFinalEconomy {

    // -----------------------------------------------------------------------------------------------------------------------------//
    // GIVE
    // -----------------------------------------------------------------------------------------------------------------------------//

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §a$${amount} added to ${receiver} account. New balance: §e$${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §a$${amount} adicionado ao jogador §e${receiver}§a. Novo Saldo: §e$${balance}")
    public static LocaleMessage GIVE_SUCCESS;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §a$${amount} was added to your account. New balance: §e$${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §a$${amount} foi adicionado a sua conta. Novo Saldo: §e$${balance}")
    public static LocaleMessage MONEY_WAS_ADDED_TO_YOUR_ACCOUNT;

    @FinalCMD.SubCMD(
            subcmd = {"give", "add"},
            locales = {
                    @FCLocale(lang = LocaleType.EN_US, text = "§bGive a specific amount of money to a player!"),
                    @FCLocale(lang = LocaleType.PT_BR, text = "§bDá uma quantidade específica de money para um jogador!")
            }
    )
    public void give(FCommandSender sender, @Arg("<Player>") FEPlayerData target,
                     @Arg(value = "<Amount>", context = "[0.01:*]") Double amount) {
        target.addMoney(BigDecimal.valueOf(amount));

        GIVE_SUCCESS
                .addPlaceholder("receiver", target.getName())
                .addPlaceholder("amount", FCMathUtil.toString(amount))
                .addPlaceholder("balance", target.getMoneyFormatted())
                .send(sender);

        if (ConfigManager.settings.getNotification().isNotifyOnEcoGive() && target.isPlayerOnline()) {
            MONEY_WAS_ADDED_TO_YOUR_ACCOUNT
                    .addPlaceholder("amount", FCMathUtil.toString(amount))
                    .addPlaceholder("balance", target.getMoneyFormatted())
                    .send(target.getPlayer());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------//
    // TAKE
    // -----------------------------------------------------------------------------------------------------------------------------//

    @FCLocale(lang = LocaleType.EN_US, text = "§e§l ▶ §c${payer} does not have enough money. His balance: §e$${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§e§l ▶ §c${payer} não possui dinheiro suficiente. Seu saldo é: §e$${balance}")
    public static LocaleMessage NOT_ENOUGH_MONEY;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §a$${amount} taken from ${payer}'s account. New balance: §e$${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §a$${amount} removido do jogador ${payer}. Novo Saldo: §e$${balance}")
    public static LocaleMessage TAKE_SUCCESS;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §e$${amount} was removed from your account. New balance: §e$${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §e$${amount} foi removido da sua conta. Novo Saldo: §e$${balance}")
    public static LocaleMessage MONEY_WAS_REMOVED_FROM_YOUR_ACCOUNT;

    @FinalCMD.SubCMD(
            subcmd = {"take", "remove"},
            locales = {
                    @FCLocale(lang = LocaleType.EN_US, text = "§bRemove a specific amount of money from a player!"),
                    @FCLocale(lang = LocaleType.PT_BR, text = "§bRemove uma quantidade específica de money de um jogador!")
            }
    )
    public void take(FCommandSender sender, @Arg("<Player>") FEPlayerData target,
                     @Arg(value = "<Amount>", context = "[0.01:*]") Double amount) {

        BigDecimal value = BigDecimal.valueOf(amount);

        if (!target.hasMoney(value)) {
            NOT_ENOUGH_MONEY
                    .addPlaceholder("payer", target.getName())
                    .addPlaceholder("amount", FCMathUtil.toString(amount))
                    .addPlaceholder("balance", target.getMoneyFormatted())
                    .send(sender);
            return;
        }

        target.removeMoney(value);

        TAKE_SUCCESS
                .addPlaceholder("payer", target.getName())
                .addPlaceholder("amount", FCMathUtil.toString(amount))
                .addPlaceholder("balance", target.getMoneyFormatted())
                .send(sender);

        if (ConfigManager.settings.getNotification().isNotifyOnEcoTake() && target.isPlayerOnline()) {
            MONEY_WAS_REMOVED_FROM_YOUR_ACCOUNT
                    .addPlaceholder("amount", FCMathUtil.toString(amount))
                    .addPlaceholder("balance", target.getMoneyFormatted())
                    .send(target.getPlayer());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------//
    // SET
    // -----------------------------------------------------------------------------------------------------------------------------//

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §a${player} balance's was set to §e$${balance}")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aO Saldo do jogador ${player} foi definido para §e$${balance}")
    public static LocaleMessage SET_SUCESS;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §eYour balance was changed from $${old_balance} to $${new_balance}.")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §eSeu saldo foi alterado de $${old_balance} para $${new_balance}.")
    public static LocaleMessage MONEY_WAS_SET_FOR_YOUR_ACCOUNT;

    @FinalCMD.SubCMD(
            subcmd = {"set"},
            locales = {
                    @FCLocale(lang = LocaleType.EN_US, text = "§bSet a player's balance to a specific amount!"),
                    @FCLocale(lang = LocaleType.PT_BR, text = "§bDefina o saldo de um jogador para um valor específico!")
            }
    )
    public void set(FCommandSender sender, @Arg("<Player>") FEPlayerData target,
                    @Arg(value = "<Amount>", context = "[0:*]") Double amount) {
        String oldBalance = target.getMoneyFormatted();
        target.setMoney(BigDecimal.valueOf(amount));

        SET_SUCESS
                .addPlaceholder("player", target.getName())
                .addPlaceholder("balance", target.getMoneyFormatted())
                .send(sender);

        if (ConfigManager.settings.getNotification().isNotifyOnEcoSet() && target.isPlayerOnline()) {
            MONEY_WAS_SET_FOR_YOUR_ACCOUNT
                    .addPlaceholder("old_balance", oldBalance)
                    .addPlaceholder("new_balance", target.getMoneyFormatted())
                    .send(target.getPlayer());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------//
    // RELOAD
    // -----------------------------------------------------------------------------------------------------------------------------//

    @FinalCMD.SubCMD(
            subcmd = {"reload"},
            permission = PermissionNodes.COMMAND_RELOAD,
            locales = {
                    @FCLocale(lang = LocaleType.EN_US, text = "§bReload this plugin's configuration!"),
                    @FCLocale(lang = LocaleType.PT_BR, text = "§bRecarrega a configuração deste plugin!")
            }
    )
    public void reload(FCommandSender sender) {
        ECPluginManager.reloadPlugin(sender, FEBootstrap.get().getPluginData());
    }

}
