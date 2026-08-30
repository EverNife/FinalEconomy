package br.com.finalcraft.finaleconomy.common.event;

import br.com.finalcraft.evernifecore.api.events.base.ECEvent;
import br.com.finalcraft.evernifecore.api.events.base.IECEvent;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * An account's balance changed. Posted after the new balance is already in the section, so a handler
 * that reads the balance back sees the value this event announces.
 *
 * <p>It travels the EverNifeCore bus and mirrors onto the server, so a consumer can hear it either
 * way: a bus subscription works on both platforms, and on Bukkit a plain {@code @EventHandler} works
 * too. That second half is what lets an economy bridge keep listening the way it always did.</p>
 *
 * <p>What travels is the ACCOUNT id, never the section and never a player: the wallet belongs to the
 * account, and the change may have been made for somebody offline, on another server of the network,
 * or linked under an identity this server never saw. {@code PlayerController.getAccountMembers(id)}
 * is how a handler gets from here to the players it means.</p>
 */
public class EconomyUpdateEvent extends ECEvent implements IECEvent {

    /** Bukkit-only: this event's own native handler list. */
    public static Object getHandlerList() {
        return ECEvent.getHandlerListOf(EconomyUpdateEvent.class);
    }

    private final UUID accountId;
    private final BigDecimal previousBalance;
    private final BigDecimal currentBalance;

    public EconomyUpdateEvent(UUID accountId, BigDecimal previousBalance, BigDecimal currentBalance) {
        this.accountId = accountId;
        this.previousBalance = previousBalance;
        this.currentBalance = currentBalance;
    }

    /** The account whose balance moved. */
    public UUID getAccountId() {
        return accountId;
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
