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
import org.bukkit.command.CommandSender;

@FinalCMD(
        aliases = {"finaleconomy","eco","economy"},
        permission = PermissionNodes.COMMAND_ECO
)
public class CMDEco {

    // -----------------------------------------------------------------------------------------------------------------------------//
    // GIVE
    // -----------------------------------------------------------------------------------------------------------------------------//

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §a$%amount% added to %receiver% account. New balance: §e$%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §a$%amount% adicionado ao jogador §e%receiver%§a. Novo Saldo: §e$%balance%")
    public static LocaleMessage GIVE_SUCESS;

    @FinalCMD.SubCMD(
            subcmd = {"give","add"},
            usage = "%name% <Player> <Amount>",
            locales = {
                    @FCLocale(lang = LocaleType.EN_US, text = "§bGive a specific amount of money to a player!"),
                    @FCLocale(lang = LocaleType.PT_BR, text = "§bDá uma quantidade específica de money para um jogador!")
            }
    )
    public void give(CommandSender sender, String label, MultiArgumentos argumentos, HelpLine helpLine) {

        if (argumentos.emptyArgs(1,2)) {
            helpLine.sendTo(sender);
            return;
        }

        FEPlayerData playerData = argumentos.get(1).getPDSection(FEPlayerData.class);
        if (playerData == null){
            FCMessageUtil.playerDataNotFound(sender, argumentos.getStringArg(1));
            return;
        }

        Double amount = argumentos.get(2).getDouble();
        if (amount == null){
            FCMessageUtil.needsToBeDouble(sender, argumentos.getStringArg(2));
            return;
        }

        playerData.addMoney(amount);

        GIVE_SUCESS
                .addPlaceholder("%receiver%", playerData.getPlayerName())
                .addPlaceholder("%amount%", FCMathUtil.toString(amount))
                .addPlaceholder("%balance%", playerData.getMoneyFormatted())
                .send(sender);
    }

    // -----------------------------------------------------------------------------------------------------------------------------//
    // TAKE
    // -----------------------------------------------------------------------------------------------------------------------------//

    @FCLocale(lang = LocaleType.EN_US, text = "§e§l ▶ §c$%payer% does not have enought money. His balance: §e$%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§e§l ▶ §c$%payer% não possui dinheiro suficiente. Seu saldo é: §e$%balance%")
    public static LocaleMessage NOT_ENOUGH_MONEY;

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §a$%amount% taken from %payer%'s account. New balance: §e$%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §a$%amount% removido do jogador %payer%. Novo Saldo: §e$%balance%")
    public static LocaleMessage TAKE_SUCESS;

    @FinalCMD.SubCMD(
            subcmd = {"take","remove"},
            usage = "%name% <Player> <Amount>",
            locales = {
                    @FCLocale(lang = LocaleType.EN_US, text = "§bRemove a specific amount of money from a player!"),
                    @FCLocale(lang = LocaleType.PT_BR, text = "§bRemove uma quantidade específica de money de um jogador!")
            }
    )
    public void take(CommandSender sender, String label, MultiArgumentos argumentos, HelpLine helpLine) {

        if (argumentos.emptyArgs(1,2)) {
            helpLine.sendTo(sender);
            return;
        }

        FEPlayerData playerData = argumentos.get(1).getPDSection(FEPlayerData.class);
        if (playerData == null){
            FCMessageUtil.playerDataNotFound(sender, argumentos.getStringArg(1));
            return;
        }

        Double amount = argumentos.get(2).getDouble();
        if (amount == null){
            FCMessageUtil.needsToBeDouble(sender, argumentos.getStringArg(2));
            return;
        }

        if (!playerData.hasMoney(amount)){
            NOT_ENOUGH_MONEY
                    .addPlaceholder("%payer%", playerData.getPlayerName())
                    .addPlaceholder("%amount%", amount)
                    .addPlaceholder("%balance%", playerData.getMoneyFormatted())
                    .send(sender);
            return;
        }

        playerData.removeMoney(amount);

        TAKE_SUCESS
                .addPlaceholder("%payer%", playerData.getPlayerName())
                .addPlaceholder("%money%", FCMathUtil.toString(amount))
                .addPlaceholder("%balance%", playerData.getMoneyFormatted())
                .send(sender);
    }

    // -----------------------------------------------------------------------------------------------------------------------------//
    // SET
    // -----------------------------------------------------------------------------------------------------------------------------//

    @FCLocale(lang = LocaleType.EN_US, text = "§2§l ▶ §a%player% balance's was set to §e$%balance%")
    @FCLocale(lang = LocaleType.PT_BR, text = "§2§l ▶ §aO Saldo do jogador %player% foi definido para §e$%balance%")
    public static LocaleMessage SET_SUCESS;

    @FinalCMD.SubCMD(
            subcmd = {"set"},
            usage = "%name% <Player> <Amount>",
            locales = {
                    @FCLocale(lang = LocaleType.EN_US, text = "§bSet a player's balance to a specific amount!"),
                    @FCLocale(lang = LocaleType.PT_BR, text = "§bDefina o saldo de um jogador para um valor específico!")
            }
    )
    public void set(CommandSender sender, String label, MultiArgumentos argumentos, HelpLine helpLine) {

        if (argumentos.emptyArgs(1,2)) {
            helpLine.sendTo(sender);
            return;
        }

        FEPlayerData playerData = argumentos.get(1).getPDSection(FEPlayerData.class);
        if (playerData == null){
            FCMessageUtil.playerDataNotFound(sender, argumentos.getStringArg(1));
            return;
        }

        Double amount = argumentos.get(2).getDouble();
        if (amount == null){
            FCMessageUtil.needsToBeDouble(sender, argumentos.getStringArg(2));
            return;
        }

        playerData.setMoney(amount);

        SET_SUCESS
                .addPlaceholder("%player%", playerData.getPlayerName())
                .addPlaceholder("%balance%", playerData.getMoneyFormatted())
                .send(sender);
    }

}
