package core.paper.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
@ApiStatus.Experimental
public class AnvilGUI<P extends Plugin> extends AbstractGUI<P> {

    /**
     * Construct a new AnvilGUI
     *
     * @param plugin the plugin owning this gui
     * @param title  the initial title of this gui
     */
    public AnvilGUI(P plugin, Component title) {
        super(plugin, title);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilTakeResult(PrepareAnvilEvent event) {
        // todo: do stuff
    }

    @Override
    protected final Inventory createInventory() {
        return Bukkit.createInventory(this, InventoryType.ANVIL, title()); // not a real anvil inventory
    }
}
