package br.com.finalcraft.finaleconomy.baltop;

import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.finaleconomy.config.FESettings;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BaltopTrackingCenter {

    private static long lastCalculationTime = 0;
    private static List<FEPlayerData> TOP_PLAYERS = new ArrayList<>();

    public static List<FEPlayerData> getTopPlayers(boolean forceRecalculation){
        refreshBalTop(forceRecalculation);
        return TOP_PLAYERS;
    }

    public static void refreshBalTop(boolean force){

        if (force || (System.currentTimeMillis() - lastCalculationTime > TimeUnit.SECONDS.toMillis(FESettings.PLACEHOLDER_TOP_TIME_CACHE))){
            lastCalculationTime = System.currentTimeMillis();

            TOP_PLAYERS = PlayerController.getAllPlayerData(FEPlayerData.class)
                    .stream()
                    .sorted(Comparator.comparing((FEPlayerData pointsPlayerData) -> pointsPlayerData.getMoney()).reversed())
                    .collect(Collectors.toList());

            for (int i = 0; i < TOP_PLAYERS.size(); i++) {
                TOP_PLAYERS.get(i).setBaltopPosition(i + 1);
            }

        }

    }

}
