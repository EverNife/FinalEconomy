package br.com.finalcraft.finaleconomy.common.config.settings;

import br.com.finalcraft.everyconfig.annotation.Comment;

/**
 * Whether the target of an admin balance change hears about it. Bound to
 * {@code Settings.Notification}.
 */
public class NotificationSettings {

    @Comment("If true, tells the player when an admin gives them money.")
    private boolean notifyOnEcoGive = false;

    @Comment("If true, tells the player when an admin takes money from them.")
    private boolean notifyOnEcoTake = false;

    @Comment("If true, tells the player when an admin sets their balance.")
    private boolean notifyOnEcoSet = false;

    public boolean isNotifyOnEcoGive() {
        return notifyOnEcoGive;
    }

    public void setNotifyOnEcoGive(boolean notifyOnEcoGive) {
        this.notifyOnEcoGive = notifyOnEcoGive;
    }

    public boolean isNotifyOnEcoTake() {
        return notifyOnEcoTake;
    }

    public void setNotifyOnEcoTake(boolean notifyOnEcoTake) {
        this.notifyOnEcoTake = notifyOnEcoTake;
    }

    public boolean isNotifyOnEcoSet() {
        return notifyOnEcoSet;
    }

    public void setNotifyOnEcoSet(boolean notifyOnEcoSet) {
        this.notifyOnEcoSet = notifyOnEcoSet;
    }

}
