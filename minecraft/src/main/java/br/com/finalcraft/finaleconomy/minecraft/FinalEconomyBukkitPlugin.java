package br.com.finalcraft.finaleconomy.minecraft;

import br.com.finalcraft.evernifecore.ecplugin.annotations.ECPlugin;
import br.com.finalcraft.evernifecore.minecraft.ecplugin.ECBukkitPlugin;
import br.com.finalcraft.finaleconomy.common.FinalEconomy;
import br.com.finalcraft.finaleconomy.minecraft.vault.FinalEconomyVault1;
import br.com.finalcraft.finaleconomy.minecraft.vault.VaultUnlockedIntegration;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

/**
 * Bukkit entry point. The shared wiring lives in {@link FinalEconomy}; only publishing this plugin as
 * the server's economy is Bukkit-specific.
 */
@ECPlugin(
        spigotID = "97740",
        bstatsID = "13365"
)
public class FinalEconomyBukkitPlugin extends ECBukkitPlugin implements FinalEconomy {

    private final FinalEconomyVault1 vaultEconomy = new FinalEconomyVault1();

    /**
     * Registered after the shared wiring, not before it: the balance section has to be registered
     * before anything can be charged through these services. Both are inside the same enable, so
     * they are still up before any other plugin is asked to work.
     */
    @Override
    public void onECPluginEnablePost() {
        ServicesManager servicesManager = getServer().getServicesManager();

        getLog().info("Integrating to VAULT...");
        servicesManager.register(Economy.class, vaultEconomy, this, ServicePriority.Highest);

        if (VaultUnlockedIntegration.isPresent()) {
            getLog().info("Integrating to VAULT v2...");
            VaultUnlockedIntegration.register(servicesManager, this);
        }
    }

    @Override
    public void onECPluginShutdownPost() {
        ServicesManager servicesManager = getServer().getServicesManager();
        servicesManager.unregister(Economy.class, vaultEconomy);
        if (VaultUnlockedIntegration.isPresent()) {
            VaultUnlockedIntegration.unregister(servicesManager);
        }
    }
}
