package br.com.finalcraft.finaleconomy.minecraft.vault;

import br.com.finalcraft.evernifecore.config.uuids.UUIDsController;
import br.com.finalcraft.evernifecore.economy.EcoResponse;
import br.com.finalcraft.finaleconomy.common.economy.EconomyService;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This plugin as a Vault 1 economy - the {@code OfflinePlayer}-keyed generation, which is why it
 * lives in the Bukkit module while the Vault 2 bridge is platform-agnostic.
 *
 * <p>Banks are not supported. Every bank method answers with a refusal carrying the reason instead
 * of {@code null}: a caller that does not check {@link #hasBankSupport()} first would otherwise get
 * a NullPointerException inside its own code.</p>
 */
public class FinalEconomyVault1 implements Economy {

    private static final String NO_BANK_SUPPORT = "FinalEconomy does not support bank accounts";

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "FinalEconomy";
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

    @Override
    public String format(double amount) {
        return EconomyService.format(amount);
    }

    // ------------------------------------------------------------------
    //  Accounts
    // ------------------------------------------------------------------

    @Override
    public boolean hasAccount(String playerName) {
        return uuidOf(playerName) != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true; //we assume every player has an account
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
    public boolean createPlayerAccount(String playerName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return player != null; //only create an account for a REAL player
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }

    // ------------------------------------------------------------------
    //  Balance
    // ------------------------------------------------------------------

    @Override
    public double getBalance(String playerName) {
        UUID uuid = uuidOf(playerName);
        return uuid == null ? 0D : EconomyService.getBalance(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return EconomyService.getBalance(player.getUniqueId());
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
        UUID uuid = uuidOf(playerName);
        return uuid != null && EconomyService.has(uuid, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return EconomyService.has(player.getUniqueId(), amount);
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
        UUID uuid = uuidOf(playerName);
        return uuid == null ? unknownPlayer(playerName) : responseOf(EconomyService.withdraw(uuid, amount));
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return responseOf(EconomyService.withdraw(player.getUniqueId(), amount));
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
        UUID uuid = uuidOf(playerName);
        return uuid == null ? unknownPlayer(playerName) : responseOf(EconomyService.deposit(uuid, amount));
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return responseOf(EconomyService.deposit(player.getUniqueId(), amount));
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    // ------------------------------------------------------------------
    //  Banks - unsupported, and every answer says so instead of being null
    // ------------------------------------------------------------------

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return noBankSupport();
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return noBankSupport();
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    // ------------------------------------------------------------------

    /** The uuid behind a name the server has seen before, or null when it has seen none. */
    private static UUID uuidOf(String playerName) {
        return UUIDsController.getUUIDFromName(playerName);
    }

    private static EconomyResponse responseOf(EcoResponse response) {
        return new EconomyResponse(
                response.getAmount().doubleValue(),
                response.getBalance().doubleValue(),
                response.isSuccess()
                        ? EconomyResponse.ResponseType.SUCCESS
                        : EconomyResponse.ResponseType.FAILURE,
                response.isSuccess() ? null : failureTextOf(response));
    }

    private static String failureTextOf(EcoResponse response) {
        return response.getDetail() != null ? response.getDetail() : response.getReason().name();
    }

    private static EconomyResponse unknownPlayer(String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                "No player named '" + playerName + "' is known to this server");
    }

    private static EconomyResponse noBankSupport() {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, NO_BANK_SUPPORT);
    }
}
