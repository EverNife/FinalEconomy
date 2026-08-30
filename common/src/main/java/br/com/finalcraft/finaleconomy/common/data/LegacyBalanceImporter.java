package br.com.finalcraft.finaleconomy.common.data;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.playerdata.PlayerController;
import br.com.finalcraft.everydatabase.query.Query;
import br.com.finalcraft.everydatabase.query.QueryOptions;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Moves every balance the core imported from the 2.x files into the account row that now owns it.
 *
 * <p>Runs once per amount, not once per boot: each {@link FELegacyBalance} is marked claimed as it is
 * folded in, and the pass asks the backend only for the unclaimed ones. A server interrupted halfway
 * finishes on the next start, and a server with nothing left to import asks one indexed question and
 * stops.</p>
 *
 * <p>It reads and never deletes - the {@code FELegacyBalance} row survives with its amount and its
 * claimed flag, so what was imported stays auditable next to what it became.</p>
 */
public class LegacyBalanceImporter {

    /**
     * Folds the pending amounts in, in the background.
     *
     * <p>Resolution goes through the player: an amount was stored under a player uuid, and only that
     * player knows which account it belongs to today. For a server upgrading from 2.x that account is
     * the player itself - nothing has been linked yet - but going through the player is what keeps
     * the import correct on a server where something already was.</p>
     */
    public static CompletableFuture<Integer> claimPending() {
        return PlayerController.get()
                .querySection(FELegacyBalance.class, Query.eq("claimed", false), QueryOptions.none())
                .thenCompose(LegacyBalanceImporter::claimAll)
                .whenComplete((claimed, failure) -> {
                    if (failure != null) {
                        EverNifeCore.getLog().warning("Could not import the 2.x balances: {}"
                                + " - they stay unclaimed and the next start tries again.", failure.toString());
                    } else if (claimed > 0) {
                        EverNifeCore.getLog().info("Imported {} balance(s) from the 2.x files.", claimed);
                    }
                });
    }

    private static CompletableFuture<Integer> claimAll(List<FELegacyBalance> pending) {
        CompletableFuture<Integer> chain = CompletableFuture.completedFuture(0);
        for (FELegacyBalance row : pending) {
            UUID uuid = row.getUniqueId();
            chain = chain.thenCompose(claimed -> claimOne(uuid).thenApply(moved -> claimed + (moved ? 1 : 0)));
        }
        return chain;
    }

    /**
     * One player's amount. The rows the query returned are detached, so both sides are resolved live
     * here: the amount is added to the account and the legacy row is marked in the same step, and a
     * failure on either leaves the flag untouched for the next start.
     */
    private static CompletableFuture<Boolean> claimOne(UUID uuid) {
        return PlayerController.getPDSection(uuid, FELegacyBalance.class).thenCompose(legacy -> {
            if (legacy == null || legacy.isClaimed()) {
                return CompletableFuture.completedFuture(false);
            }
            return PlayerController.getAccountSection(uuid, FEPlayerData.class).thenApply(account -> {
                if (account == null) {
                    return false;
                }
                account.addMoney(BigDecimal.valueOf(legacy.getMoney()));
                legacy.markClaimed();
                return true;
            });
        });
    }
}
