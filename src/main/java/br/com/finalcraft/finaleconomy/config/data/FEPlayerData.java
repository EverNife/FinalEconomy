package br.com.finalcraft.finaleconomy.config.data;

import br.com.finalcraft.evernifecore.config.playerdata.PDSection;
import br.com.finalcraft.evernifecore.config.playerdata.PlayerData;
import br.com.finalcraft.evernifecore.util.FCBukkitUtil;
import br.com.finalcraft.evernifecore.util.FCMathUtil;
import br.com.finalcraft.evernifecore.util.numberwrapper.NumberWrapper;
import br.com.finalcraft.finaleconomy.api.events.EconomyUpdateEvent;
import br.com.finalcraft.finaleconomy.config.FESettings;
import org.bukkit.Bukkit;

public class FEPlayerData extends PDSection implements Comparable<FEPlayerData> {

    private NumberWrapper<Double> moneyWrapper;

    public FEPlayerData(PlayerData playerData) {
        super(playerData);

        moneyWrapper = NumberWrapper.of(getConfig().getDouble("FinalEconomy.money", 0D));
    }

    public NumberWrapper<Double> getMoneyWrapper(){
        return moneyWrapper;
    }

    public double getMoney() {
        return moneyWrapper.doubleValue();
    }

    public boolean hasMoney(double amount){
        return getMoney() >= amount;
    }

    public void addMoney(double amount){
        getMoneyWrapper().increment(amount);
        setRecentChanged();
    }

    public void removeMoney(double amount){
        getMoneyWrapper().decrement(amount);
        setRecentChanged();
    }

    public void setMoney(double amount){
        getMoneyWrapper().setValue(amount);
        setRecentChanged();
    }

    @Override
    public void setRecentChanged() {
        this.moneyWrapper.boundLower(0D);
        Bukkit.getPluginManager().callEvent(new EconomyUpdateEvent(this,
                getMoney(),
                FESettings.allowAsyncEconomyChanges == false ? false : FCBukkitUtil.isMainThread() == false
        ));
        super.setRecentChanged();
    }

    public String getMoneyFormatted(){
        return FESettings.MONEY_FORMATTER.format(FCMathUtil.normalizeDouble(this.moneyWrapper.get()));
    }

    @Override
    public void savePDSection() {
        getConfig().setValue("FinalEconomy.money", moneyWrapper.get());
    }

    @Override
    public int compareTo(FEPlayerData o) {
        NumberWrapper<Double> thisValue = this.moneyWrapper;
        NumberWrapper<Double> otherValue = o.moneyWrapper;
        return thisValue.compareTo(otherValue);
    }

}
