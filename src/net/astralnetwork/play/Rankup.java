package net.astralnetwork.play;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.text.NumberFormat;

import static net.astralnetwork.play.Main.cfgm;

public class Rankup implements CommandExecutor, Listener {

    Plugin plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("rankup") && sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 0) {

                //Messages
                //String success = plugin.getConfig().getString("rankup-success");
                //String nomoney = plugin.getConfig().getString("rankup-nomoney");
                //String norank = plugin.getConfig().getString("rankup-norank");

                int rank = cfgm.getPlayers().getInt("Players." + p.getUniqueId().toString() + ".Rank");
                long cost = rankCost(p, rank);
                String prestige = cfgm.getPlayers().getString("Players." + p.getUniqueId().toString() + ".Prestige");
                int new_rank = rank + 1;

                //Multiply rankup cost by prestige
                String multiplier = prestige + 1;
                long charge = 0;
                if (multiplier >= 1){
                    charge = cost * multiplier;
                } else {
                    charge = cost;
                }

                EconomyResponse r = Main.economy.withdrawPlayer(p, charge);

                if(rank >= 1 && rank < 15) {
                    if (r.transactionSuccess()) {
                        plugin.getConfig().set("Players." + p.getUniqueId().toString() + ".Rank", new_rank);
                        plugin.saveConfig();
                        // Messages
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou ranked up to " + rankName(p, new_rank) + "!"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l - $" + NumberFormat.getNumberInstance().format(charge)));

                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou need $" + NumberFormat.getNumberInstance().format(charge) + " to rankup to " + rankName(p, new_rank)));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou are already the top rank! (" + rankName(p, rank) + ") Do &e/Prestige &ato continue!"));
                }


            }
        }
        return true;
    }

    public static String rankName(Player p, int rank) {
        String a = null;
        // Rankup List
        if(rank == 1) {
            a = "A";
        }
        return a;
    }

    public int rankCost(Player p, int rank) {
        int a = 0;
        // Rankup Price List
        if(rank == 1) {
            a = 1;
        } return a;
    }
}
