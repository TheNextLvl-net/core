package core.paper.messenger;

import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

@Deprecated
public class PluginMessenger {
    private final Plugin plugin;

    public PluginMessenger(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    /**
     * Register a {@link PluginMessageListener listener}
     *
     * @param listener the listener to register
     * @see Messenger#registerIncomingPluginChannel(Plugin, String, PluginMessageListener)
     */
    public void registerListener(PluginMessageListener listener) {
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", listener);
    }

    /**
     * Connects the player to the given server
     *
     * @param player the player to connect to the server
     * @param server the server to connect the player to
     */
    public void connect(Player player, String server) {
        var dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("Connect");
        dataOutput.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", dataOutput.toByteArray());
    }
}
