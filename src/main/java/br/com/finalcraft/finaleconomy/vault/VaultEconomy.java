package br.com.finalcraft.finaleconomy.vault;

import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultEconomy extends FinalEcoAbstract {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "FinalEconomy";
    }

    //------------------------------------------------------------------------------------------------------------------
    // Implementation
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean hasAccount(FEPlayerData playerData) {
        return true;//We assume every player has an account
    }

    @Override
    public double getBalance(FEPlayerData playerData) {
        return playerData.getMoney();
    }

    @Override
    public boolean has(FEPlayerData playerData, double amount) {
        return playerData.hasMoney(amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(FEPlayerData playerData, double amount) {
        if (!playerData.hasMoney(amount)){
            return new EconomyResponse(0, playerData.getMoney(), EconomyResponse.ResponseType.FAILURE, null);
        }

        playerData.removeMoney(amount);

        return new EconomyResponse(amount, playerData.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(FEPlayerData playerData, double amount) {
        if (amount < 0) return new EconomyResponse(0, playerData.getMoney(), EconomyResponse.ResponseType.FAILURE, null);
        if (amount == 0) return new EconomyResponse(0, playerData.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);

        playerData.addMoney(amount);

        return new EconomyResponse(amount, playerData.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public boolean createPlayerAccount(FEPlayerData playerData) {
        return playerData != null;//Only create account for REAL players
    }

    //------------------------------------------------------------------------------------------------------------------
    // Bank Support (TODO Implement this)
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public EconomyResponse createBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, FEPlayerData playerData) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, FEPlayerData playerData) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, FEPlayerData playerData) {
        return null;
    }

}
