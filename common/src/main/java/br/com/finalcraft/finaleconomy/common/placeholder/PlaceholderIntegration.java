package br.com.finalcraft.finaleconomy.common.placeholder;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.config.uuids.UUIDsController;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.placeholder.replacer.RegexReplacer;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.playerdata.PlayerData;
import br.com.finalcraft.everylibs.util.FCInputReader;
import br.com.finalcraft.finaleconomy.common.baltop.BaltopRanking;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;

import java.util.List;
import java.util.function.Function;

/**
 * Registers this plugin's placeholders under the {@code finaleconomy} prefix - PlaceholderAPI on
 * Bukkit, the native integration on Hytale, same code.
 *
 * <p>The integration is keyed by {@code PlayerData}, not by the balance row: the placeholder API asks
 * for an {@code IPlayerData}, and an account row is not one. Each parser reaches the wallet from the
 * player, out of the cache and never through storage - a placeholder resolves on the server thread,
 * and the account row is resident for as long as one of its members is online.</p>
 */
public class PlaceholderIntegration {

    public static RegexReplacer<PlayerData> BALANCE_REPLACER;

    /** What a balance placeholder answers while the account row is not in memory. */
    private static final String NOT_LOADED = "-";

    public static void initialize(ECPluginData ecPluginData) {
        BALANCE_REPLACER = EverNifeCore.getPlatform()
                .createPlaceholderIntegration(ecPluginData, "finaleconomy", PlayerData.class)
                //Player Related
                .addParser("money", playerData -> {
                    FEPlayerData wallet = walletOf(playerData);
                    return wallet == null ? NOT_LOADED : wallet.getMoneyFormatted();
                })
                .addParser("top_position", playerData -> BaltopRanking.positionOf(playerData.getAccountId()))

                //Baltop Related
                .addParser("magnata_name", playerData -> topOf(1, PlaceholderIntegration::nameOf))
                .addParser("magnata_money", playerData -> topOf(1, FEPlayerData::getMoneyFormatted))
                .addManipulator("top_{number}_{operation}", (playerData, simpleContext) -> topOf(
                        simpleContext.getString("{number}"),
                        topRow -> {
                            switch (simpleContext.getString("{operation}").toLowerCase()) {
                                case "money":
                                    return topRow.getMoneyFormatted();
                                case "name":
                                    return nameOf(topRow);
                                default:
                                    return null;
                            }
                        }
                ));
    }

    /** The cached wallet of that player's account, or null while nothing holds it in memory. */
    private static FEPlayerData walletOf(PlayerData playerData) {
        return PlayerController.getLoadedAccountSection(playerData.getUniqueId(), FEPlayerData.class);
    }

    /** A ranking row carries no player, so the name comes from the canonical account id. */
    private static String nameOf(FEPlayerData row) {
        return UUIDsController.getNameFromUUID(row.getAccountId());
    }

    private static String topOf(String numberString, Function<FEPlayerData, String> function) {
        Integer number = FCInputReader.parseInt(numberString);
        if (number == null || number < 1) {
            return "[Invalid number]";
        }
        return topOf(number, function);
    }

    private static String topOf(Integer number, Function<FEPlayerData, String> function) {
        List<FEPlayerData> topAccounts = BaltopRanking.current();

        if (number > topAccounts.size()) {
            return String.format("[Cannot get Top-%s because there is only %s accounts on the database.]",
                    number, topAccounts.size());
        }

        return function.apply(topAccounts.get(number - 1));
    }

}
