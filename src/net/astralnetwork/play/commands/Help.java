package net.astralnetwork.play.commands;


import net.astralnetwork.play.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Help implements Listener {

    private Inventory inv;
    Main plugin = Main.getPlugin(Main.class);

    public void openInventory(Player p) {

        Inventory inv = Bukkit.getServer().createInventory(null, 54, "Help");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int color = random.nextInt((15 -1) + 1) + 1;

                ItemStack warpIcon = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                ItemMeta warpMeta = warpIcon.getItemMeta();
                warpMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lWarps"));
                warpMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&eClick here to view available warps!")));
                warpIcon.setItemMeta(warpMeta);

                ItemStack tokenIcon = new ItemStack(Material.MAGMA_CREAM, 1);
                ItemMeta tokenMeta = tokenIcon.getItemMeta();
                tokenMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lTokens"));
                tokenMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Click here to open the", ChatColor.YELLOW + "Token withdraw GUI!"));
                tokenIcon.setItemMeta(tokenMeta);

                ItemStack pvpIcon = new ItemStack(Material.DIAMOND_SWORD, 1);
                ItemMeta pvpMeta = pvpIcon.getItemMeta();
                pvpMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lPvP"));
                pvpMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Click here to warp to the PVP Arena", ChatColor.YELLOW + "ITEMS ARE KEPT ON DEATH!"));
                pvpIcon.setItemMeta(pvpMeta);

                ItemStack staffIcon = new ItemStack(Material.SKULL_ITEM, 1, (short) 4);
                ItemMeta staffMeta = staffIcon.getItemMeta();
                staffMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lStaff"));
                staffMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Click here to view all the staff members!"));
                staffIcon.setItemMeta(staffMeta);

                ItemStack exitIcon = new ItemStack(Material.BARRIER, 1);
                ItemMeta exitMeta = exitIcon.getItemMeta();
                exitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lClose"));
                exitMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Click here to close this menu!"));
                exitIcon.setItemMeta(exitMeta);

                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) color);
                ItemMeta fillerMeta = filler.getItemMeta();
                fillerMeta.setDisplayName(" ");
                filler.setItemMeta(fillerMeta);

                inv.setItem(12, warpIcon);
                inv.setItem(14, tokenIcon);
                inv.setItem(29, pvpIcon);
                inv.setItem(33, staffIcon);
                inv.setItem(40, exitIcon);

                for (int i = 0; i < 54; i++) {
                    if (inv.getItem(i) == null) {
                        inv.setItem(i, filler);
                    }
                }

            }
        }, 0, 20);
        p.openInventory(inv);

    }



    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();

        if(inv == null) return;

        if (e.getInventory().getTitle().contains("Help")) {
            e.setCancelled(true);
            if (item == null || !item.hasItemMeta())return;
            if(item.getItemMeta().getDisplayName().contains("Help")){
                p.sendMessage("you clicked in the GUI");
            }
        }
    }
}