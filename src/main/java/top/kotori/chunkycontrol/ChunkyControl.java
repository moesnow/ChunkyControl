package top.kotori.chunkycontrol;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.chunky.api.ChunkyAPI;

import java.util.logging.Logger;

public final class ChunkyControl extends JavaPlugin implements Listener {
    public final Logger logger = Logger.getLogger("ChunkyControl");
    private ChunkyAPI chunky;
    private int playerTaskLimit = -1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        playerTaskLimit = getConfig().getInt("player-task-limit");
        logger.info("player-task-limit: " + playerTaskLimit);

        chunky = Bukkit.getServer().getServicesManager().load(ChunkyAPI.class);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Server server = event.getPlayer().getServer();
        final int playerOnline = server.getOnlinePlayers().size();
        if (playerTaskLimit > 0 && playerOnline >= playerTaskLimit) {
            for (World world : server.getWorlds()) {
                chunky.pauseTask(world.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Server server = event.getPlayer().getServer();
        final int playerOnline = server.getOnlinePlayers().size() - 1;
        if (playerTaskLimit > 0 && playerOnline < playerTaskLimit) {
            for (World world : server.getWorlds()) {
                chunky.continueTask(world.getName());
            }
        }
    }
}
