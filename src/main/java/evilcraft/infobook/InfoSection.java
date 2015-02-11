package evilcraft.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.infobook.pageelement.SectionAppendix;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Section of the info book.
 * Can have child sections.
 * @author rubensworks
 */
public class InfoSection {

    public static final int Y_OFFSET = 16;
    private static final int TITLE_LINES = 3;
    private static final int APPENDIX_OFFSET_LINE = 1;
    private static final int LINK_INDENT = 8;

    private InfoSection parent;
    private int childIndex;
    private String unlocalizedName;
    private List<InfoSection> sections = Lists.newLinkedList();
    private List<List<HyperLink>> links = Lists.newLinkedList();
    private List<SectionAppendix> appendixes;
    protected List<String> paragraphs;
    private ArrayList<String> tagList;
    private int pages;
    private List<String> localizedPages;
    private Map<Integer, List<AdvancedButton>> advancedButtons = Maps.newHashMap();

    public InfoSection(InfoSection parent, int childIndex, String unlocalizedName, List<String> paragraphs,
                       List<SectionAppendix> appendixes, ArrayList<String> tagList) {
        this.parent = parent;
        this.childIndex = childIndex;
        this.unlocalizedName = unlocalizedName;
        this.paragraphs = paragraphs;
        this.appendixes = appendixes;
        this.tagList = tagList;
    }

    /**
     * Add all links from the given map to this section, starting from page 0.
     * @param maxLines The maximum amount of lines per page.
     * @param lineHeight The line height.
     * @param softLinks The map of links.
     */
    protected void addLinks(int maxLines, int lineHeight, Map<String, Pair<InfoSection, Integer>> softLinks) {
        int linesOnPage = 0;
        if(isTitlePage(0)) {
            linesOnPage += TITLE_LINES;
        }
        List<HyperLink> pageLinks = Lists.newArrayListWithCapacity(maxLines);
        StringBuilder lines = new StringBuilder();
        for(Map.Entry<String, Pair<InfoSection, Integer>> entry : softLinks.entrySet()) {
            lines.append(" \n");
            linesOnPage++;
            if(linesOnPage >= maxLines) {
                linesOnPage = 0;
                links.add(pageLinks);
                pageLinks = Lists.newArrayListWithCapacity(maxLines);
            }
            pageLinks.add(new HyperLink(entry.getValue().getRight(), Y_OFFSET + (linesOnPage - 1) * lineHeight, entry.getValue().getLeft(), entry.getKey()));
        }
        paragraphs.add(lines.toString());
        links.add(pageLinks);
    }

    protected boolean shouldAddIndex() {
        return true;
    }

    protected static void constructAllLinks(InfoSection root, Map<String, Pair<InfoSection, Integer>> softLinks, int indent, int maxDepth) {
        for(InfoSection section : root.sections) {
            softLinks.put(section.unlocalizedName, Pair.of(section, indent));
            if(maxDepth - 1 > 0) {
                constructAllLinks(section, softLinks, indent + LINK_INDENT, maxDepth - 1);
            }
        }
    }

