package core.paper.gui;

import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class PageableGUI<T> extends GUI {
    private final List<T> elements;
    private final int[] slots;
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
        this.slots = slots;
        loadPage(0);
    }

    /**
     * Construct an item for a given element
     *
     * @param element the element to construct the item for
     * @return the item representing the element
     */

    /**
     * This method is called after the page was successfully loaded
     */
    public void pageLoaded() {
        formatDefault();
    }

    /**
     * Get the starting point of a page
     *
     * @param page the page to load
     */
    public void loadPage(int page) {
        clear();
        if (!isPageEmpty(page)) {
            this.currentPage = page;
            var slots = Arrays.stream(getSlots()).iterator();
            getElements(page).forEach(element -> setSlot(slots.next(), constructItem(element)));
        }
        pageLoaded();
    }

    /**
     * @param page the page to get the elements for
     * @return the elements on the desired page
     */
    public List<T> getElements(int page) {
        if (page < 0) return new ArrayList<>(0);
        var startingPoint = getStartingPoint(page);
        if (elements.size() < startingPoint) return new ArrayList<>(0);
        return new ArrayList<>(elements).subList(startingPoint, getEndPoint(page));
    }

    /**
     * @param page the page to get the starting point for
     * @return the starting point of the desired page
     */
    public int getStartingPoint(int page) {
        return slots.length * page;
    }

    /**
     * Get the end point of a page
     *
     * @param page the page to get the end point for
     * @return the end point of the desired page
     */
    public int getEndPoint(int page) {
        return Math.min(elements.size(), getStartingPoint(page) + slots.length);
    }

    /**
     * Check whether a page is empty
     *
     * @param page the page to check for elements
     * @return whether the page contains elements
     */
    public boolean isPageEmpty(int page) {
        return page < 0 || getElements(page).isEmpty();
    }

    /**
     * This method is called after the page was successfully loaded
     */
    public void pageLoaded() {
        formatDefault();
        formatButtons();
    }

    /**
     * loads the previous page
     */
    public void previousPage() {
        loadPage(getCurrentPage() - 1);
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
