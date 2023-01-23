package br.com.finalcraft.finaleconomy.api.events.base;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract  class EconomyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public EconomyEvent() {
    }

    public EconomyEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
