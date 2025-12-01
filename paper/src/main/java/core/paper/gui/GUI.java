package core.paper.gui;

import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.stream.IntStream;

/**
 * Represents a GUI with a chest inventory.
 *
 * @param <P> the type of the plugin that owns this GUI
 */
@Deprecated
public class GUI<P extends Plugin> extends AbstractGUI implements Listener {
    protected final P plugin;
    private final Inventory inventory;
    private final int size;
    private boolean passUnknownClickTypes;

    /**
     * Construct a new GUI
     *
     * @param plugin the plugin owning this gui
     * @param owner  the player owning this gui
     * @param title  the initial title of this gui
     * @param rows   the number of rows in this gui
     */
    public GUI(P plugin, Player owner, Component title, int rows) {
        super(owner, title);
        this.plugin = plugin;
        this.size = rows * 9;
        this.inventory = Bukkit.createInventory(this, getSize(), title());
        Bukkit.getPluginManager().registerEvents(this, plugin);
        formatDefault();
    }

    /**
     * Formats the gui with the default style
     */
    protected void formatDefault() {
        var placeholder1 = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip();
        var placeholder2 = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip();
        var stream = IntStream.of(0, 8, getSize() - 1, getSize() - 9).boxed().toList();
        IntStream.range(0, getSize()).filter(value -> !stream.contains(value))
                .forEach(slot -> setSlotIfAbsent(slot, placeholder1));
        stream.forEach(slot -> setSlotIfAbsent(slot, placeholder2));
    }

    /**
     * This method should be overridden in subclasses to provide custom behavior when the GUI is closed.
     * It is invoked when the player closes the GUI.
     * <p>
     * Note: Do not call this method directly, it is automatically called by the system.
     */
    protected void onClose() {
    }

    /**
     * This method should be overridden in subclasses to provide custom behavior when the GUI is opened.
     * It is invoked when the player opens the GUI.
     * <p>
     * Note: Do not call this method directly, it is automatically called by the system.
     */
    protected void onOpen() {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void onInventoryClick(InventoryClickEvent event) {
        if (!equals(event.getInventory().getHolder())) return;
        if (event.getInventory().equals(getInventory())) try {
            if (event.getClick().equals(ClickType.UNKNOWN) && !isPassUnknownClickTypes()) return;
            if (!event.getInventory().equals(event.getClickedInventory())) return;
            if (!(event.getWhoClicked() instanceof Player player)) return;
            var action = getActions().get(event.getSlot());
            if (action != null) action.click(event.getClick(), event.getHotbarButton(), player);
        } catch (Exception e) {
            plugin.getComponentLogger().error("Something went wrong while handling a gui interaction", e);
        } finally {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public final void onInventoryOpen(InventoryOpenEvent event) {
        if (!equals(event.getInventory().getHolder())) return;
        if (!event.getInventory().equals(getInventory())) return;
        if (!event.getPlayer().equals(getOwner())) {
            plugin.getComponentLogger().warn("Tried to open GUI for unauthorized player");
            event.setCancelled(true);
        } else {
            onOpen();
            event.titleOverride(title());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public final void onInventoryClose(InventoryCloseEvent event) {
        if (!equals(event.getInventory().getHolder())) return;
        if (!event.getInventory().equals(getInventory())) return;
        HandlerList.unregisterAll(this);
        onClose();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public boolean isPassUnknownClickTypes() {
        return passUnknownClickTypes;
    }

    public void setPassUnknownClickTypes(boolean passUnknownClickTypes) {
        this.passUnknownClickTypes = passUnknownClickTypes;
    }

    public int getSize() {
        return size;
    }
}
