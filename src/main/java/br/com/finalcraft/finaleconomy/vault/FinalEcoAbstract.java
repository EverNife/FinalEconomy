package br.com.finalcraft.finaleconomy.vault;

import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.util.FCMathUtil;
import br.com.finalcraft.finaleconomy.api.IFinalEconomy;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public abstract class FinalEcoAbstract implements Economy, IFinalEconomy {

    @Override
    public String format(double amount) {
        return FCMathUtil.toString(amount);
    }

    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(PlayerController.getPDSection(playerName, FEPlayerData.class));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return hasAccount(PlayerController.getPDSection(player, FEPlayerData.class));
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(PlayerController.getPDSection(playerName, FEPlayerData.class));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(PlayerController.getPDSection(player, FEPlayerData.class));
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return has(PlayerController.getPDSection(playerName, FEPlayerData.class), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(PlayerController.getPDSection(player, FEPlayerData.class), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(PlayerController.getPDSection(playerName, FEPlayerData.class), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(PlayerController.getPDSection(player, FEPlayerData.class), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(PlayerController.getPDSection(playerName, FEPlayerData.class), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(PlayerController.getPDSection(player, FEPlayerData.class), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        if (player == null) return createBank(name);
        return createBank(name, PlayerController.getPDSection(player, FEPlayerData.class));
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        if (player == null) return createBank(name);
        return createBank(name, PlayerController.getPDSection(player, FEPlayerData.class));
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return isBankOwner(name, PlayerController.getPDSection(playerName, FEPlayerData.class));
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return isBankOwner(name, PlayerController.getPDSection(player, FEPlayerData.class));
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return isBankMember(name, PlayerController.getPDSection(playerName, FEPlayerData.class));
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return isBankMember(name, PlayerController.getPDSection(player, FEPlayerData.class));
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(PlayerController.getPDSection(playerName, FEPlayerData.class));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return createPlayerAccount(PlayerController.getPDSection(player, FEPlayerData.class));
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}