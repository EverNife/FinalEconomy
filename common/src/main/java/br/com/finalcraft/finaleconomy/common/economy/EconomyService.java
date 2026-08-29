package br.com.finalcraft.finaleconomy.common.economy;

import br.com.finalcraft.evernifecore.economy.EcoResponse;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.everylibs.util.FCMathUtil;
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
 * memory already, which is what {@code SectionLifecycle.PRELOADED} buys (see
 * {@code PlayerDataRegistry}). {@link #sectionOf(UUID)} is the single documented fallback for the
 * player who is not in the cache yet.</p>
 *
 * <p>Amounts are {@code double} because that is what the balance is stored as; the outcome travels
 * as EverNifeCore's {@link EcoResponse}, which carries the amount and the resulting balance both
 * Vault generations need to build their own response.</p>
 */
public final class EconomyService {

    private EconomyService() {
    }

    /**
     * That player's balance section, resolved without leaving this thread.
     *
     * <p>Answers from the cache, which under {@code PRELOADED} holds every known player. The
     * {@code join()} is the fallback for a player the collection does not have yet - a brand new
     * account - and it is the only blocking read in this plugin.</p>
     */
    public static FEPlayerData sectionOf(UUID uuid) {
        FEPlayerData loaded = PlayerController.getLoadedSection(uuid, FEPlayerData.class);
        return loaded != null ? loaded : PlayerController.getPDSection(uuid, FEPlayerData.class).join();
    }

    public static double getBalance(UUID uuid) {
        return sectionOf(uuid).getMoney();
    }

    public static boolean has(UUID uuid, double amount) {
        return sectionOf(uuid).hasMoney(amount);
    }

    /** Takes {@code amount}, or refuses without moving anything when the balance does not cover it. */
    public static EcoResponse withdraw(UUID uuid, double amount) {
        FEPlayerData section = sectionOf(uuid);

        if (amount < 0) {
            return EcoResponse.invalidAmount(BigDecimal.valueOf(amount));
        }
        if (!section.hasMoney(amount)) {
            return EcoResponse.insufficientFunds(BigDecimal.valueOf(amount), BigDecimal.valueOf(section.getMoney()));
        }

        section.removeMoney(amount);
        return EcoResponse.success(BigDecimal.valueOf(amount), BigDecimal.valueOf(section.getMoney()));
    }

    /** Adds {@code amount}; zero succeeds as a no-op. */
    public static EcoResponse deposit(UUID uuid, double amount) {
        FEPlayerData section = sectionOf(uuid);

        if (amount < 0) {
            return EcoResponse.invalidAmount(BigDecimal.valueOf(amount));
        }

        section.addMoney(amount);
        return EcoResponse.success(BigDecimal.valueOf(amount), BigDecimal.valueOf(section.getMoney()));
    }

    /** How this economy renders a raw amount for another plugin. */
    public static String format(double amount) {
        return FCMathUtil.toString(amount);
    }
}
