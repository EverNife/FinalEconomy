package br.com.finalcraft.finaleconomy;

import br.com.finalcraft.evernifecore.EverNifeCore;
import br.com.finalcraft.evernifecore.autoupdater.SpigotUpdateChecker;
import br.com.finalcraft.evernifecore.metrics.Metrics;
import br.com.finalcraft.finaleconomy.api.FinalEconomyAPI;
import br.com.finalcraft.finaleconomy.commands.CommandRegisterer;
import br.com.finalcraft.finaleconomy.config.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

        SpigotUpdateChecker.checkForUpdates(
                this,
                "97740", //FinalEconomy SpigotID: 97740
                ConfigManager.getMainConfig()
        );

        Metrics metrics = new Metrics(FinalEconomy.this, 13365); //13365 FinalEconomy BStats

        new BukkitRunnable(){
            @Override
            public void run() {
                //Register commands only after all other plugins are loaded
                //This is required to override EssentialsECO commands
                info("§aRegistering Commands...");
                CommandRegisterer.registerCommands(FinalEconomy.this);
            }
        }.runTaskLater(this, 1);
    }

}
