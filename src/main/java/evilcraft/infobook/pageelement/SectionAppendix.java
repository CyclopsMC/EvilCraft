package evilcraft.infobook.pageelement;

import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import lombok.Data;

/**
 * Separate elements that can be appended to sections.
 * @author rubensworks
 */
@Data public abstract class SectionAppendix {

    private int page;
    private int lineStart;

    public SectionAppendix() {

    }

    /**
     * @return The height of this element.
     */
    public abstract int getHeight();

    public abstract void drawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my);

}
