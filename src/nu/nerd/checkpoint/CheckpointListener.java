package nu.nerd.checkpoint;

import org.bukkit.block.Block;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CheckpointListener implements Listener {

    private CheckpointPlugin plugin;
    private PlayerManager playerManager;
    private TriggerDispatcher triggerDispatcher;

    public CheckpointListener() {
        plugin = CheckpointPlugin.getInstance();
        playerManager = plugin.getPlayerManager();
        triggerDispatcher = plugin.getTriggerDispatcher();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerManager.registerPlayer(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerManager.unregisterPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        CheckpointPlayer player = playerManager.wrap(event.getPlayer());
        Block block = event.getClickedBlock();
        ItemStack heldItem = event.getItem();

        if (triggerDispatcher.interactBlock(player, block) && event.getHand() != EquipmentSlot.OFF_HAND) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }

        if (triggerDispatcher.useItem(player, heldItem)) {
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        CheckpointPlayer player = playerManager.wrap((Player) event.getView().getPlayer());
        Inventory topInventory = event.getView().getTopInventory();
        if (player.hasIndex(topInventory)) {
            if (event.getClickedInventory() == topInventory) {
                player.clickIndex(event.getSlot());
            }
            event.setCancelled(true);
        }
    }

}
