package me.scarday.whitelistManager;

import dev.rollczi.litecommands.jda.LiteJDAFactory;
import lombok.Getter;
import me.scarday.whitelistManager.discord.commands.WhitelistCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.necko1.whitelist.Whitelist;
import org.necko1.whitelist.api.WhitelistAPI;

public final class WhitelistManager extends JavaPlugin {

    private WhitelistAPI whitelistAPI;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Whitelist whitelist = (Whitelist) getServer().getPluginManager().getPlugin("Whitelist");

        if (whitelist == null || !whitelist.isEnabled()) {
            getLogger().info("Whitelist plugin not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        whitelistAPI = whitelist.getAPI();

        String token = getConfig()
                .getString("token");

        if (token == null || token.isEmpty()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();

        LiteJDAFactory.builder(jda)
                .commands(new WhitelistCommand(this))
                .build();

    }

    public WhitelistAPI getWhitelist() {
        return whitelistAPI;
    }

    @Override
    public void onDisable() {}
}
