package core.paper.gui;

import com.google.common.base.Preconditions;
import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static org.bukkit.event.inventory.InventoryCloseEvent.Reason.CANT_USE;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class GUI implements Listener {
    private final HashMap<Integer, ActionItem.Action> actions = new HashMap<>();
    private final Plugin plugin;
    private final Player owner;
    private Inventory inventory;
    private Component title;
    private boolean disposed;

    /**
     * Construct a new GUI
     *
     * @param plugin the plugin owning this gui
     * @param owner the player who owns this gui
     * @param title  the title of this gui
     * @param rows   the amount of rows of this gui
     */
    public GUI(Plugin plugin, Player owner, Component title, int rows) {
        this.inventory = Bukkit.createInventory(owner, rows * 9, title);
        this.plugin = plugin;
        this.owner = owner;
        this.title = title;
        formatDefault();
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    /**
     * Sets the title of this gui
     *
     * @param title the new title
     */
    public void setTitle(Component title) {
        var inventory = Bukkit.createInventory(getOwner(), getSize(), title);
        inventory.setContents(getInventory().getContents());
        this.inventory = inventory;
        this.title = title;
    }

    /**
     * Returns the rows of the gui
     *
     * @return the rows of the gui
     */
    public int getRows() {
        return getSize() / 9;
    }

    /**
     * Returns the size of the gui
     *
     * @return the size of the gui
     */
    public int getSize() {
        return getInventory().getSize();
    }

    /**
     * Clears the entire gui
     */
    public void clear() {
        getInventory().clear();
        getActions().clear();
    }

    /**
     * Formats the gui with the default style
     */
    @ApiStatus.OverrideOnly
    protected void formatDefault() {
        var placeholder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("§7-§8/§7-").withAction();
        IntStream.range(0, getSize()).forEach(slot -> setSlot(slot, placeholder));
    }

    /**
     * Checks if a certain slot contains an item
     *
     * @param slot the slot
     * @return whether the slot is empty or not
     */
    public boolean isEmpty(int slot) {
        return getInventory().getItem(slot) == null;
    }

    /**
     * Stores the item at the given slot of the inventory
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlot(int slot, ActionItem item) {
        getActions().put(slot, item.action());
        getInventory().setItem(slot, item.stack());
    }

    /**
     * Stores the item at the given slot of the inventory
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlot(int slot, ItemStack item) {
        getInventory().setItem(slot, item);
        getActions().remove(slot);
    }

    /**
     * Stores the item at the given slot of the inventory when empty
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlotIfAbsent(int slot, ActionItem item) {
        if (isEmpty(slot)) setSlot(slot, item);
    }

    /**
     * Stores the item at the given slot of the inventory when empty
     *
     * @param slot The slot where to put the item
     * @param item The item to set
     */
    public void setSlotIfAbsent(int slot, ItemStack item) {
        if (isEmpty(slot)) setSlot(slot, item);
    }

    /**
     * Removes the item at the given slot of the inventory
     *
     * @param slot the slot to clear
     */
    public void remove(int slot) {
        getActions().remove(slot);
        getInventory().setItem(slot, null);
    }

    /**
     * Opens the gui for its owner
     */
    public void open() {
        Preconditions.checkState(!disposed, "This GUI is disposed");
        getOwner().openInventory(getInventory());
    }

    /**
     * Disposes this gui
     */
    public void dispose() {
        HandlerList.unregisterAll(this);
        List.copyOf(getInventory().getViewers()).forEach(humanEntity ->
                humanEntity.closeInventory(CANT_USE));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInventoryOpen(InventoryOpenEvent event) {
        if (!getInventory().equals(event.getView().getTopInventory())) return;
        if (event.getPlayer().equals(getOwner())) return;
        event.setCancelled(true);
        throw new IllegalStateException("This GUI does not belong to this player");
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked().equals(getOwner()))) return;
        if (getInventory().equals(event.getView().getTopInventory())) try {
            if (event.getView().getBottomInventory().equals(event.getClickedInventory())) return;
            var action = getActions().get(event.getSlot());
            if (action != null) action.click(event.getClick(), event.getHotbarButton(), getOwner());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(getPlugin())) dispose();
    }
}
