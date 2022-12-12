package br.com.finalcraft.finaleconomy.integration;

import br.com.finalcraft.evernifecore.economy.EconomyManager;

public class EverNifeCoreIntegration {

    public static void initialize(){
        //This will make EverNifeCore's internal economy faster!
        EconomyManager.setEconomyProvider(new FinalEconomyProvider());
    }
}
