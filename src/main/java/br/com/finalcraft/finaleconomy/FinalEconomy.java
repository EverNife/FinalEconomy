package br.com.finalcraft.finaleconomy;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.ecplugin.annotations.ECPlugin;
import br.com.finalcraft.finaleconomy.api.FinalEconomyAPI;
import br.com.finalcraft.finaleconomy.commands.CommandRegisterer;
import br.com.finalcraft.finaleconomy.config.ConfigManager;
import br.com.finalcraft.finaleconomy.integration.EverNifeCoreIntegration;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@ECPlugin(
        spigotID = "97740",
        bstatsID = "13365"
)
public class FinalEconomy extends JavaPlugin{

    public static FinalEconomy instance;

    public static void info(String msg){
        instance.getLogger().info("[Info] " + msg);
    }

    public static void debug(String msg){
        instance.getLogger().info("[Debug] " + msg);
    }

    public static void warning(String msg){
        instance.getLogger().info("[Warning] " + msg);
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            EverNifeCore.class.getSimpleName(); //This will throw NoClassDefFoundError if EverNifeCore is not Present
        }catch (NoClassDefFoundError e){
            for (int i = 0; i < 10; i++) {
                warning("FinalEconomy Requires the plugin 'EverNifeCore' to work!");
            }
            throw e;
        }

        info("§aIntegrating to VAULT...");
        this.getServer().getServicesManager().register(Economy.class, FinalEconomyAPI.getVaultAPI(), this, ServicePriority.Highest);

        info("§aLoading Configuration...");
        ConfigManager.initialize(this);

        new BukkitRunnable(){
            @Override
            public void run() {
                //Integrate to EverNifeCore aftert server startup
                EverNifeCoreIntegration.initialize();

                //Register commands only after all other plugins are loaded
                //This is required to override EssentialsECO commands
                info("§aRegistering Commands...");
                CommandRegisterer.registerCommands(FinalEconomy.this);
            }
        }.runTaskLater(this, 1);
    }

    @ECPlugin.Reload
    public void reload(){
        ConfigManager.initialize(this);
    }

}
