package br.com.finalcraft.finaleconomy.integration;

import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.integration.placeholders.PAPIIntegration;
import br.com.finalcraft.evernifecore.util.FCInputReader;
import br.com.finalcraft.evernifecore.util.commons.Tuple;
import br.com.finalcraft.finaleconomy.config.FESettings;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceholderIntegration {

    public static void initialize(JavaPlugin plugin){
        PAPIIntegration.createPlaceholderIntegration(plugin, "finaleconomy", FEPlayerData.class)
                .addParser("money", fePlayerData -> fePlayerData.getMoneyFormatted())
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

    private static Tuple<Long, List<WeakReference<FEPlayerData>>> TOP_PLAYERS = Tuple.of(0L, new ArrayList<>());

    public static List<WeakReference<FEPlayerData>> getTopPlayers() {
        Tuple<Long, List<WeakReference<FEPlayerData>>> tuple = TOP_PLAYERS;

        long lastTimeTopPlayersWasUpdated = tuple.getLeft();
        if (System.currentTimeMillis() - lastTimeTopPlayersWasUpdated > TimeUnit.SECONDS.toMillis(FESettings.PLACEHOLDER_TOP_TIME_CACHE)){
            List<WeakReference<FEPlayerData>> collect = PlayerController.getAllPlayerData(FEPlayerData.class)
                    .stream()
                    .sorted(Comparator.comparing((FEPlayerData pointsPlayerData) -> pointsPlayerData.getMoney()).reversed())
                    .map(WeakReference::new)
                    .collect(Collectors.toList());
            tuple.setRight(
                    collect
            );
        }

        return tuple.getRight();
    }

    private static String calculateTop(String numberString, Function<FEPlayerData, String> function) {
        Integer number = FCInputReader.parseInt(numberString);
        if (number == null || number < 1) {
            return "[Invalid number]";
        }
        return calculateTop(number, function);
    }

    private static String calculateTop(Integer number, Function<FEPlayerData, String> function){
        List<WeakReference<FEPlayerData>> topPlayers = getTopPlayers();

        if (number > topPlayers.size()){
            return String.format("[Cannot get Top-%s because there is only %s players on the database.]", number, topPlayers.size());
        }

        FEPlayerData fePlayerData = topPlayers.get(number - 1).get();

        return function.apply(fePlayerData);
    }

}
