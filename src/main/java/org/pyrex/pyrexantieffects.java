package org.pyrex;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PyrexAntiEffects extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("PyrexAntiEffects has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PyrexAntiEffects has been disabled.");
    }

    // Scan player's inventory on interaction
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        scanAndRemoveItems(player);
    }

    // Scan player's inventory on item consume
    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        scanAndRemoveItems(player);
    }

    // Scan player's inventory on inventory click
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            scanAndRemoveItems(player);
        }
    }

    // Scan and remove potions/arrows with specific effects from inventory
    private void scanAndRemoveItems(Player player) {
        // Iterate through the player's entire inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isSlownessOrSlowFallingItem(item, player)) {
                player.getInventory().remove(item);
            }
        }

        // Also check offhand and armor slots (in case of future expansions)
        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (offhand != null && isSlownessOrSlowFallingItem(offhand, player)) {
            player.getInventory().remove(offhand);
        }
    }

    // Check if the item is a potion or arrow with slowness, slow falling, or turtle master effects
    private boolean isSlownessOrSlowFallingItem(ItemStack item, Player player) {
        // Handle potions (including Lingering, Splash, and Normal Potions)
        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION) {
            if (item.getItemMeta() instanceof PotionMeta) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();

                for (PotionEffect effect : meta.getCustomEffects()) {
                    if (isRelevantEffect(effect)) {
                        sendDebugMessage(player, item, effect);
                        return true;
                    }
                }

                // Check for base potion types like Turtle Master
                if (meta.getBasePotionData().getType().toString().contains("TURTLE_MASTER")) {
                    sendDebugMessage(player, item, null); // No specific custom effect
                    return true;
                }
            }
        }

        // Handle tipped arrows with slowness or slow falling effects
        if (item.getType() == Material.TIPPED_ARROW) {
            if (item.getItemMeta() instanceof PotionMeta) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                for (PotionEffect effect : meta.getCustomEffects()) {
                    if (isRelevantEffect(effect)) {
                        sendDebugMessage(player, item, effect);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Determine if the potion effect is slowness or slow falling
    private boolean isRelevantEffect(PotionEffect effect) {
        return effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.SLOW_FALLING;
    }

    // Send debug message about removed items and effects
    private void sendDebugMessage(Player player, ItemStack item, PotionEffect effect) {
        String itemType = item.getType().toString();
        if (effect != null) {
            getLogger().info("Removed item: " + itemType + " from " + player.getName() + " with effect: " + effect.getType().getName()
                    + " for " + effect.getDuration() + " ticks.");
            player.sendMessage("Removed: " + itemType + " with effect: " + effect.getType().getName()
                    + " for " + effect.getDuration() + " ticks.");
        } else {
            getLogger().info("Removed item: " + itemType + " from " + player.getName() + " (Turtle Master or base potion)");
            player.sendMessage("Removed: " + itemType + " (Turtle Master or base potion)");
        }
    }
}
