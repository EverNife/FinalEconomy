package br.com.finalcraft.finaleconomy.integration;

import br.com.finalcraft.evernifecore.integration.placeholders.PAPIIntegration;
import br.com.finalcraft.evernifecore.util.FCInputReader;
import br.com.finalcraft.finaleconomy.baltop.BaltopTrackingCenter;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.function.Function;

public class PlaceholderIntegration {

    public static void initialize(JavaPlugin plugin){
        PAPIIntegration.createPlaceholderIntegration(plugin, "finaleconomy", FEPlayerData.class)
                //Player Related
                .addParser("money", fePlayerData -> fePlayerData.getMoneyFormatted())
                .addParser("top_position", fePlayerData -> fePlayerData.getBaltopPosition(false))

                //Baltop Related
                .addParser("magnata_name", fePlayerData -> calculateTop(1, FEPlayerData::getPlayerName))
                .addParser("magnata_money", fePlayerData -> calculateTop(1, FEPlayerData::getMoneyFormatted))
                .addManipulator("top_{number}_{operation}", (playerData, simpleContext) -> {
                    return calculateTop(
                            simpleContext.getString("{number}"),
                            (topPlayerData) -> {
                                switch (simpleContext.getString("{operation}").toLowerCase()){
                                    case "money":
                                        return topPlayerData.getMoneyFormatted();
                                    case "name":
                                        return topPlayerData.getPlayerName();
                                    default:
                                        return null;
                                }
                            }
                    );
                });
    }

    private static String calculateTop(String numberString, Function<FEPlayerData, String> function) {
        Integer number = FCInputReader.parseInt(numberString);
        if (number == null || number < 1) {
            return "[Invalid number]";
        }
        return calculateTop(number, function);
    }

    private static String calculateTop(Integer number, Function<FEPlayerData, String> function){
        List<FEPlayerData> topPlayers = BaltopTrackingCenter.getTopPlayers(false);

        if (number > topPlayers.size()){
            return String.format("[Cannot get Top-%s because there is only %s players on the database.]", number, topPlayers.size());
        }

        FEPlayerData fePlayerData = topPlayers.get(number - 1);

        return function.apply(fePlayerData);
    }

}
