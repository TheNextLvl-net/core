package core.paper.gui;

import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents a paged graphical user interface.
 *
 * @param <P> the type of plugin that owns this GUI
 * @param <T> the type of elements rendered in this GUI
 */
@Deprecated
public abstract class PaginatedGUI<P extends Plugin, T> extends GUI<P> {
    private int currentPage;

    /**
     * Construct a new Paginated GUI
     *
     * @param plugin the plugin owning this gui
     * @param owner  the player owning this gui
     * @param title  the title of this gui
     * @param rows   the number of rows in this gui
     */
    public PaginatedGUI(P plugin, Player owner, Component title, int rows) {
        super(plugin, owner, title, rows);
    }

    /**
     * Construct an item for a given element
     *
     * @param element the element to construct the item for
     * @return the item representing the element
     */
    public abstract ActionItem constructItem(T element);

    /**
     * Get the starting point of a page
     *
     * @param page the page to get the starting point for
     * @return the starting point of the desired page
     */
    public int getStartingPoint(int page) {
        return getPagination().slots().length * page;
    }

    /**
     * Get the end point of a page
     *
     * @param page the page to get the end point for
     * @return the end point of the desired page
     */
    public int getEndPoint(int page) {
        return Math.min(getElements().size(), getStartingPoint(page) + getPagination().slots().length);
    }

    /**
     * Check whether a page is empty
     *
     * @param page the page to check for elements
     * @return whether the page contains elements
     */
    public boolean isPageEmpty(int page) {
        return getElements(page).isEmpty();
    }

    /**
     * Loads the desired page into the gui
     *
     * @param page the page to load
     * @return whether the page was loaded
     */
    public boolean loadPage(int page) {
        this.clear();
        var elements = getElements(page);
        if (elements.isEmpty()) return false;
        this.currentPage = page;
        var slots = Arrays.stream(getPagination().slots()).iterator();
        elements.forEach(element -> setSlot(slots.next(), constructItem(element)));
        this.pageLoaded();
        return true;
    }

    /**
     * Loads the next page
     *
     * @return whether the next page was loaded
     */
    public boolean nextPage() {
        return loadPage(getCurrentPage() + 1);
    }

    /**
     * Loads the previous page
     *
     * @return whether the previous page was loaded
     */
    public boolean previousPage() {
        return loadPage(getCurrentPage() - 1);
    }

    /**
     * This method is called after the page was successfully loaded
     */
    public void pageLoaded() {
        formatDefault();
        formatButtons();
    }

    /**
     * Gets all elements from a certain page
     *
     * @param page the page to get the elements for
     * @return the elements on the desired page
     */
    public Collection<T> getElements(int page) {
        if (page < 0) return Collections.emptyList();
        var startingPoint = getStartingPoint(page);
        if (getElements().size() < startingPoint) return Collections.emptyList();
        return new ArrayList<>(getElements()).subList(startingPoint, getEndPoint(page));
    }

    /**
     * Get the text that will be display on the navigation buttons
     *
     * @param page the target page
     * @return the text to be displayed on the navigation buttons
     */
    public abstract Component getPageFormat(int page);

    /**
     * Gets all elements for the entire GUI
     *
     * @return the elements for this GUI
     */
    public abstract Collection<T> getElements();

    /**
     * Retrieves the pagination options associated with this GUI.
     *
     * @return the pagination options for this GUI
     */
    public abstract Pagination getPagination();

    /**
     * Formats the navigation buttons for the paginated gui.
     * It constructs the previous and next buttons with appropriate names and actions based on the current page.
     * If there are elements on the previous or next page, it sets the respective buttons on the GUI.
     */
    protected void formatButtons() {
        var previous = ItemBuilder.of(Material.ARROW)
                .itemName(getPageFormat(getCurrentPage() - 1))
                .withAction(this::previousPage);
        var next = ItemBuilder.of(Material.ARROW)
                .itemName(getPageFormat(getCurrentPage() + 1))
                .withAction(this::nextPage);

        if (!isPageEmpty(getCurrentPage() - 1)) setSlot(getPagination().buttonSlotPrevious(), previous);
        if (!isPageEmpty(getCurrentPage() + 1)) setSlot(getPagination().buttonSlotNext(), next);
    }

    /**
     * Class representing the pagination options for a GUI
     *
     * @param slots              all slots that will be formatted
     * @param buttonSlotPrevious the slot for the "previous" button
     * @param buttonSlotNext     the slot for the "next" button
     */
    public record Pagination(
            int[] slots,
            int buttonSlotPrevious,
            int buttonSlotNext
    ) {
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
