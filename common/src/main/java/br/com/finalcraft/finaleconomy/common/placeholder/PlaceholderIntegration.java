package br.com.finalcraft.finaleconomy.common.placeholder;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.config.uuids.UUIDsController;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.placeholder.replacer.RegexReplacer;
import br.com.finalcraft.everylibs.util.FCInputReader;
import br.com.finalcraft.finaleconomy.common.baltop.BaltopRanking;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;

import java.util.List;
import java.util.function.Function;

/**
 * Registers this plugin's placeholders under the {@code finaleconomy} prefix - PlaceholderAPI on
 * Bukkit, the native integration on Hytale, same code.
 */
public final class PlaceholderIntegration {

    public static RegexReplacer<FEPlayerData> BALANCE_REPLACER;

    private PlaceholderIntegration() {
    }

    public static void initialize(ECPluginData ecPluginData) {
        BALANCE_REPLACER = EverNifeCore.getPlatform()
                .createPlaceholderIntegration(ecPluginData, "finaleconomy", FEPlayerData.class)
                //Player Related
                .addParser("money", FEPlayerData::getMoneyFormatted)
                .addParser("top_position", playerData -> BaltopRanking.positionOf(playerData.getUniqueId()))

                //Baltop Related
                .addParser("magnata_name", playerData -> topOf(1, PlaceholderIntegration::nameOf))
                .addParser("magnata_money", playerData -> topOf(1, FEPlayerData::getMoneyFormatted))
                .addManipulator("top_{number}_{operation}", (playerData, simpleContext) -> topOf(
                        simpleContext.getString("{number}"),
                        topPlayerData -> {
                            switch (simpleContext.getString("{operation}").toLowerCase()) {
                                case "money":
                                    return topPlayerData.getMoneyFormatted();
                                case "name":
                                    return nameOf(topPlayerData);
                                default:
                                    return null;
                            }
                        }
                ));
    }

    /** A ranking row carries no PlayerData, so the name comes from the stored uuid. */
    private static String nameOf(FEPlayerData playerData) {
        return UUIDsController.getNameFromUUID(playerData.getUniqueId());
    }

    private static String topOf(String numberString, Function<FEPlayerData, String> function) {
        Integer number = FCInputReader.parseInt(numberString);
        if (number == null || number < 1) {
            return "[Invalid number]";
        }
        return topOf(number, function);
    }

    private static String topOf(Integer number, Function<FEPlayerData, String> function) {
        List<FEPlayerData> topPlayers = BaltopRanking.current();

        if (number > topPlayers.size()) {
            return String.format("[Cannot get Top-%s because there is only %s players on the database.]",
                    number, topPlayers.size());
        }

        return function.apply(topPlayers.get(number - 1));
    }

}
