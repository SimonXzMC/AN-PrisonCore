package net.astralnetwork.play;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import static net.astralnetwork.play.Main.*;

public class ConfigRegPlayer implements Listener {

    Plugin plugin = getPlugin(Main.class);


    @EventHandler
    public void createConfig(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        addPlayerConfig(p);
        return;
    }

    public void addPlayerConfig(Player p) {
        if (!cfgm.getPlayers().contains("Players." + p.getUniqueId().toString() + ".Name")) {
            cfgm.getPlayers().set("Players." + p.getUniqueId().toString() + ".Name", p.getName());
            cfgm.savePlayers();
        }
        if (!cfgm.getPlayers().contains("Players." + p.getUniqueId().toString() + ".Rank")) {
            cfgm.getPlayers().set("Players." + p.getUniqueId().toString() + ".Rank", 1);
            cfgm.savePlayers();
        }
        if (!cfgm.getPlayers().contains("Players." + p.getUniqueId().toString() + ".Prestige")) {
            cfgm.getPlayers().set("Players." + p.getUniqueId().toString() + ".Prestige", 0);
            cfgm.savePlayers();
        }
        if (!cfgm.getPlayers().contains("Players." + p.getUniqueId().toString() + ".Crystals")) {
            cfgm.getPlayers().set("Players." + p.getUniqueId().toString() + ".Crystals", 0);
            cfgm.savePlayers();
        }
    }
}
