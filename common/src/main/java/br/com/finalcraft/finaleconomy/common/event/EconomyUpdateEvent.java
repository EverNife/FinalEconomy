package br.com.finalcraft.finaleconomy.common.event;

import br.com.finalcraft.evernifecore.api.events.base.ECEvent;
import br.com.finalcraft.evernifecore.api.events.base.IECEvent;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * A player's balance changed. Posted after the new balance is already in the section, so a handler
 * that reads the balance back sees the value this event announces.
 *
 * <p>It travels the EverNifeCore bus and mirrors onto the server, so a consumer can hear it either
 * way: a bus subscription works on both platforms, and on Bukkit a plain {@code @EventHandler} works
 * too. That second half is what lets an economy bridge keep listening the way it always did.</p>
 *
 * <p>Only the uuid travels, never the section: the balance can change for a player who is offline,
 * on another server of the network, or not resolvable to a name here.</p>
 */
public class EconomyUpdateEvent extends ECEvent implements IECEvent {

    /** Bukkit-only: this event's own native handler list. */
    public static Object getHandlerList() {
        return ECEvent.getHandlerListOf(EconomyUpdateEvent.class);
    }

    private final UUID uniqueId;
    private final BigDecimal previousBalance;
    private final BigDecimal currentBalance;

    public EconomyUpdateEvent(UUID uniqueId, BigDecimal previousBalance, BigDecimal currentBalance) {
        this.uniqueId = uniqueId;
        this.previousBalance = previousBalance;
        this.currentBalance = currentBalance;
    }

    /** Whose balance moved. */
    public UUID getUniqueId() {
        return uniqueId;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    /** Negative when money left the account. */
    public BigDecimal getDelta() {
        return currentBalance.subtract(previousBalance);
    }

}
