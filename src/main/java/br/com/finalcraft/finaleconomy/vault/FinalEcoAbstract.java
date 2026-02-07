package br.com.finalcraft.finaleconomy.vault;

import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.util.FCMathUtil;
import br.com.finalcraft.finaleconomy.api.IFinalEconomy;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault2.economy.AccountPermission;
import net.milkbowl.vault2.economy.Economy;

import java.math.BigDecimal;
import java.util.*;

public abstract class FinalEcoAbstract implements Economy, IFinalEconomy {

    @Override
    public boolean hasSharedAccountSupport() {
        return false;
    }

    @Override
    public boolean hasMultiCurrencySupport() {
        return false;
    }

    @Override
    public int fractionalDigits(String pluginName) {
        return 2;
    }

    @Override
    public String format(BigDecimal amount) {
        return FCMathUtil.toString(amount.doubleValue());
    }

    @Override
    public String format(String pluginName, BigDecimal amount) {
        return format(amount);
    }

    @Override
    public String format(BigDecimal amount, String currency) {
        return format(amount);
    }

    @Override
    public String format(String pluginName, BigDecimal amount, String currency) {
        return format(amount);
    }

    @Override
    public boolean hasCurrency(String currency) {
        return true;
    }

    @Override
    public String getDefaultCurrency(String pluginName) {
        return "";
    }

    @Override
    public String defaultCurrencyNamePlural(String pluginName) {
        return "";
    }

    @Override
    public String defaultCurrencyNameSingular(String pluginName) {
        return "";
    }

    @Override
    public Collection<String> currencies() {
        return List.of();
    }

    @Override
    public boolean createAccount(UUID accountID, String name) {
        return createPlayerAccount(PlayerController.getPDSection(accountID, FEPlayerData.class));
    }

    @Override
    public boolean createAccount(UUID accountID, String name, boolean player) {
        return createAccount(accountID, name);
    }

    @Override
    public boolean createAccount(UUID accountID, String name, String worldName) {
        return createAccount(accountID, name);
    }

    @Override
    public boolean createAccount(UUID accountID, String name, String worldName, boolean player) {
        return createAccount(accountID, name);
    }

    @Override
    public Map<UUID, String> getUUIDNameMap() {
        return Map.of();
    }

    @Override
    public Optional<String> getAccountName(UUID accountID) {
        return Optional.empty();
    }

    @Override
    public boolean hasAccount(UUID accountID) {
        return hasAccount(PlayerController.getPDSection(accountID, FEPlayerData.class));
    }

    @Override
    public boolean hasAccount(UUID accountID, String worldName) {
        return hasAccount(accountID);
    }

    @Override
    public boolean renameAccount(UUID accountID, String name) {
        return false;
    }

    @Override
    public boolean renameAccount(String plugin, UUID accountID, String name) {
        return renameAccount(accountID, name);
    }

    @Override
    public boolean deleteAccount(String plugin, UUID accountID) {
        return false;
    }

    @Override
    public boolean accountSupportsCurrency(String plugin, UUID accountID, String currency) {
        return true;
    }

    @Override
    public boolean accountSupportsCurrency(String plugin, UUID accountID, String currency, String world) {
        return accountSupportsCurrency(plugin, accountID, currency);
    }

    @Override
    public BigDecimal getBalance(String pluginName, UUID accountID) {
        return BigDecimal.valueOf(getBalance(PlayerController.getPDSection(accountID, FEPlayerData.class)));
    }

    @Override
    public BigDecimal getBalance(String pluginName, UUID accountID, String world) {
        return getBalance(pluginName, accountID);
    }

    @Override
    public BigDecimal getBalance(String pluginName, UUID accountID, String world, String currency) {
        return getBalance(pluginName, accountID);
    }

    @Override
    public boolean has(String pluginName, UUID accountID, BigDecimal amount) {
        return has(PlayerController.getPDSection(accountID, FEPlayerData.class), amount.doubleValue());
    }

    @Override
    public boolean has(String pluginName, UUID accountID, String worldName, BigDecimal amount) {
        return has(pluginName, accountID, amount);
    }

    @Override
    public boolean has(String pluginName, UUID accountID, String worldName, String currency, BigDecimal amount) {
        return has(pluginName, accountID, amount);
    }

    @Override
    public net.milkbowl.vault2.economy.EconomyResponse withdraw(String pluginName, UUID accountID, BigDecimal amount) {
        EconomyResponse oldResponse = withdrawPlayer(PlayerController.getPDSection(accountID, FEPlayerData.class), amount.doubleValue());
        return new net.milkbowl.vault2.economy.EconomyResponse(
                BigDecimal.valueOf(oldResponse.amount),
                BigDecimal.valueOf(oldResponse.balance),
                net.milkbowl.vault2.economy.EconomyResponse.ResponseType.valueOf(oldResponse.type.name()),
                oldResponse.errorMessage
        );
    }

    @Override
    public net.milkbowl.vault2.economy.EconomyResponse withdraw(String pluginName, UUID accountID, String worldName, BigDecimal amount) {
        return withdraw(pluginName, accountID, amount);
    }

    @Override
    public net.milkbowl.vault2.economy.EconomyResponse withdraw(String pluginName, UUID accountID, String worldName, String currency, BigDecimal amount) {
        return withdraw(pluginName, accountID, amount);
    }

    @Override
    public net.milkbowl.vault2.economy.EconomyResponse deposit(String pluginName, UUID accountID, BigDecimal amount) {
        EconomyResponse oldResponse = depositPlayer(PlayerController.getPDSection(accountID, FEPlayerData.class), amount.doubleValue());
        return new net.milkbowl.vault2.economy.EconomyResponse(
                BigDecimal.valueOf(oldResponse.amount),
                BigDecimal.valueOf(oldResponse.balance),
                net.milkbowl.vault2.economy.EconomyResponse.ResponseType.valueOf(oldResponse.type.name()),
                oldResponse.errorMessage
        );
    }

    @Override
    public net.milkbowl.vault2.economy.EconomyResponse deposit(String pluginName, UUID accountID, String worldName, BigDecimal amount) {
        return deposit(pluginName, accountID, amount);
    }

    @Override
    public net.milkbowl.vault2.economy.EconomyResponse deposit(String pluginName, UUID accountID, String worldName, String currency, BigDecimal amount) {
        return deposit(pluginName, accountID, amount);
    }

    @Override
    public boolean createSharedAccount(String pluginName, UUID accountID, String name, UUID owner) {
        return false;
    }

    @Override
    public boolean isAccountOwner(String pluginName, UUID accountID, UUID uuid) {
        return false;
    }

    @Override
    public boolean setOwner(String pluginName, UUID accountID, UUID uuid) {
        return false;
    }

    @Override
    public boolean isAccountMember(String pluginName, UUID accountID, UUID uuid) {
        return false;
    }

    @Override
    public boolean addAccountMember(String pluginName, UUID accountID, UUID uuid) {
        return false;
    }

    @Override
    public boolean addAccountMember(String pluginName, UUID accountID, UUID uuid, AccountPermission... initialPermissions) {
        return false;
    }

    @Override
    public boolean removeAccountMember(String pluginName, UUID accountID, UUID uuid) {
        return false;
    }

    @Override
    public boolean hasAccountPermission(String pluginName, UUID accountID, UUID uuid, AccountPermission permission) {
        return false;
    }

    @Override
    public boolean updateAccountPermission(String pluginName, UUID accountID, UUID uuid, AccountPermission permission, boolean value) {
        return false;
    }
}