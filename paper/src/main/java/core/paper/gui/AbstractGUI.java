package core.paper.gui;

import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an abstract GUI that can be used as a base class for creating GUIs.
 */
@NullMarked
public abstract class AbstractGUI implements InventoryHolder {
    private final Map<Integer, ActionItem.Action> actions = new HashMap<>();
    private Component title;
    protected final Player owner;

    /**
     * Construct a new AbstractGUI
     *
     * @param owner the player owning this gui
     * @param title the initial title of this gui
     */
    protected AbstractGUI(Player owner, Component title) {
        this.title = title;
        this.owner = owner;
    }

    /**
     * Sets the title of this gui
     *
     * @param title the new title
     */
    public void title(Component title) {
        this.title = title;
    }

    /**
     * Clears the entire gui
     */
    public void clear() {
        getInventory().clear();
        actions.clear();
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
     * @param slot    The slot where to put the item
     * @param builder The item to set
     */
    public void setSlot(int slot, ItemBuilder builder) {
        getInventory().setItem(slot, builder.item());
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
     * @param slot    The slot where to put the item
     * @param builder The item to set
     */
    public void setSlotIfAbsent(int slot, ItemBuilder builder) {
        if (isEmpty(slot)) setSlot(slot, builder);
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
     * Opens the gui
     */
    public void open() {
        owner.openInventory(getInventory());
    }

    /**
     * Closes the gui
     */
    public void close() {
        getInventory().close();
    }

    public Map<Integer, ActionItem.Action> getActions() {
        return actions;
    }

    public Component title() {
        return title;
    }

    public Player getOwner() {
        return owner;
    }
}
