package br.com.finalcraft.finaleconomy.config;

import br.com.finalcraft.finaleconomy.FinalEconomy;

import java.text.NumberFormat;
import java.util.Locale;

public class FESettings {

    public static NumberFormat MONEY_FORMATTER = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt_BR"));

    public static void initialize(){
        String localeMoneyFormatter = ConfigManager.getMainConfig().getOrSetDefaultValue("Settings.moneyFormatLocale", "en_US");

        Locale locale = Locale.forLanguageTag(localeMoneyFormatter);
        if (locale == null){
            FinalEconomy.warning("No Locale found for [" + localeMoneyFormatter + "], using 'en_US' instead!");
            locale = Locale.forLanguageTag("en_US");
        }

        MONEY_FORMATTER = NumberFormat.getNumberInstance(locale);
        ConfigManager.getMainConfig().saveIfNewDefaults();
    }


}
