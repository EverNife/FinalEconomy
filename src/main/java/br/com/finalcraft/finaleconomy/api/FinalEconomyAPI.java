package br.com.finalcraft.finaleconomy.api;

import br.com.finalcraft.finaleconomy.vault.VaultEconomy;

public class FinalEconomyAPI {

    private static final VaultEconomy VAULT_ECONOMY = new VaultEconomy();

    public static VaultEconomy getVaultAPI(){
        return VAULT_ECONOMY;
    }

}
