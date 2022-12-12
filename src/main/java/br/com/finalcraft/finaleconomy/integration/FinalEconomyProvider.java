package br.com.finalcraft.finaleconomy.integration;

import br.com.finalcraft.evernifecore.config.playerdata.IPlayerData;
import br.com.finalcraft.evernifecore.economy.IEconomyProvider;
import br.com.finalcraft.finaleconomy.api.FinalEconomyAPI;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;

public class FinalEconomyProvider implements IEconomyProvider {

    @Override
    public double ecoGet(IPlayerData playerData) {
        return FinalEconomyAPI.getVaultAPI().getBalance(playerData.getPDSection(FEPlayerData.class));
    }

    @Override
    public void ecoGive(IPlayerData playerData, double value) {
        FinalEconomyAPI.getVaultAPI().depositPlayer(playerData.getPDSection(FEPlayerData.class), value);
    }

    @Override
    public boolean ecoTake(IPlayerData playerData, double value) {
        return FinalEconomyAPI.getVaultAPI().withdrawPlayer(playerData.getPDSection(FEPlayerData.class), value).transactionSuccess();
    }

    @Override
    public void ecoSet(IPlayerData playerData, double value) {
        FEPlayerData fePlayerData = playerData.getPDSection(FEPlayerData.class);

        double current = ecoGet(fePlayerData);
        double needed = value - current;
        if (needed >= 0){
            ecoGive(fePlayerData,needed);
        }else {
            ecoTake(fePlayerData,-needed);
        }
    }

}
