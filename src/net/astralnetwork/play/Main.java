package net.astralnetwork.play;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import net.astralnetwork.play.commands.Help;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    public static Economy economy;
    public Help help;
    public ConfigManager cfgm;

    @Override
    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        help = new Help();
        getServer().getPluginManager().registerEvents(new Help(), this);
        getServer().getPluginManager().registerEvents(new Rankup(), this);
        getServer().getPluginManager().registerEvents(new ConfigRegPlayer(), this);
        this.getCommand("rankup").setExecutor(new Rankup());
        getLogger().info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has been enabled!");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }
        loadConfigManager();

        setupEconomy();
        registerPlaceholders();
    }

    public void loadConfigManager(){
        cfgm = new ConfigManager();
        cfgm.setupPlayers();
    }



    public String prefix = ChatColor.translateAlternateColorCodes('&', "&a&lAstral&f&lNetwork &b>> ");
    public ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

    public void registerConfig() {
        File f = new File(getDataFolder(), File.separator + "config.yml");
        if(!f.exists()) {
            saveResource("config.yml", false);
            consoleSender.sendMessage(prefix + ChatColor.YELLOW + "Creating default configuration");
        } else {
            consoleSender.sendMessage(prefix + ChatColor.YELLOW + "Configuration found, processing load");
        }
    }


    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info(pdfFile.getName() + " Has beeen disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;

        if(commandLabel.equalsIgnoreCase("help")) {
           if(!sender.hasPermission("astral.help")) {
                sender.sendMessage(ChatColor.RED + "You don't have enough permissions. Speak to the server Administrator's if you think this is a problem!");
                return true;
            }
            help.openInventory(player);
        }
        if(commandLabel.equalsIgnoreCase("list")) {
            String staff = "";
            for(Player players : Bukkit.getOnlinePlayers()){
                if(players.hasPermission("staff")){
                    staff += players.getName() + ", ";
                }
            }
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + "--------------------");
            sender.sendMessage(ChatColor.RED + "Staff Online: " + "" + staff);
            sender.sendMessage(ChatColor.GREEN  + "Online Players: " + Bukkit.getOnlinePlayers().size());
            sender.sendMessage(ChatColor.GRAY + "--------------------");
            sender.sendMessage(" ");
            return true;
        }
        if(commandLabel.equalsIgnoreCase("anreload")) {
            if (!sender.hasPermission("astral.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have enough permissions. Speak to the server Administrator's if you think this is a problem!");
                return true;
            }
            super.reloadConfig();
            saveConfig();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloading Astrals config"));
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPlayedBefore()) {
            e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&a&l + NEW &e" + p.getName()));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&lWelcome to " + getConfig().getString("server-name")+ " " + p.getName()));
            p.sendMessage("");
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2»&a» &f&lWEBSITE " + getConfig().getString("server-website")));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2»&a» &f&lDONATE " + getConfig().getString("server-donate")));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2»&a» &f&lDISCORD " + getConfig().getString("server-discord")));
            p.sendMessage("");
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------"));
        } else {
            e.setJoinMessage(ChatColor.GREEN + "+ " + p.getName());
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "        &f&lWelcome back to "));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("server-name") + " " + p.getName()));
            p.sendMessage("");
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2»&a» &f&lWEBSITE " + getConfig().getString("server-website")));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2»&a» &f&lDONATE " + getConfig().getString("server-donate")));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2»&a» &f&lDISCORD " + getConfig().getString("server-discord")));
            p.sendMessage("");
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------"));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&c- " + (p.getName())));
    }


    // REGISTER PLACEHOLDERS
    public void registerPlaceholders() {
        PlaceholderAPI.registerPlaceholderHook("prisoncore", new PlaceholderHook() {
            @Override
            public String onRequest(OfflinePlayer p, String params) {
                if(p != null && p.isOnline()) {
                    return onPlaceholderRequest(p.getPlayer(), params);
                }
                return null;
            }

            @Override
            public String onPlaceholderRequest(Player p, String params) {
                if (p == null) {
                    return null;
                }
                if(params.equalsIgnoreCase("prestige")) {
                    String prestige = cfgm.getPlayers().getString("Players." + p.getUniqueId().toString() + ".Prestige");
                    return prestige;
                }
                if(params.equalsIgnoreCase("rank")) {
                    int rank = cfgm.getPlayers().getInt("Players." + p.getUniqueId().toString() + ".Rank");
                    String a = Rankup.rankName(p, rank);
                    return a;
                }

                return null;
            }
        });
    }

    // Econ
    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    /**
     * VAULT API
     *
     */
}
