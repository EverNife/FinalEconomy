package br.com.finalcraft.finaleconomy.api;

import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import net.milkbowl.vault.economy.EconomyResponse;

public interface IFinalEconomy {

    public boolean hasAccount(FEPlayerData playerData);

    public double getBalance(FEPlayerData playerData);

    public boolean has(FEPlayerData playerData, double amount);

    public EconomyResponse withdrawPlayer(FEPlayerData playerData, double amount);

    public EconomyResponse depositPlayer(FEPlayerData playerData, double amount);

    public EconomyResponse createBank(String name);

    public EconomyResponse createBank(String name, FEPlayerData playerData);

    public EconomyResponse isBankOwner(String name, FEPlayerData playerData);

    public EconomyResponse isBankMember(String name, FEPlayerData playerData);

    public boolean createPlayerAccount(FEPlayerData playerData);

}
