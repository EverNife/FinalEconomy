package br.com.finalcraft.finaleconomy.hytale.vault;

import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;
import br.com.finalcraft.finaleconomy.common.economy.vault2.FinalEconomyVault2;
import net.cfh.vault.VaultUnlockedServicesManager;

/**
 * Publishes this plugin as the Hytale server's economy, when VaultUnlocked is installed.
 *
 * <p>{@code VaultUnlockedServicesManager} is exactly what EverNifeCore's own Hytale integration
 * reads to find an economy, so registering here is what makes {@code FCEcoUtil} answer on this
 * platform. VaultUnlocked is an optional dependency, so detection and wiring are concentrated in
 * this class: a server without it never resolves a {@code net.cfh.vault} type.</p>
 */
public final class VaultUnlockedIntegration {

    private static FinalEconomyVault2 economy;

    private VaultUnlockedIntegration() {
    }

    public static boolean isPresent() {
        return FCReflectionUtil.getClasses().isClassLoaded("net.cfh.vault.VaultUnlocked");
    }

    public static void register() {
        economy = new FinalEconomyVault2();
        VaultUnlockedServicesManager.get().economy(economy);
    }

    public static void unregister() {
        if (economy != null) {
            VaultUnlockedServicesManager.get().unregister(economy);
            economy = null;
        }
    }
}
