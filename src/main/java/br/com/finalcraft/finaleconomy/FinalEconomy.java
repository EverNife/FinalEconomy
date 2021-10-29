package br.com.finalcraft.finaleconomy;

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

    @Override
    public void onEnable() {
        instance = this;

        info("§aIntegrating to VAULT...");
        this.getServer().getServicesManager().register(Economy.class, FinalEconomyAPI.getVaultAPI(), this, ServicePriority.Highest);

        info("§aLoading up Configs...");
        ConfigManager.initialize(FinalEconomy.this);

        new BukkitRunnable(){
            @Override
            public void run() {
                info("§aRegistering Commands...");
                CommandRegisterer.registerCommands(FinalEconomy.this);
            }
        }.runTaskLater(this, 1);

    }

}
