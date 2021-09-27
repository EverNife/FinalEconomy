package br.com.finalcraft.finaleconomy.api.events;

import br.com.finalcraft.finaleconomy.api.events.base.EconomyEvent;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;

public class EconomyUpdateEvent extends EconomyEvent {

    private final FEPlayerData playerData;
    private final double amount;

    public EconomyUpdateEvent(FEPlayerData playerData, double amount) {
        this.playerData = playerData;
        this.amount = amount;
    }

    public FEPlayerData getPlayerData() {
        return playerData;
    }

    public double getAmount() {
        return amount;
    }

}
