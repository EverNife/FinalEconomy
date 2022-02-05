package br.com.finalcraft.finaleconomy.config;

import br.com.finalcraft.evernifecore.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private static Config mainConfig;

    public static Config getMainConfig(){
        return mainConfig;
    }

    public static void initialize(JavaPlugin instance){
        mainConfig = new Config(instance,"config.yml");

        FESettings.initialize();
    }

}
