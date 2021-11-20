package br.com.finalcraft.finaleconomy;

import br.com.finalcraft.evernifecore.metrics.Metrics;
import br.com.finalcraft.finaleconomy.api.FinalEconomyAPI;
import br.com.finalcraft.finaleconomy.commands.CommandRegisterer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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

        info("§aIntegrating to VAULT...");
        this.getServer().getServicesManager().register(Economy.class, FinalEconomyAPI.getVaultAPI(), this, ServicePriority.Highest);

        new BukkitRunnable(){
            @Override
            public void run() {
                if (!Bukkit.getPluginManager().isPluginEnabled("EverNife")){
                    for (int i = 0; i < 5; i++) {
                        warning("FinalEconomy Requires EverNifeCore to work!");
                    }
                }

                info("§aRegistering Commands...");
                CommandRegisterer.registerCommands(FinalEconomy.this);

                Metrics metrics = new Metrics(FinalEconomy.this, 13365); //13365 FinalEconomy BStats
            }
        }.runTaskLater(this, 1);

    }

}
