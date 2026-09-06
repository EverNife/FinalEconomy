package br.com.finalcraft.finaleconomy.hytale;

import br.com.finalcraft.evernifecore.ecplugin.annotations.ECPlugin;
import br.com.finalcraft.evernifecore.hytale.ecplugin.ECHytalePlugin;
import br.com.finalcraft.finaleconomy.common.FinalEconomy;
import br.com.finalcraft.finaleconomy.hytale.vault.VaultUnlockedIntegration;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

/**
 * Hytale entry point. The shared wiring lives in {@link FinalEconomy}; only publishing this plugin as
 * the server's economy is platform-specific - and on Hytale that is the Vault 2 bridge, which is the
 * only economy contract this platform has.
 */
@ECPlugin
public class FinalEconomyHytalePlugin extends ECHytalePlugin implements FinalEconomy {

    public FinalEconomyHytalePlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    public void onECPluginEnablePost() {
        if (VaultUnlockedIntegration.isPresent()) {
            getLog().info("Integrating to VAULT v2...");
            VaultUnlockedIntegration.register();
        } else {
            getLog().warning("VaultUnlocked was not found - no plugin on this server can reach this economy.");
        }
    }

    @Override
    public void onECPluginShutdownPost() {
        if (VaultUnlockedIntegration.isPresent()) {
            VaultUnlockedIntegration.unregister();
        }
    }
}
