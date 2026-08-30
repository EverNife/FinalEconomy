package br.com.finalcraft.finaleconomy.common.config.settings;

import br.com.finalcraft.everyconfig.annotation.Comment;

import java.time.Duration;

/**
 * What the placeholder layer is allowed to cost. Bound to {@code Settings.Placeholders}.
 */
public class PlaceholderSettings {

    @Comment({
            "How long, in seconds, one read of the balance ranking stays good for.",
            "A scoreboard asking for the top player every tick reads the cache, not the database.",
            "Values below 1 are raised to 1."
    })
    private int topTimeCache = 5;

    public int getTopTimeCache() {
        return topTimeCache;
    }

    public void setTopTimeCache(int topTimeCache) {
        this.topTimeCache = topTimeCache;
    }

    /** The cache window, never shorter than a second whatever the file says. */
    public Duration topCacheDuration() {
        return Duration.ofSeconds(Math.max(1, topTimeCache));
    }

}