    /**
     * Will make a localized version of this section with a variable amount of paragraphs.
     * Must be called once before the section will be drawn.
     */
    public void bakeSection(FontRenderer fontRenderer, int width, int maxLines, int lineHeight) {
        if(paragraphs.size() == 0 && shouldAddIndex()) {
            // linkedmap to make sure the contents are sorted by insertion order.
            Map<String, Pair<InfoSection, Integer>> softLinks = Maps.newLinkedHashMap();
            constructAllLinks(this, softLinks, 0, 2);
            addLinks(maxLines, lineHeight, softLinks);
        }

        // Localize paragraphs and fit them into materialized paragraphs.
        String contents = "";
        for(String paragraph : paragraphs) {
            contents += L10NHelpers.localize(paragraph) + "\n\n";
        }

        // Wrap the text into pages.
        List<String> allLines = fontRenderer.listFormattedStringToWidth(contents, width);
        localizedPages = Lists.newLinkedList();
        int linesOnPage = 0;
        StringBuilder currentPage = new StringBuilder();
        if(isTitlePage(0)) {
            for(int i = 1; i < TITLE_LINES; i++) currentPage.append("\n"); // Make a blank space for the section title.
            linesOnPage += TITLE_LINES;
        }
        pages = 1;
        for(String line : allLines) {
            if(linesOnPage >= maxLines) {
                linesOnPage = 0;
                pages++;
                localizedPages.add(currentPage.toString());
                currentPage = new StringBuilder();
            }
            linesOnPage++;
            if(linesOnPage > 1) currentPage.append("\n");
            currentPage.append(line);
        }
        localizedPages.add(currentPage.toString());

        linesOnPage += APPENDIX_OFFSET_LINE;

        // Distribute appendixes among pages.
        Map<Integer, List<SectionAppendix>> appendixesPerPage = Maps.newHashMap();
        List<SectionAppendix> appendixCurrentPage = Lists.newLinkedList();
        int appendixPageStart = pages - 1;
        int appendixLineStart = linesOnPage;
        for(SectionAppendix appendix : appendixes) {
            int lines = getAppendixLineHeight(appendix, fontRenderer);
            if(linesOnPage + lines > maxLines) {
                appendixesPerPage.put(pages - 1, appendixCurrentPage);
                pages++;
                linesOnPage = 0;
                appendixCurrentPage = Lists.newLinkedList();
            }
            // Start line will be set in a later iteration.
            //appendix.setLineStart(linesOnPage);
            appendixCurrentPage.add(appendix);
            appendix.setPage(pages - 1);
            linesOnPage += lines + APPENDIX_OFFSET_LINE;
        }
        appendixesPerPage.put(pages - 1, appendixCurrentPage);

        // Loop over each page to determine optimal vertical float positioning of appendixes.
        for(Map.Entry<Integer, List<SectionAppendix>> entry : appendixesPerPage.entrySet()) {
            int freeLines = maxLines;
            int lineStart = 0;

            // Special case if appendixes occurs on a page that still has text content, so this needs an offset.
            if(entry.getKey() == appendixPageStart) {
                lineStart = appendixLineStart;
                freeLines -= appendixLineStart;
            }

            // Count total lines that are free.
            for(SectionAppendix appendix : entry.getValue()) {
                freeLines -= getAppendixLineHeight(appendix, fontRenderer);
            }

            // Distribute the free lines among all appendixes on this page.
            int linesOffset = freeLines / (entry.getValue().size() + 1);
            int linesOffsetMod = freeLines % (entry.getValue().size() + 1);
            lineStart += linesOffset;
            for(SectionAppendix appendix : entry.getValue()) {
                appendix.setLineStart(lineStart);
                lineStart += linesOffset + getAppendixLineHeight(appendix, fontRenderer) + (linesOffsetMod > 0 ? linesOffsetMod-- : 0);
            }
        }

        // Bake appendix contents
        advancedButtons.clear();
        for(SectionAppendix appendix : appendixes) {
            appendix.preBakeElement(this);
            appendix.bakeElement(this);
        }
    }

    protected static final int getAppendixLineHeight(SectionAppendix appendix, FontRenderer fontRenderer) {
        return (int) Math.ceil((double) appendix.getFullHeight() / (double) getFontHeight(fontRenderer));
    }

    public static int getFontHeight(FontRenderer fontRenderer) {
        return fontRenderer.FONT_HEIGHT;
    }

    public boolean isTitlePage(int page) {
        return this.unlocalizedName != null && page == 0;
    }

    public void registerSection(InfoSection section) {
        sections.add(section);
        section.childIndex = sections.size() - 1;
    }

    public int getPages() {
        return pages;
    }

    /**
     * Give the correct format to a string.
     * Will allow the convenient "&" format codes to be used instead of "§": http://minecraft.gamepedia.com/Formatting_codes
     * Will also refresh all formats at the end of the string.
     * @param string The string to format.
     * @return The formatted string.
     */
    public static String formatString(String string) {
        return (string + "&r").replaceAll("&", "§");
    }

    protected String getLocalizedPageString(int page) {
        if(page >= localizedPages.size() || page < 0) return null;
        return formatString(localizedPages.get(page));
    }

    public String getLocalizedTitle() {
        return formatString(L10NHelpers.localize(unlocalizedName));
    }

    public int getSubSections() {
        return sections.size();
    }

    public InfoSection getSubSection(int index) {
        return sections.get(index);
    }

