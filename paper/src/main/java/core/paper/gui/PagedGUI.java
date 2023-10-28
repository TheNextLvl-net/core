package core.paper.gui;

import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Getter
public abstract class PagedGUI<T> extends GUI {
    private final Collection<T> elements;
    private final Options options;
    private int currentPage;

    /**
     * Construct a new Paged GUI
     *
     * @param plugin   the plugin owning this paged gui
     * @param owner    the player who owns this paged gui
     * @param title    the title of this paged gui
     * @param rows     the amount of rows of this paged gui
     * @param elements the elements of this paged gui
     * @param options  the options of this paged gui
     */
    public PagedGUI(Plugin plugin, Player owner, Component title, int rows, Collection<T> elements, Options options) {
        super(plugin, owner, title, rows);
        this.elements = elements;
        this.options = options;
        loadPage(0);
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
        return getOptions().slots().length * page;
    }

    /**
     * Get the end point of a page
     *
     * @param page the page to get the end point for
     * @return the end point of the desired page
     */
    public int getEndPoint(int page) {
        return Math.min(elements.size(), getStartingPoint(page) + getOptions().slots().length);
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
        var slots = Arrays.stream(getOptions().slots()).iterator();
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
        if (elements.size() < startingPoint) return Collections.emptyList();
        return new ArrayList<>(this.elements).subList(startingPoint, getEndPoint(page));
    }

    /**
     * Get the text that will be display on the navigation buttons
     *
     * @param page the target page
     * @return the text to be displayed on the navigation buttons
     */
    public abstract Component getPageFormat(int page);

    @ApiStatus.OverrideOnly
    protected void formatButtons() {
        var previous = new ItemBuilder(Material.ARROW).name(getPageFormat(getCurrentPage() - 1))
                .withAction(this::previousPage);
        var next = new ItemBuilder(Material.ARROW).name(getPageFormat(getCurrentPage() + 1))
                .withAction(this::nextPage);
        if (!isPageEmpty(getCurrentPage() - 1)) setSlot(getOptions().buttonSlotPrevious(), previous);
        if (!isPageEmpty(getCurrentPage() + 1)) setSlot(getOptions().buttonSlotNext(), next);
    }

    public record Options(
            int[] slots,
            int buttonSlotPrevious,
            int buttonSlotNext
    ) {
    }
}
