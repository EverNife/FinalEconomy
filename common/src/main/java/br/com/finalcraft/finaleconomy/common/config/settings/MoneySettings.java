package br.com.finalcraft.finaleconomy.common.config.settings;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.everyconfig.annotation.Comment;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * How an amount of money is written out. Bound to {@code Settings.Money}.
 */
public class MoneySettings {

    @Comment({
            "The locale that decides how a number is written, for example:",
            "on 'en-US' the value of 10000000 becomes 10,000,000",
            "on 'pt-BR' the value of 10000000 becomes 10.000.000"
    })
    private String locale = "en-US";

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * A formatter for {@link #getLocale()}, falling back to {@code en-US} when the tag names no
     * language.
     *
     * <p>{@code Locale.forLanguageTag} reads IETF tags ({@code pt-BR}), so the underscore spelling
     * this setting used to document parses to no language at all instead of failing - which is why
     * the check is on an empty language rather than on a null locale.</p>
     */
    public NumberFormat newFormatter() {
        Locale parsed = Locale.forLanguageTag(locale.replace('_', '-'));
        if (parsed.getLanguage().isEmpty()) {
            EverNifeCore.getLog().warning("No Locale found for [{}], using 'en-US' instead!", locale);
            parsed = Locale.forLanguageTag("en-US");
        }
        return NumberFormat.getNumberInstance(parsed);
    }

}
