package br.com.finalcraft.finaleconomy.common.economy.vault2;

import br.com.finalcraft.evernifecore.config.uuids.UUIDsController;
import br.com.finalcraft.evernifecore.economy.EcoResponse;
import br.com.finalcraft.finaleconomy.common.economy.EconomyService;
import net.milkbowl.vault2.economy.AccountPermission;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This plugin as a Vault 2 (VaultUnlocked) economy: UUID-keyed, BigDecimal-valued, single currency.
 *
 * <p>Lives in the agnostic module because the contract names no server type, and both platforms
 * publish an economy through it - the Bukkit {@code ServicesManager} and the Hytale
 * {@code VaultUnlockedServicesManager} take this same class.</p>
 *
 * <p>Shared accounts and multiple currencies are not supported and every such answer says so.
 * Account existence is always true: a balance exists for anyone the server has ever seen.</p>
 */
public class FinalEconomyVault2 implements Economy {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "FinalEconomy";
    }

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
        return EconomyService.format(amount);
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
        return Collections.emptyList();
    }

    // ------------------------------------------------------------------
    //  Accounts
    // ------------------------------------------------------------------

    @Override
    public boolean createAccount(UUID accountID, String name) {
        return accountID != null; //only create an account for a REAL player
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
        return Collections.emptyMap();
    }

    @Override
    public Optional<String> getAccountName(UUID accountID) {
        return Optional.ofNullable(UUIDsController.getNameFromUUID(accountID));
    }

    @Override
    public boolean hasAccount(UUID accountID) {
        return true; //we assume every player has an account
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

    // ------------------------------------------------------------------
    //  Balance
    // ------------------------------------------------------------------

    @Override
    public BigDecimal getBalance(String pluginName, UUID accountID) {
        return EconomyService.getBalance(accountID);
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
        return EconomyService.has(accountID, amount);
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
    public EconomyResponse withdraw(String pluginName, UUID accountID, BigDecimal amount) {
        return responseOf(EconomyService.withdraw(accountID, amount));
    }

    @Override
    public EconomyResponse withdraw(String pluginName, UUID accountID, String worldName, BigDecimal amount) {
        return withdraw(pluginName, accountID, amount);
    }

    @Override
    public EconomyResponse withdraw(String pluginName, UUID accountID, String worldName, String currency, BigDecimal amount) {
        return withdraw(pluginName, accountID, amount);
    }

    @Override
    public EconomyResponse deposit(String pluginName, UUID accountID, BigDecimal amount) {
        return responseOf(EconomyService.deposit(accountID, amount));
    }

    @Override
    public EconomyResponse deposit(String pluginName, UUID accountID, String worldName, BigDecimal amount) {
        return deposit(pluginName, accountID, amount);
    }

    @Override
    public EconomyResponse deposit(String pluginName, UUID accountID, String worldName, String currency, BigDecimal amount) {
        return deposit(pluginName, accountID, amount);
    }

    private static EconomyResponse responseOf(EcoResponse response) {
        return new EconomyResponse(
                response.getAmount(),
                response.getBalance(),
                response.isSuccess()
                        ? EconomyResponse.ResponseType.SUCCESS
                        : EconomyResponse.ResponseType.FAILURE,
                response.isSuccess() ? null : failureTextOf(response));
    }

    private static String failureTextOf(EcoResponse response) {
        return response.getDetail() != null ? response.getDetail() : response.getReason().name();
    }

    // ------------------------------------------------------------------
    //  Shared accounts - unsupported, and each answer says so
    // ------------------------------------------------------------------

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
