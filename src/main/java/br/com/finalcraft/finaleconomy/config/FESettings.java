package br.com.finalcraft.finaleconomy.config;

import br.com.finalcraft.evernifecore.version.MCVersion;
import br.com.finalcraft.finaleconomy.FinalEconomy;

import java.text.NumberFormat;
import java.util.Locale;

public class FESettings {

    public static NumberFormat MONEY_FORMATTER = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt_BR"));

    public static boolean allowAsyncEconomyChanges = false;

    public static boolean BALTOP_INCLUDE_TOTAL_USERS_COUNT = true;
    public static boolean BALTOP_INCLUDE_DAY_OF_TODAY = true;
    public static int BALTOP_MAX_PAGES = -1;

    public static void initialize(){

        String localeMoneyFormatter = ConfigManager.getMainConfig().getOrSetDefaultValue(
                "Settings.moneyFormatLocale",
                "en_US",
                "This means the way the number will be formatted, for example: " +
                        "\non 'en_US' the value of 10000000 will be 100,000,000" +
                        "\non 'pt_BR' the value of 10000000 will be 100.000.000"
        );

        Locale locale = Locale.forLanguageTag(localeMoneyFormatter);
        if (locale == null){
            FinalEconomy.instance.getLogger().warning("No Locale found for [" + localeMoneyFormatter + "], using 'en_US' instead!");
            locale = Locale.forLanguageTag("en_US");
        }
        MONEY_FORMATTER = NumberFormat.getNumberInstance(locale);

        if (MCVersion.isHigherEquals(MCVersion.v1_13)){
            allowAsyncEconomyChanges = ConfigManager.getMainConfig().getOrSetDefaultValue(
                    "Settings.allowAsyncEconomyChanges",
                    false,
                    "By default, almost all plugins do economy transactions from the main thread! " +
                            "\nBut some plugins do it from an async thread, and this can cause some problems, " +
                            "\nso if you are using a plugin that does economy transactions from an async thread, " +
                            "\nset this to true! (FinalEconomy will try to handle this scenarios)" +
                            "\n" +
                            "\nTo be clear, this will not FIX the problem, will just 'minimize it', the problem is still there!" +
                            "\nJust waiting to go wrong!" +
                            "\n" +
                            "\nThe author of such plugin should fix it, not you or me!"
            );
        }

        BALTOP_INCLUDE_TOTAL_USERS_COUNT = ConfigManager.getMainConfig().getOrSetDefaultValue(
                "Settings.Baltop.includeTotalUsersCount",
                true,
                "If true, the baltop command will include the total users count on the first line!"
        );

        BALTOP_INCLUDE_DAY_OF_TODAY = ConfigManager.getMainConfig().getOrSetDefaultValue(
                "Settings.Baltop.includeDayOfToday",
                true,
                "If true, the baltop command will include the [day of today] on the second line!"
        );

        BALTOP_MAX_PAGES = ConfigManager.getMainConfig().getOrSetDefaultValue(
                "Settings.Baltop.maxPages",
                -1,
                "Defines how many pages will be shown on the baltop command, if -1, will show all pages!"
        ) * 10;

        ConfigManager.getMainConfig().saveIfNewDefaults();
    }


}
