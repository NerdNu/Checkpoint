package nu.nerd.checkpoint;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class IndexView {

    private CheckpointPlayer player;
    private CheckpointCourse course;
    private Inventory inventory;
    private List<Checkpoint> checkpoints;

    public IndexView(CheckpointPlayer player, CheckpointCourse course) {
        this.player = player;
        this.course = course;
        buildInventory();
    }

    private void buildInventory() {
        CheckpointPlugin plugin = CheckpointPlugin.getInstance();
        checkpoints = course.getCheckpointsWithIcons();
        int rows = (checkpoints.size() - 1) / 9 + 1;
        int inventorySize = rows * 9;
        inventory = plugin.getServer().createInventory(player.getPlayer(), inventorySize,
                "Choose a checkpoint");

        ItemStack[] contents = new ItemStack[inventorySize];
        for (int i = 0; i < checkpoints.size(); i++) {
            Checkpoint checkpoint = checkpoints.get(i);
            ItemStack icon = checkpoint.getIcon();
            if (player.hasCheckpoint(checkpoint)) {
                contents[i] = icon;
            }
        }
        inventory.setContents(contents);
    }

    /**
     * Opens an index view as an inventory.
     */
    public void open() {
        player.getPlayer().openInventory(inventory);
    }

    /**
     * Closes the index view inventory.
     */
    public void close() {
        Inventory openInventory = player.getPlayer().getOpenInventory().getTopInventory();
        if (openInventory == inventory) {
            player.getPlayer().closeInventory();
        }
    }

    /**
     * Handles clicking on a slot in the inventory.
     * @param slot the slot that was clicked
     */
    public void click(int slot) {
        if (slot >= checkpoints.size()) {
            return;
        }

        Checkpoint checkpoint = checkpoints.get(slot);
        if (checkpoint != null && player.hasCheckpoint(checkpoint)) {
            CheckpointPlugin plugin = CheckpointPlugin.getInstance();
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.closeIndex();
                    player.teleport(checkpoint);
                }
            }.runTask(plugin);
        }
    }

    /**
     * Returns true if the given inventory is this view's inventory UI.
     * @param inventory the inventory to check
     * @return whether the inventory is an index view UI
     */
    public boolean hasInventory(Inventory inventory) {
        return this.inventory.equals(inventory);
    }

}
