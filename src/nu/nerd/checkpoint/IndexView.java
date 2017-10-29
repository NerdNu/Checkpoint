package nu.nerd.checkpoint;

import org.bukkit.inventory.Inventory;

public class IndexView {

    private CheckpointPlayer player;
    private CheckpointCourse course;
    private Inventory inventory;

    private IndexView(CheckpointPlayer player, CheckpointCourse course) {
        this.player = player;
        this.course = course;
        this.inventory = null;
    }

    /**
     * Opens an index view as an inventory.
     * @param player the player to open the view for
     * @param course the checkpoint course to show in the view
     * @return the created {@code IndexView}
     */
    public static IndexView open(CheckpointPlayer player, CheckpointCourse course) {
        IndexView view = new IndexView(player, course);
        return view;
    }

    /**
     * Closes the index view inventory.
     */
    public void close() {
    }

    /**
     * Returns true if the given inventory is this view's inventory UI.
     * @param inventory the inventory to check
     * @return whether the inventory is an index view UI
     */
    public boolean hasInventory(Inventory inventory) {
        return this.inventory == inventory;
    }

}
