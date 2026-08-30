package br.com.finalcraft.finaleconomy.common.command;

import br.com.finalcraft.evernifecore.api.common.commandsender.FCommandSender;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.Arg;
import br.com.finalcraft.evernifecore.commands.finalcmd.annotations.FinalCMD;
import br.com.finalcraft.evernifecore.config.uuids.UUIDsController;
import br.com.finalcraft.evernifecore.locale.FCLocale;
import br.com.finalcraft.evernifecore.locale.LocaleMessage;
import br.com.finalcraft.evernifecore.locale.LocaleType;
import br.com.finalcraft.evernifecore.pageviewer.CachePolicy;
import br.com.finalcraft.evernifecore.pageviewer.PageRegistry;
import br.com.finalcraft.evernifecore.pageviewer.PageViewer;
import br.com.finalcraft.evernifecore.pageviewer.PageVisualization;
import br.com.finalcraft.evernifecore.pageviewer.theme.ClassicPageTheme;
import br.com.finalcraft.evernifecore.pageviewer.theme.PageTheme;
import br.com.finalcraft.finaleconomy.common.PermissionNodes;
import br.com.finalcraft.finaleconomy.common.baltop.BaltopRanking;
import br.com.finalcraft.finaleconomy.common.config.ConfigManager;
import br.com.finalcraft.finaleconomy.common.config.settings.BaltopSettings;
import br.com.finalcraft.finaleconomy.common.data.FEPlayerData;

public class CMDBalanceTop {

    @FCLocale(lang = LocaleType.EN_US, text = "§e§m-----------------------§6§l[§eBalTop§6§l]§r§e§m-----------------------§r")
    public static LocaleMessage BALTOP_PREFIX;

    @FCLocale(lang = LocaleType.EN_US, text = "§7#  ${number}:   §6${player}§r - §a$${money_formatted}")
    public static LocaleMessage BALTOP_LINE;

    @FCLocale(lang = LocaleType.EN_US, text = "")
    public static LocaleMessage BALTOP_FOOTER;

    /** What {@code /ecpage <id> <page>} resolves, and what has to be given back on shutdown. */
    private static final String PAGE_ID = "finaleconomy:baltop";

    private static PageViewer<FEPlayerData> balTop;

    /**
     * Builds the page from the settings currently loaded. Called again after a reload, because the
     * cap, the chrome and the cache time are all read from config at build time.
     */
    public static void rebuild() {
        BaltopSettings baltop = ConfigManager.settings.getBaltop();

        ClassicPageTheme theme = PageTheme.classic();
        if (baltop.isIncludeDayOfToday()) {
            theme = theme.withDate();
        }
        if (baltop.isIncludeTotalUsersCount()) {
            theme = theme.withTotalCount();
        }

        PageViewer.IStepLimit<FEPlayerData> limitStep = PageViewer.of(FEPlayerData.class)
                .id(PAGE_ID)
                .source(BaltopRanking::current);

        balTop = (baltop.cappedRowCount() > 0
                ? limitStep.maxEntries(baltop.cappedRowCount())
                : limitStep.unlimitedEntries())
                .orderBy(FEPlayerData::getMoney).descending()
                .setPageSize(BaltopSettings.PAGE_SIZE)
                .setFormatHeader(BALTOP_PREFIX)
                .setFormatLine(BALTOP_LINE)
                .setFormatFooter(BALTOP_FOOTER)
                .theme(theme)
                // The rows come straight from the backend and carry no PlayerData, so the built-in
                // ${player} would answer null; the name is looked up from the stored uuid instead.
                .addRowPlaceholder("player", row -> UUIDsController.getNameFromUUID(row.getUniqueId()))
                .addRowPlaceholder("money_formatted", FEPlayerData::getMoneyFormatted)
                .cache(CachePolicy.ttl(ConfigManager.settings.getPlaceholders().topCacheDuration()))
                .build();
    }

    /** Takes the page out of the registry, so a disabled plugin leaves no page link answering. */
    public static void dispose() {
        PageRegistry.unregister(PAGE_ID);
        balTop = null;
    }

    @FinalCMD(
            aliases = {"febalancetop", "balancetop", "baltop", "moneytop"},
            permission = PermissionNodes.COMMAND_BALANCETOP
    )
    public void top(FCommandSender sender, @Arg("[page]") PageVisualization page) {
        balTop.send(page, sender);
    }

}
