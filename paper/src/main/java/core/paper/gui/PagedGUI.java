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
    private int currentPage;

    public PageableGUI(Plugin plugin, Player owner, Component title, int rows, List<T> elements, int[] slots) {
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
     * loads the desired page
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
     * @param page the page to get the end point for
     * @return the end point of the desired page
     */
    public int getEndPoint(int page) {
        return Math.min(elements.size(), getStartingPoint(page) + slots.length);
    }

    /**
     * @param page the page to check for elements
     * @return whether the page contains elements
     */
    public boolean isPageEmpty(int page) {
        return page < 0 || getElements(page).isEmpty();
    }

    /**
     * loads the next page
     */
    public void nextPage() {
        loadPage(getCurrentPage() + 1);
    }

    /**
     * loads the previous page
     */
    public void previousPage() {
        loadPage(getCurrentPage() - 1);
    }

    /**
     * @param page the target page
     * @return the text to be displayed on the navigation arrows
     */
    public Component formattedPage(int page) {
        return Component.text("§fGo to page§8: §a" + (page + 1));
    }

    @Override
    protected void formatDefault() {
        var previous = new ItemBuilder(Material.ARROW).name(formattedPage(getCurrentPage() - 1)).toGUIItem(this::previousPage);
        var next = new ItemBuilder(Material.ARROW).name(formattedPage(getCurrentPage() + 1)).toGUIItem(this::nextPage);
        if (isEmpty(getSize() - 1) && !isPageEmpty(getCurrentPage() + 1)) setSlot(getSize() - 1, next);
        if (isEmpty(getSize() - 9) && !isPageEmpty(getCurrentPage() - 1)) setSlot(getSize() - 9, previous);
        super.formatDefault();
    }
}
