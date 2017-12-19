package nu.nerd.checkpoint.trigger;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.DescribableMeta;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Queue;

@DescribableMeta(
        name = "item",
        description = "triggered when a player interacts using the item you're holding"
)
public class ItemTrigger extends Trigger {

    private ItemStack item;

    @Override
    public String getParams() {
        return item.getType().toString();
    }

    /**
     * Returns true if the given item should dispatch this trigger.
     * @return whether the item matches
     */
    public boolean matchesItem(ItemStack otherItem) {
        return item.isSimilar(otherItem);
    }

    /**
     * Returns the item stack for this trigger.
     * @return the item stack
     */
    public ItemStack getItem() {
        return item;
    }

    @Override
    protected void loadFromConfig(CheckpointCourse course, Map<String, Object> section) throws CheckpointException {
        Map<String, Object> itemSection = Utils.getSection(section, "item");
        if (itemSection == null) {
            throw new CheckpointException("No item provided");
        }
        item = ItemStack.deserialize(itemSection);
    }

    @Override
    protected void loadFromCommand(CheckpointPlayer player, Queue<String> params) throws CheckpointException {
        ItemStack heldItem = player.getHeldItem();
        if (heldItem == null) {
            throw new CheckpointException("You are not holding an item");
        }
        item = heldItem.clone();
    }

    @Override
    protected void saveToConfig(Map<String, Object> config) {
        config.put("item", item.serialize());
    }

}
