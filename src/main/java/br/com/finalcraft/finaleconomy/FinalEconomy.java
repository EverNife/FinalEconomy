package br.com.finalcraft.finaleconomy;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.ecplugin.annotations.ECPlugin;
import br.com.finalcraft.evernifecore.integration.placeholders.PAPIIntegration;
import br.com.finalcraft.evernifecore.logger.ECLogger;
import br.com.finalcraft.evernifecore.scheduler.FCScheduler;
import br.com.finalcraft.finaleconomy.baltop.BaltopTrackingCenter;
import br.com.finalcraft.finaleconomy.commands.CMDBalanceTop;
import br.com.finalcraft.finaleconomy.commands.CommandRegisterer;
import br.com.finalcraft.finaleconomy.config.ConfigManager;
import br.com.finalcraft.finaleconomy.integration.EverNifeCoreIntegration;
import br.com.finalcraft.finaleconomy.integration.PlaceholderIntegration;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jspecify.annotations.NonNull;

@ECPlugin(
        spigotID = "97740",
        bstatsID = "13365"
)
public class FinalEconomy extends JavaPlugin{

    public static FinalEconomy instance; { instance = this; } //Instance as early as possible!

    private final ECLogger ecLogger = new ECLogger(this);

    public FinalEconomy(@NonNull JavaPluginInit init) {
        super(init);
    }

    public static ECLogger<?> getLog() {
        return instance.ecLogger;
    }

    @Override
    public void setup() {
        try {
            EverNifeCore.class.getSimpleName(); //This will throw NoClassDefFoundError if EverNifeCore is not Present
        }catch (NoClassDefFoundError e){
            for (int i = 0; i < 10; i++) {
                getLog().severe("FinalEconomy Requires the plugin 'EverNifeCore' to work!");
            }
            throw e;
        }

        getLog().info("§aIntegrating to VAULT...");
        // Vault integration removed for Hytale - VaultUnlocked handles this differently

        getLog().info("§aLoading Configuration...");
        ConfigManager.initialize(this);

        FCScheduler.scheduleAsync(() -> {
            //Integrate to EverNifeCore after server startup
            EverNifeCoreIntegration.initialize();

            //Register commands only after all other plugins are loaded
            getLog().info("§aRegistering Commands...");
            CommandRegisterer.registerCommands(FinalEconomy.this);
            CMDBalanceTop.instance.recalculateBalTop();

            if (PAPIIntegration.isPresent()) {
                getLog().info("§aRegistering Placeholders...");
                PlaceholderIntegration.initialize(instance);
            }
        }, 4000);
    }

    @ECPlugin.Reload(reloadAfter = "EverNifeCore")
    public void reload(){
        ConfigManager.initialize(this);
        CMDBalanceTop.instance.recalculateBalTop();
        BaltopTrackingCenter.refreshBalTop(true);
    }

}
