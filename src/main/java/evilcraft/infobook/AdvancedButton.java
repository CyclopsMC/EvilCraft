package evilcraft.infobook;

import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * An advanced button type.
 * @author rubensworks
 */
public class AdvancedButton extends GuiButton {

    private InfoSection target;
    protected GuiOriginsOfDarkness gui;

    public AdvancedButton() {
        super(-1, 0, 0, "");
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * This is called each render tick to update the button to the latest render state.
     * @param x The X position.
     * @param y The Y position.
     * @param displayName The text to display.
     * @param target The target section.
     * @param gui The gui.
     */
    public void update(int x, int y, String displayName, InfoSection target, GuiOriginsOfDarkness gui) {
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = displayName;
        this.target = target;
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.enabled = isVisible();
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if(isVisible() && isHover(mouseX, mouseY)) {
            minecraft.fontRendererObj.drawString(("§n") +
                            displayString + "§r", xPosition, xPosition,
                    Helpers.RGBToInt(100, 100, 150));
        }
    }

    protected boolean isHover(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition &&
                mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isVisible() {
        return this.visible && getTarget() != null;
    }

    public InfoSection getTarget() {
        return this.target;
    }

    public static class Enum {

        private Enum() {

        }

        public static Enum create() {
            return new Enum();
        }

    }
}
