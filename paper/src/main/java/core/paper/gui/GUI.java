package core.paper.gui;

import core.paper.item.ItemBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.stream.IntStream;

/**
 * Represents a GUI with a chest inventory.
 *
 * @param <P> the type of the plugin that owns this GUI
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class GUI<P extends Plugin> extends AbstractGUI<P> {
    private final Inventory inventory;
    private final int size;

    /**
     * Construct a new GUI
     *
     * @param plugin the plugin owning this gui
     * @param owner  the player owning this gui
     * @param title  the initial title of this gui
     * @param rows   the amount of rows of this gui
     */
    public GUI(P plugin, Player owner, Component title, int rows) {
        super(plugin, owner, title);
        this.size = rows * 9;
        this.inventory = Bukkit.createInventory(this, getSize(), title());
        formatDefault();
    }

    /**
     * Formats the gui with the default style
     */
    protected void formatDefault() {
        var placeholder1 = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).hideTooltip(true);
        var placeholder2 = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        var stream = IntStream.of(0, 8, getSize() - 1, getSize() - 9).boxed().toList();
        IntStream.range(0, getSize()).filter(value -> !stream.contains(value))
                .forEach(slot -> setSlotIfAbsent(slot, placeholder1));
        stream.forEach(slot -> setSlotIfAbsent(slot, placeholder2));
    }
}
