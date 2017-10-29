package nu.nerd.checkpoint;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.CheckpointPlugin;
import nu.nerd.checkpoint.trigger.BlockTrigger;
import nu.nerd.checkpoint.trigger.ItemTrigger;
import nu.nerd.checkpoint.trigger.Trigger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerDispatcher {

    private CheckpointPlugin plugin;
    private Map<Location, List<BlockTrigger>> blockTriggers;
    private Map<Material, List<ItemTrigger>> itemTriggers;

    public TriggerDispatcher() {
        plugin = CheckpointPlugin.getInstance();
    }

    /**
     * Dispatches all triggers listening to the given block.
     * @param player the player who clicked the block
     * @param block the block that was clicked
     * @return true if a trigger was found, false otherwise
     */
    public boolean interactBlock(CheckpointPlayer player, Block block) {
        if (block == null) {
            return false;
        }

        Location location = block.getLocation();
        List<BlockTrigger> triggers = blockTriggers.get(location);
        boolean dispatched = false;
        if (triggers != null) {
            for (BlockTrigger trigger : triggers) {
                trigger.dispatch(player);
                dispatched = true;
            }
        }
        return dispatched;
    }

    /**
     * Dispatches all triggers listening to the given item.
     * @param player the player who used the item
     * @param item the item that was used
     * @return true if a trigger was found, false otherwise
     */
    public boolean useItem(CheckpointPlayer player, ItemStack item) {
        if (item == null) {
            return false;
        }

        Material material = item.getType();
        List<ItemTrigger> triggers = itemTriggers.get(material);
        boolean dispatched = false;
        if (triggers != null) {
            for (ItemTrigger trigger : triggers) {
                if (trigger.matchesItem(item)) {
                    trigger.dispatch(player);
                    dispatched = true;
                }
            }
        }
        return dispatched;
    }

    /**
     * Reindexes all triggers for all courses.
     */
    public void reindexTriggers() {
        blockTriggers = new HashMap<>();
        itemTriggers = new HashMap<>();
        for (CheckpointCourse course : plugin.getCourses()) {
            for (Trigger trigger : course.getTriggers()) {
                addTrigger(trigger);
            }
        }
    }

    private void addTrigger(Trigger trigger) {
        if (trigger instanceof BlockTrigger) {
            addBlockTrigger((BlockTrigger) trigger);
        } else if (trigger instanceof ItemTrigger) {
            addItemTrigger((ItemTrigger) trigger);
        }
    }

    private void addBlockTrigger(BlockTrigger trigger) {
        Location location = trigger.getLocation();
        if (blockTriggers.containsKey(location)) {
            List<BlockTrigger> triggersAtLocation = blockTriggers.get(location);
            triggersAtLocation.add(trigger);
        } else {
            List<BlockTrigger> triggersAtLocation = new ArrayList<>();
            triggersAtLocation.add(trigger);
            blockTriggers.put(location, triggersAtLocation);
        }
    }

    private void addItemTrigger(ItemTrigger trigger) {
        ItemStack item = trigger.getItem();
        Material material = item.getType();
        if (itemTriggers.containsKey(material)) {
            List<ItemTrigger> triggersForItem = itemTriggers.get(material);
            triggersForItem.add(trigger);
        } else {
            List<ItemTrigger> triggersForItem = new ArrayList<>();
            triggersForItem.add(trigger);
            itemTriggers.put(material, triggersForItem);
        }
    }

}
