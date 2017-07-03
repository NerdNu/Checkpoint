package nu.nerd.checkpoint.command;

import nu.nerd.checkpoint.CheckpointException;
import nu.nerd.checkpoint.CheckpointPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CmdSetItem extends CheckpointCommand {

    public CmdSetItem(CheckpointPlugin plugin) {
        super(plugin);
    }

    @Override
    public String execute(Player player, List<String> args) throws CheckpointException {
        if (args.size() != 1) {
            throw new CheckpointException(null, true);
        }

        String name = args.get(0).toLowerCase();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) {
            return "You must be holding an item.";
        }

        item = item.clone();
        item.setAmount(1);
        plugin.getCourse(name).setItem(item);
        return "Teleporter item for course {{" + name + "}} set.";
    }

    @Override
    public String getName() {
        return "setitem";
    }

    @Override
    public String getDescription() {
        return "sets the teleporter item to your currently held item";
    }

    @Override
    public String getUsage(Player player) {
        return "<course>";
    }

}