    public InfoSection getParent() {
        return this.parent;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    public int getChildIndex() {
        return this.childIndex;
    }

    public List<HyperLink> getLinks(int page) {
        if(links.size() <= page || page < 0) return Collections.EMPTY_LIST;
        return links.get(page);
    }

    /**
     * Draw the screen for a given page.
     * @param gui The gui.
     * @param x X.
     * @param y Y.
     * @param width The width of the page.
     * @param height The height of the page.
     * @param page The page to render.
     * @param mx Mouse X.
     * @param my Mouse Y.
     */
    public void drawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        if(page < getPages()) {
            FontRenderer fontRenderer = gui.getFontRenderer();
            boolean oldUnicode = fontRenderer.getUnicodeFlag();
            fontRenderer.setUnicodeFlag(true);
            fontRenderer.setBidiFlag(false);

            // Draw text content
            String content = getLocalizedPageString(page);
            if (content != null) fontRenderer.drawSplitString(content, x, y + Y_OFFSET, width, 0);

            // Draw title if on first page
            if (isTitlePage(page)) {
                gui.drawScaledCenteredString(getLocalizedTitle(), x, y + Y_OFFSET + 10, width, 1.5f, width, RenderHelpers.RGBToInt(120, 20, 30));
                gui.drawHorizontalRule(x + width / 2, y + Y_OFFSET);
                gui.drawHorizontalRule(x + width / 2, y + Y_OFFSET + 21);
            }
            fontRenderer.setUnicodeFlag(oldUnicode);

            // Draw current page/section indication
            gui.drawScaledCenteredString(getLocalizedTitle() + " - " + (page + 1) +  "/" + getPages(), x + ((page % 2 == 0) ? 10 : -10), y + height - Y_OFFSET, width, 0.6f, RenderHelpers.RGBToInt(190, 190, 190));

            // Draw appendixes
            for (SectionAppendix appendix : appendixes) {
                if (appendix.getPage() == page) {
                    appendix.drawScreen(gui, x, y + Y_OFFSET + getAppendixOffsetLine(fontRenderer, appendix), width,
                            height, page, mx, my, true);
                }
            }
        }
    }

    /**
     * Draw the overlays for the given page, for tooltips and such.
     * @param gui The gui.
     * @param x X.
     * @param y Y.
     * @param width The width of the page.
     * @param height The height of the page.
     * @param page The page to render.
     * @param mx Mouse X.
     * @param my Mouse Y.
     */
    public void postDrawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        if(page < getPages()) {
            FontRenderer fontRenderer = gui.getFontRenderer();
            // Post draw appendixes
            for (SectionAppendix appendix : appendixes) {
                if (appendix.getPage() == page) {
                    appendix.drawScreen(gui, x, y + Y_OFFSET + getAppendixOffsetLine(fontRenderer, appendix), width,
                            height, page, mx, my, false);
                }
            }
        }
    }

    /**
     * Get the next InfoSection relative to this one plus page in this tree hierarchy using pre-order traversal.
     * @param page The current page.
     * @param stepSection Take a complete section as traversal step.
     * @return The next location or null if this was the last location.
     */
    public InfoSection.Location getNext(int page, boolean stepSection) {
        if(page < getPages() - 1 && !stepSection) {
            return new InfoSection.Location(page + 1, this);
        } else if(getSubSections() > 0) {
            return new InfoSection.Location(0, getSubSection(0));
        } else {
            InfoSection current = this;
            while(!current.isRoot()) {
                if (current.getChildIndex() < current.getParent().getSubSections() - 1) {
                    return new Location(0, current.getParent().getSubSection(current.getChildIndex() + 1));
                }
                current = current.getParent();
            }
        }
        return null;
    }

    /**
     * Get the previous InfoSection relative to this one plus page in this tree hierarchy using pre-order traversal.
     * @param page The current page.
     * @param stepSection Take a complete section as traversal step.
     * @return The previous location or null if this was the last location.
     */
    public InfoSection.Location getPrevious(int page, boolean stepSection) {
        if(page > 0) {
            return new InfoSection.Location(stepSection ? 0 : page - 2, this);
        } else if(!isRoot() && getChildIndex() == 0) {
            return new InfoSection.Location(0, getParent());
        } else if(!isRoot() && getChildIndex() > 0) {
            InfoSection current = getParent().getSubSection(getChildIndex() - 1);
            while(current.getSubSections() > 0) {
                current = current.getSubSection(current.getSubSections() - 1);
            }
            return new InfoSection.Location(0, current);
        }
        return null;
    }

    protected static int getAppendixOffsetLine(FontRenderer fontRenderer, SectionAppendix appendix) {
        return getFontHeight(fontRenderer) * appendix.getLineStart();
    }

    public ArrayList<String> getTags() {
        return tagList;
    }

    public List<AdvancedButton> getAdvancedButtons(int page) {
        if(!advancedButtons.containsKey(page)) {
            return Collections.EMPTY_LIST;
        }
        return advancedButtons.get(page);
    }

    public void addAdvancedButton(int page, AdvancedButton advancedButton) {
        if(!advancedButtons.containsKey(page)) {
            advancedButtons.put(page, Lists.<AdvancedButton>newLinkedList());
        }
        advancedButtons.get(page).add(advancedButton);
    }

    public <T extends AdvancedButton> void addAdvancedButtons(int page, Collection<T> advancedButtons) {
        for(AdvancedButton advancedButton : advancedButtons) addAdvancedButton(page, advancedButton);
    }

    @Data @AllArgsConstructor public static class Location {

        private int page;
        private InfoSection infoSection;

    }

}
