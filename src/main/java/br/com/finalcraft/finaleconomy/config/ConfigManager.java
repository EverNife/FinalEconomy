package br.com.finalcraft.finaleconomy.config;

import br.com.finalcraft.evernifecore.config.Config;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;

public class ConfigManager {

    private static Config mainConfig;

    public static Config getMainConfig(){
        return mainConfig;
    }

    public static void initialize(JavaPlugin instance){
        mainConfig = new Config(instance,"config.yml");

        FESettings.initialize();

        PlayerController.registerPDSectionCfg(
                instance,
                FEPlayerData.class
        );
    }

}
