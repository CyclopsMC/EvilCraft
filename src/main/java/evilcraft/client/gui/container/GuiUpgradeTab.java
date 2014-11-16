package evilcraft.client.gui.container;

import evilcraft.Reference;
import evilcraft.core.client.gui.container.GuiContainerExtended;
import evilcraft.core.client.gui.container.GuiTab;
import net.minecraft.util.ResourceLocation;

/**
 * Tab for upgrades in guis.
 * @author rubensworks
 */
public class GuiUpgradeTab extends GuiTab {

    private ResourceLocation texture;

    public GuiUpgradeTab(GuiContainerExtended gui) {
        super(28, 82, 0, 0, 0, 0, gui);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "gui_upgrades.png");
    }

    @Override
    protected ResourceLocation getResourceLocation() {
        return texture;
    }
}
