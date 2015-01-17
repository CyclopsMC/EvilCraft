package evilcraft.infobook;

import com.google.common.collect.Lists;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.helper.L10NHelpers;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Section of the info book.
 * Can have child sections.
 * @author rubensworks
 */
public class InfoSection {

    private String unlocalizedName;
    private List<InfoSection> sections = Lists.newLinkedList();
    private ArrayList<ArrayList<String>> pages;

    public InfoSection(String unlocalizedName, ArrayList<ArrayList<String>> pages) {
        this.unlocalizedName = unlocalizedName;
        this.pages = pages;
    }

    public void bakeSection() {
        if(getPages() == 0) {
            // TODO: make an index from all subsections (recursive) with links
            // TODO: limit links per page
            // TODO: buttons for returning to an index
            ArrayList<String> links = Lists.newArrayList();
            for(InfoSection section : sections) links.add(section.getLocalizedTitle());
            pages.add(links);
        }
    }

    public void registerSection(InfoSection section) {
        sections.add(section);
    }

    public int getPages() {
        return pages.size();
    }

    protected String getLocalizedPageString(int page) {
        String contents = "";
        for(String paragraph : pages.get(page)) {
            contents += L10NHelpers.localize(paragraph) + "\n\n";
        }
        return contents;
    }

    protected String getLocalizedTitle() {
        return L10NHelpers.localize(unlocalizedName);
    }

    public void drawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page) {
        FontRenderer fontRenderer = gui.getFontRenderer();
        String content = getLocalizedPageString(page);
        fontRenderer.drawSplitString(content, x + 14, y + 16, 116, 0);
        String title = getLocalizedTitle();
        int titleLength = fontRenderer.getStringWidth(title);
        fontRenderer.drawString(title, x - titleLength + width - 22, 2 + 16, 0);
    }

}
