package br.com.finalcraft.finaleconomy.common;

import br.com.finalcraft.evernifecore.ecplugin.ECBootstrap;
import br.com.finalcraft.evernifecore.ecplugin.ECPluginData;
import br.com.finalcraft.evernifecore.ecplugin.IECPluginBootstrap;
import br.com.finalcraft.finaleconomy.common.baltop.BaltopRanking;
import br.com.finalcraft.finaleconomy.common.command.CMDBalanceTop;
import br.com.finalcraft.finaleconomy.common.command.CommandRegisterer;
import br.com.finalcraft.finaleconomy.common.config.ConfigManager;
import br.com.finalcraft.finaleconomy.common.data.LegacyBalanceImporter;
import br.com.finalcraft.finaleconomy.common.data.PlayerDataRegistry;
import br.com.finalcraft.finaleconomy.common.placeholder.PlaceholderIntegration;

/**
 * The platform-agnostic bootstrap: every enable phase both entry points share lives here, once.
 * Each platform main class implements this on top of its platform base class and adds its own
 * extras through {@code onECPluginEnablePost()}.
 */
public interface FinalEconomy extends IECPluginBootstrap {

    ECBootstrap<FinalEconomy> INSTANCE = ECBootstrap.of(FinalEconomy.class);

    /** The plugin running on this server, whatever the platform, or {@code null} while there is none. */
    static FinalEconomy get() {
        return INSTANCE.get();
    }

    @Override
    default void onECPluginEnable() {
        ECPluginData pd = getPluginData();

        getLog().info("Loading Configuration...");
        ConfigManager.initialize(pd);

        getLog().info("Registering PlayerData...");
        PlayerDataRegistry.registerAll(pd);

        getLog().info("Registering Placeholders...");
        PlaceholderIntegration.initialize(pd);

        CMDBalanceTop.rebuild();
    }

    /**
     * Commands are registered a tick late, after every other plugin has enabled, so that the aliases
     * this plugin shares with EssentialsEco ({@code /balance}, {@code /pay}, {@code /eco}) end up
     * pointing here. The core's read of the 2.x files runs on this same tick, ahead of it, which is
     * why the amounts it landed are folded into the account rows before the ranking is read.
     */
    @Override
    default Runnable runOnFirstTick() {
        return () -> {
            getLog().info("Registering Commands...");
            CommandRegisterer.registerCommands(getPluginData());
            LegacyBalanceImporter.claimPending().thenRun(BaltopRanking::refresh);
        };
    }

    @Override
    default void onECPluginShutdown() {
        //the default onECPluginShutdownPre() already took back every listener and command; the page
        //registry is keyed by id and outlives them, so this one is given back by hand
        CMDBalanceTop.dispose();
    }

    @Override
    default void onECPluginReload() {
        ConfigManager.reload();
        CMDBalanceTop.rebuild();
        BaltopRanking.invalidate();
    }
}
