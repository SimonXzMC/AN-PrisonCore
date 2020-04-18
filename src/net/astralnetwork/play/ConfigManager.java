package net.astralnetwork.play;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private Main plugin = Main.getPlugin(Main.class);

    public FileConfiguration playerscfg;
    public File playersfile;

    public void setupPlayers() {

        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        playersfile = new File(plugin.getDataFolder(), "players.yml");

        if(!playersfile.exists()) {
            try {
                playersfile.createNewFile();
            }catch(IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Could not create players.yml file.");
            }
        }

        playerscfg = YamlConfiguration.loadConfiguration(playersfile);
        Bukkit.getServer().getConsoleSender()
                .sendMessage(ChatColor.GREEN + "players.yml file has been created.");

    }

    public FileConfiguration getPlayers() {
        return playerscfg;
    }

    public void savePlayers() {
        try {
            playerscfg.save(playersfile);
        }catch(IOException e) {
            Bukkit.getServer().getConsoleSender()
                    .sendMessage(ChatColor.RED + "Could not save players.yml file.");
        }
    }

    public void reloadPlayers() {
        playerscfg = YamlConfiguration.loadConfiguration(playersfile);
    }
}