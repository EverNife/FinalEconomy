package br.com.finalcraft.finaleconomy.common.economy;

import br.com.finalcraft.evernifecore.economy.EcoResponse;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.finaleconomy.common.config.ConfigManager;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The balance operations behind every economy bridge this plugin publishes, and the ONLY place it
 * resolves a section synchronously.
 *
 * <p><b>Why synchronous at all.</b> This plugin IMPLEMENTS an interface owned by somebody else: a
 * shop calls {@code Economy.withdraw(...)} on the server thread, inside a click event, and the
 * signature has to hand back a number right there - there is no continuation to hang a
 * {@code thenAccept} on. The answer is not a scattered {@code join()}: it is having the data in
 * memory already, which is what an account row being resident while a member is online gives for
 * free. {@link #sectionOf(UUID)} is the single documented fallback for the player who is not.</p>
 *
 * <p><b>The offline path costs a read.</b> An account row is released once the last member quits, and
 * {@code AccountSectionConfiguration} has no lifecycle to override that - so paying an offline player
 * resolves through storage on the calling thread. It is the one blocking read in this plugin and it
 * is here, named, instead of scattered.</p>
 *
 * <p>Amounts are {@link BigDecimal} end to end, which is also what Vault 2 speaks; the outcome
 * travels as EverNifeCore's {@link EcoResponse}, carrying the amount and the resulting balance both
 * Vault generations need to build their own response.</p>
 */
public class EconomyService {

    /**
     * That player's balance section, resolved without leaving this thread.
     *
     * <p>Answers from the cache while any member of the account is online. The {@code join()} is the
     * fallback for everybody else, and it is the only blocking read in this plugin.</p>
     */
    public static FEPlayerData sectionOf(UUID playerUuid) {
        FEPlayerData loaded = PlayerController.getLoadedAccountSection(playerUuid, FEPlayerData.class);
        return loaded != null ? loaded : PlayerController.getAccountSection(playerUuid, FEPlayerData.class).join();
    }

    public static BigDecimal getBalance(UUID uuid) {
        return sectionOf(uuid).getMoney();
    }

    public static boolean has(UUID uuid, BigDecimal amount) {
        return sectionOf(uuid).hasMoney(amount);
    }

    /** Takes {@code amount}, or refuses without moving anything when the balance does not cover it. */
    public static EcoResponse withdraw(UUID uuid, BigDecimal amount) {
        FEPlayerData section = sectionOf(uuid);

        if (amount.signum() < 0) {
            return EcoResponse.invalidAmount(amount);
        }
        if (!section.hasMoney(amount)) {
            return EcoResponse.insufficientFunds(amount, section.getMoney());
        }

        section.removeMoney(amount);
        return EcoResponse.success(amount, section.getMoney());
    }

    /** Adds {@code amount}; zero succeeds as a no-op. */
    public static EcoResponse deposit(UUID uuid, BigDecimal amount) {
        FEPlayerData section = sectionOf(uuid);

        if (amount.signum() < 0) {
            return EcoResponse.invalidAmount(amount);
        }

        section.addMoney(amount);
        return EcoResponse.success(amount, section.getMoney());
    }

    /** How this economy renders a raw amount for another plugin. */
    public static String format(BigDecimal amount) {
        return ConfigManager.settings.getMoneyFormat().format(amount);
    }
}
