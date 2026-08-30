package br.com.finalcraft.finaleconomy.common.config;

import br.com.finalcraft.everyconfig.config.Config;
import br.com.finalcraft.finaleconomy.common.config.settings.BaltopSettings;
import br.com.finalcraft.finaleconomy.common.config.settings.MoneySettings;
import br.com.finalcraft.finaleconomy.common.config.settings.NotificationSettings;
import br.com.finalcraft.finaleconomy.common.config.settings.PlaceholderSettings;

import java.text.NumberFormat;

/**
 * The whole of {@code config.yml}, as one typed object per block of it.
 *
 * <p>Each block is a POJO bound with {@code getOrMergeValue}, which is merge semantics rather than
 * set-if-absent: a key already in the file wins, and only the keys the file lacks are seeded, with
 * their comment. A setting added in a later release therefore appears on upgrade without resetting
 * what the admin already tuned - which key-by-key reading could not promise for a block that existed
 * but was missing one entry.</p>
 */
public class FESettings {

    private final MoneySettings money;
    private final BaltopSettings baltop;
    private final NotificationSettings notification;
    private final PlaceholderSettings placeholders;
    private final NumberFormat moneyFormat;

    public FESettings(Config config) {
        this.money = config.getOrMergeValue("Settings.Money", new MoneySettings());
        this.baltop = config.getOrMergeValue("Settings.Baltop", new BaltopSettings());
        this.notification = config.getOrMergeValue("Settings.Notification", new NotificationSettings());
        this.placeholders = config.getOrMergeValue("Settings.Placeholders", new PlaceholderSettings());
        this.moneyFormat = money.newFormatter();
    }

    public MoneySettings getMoney() {
        return money;
    }

    public BaltopSettings getBaltop() {
        return baltop;
    }

    public NotificationSettings getNotification() {
        return notification;
    }

    public PlaceholderSettings getPlaceholders() {
        return placeholders;
    }

    /**
     * The formatter every balance is rendered through - resolved once per load, because building one
     * per placeholder resolve would put a locale lookup on the server thread.
     */
    public NumberFormat getMoneyFormat() {
        return moneyFormat;
    }

}
