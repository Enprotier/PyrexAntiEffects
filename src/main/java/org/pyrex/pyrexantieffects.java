// tuturoal: https://www.youtube.com/watch?v=dQw4w9WgXcQ
package org.pyrex;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;

public class PyrexAntiEffects extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("PyrexAntiEffects plugin elindult!");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PotiLopas(player.getInventory().getContents());
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        getLogger().info("PyrexAntiEffects plugin elsullyed, mint a titanik");
    }

    @EventHandler
    public void EzeketAtLehetNevezniKerdojel(InventoryClickEvent event) {
        PotiLopas(event.getWhoClicked().getInventory().getContents());
    }

    @EventHandler
    public void NemTudomDeAzIDENemKotekszik(InventoryDragEvent event) {
        PotiLopas(event.getWhoClicked().getInventory().getContents());
    }

    @EventHandler
    public void AMIKORCSATLAKOZIKEGYEMBER(PlayerJoinEvent event) {
        PotiLopas(event.getPlayer().getInventory().getContents());
    }

    @EventHandler
    public void YouDroppedUrSoapMan(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        if (MilyenPotiKerdojel(droppedItem)) {
            event.getItemDrop().remove();
        }
    }

    private void PotiLopas(ItemStack[] items) {
        for (ItemStack item : items) {
            if (item == null) continue;

            if (MilyenPotiKerdojel(item)) {
                item.setAmount(0);
            }
        }
    }

    private boolean MilyenPotiKerdojel(ItemStack item) {
        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            if (potionMeta != null) {
                PotionData potionData = potionMeta.getBasePotionData();
                return potionData.getType().getEffectType() == PotionEffectType.SLOW_FALLING || potionData.getType().getEffectType() == PotionEffectType.SLOW;
            }
        }

        if (item.getType() == Material.TIPPED_ARROW) {
            PotionMeta arrowMeta = (PotionMeta) item.getItemMeta();
            if (arrowMeta != null) {
                return arrowMeta.getBasePotionData().getType().getEffectType() == PotionEffectType.SLOW_FALLING ||
                        arrowMeta.getBasePotionData().getType().getEffectType() == PotionEffectType.SLOW;
            }
        }
        return false;
    }
}
