package com.uplandjacob;

import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

public class FAutoLog extends JavaPlugin implements Listener {
    private boolean debugMode;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        getLogger().info("FAutoLog is enabling, current AuthMe version");
        saveDefaultConfig();

        if (isPluginEnabled("floodgate")) {
            getLogger().warning("The required plugin Floodgate is missing, plugin failed to enable");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (isPluginEnabled("AuthMe")) {
            getLogger().warning("The required plugin AuthMe is missing, plugin failed to enable");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadConfig();
        getServer().getPluginManager().registerEvents(this,this);
    }

    private void loadConfig() {
        reloadConfig();
        config = getConfig();
        debugMode = config.getBoolean("debug", false);
    }

    private boolean isPluginEnabled(String plugin) {
        return !Bukkit.getPluginManager().isPluginEnabled(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FloodgateApi floodgateApi = FloodgateApi.getInstance();

        if (!floodgateApi.isFloodgatePlayer(player.getUniqueId())) return;
        sendDebugLog(player.getName() + " Bedrock player detected");

        AuthMeApi.getInstance().forceLogin(player);
    }

    private void sendDebugLog(String message) {
        if (debugMode) {
            getLogger().info(message);
        }
    }

}
