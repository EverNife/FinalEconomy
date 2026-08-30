package br.com.finalcraft.finaleconomy.common.config.settings;

import br.com.finalcraft.everyconfig.annotation.Comment;

/**
 * What the {@code /baltop} page shows and how far it goes. Bound to {@code Settings.Baltop}.
 */
public class BaltopSettings {

    /** How many entries one page holds; {@link #getMaxPages()} counts in these. */
    public static final int PAGE_SIZE = 10;

    @Comment("If true, the first line of the baltop shows how many players exist in total.")
    private boolean includeTotalUsersCount = true;

    @Comment("If true, the second line of the baltop shows today's date.")
    private boolean includeDayOfToday = true;

    @Comment({
            "How many pages the baltop is capped at.",
            "Use -1 (or any value below 1) for no cap at all."
    })
    private int maxPages = -1;

    public boolean isIncludeTotalUsersCount() {
        return includeTotalUsersCount;
    }

    public void setIncludeTotalUsersCount(boolean includeTotalUsersCount) {
        this.includeTotalUsersCount = includeTotalUsersCount;
    }

    public boolean isIncludeDayOfToday() {
        return includeDayOfToday;
    }

    public void setIncludeDayOfToday(boolean includeDayOfToday) {
        this.includeDayOfToday = includeDayOfToday;
    }

    /** Pages of {@link #PAGE_SIZE} entries the ranking is capped at; below one means no cap. */
    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    /** How many ranking rows the cap allows, or zero when there is no cap. */
    public int cappedRowCount() {
        return maxPages < 1 ? 0 : maxPages * PAGE_SIZE;
    }

}
