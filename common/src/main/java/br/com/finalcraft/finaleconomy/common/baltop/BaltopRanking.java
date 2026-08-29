package br.com.finalcraft.finaleconomy.common.baltop;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.finaleconomy.common.config.ConfigManager;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The balance ranking every reader of "who is the richest" shares: the {@code /baltop} page and the
 * top-N placeholders.
 *
 * <p><b>It never blocks.</b> {@link #current()} hands back the last completed read and, when that
 * read is older than {@code Settings.Placeholders.topTimeCache}, starts the next one in the
 * background. A scoreboard placeholder resolving on the server thread therefore costs a field read,
 * and the price of freshness is that the ranking can lag by up to the cache time - which is what the
 * setting has always bought.</p>
 */
public final class BaltopRanking {

    private static volatile List<FEPlayerData> ranking = Collections.emptyList();
    private static volatile long lastRefresh = 0L;
    private static final AtomicBoolean refreshing = new AtomicBoolean(false);

    private BaltopRanking() {
    }

    /** Every player ordered by balance, richest first. Possibly one cache-time stale, never blocking. */
    public static List<FEPlayerData> current() {
        if (System.currentTimeMillis() - lastRefresh >= ConfigManager.settings.getTopCacheTime().toMillis()) {
            refresh();
        }
        return ranking;
    }

    /** That player's 1-based rank, or {@code null} when they are not in the ranking. */
    public static Integer positionOf(UUID uuid) {
        List<FEPlayerData> snapshot = current();
        for (int i = 0; i < snapshot.size(); i++) {
            if (uuid.equals(snapshot.get(i).getUniqueId())) {
                return i + 1;
            }
        }
        return null;
    }

    /**
     * Starts a read of the backend, unless one is already in flight. The clock is wound on completion
     * whether the read succeeded or not, so a backend that is down is retried once per cache time
     * instead of on every placeholder.
     */
    public static void refresh() {
        if (!refreshing.compareAndSet(false, true)) {
            return;
        }
        FEPlayerData.rankedByBalance(0).whenComplete((rows, failure) -> {
            if (failure == null) {
                ranking = rows;
            } else {
                EverNifeCore.getLog().warning("Could not read the balance ranking: {}", failure.toString());
            }
            lastRefresh = System.currentTimeMillis();
            refreshing.set(false);
        });
    }

    /** Drops the ranking and its clock, so the next read starts from the backend. */
    public static void invalidate() {
        ranking = Collections.emptyList();
        lastRefresh = 0L;
    }
}
