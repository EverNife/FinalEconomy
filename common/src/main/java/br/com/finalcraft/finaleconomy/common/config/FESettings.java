package br.com.finalcraft.finaleconomy.common.config;

import br.com.finalcraft.everyconfig.config.Config;
import br.com.finalcraft.evernifecore.EverNifeCore;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

/**
 * An immutable settings model, read from {@code config.yml} with
 * {@code getOrSetValueIfAbsent(path, default, comment)} - the commented file is generated from this
 * code, so a default and its documentation can never drift apart.
 */
public class FESettings {

    /** How many entries one page of {@code /baltop} holds; the {@code maxPages} setting counts in these. */
    public static final int BALTOP_PAGE_SIZE = 10;

    private final NumberFormat moneyFormatter;
    private final Duration topCacheTime;
    private final boolean baltopIncludeTotalUsersCount;
    private final boolean baltopIncludeDayOfToday;
    private final int baltopMaxPages;
    private final boolean notifyOnEcoGive;
    private final boolean notifyOnEcoTake;
    private final boolean notifyOnEcoSet;

    private FESettings(NumberFormat moneyFormatter, Duration topCacheTime,
                       boolean baltopIncludeTotalUsersCount, boolean baltopIncludeDayOfToday,
                       int baltopMaxPages, boolean notifyOnEcoGive, boolean notifyOnEcoTake,
                       boolean notifyOnEcoSet) {
        this.moneyFormatter = moneyFormatter;
        this.topCacheTime = topCacheTime;
        this.baltopIncludeTotalUsersCount = baltopIncludeTotalUsersCount;
        this.baltopIncludeDayOfToday = baltopIncludeDayOfToday;
        this.baltopMaxPages = baltopMaxPages;
        this.notifyOnEcoGive = notifyOnEcoGive;
        this.notifyOnEcoTake = notifyOnEcoTake;
        this.notifyOnEcoSet = notifyOnEcoSet;
    }

    public static FESettings load(Config config) {

        String moneyFormatLocale = config.getOrSetValueIfAbsent(
                "Settings.moneyFormatLocale",
                "en-US",
                "This means the way the number will be formatted, for example:"
                        + "\non 'en-US' the value of 10000000 will be 10,000,000"
                        + "\non 'pt-BR' the value of 10000000 will be 10.000.000"
        );

        int topTimeCache = config.getOrSetValueIfAbsent(
                "Settings.Placeholders.topTimeCache",
                5,
                "The time in seconds that the top list will be cached."
                        + "\nThis is to prevent the server from lagging when the top list is requested too often."
                        + "\nRecommended to keep at least in 1 second!"
        );

        boolean includeTotalUsersCount = config.getOrSetValueIfAbsent(
                "Settings.Baltop.includeTotalUsersCount",
                true,
                "If true, the baltop command will include the total users count on the first line!"
        );

        boolean includeDayOfToday = config.getOrSetValueIfAbsent(
                "Settings.Baltop.includeDayOfToday",
                true,
                "If true, the baltop command will include the [day of today] on the second line!"
        );

        int maxPages = config.getOrSetValueIfAbsent(
                "Settings.Baltop.maxPages",
                -1,
                "Defines how many pages will be shown on the baltop command, if -1, will show all pages!"
        );

        boolean notifyOnEcoGive = config.getOrSetValueIfAbsent(
                "Settings.Notification.notifyOnEcoGive",
                false,
                "If true, will notify the player when he receives money from the command"
                        + "\n'/eco give <Player>'! If false, will not notify the player!"
        );

        boolean notifyOnEcoTake = config.getOrSetValueIfAbsent(
                "Settings.Notification.notifyOnEcoTake",
                false,
                "If true, will notify the player when he loses money from the command"
                        + "\n'/eco take <Player>'! If false, will not notify the player!"
        );

        boolean notifyOnEcoSet = config.getOrSetValueIfAbsent(
                "Settings.Notification.notifyOnEcoSet",
                false,
                "If true, will notify the player when his money is set from the command"
                        + "\n'/eco set <Player>'! If false, will not notify the player!"
        );

        return new FESettings(moneyFormatterOf(moneyFormatLocale), Duration.ofSeconds(Math.max(1, topTimeCache)),
                includeTotalUsersCount, includeDayOfToday, maxPages,
                notifyOnEcoGive, notifyOnEcoTake, notifyOnEcoSet);
    }

    /**
     * {@code Locale.forLanguageTag} reads IETF tags ({@code pt-BR}), so the underscore spelling this
     * setting used to document parses to no language at all instead of failing - hence the rewrite
     * and the emptiness check, which is what "unparseable" really looks like here.
     */
    private static NumberFormat moneyFormatterOf(String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag.replace('_', '-'));
        if (locale.getLanguage().isEmpty()) {
            EverNifeCore.getLog().warning("No Locale found for [{}], using 'en-US' instead!", languageTag);
            locale = Locale.forLanguageTag("en-US");
        }
        return NumberFormat.getNumberInstance(locale);
    }

    public NumberFormat getMoneyFormatter() {
        return moneyFormatter;
    }

    /** How long one read of the balance ranking stays good for. */
    public Duration getTopCacheTime() {
        return topCacheTime;
    }

    public boolean isBaltopIncludeTotalUsersCount() {
        return baltopIncludeTotalUsersCount;
    }

    public boolean isBaltopIncludeDayOfToday() {
        return baltopIncludeDayOfToday;
    }

    /** Pages of {@link #BALTOP_PAGE_SIZE} entries the ranking is capped at; zero or less means no cap. */
    public int getBaltopMaxPages() {
        return baltopMaxPages;
    }

    public boolean isNotifyOnEcoGive() {
        return notifyOnEcoGive;
    }

    public boolean isNotifyOnEcoTake() {
        return notifyOnEcoTake;
    }

    public boolean isNotifyOnEcoSet() {
        return notifyOnEcoSet;
    }
}
