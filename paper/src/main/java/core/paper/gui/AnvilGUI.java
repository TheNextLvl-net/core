package core.paper.gui;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a GUI with an anvil inventory.
 *
 * @param <P> the type of the plugin that owns this GUI
 */
@Getter
@ApiStatus.Experimental
public class AnvilGUI<P extends Plugin> extends AbstractGUI<P> {
    private final Inventory inventory;

    /**
     * Construct a new AnvilGUI
     *
     * @param plugin the plugin owning this anvil gui
     * @param owner  the player owning this anvil gui
     * @param title  the initial title of this anvil gui
     */
    public AnvilGUI(P plugin, Player owner, Component title) {
        super(plugin, owner, title);
        this.inventory = Bukkit.createInventory(this, InventoryType.ANVIL, title());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilTakeResult(PrepareAnvilEvent event) {
        // todo: do stuff
    }
}
