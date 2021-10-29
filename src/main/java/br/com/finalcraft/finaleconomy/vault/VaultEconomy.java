package br.com.finalcraft.finaleconomy.vault;

import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;

public class VaultEconomy extends FinalEcoAbstract {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
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
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse createBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, FEPlayerData playerData) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
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

    @Override
    public List<String> getBanks() {
        return null;
    }

}
