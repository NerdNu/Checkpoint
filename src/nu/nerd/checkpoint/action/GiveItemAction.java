package nu.nerd.checkpoint.action;

import nu.nerd.checkpoint.CheckpointCourse;
import nu.nerd.checkpoint.CheckpointPlayer;
import nu.nerd.checkpoint.Utils;
import nu.nerd.checkpoint.exception.CheckpointException;
import nu.nerd.checkpoint.exception.UsageException;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Queue;

public class GiveItemAction extends Action {

    private ItemStack item;

    @Override
    public void execute(CheckpointPlayer player) {
        player.giveItem(item);
    }

    @Override
    public String getType() {
        return "give-item";
    }

    @Override
    public String getParams() {
        return item.getType().toString();
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
        if (!params.isEmpty()) {
            throw new UsageException(null);
        }

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
