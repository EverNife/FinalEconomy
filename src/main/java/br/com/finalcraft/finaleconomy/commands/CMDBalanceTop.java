package br.com.finalcraft.finaleconomy.commands;

import br.com.finalcraft.evernifecore.argumento.MultiArgumentos;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerController;
import br.com.finalcraft.evernifecore.cooldown.Cooldown;
import br.com.finalcraft.evernifecore.cooldown.FCTimeFrame;
import br.com.finalcraft.evernifecore.util.FCMessageUtil;
import br.com.finalcraft.evernifecore.util.FCTextUtil;
import br.com.finalcraft.evernifecore.util.numberwrapper.NumberWrapper;
import br.com.finalcraft.finaleconomy.config.data.FEPlayerData;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CMDBalanceTop {

    private List<String> topCommandList = new ArrayList();

    @FinalCMD(
            aliases = {"balancetop","baltop","moneytop","febalancetop"},
            usage = "<Player>"
    )
    public void top(CommandSender sender, MultiArgumentos argumentos, String label){

        Cooldown cooldown = Cooldown.getOrCreateCooldown("BALTOP");
        if (!cooldown.isInCooldown()){
            cooldown.startWith(5);

            List<FEPlayerData> playerDataList = PlayerController.getAllPlayerData(FEPlayerData.class);

            Collections.sort(playerDataList);
            Collections.reverse(playerDataList);

            int number = 0;
            topCommandList.clear();
            topCommandList.add(FCTextUtil.alignCenter("§6 §l[§eBalTop§6§l]§6 §r","§e§m-"));
            topCommandList.add("§7Data de hoje: " + new FCTimeFrame(System.currentTimeMillis()).getFormatedNoHours());
            for (FEPlayerData playerData : playerDataList){
                number++;
                if (number == 51){
                    break;
                }
                topCommandList.add("§7#  " + number +  ":   §e" + playerData.getPlayerName() + "§f - §6$" + playerData.getMoneyFormatted());
            }
            topCommandList.add("");
            topCommandList.add("§7De um total de " + playerDataList.size() + " jogadores...");
        }

        NumberWrapper<Integer> amount = NumberWrapper.of(10);
        if (!argumentos.get(1).isEmpty()){
            if (argumentos.get(1).getInteger() == null){
                FCMessageUtil.needsToBeInteger(sender, argumentos.getStringArg(1));
                return;
            }
            amount.setValue(argumentos.get(1).getInteger());
        }
        amount.bound(1,50);

        for (Integer integer = 0; integer < amount.get() && integer < topCommandList.size(); integer++) {
            sender.sendMessage(topCommandList.get(integer));
        }
    }

}
