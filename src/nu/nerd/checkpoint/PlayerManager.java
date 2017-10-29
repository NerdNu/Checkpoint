package nu.nerd.checkpoint;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private CheckpointPlugin plugin;
    private Map<UUID, CheckpointPlayer> players;

    public PlayerManager() {
        plugin = CheckpointPlugin.getInstance();
        players = new HashMap<>();
    }

    /**
     * Wraps the given {@code Player} as a {@code CheckpointPlayer}
     * @return the {@code CheckpointPlayer}
     */
    public CheckpointPlayer wrap(Player player) {
        return players.get(player.getUniqueId());
    }

    void registerPlayer(Player player) {
        CheckpointPlayer checkpointPlayer = new CheckpointPlayer(player);
        players.put(player.getUniqueId(), checkpointPlayer);
    }

    void unregisterPlayer(Player player) {
        players.remove(player.getUniqueId());
    }

}
