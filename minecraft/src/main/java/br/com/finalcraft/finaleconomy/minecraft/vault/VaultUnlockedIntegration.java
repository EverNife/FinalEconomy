package br.com.finalcraft.finaleconomy.minecraft.vault;

import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;
import br.com.finalcraft.finaleconomy.common.economy.vault2.FinalEconomyVault2;
import net.milkbowl.vault2.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

/**
 * Publishes this plugin as a Vault 2 economy on Bukkit, when VaultUnlocked is installed.
 *
 * <p>Detection and wiring are concentrated here so a server WITHOUT VaultUnlocked never resolves a
 * {@code net.milkbowl.vault2.*} type: the JVM links the references of a method only when it runs,
 * and no method of this class runs unless {@link #isPresent()} said yes.</p>
 */
public final class VaultUnlockedIntegration {

    private static FinalEconomyVault2 economy;

    private VaultUnlockedIntegration() {
    }

    public static boolean isPresent() {
        return FCReflectionUtil.getClasses().isClassLoaded("net.milkbowl.vault2.economy.Economy");
    }

    public static void register(ServicesManager servicesManager, Plugin plugin) {
        economy = new FinalEconomyVault2();
        servicesManager.register(Economy.class, economy, plugin, ServicePriority.Highest);
    }

    public static void unregister(ServicesManager servicesManager) {
        if (economy != null) {
            servicesManager.unregister(Economy.class, economy);
            economy = null;
        }
    }
}
