package me.javaftw.ertools;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ERTools extends JavaPlugin implements Listener {

    private FileConfiguration config;

    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("blocks")) {
                player.sendMessage("§2You must mine " + (config.getInt("blocks_needed") - config.getInt("players." + player.getName() + ".blocks")) + " blocks to get your next token.");
            } else if (label.equalsIgnoreCase("tokens")) {
                player.sendMessage("§eYou now have " + config.getInt("players." + player.getName() + ".tokens") + " tokens!");
            } else if (label.equalsIgnoreCase("upgrade")) {
                player.openInventory(getUpgradeSelector());
            }
        }
        return true;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand().getType() == Material.DIAMOND_PICKAXE && p.getGameMode() != GameMode.CREATIVE) {
            final int blocksNeeded = config.getInt("blocks_needed");
            int currentBlocks = config.getInt("players." + p.getName() + ".blocks" + 1, 0);
            if (currentBlocks == blocksNeeded) {
                config.set("players." + p.getName() + ".blocks", 0);
                config.set("players." + p.getName() + ".tokens", config.getInt("players." + p.getName() + ".tokens" + 1, 1));
                saveConfig();
                e.getPlayer().sendMessage("§eYou've earned a token for mining " + blocksNeeded + " blocks!");
                e.getPlayer().sendMessage("§eYou now have " + config.getInt("players." + p.getName() + ".tokens") + " tokens!");
            } else {
                config.set("players." + p.getName() + ".blocks", currentBlocks + 1);
                saveConfig();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getInventory().getTitle().equalsIgnoreCase("§2Upgrade your §kcrap")) {
                e.setCancelled(true);
            }
        }
    }

    public Inventory getUpgradeSelector() {
        Inventory inv = Bukkit.createInventory(null, 27, "§2Upgrade your §kcrap");

        for (int i = 0; i < 27; i++) {
            inv.addItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        }

        {
            ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§bUpgrade your sword");
            item.setItemMeta(meta);
            inv.setItem(10, item);
        }
        {
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§bUpgrade your pick");
            item.setItemMeta(meta);
            inv.setItem(13, item);
        }
        {
            ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§bUpgrade your armor");
            item.setItemMeta(meta);
            inv.setItem(16, item);
        }

        return inv;
    }

    public Inventory getSwordUpgradeSelector() {
        Inventory inv = Bukkit.createInventory(null, 9, "§2Upgrade your §kcrap");
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            meta.setDisplayName("§aUpgrade the knockback on your sword");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            meta.setDisplayName("§aUpgrade the sharpness on your sword");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.setDisplayName("§aUpgrade the unbreaking on your sword");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }

        return inv;
    }

    public Inventory getPickUpgradeSelector() {
        Inventory inv = Bukkit.createInventory(null, 9, "§2Upgrade your §kcrappy §rpick");
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            meta.setDisplayName("§aUpgrade the efficiency on your pick");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
            meta.setDisplayName("§aUpgrade the fortune on your pick");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.setDisplayName("§aUpgrade the unbreaking on your pick");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }

        return inv;
    }

    public Inventory getArmorUpgradeSelector() {
        Inventory inv = Bukkit.createInventory(null, 9, "§2Upgrade your §kcrappy §rsword");
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            meta.setDisplayName("§aUpgrade the protection on your armor");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.THORNS, 1, true);
            meta.setDisplayName("§aUpgrade the thorns on your armor");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }
        {
            ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = prot.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            meta.setDisplayName("§aUpgrade the protection on your armor");
            prot.setItemMeta(meta);
            inv.addItem(prot);
        }

        return inv;
    }
}
