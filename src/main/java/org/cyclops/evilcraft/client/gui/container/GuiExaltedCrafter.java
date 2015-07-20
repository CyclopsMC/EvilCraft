package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.client.gui.container.GuiContainerExtended;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.key.Keys;
import org.cyclops.evilcraft.core.client.gui.GuiButtonExtended;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;
import org.cyclops.evilcraft.item.ExaltedCrafter;

import java.io.IOException;

/**
 * GUI for the {@link ExaltedCrafter}.
 * @author rubensworks
 *
 */
public class GuiExaltedCrafter extends GuiContainerExtended {
	
	public static final int BUTTON_CLEAR = 1;
    public static final int BUTTON_BALANCE = 2;
    public static final int BUTTON_TOGGLERETURN = 3;
	private EntityPlayer player;
	private int itemIndex;
	
    /**
     * Make a new instance.
     * @param player The player.
     * @param itemIndex The index of the item in use inside the player inventory.
     */
    public GuiExaltedCrafter(EntityPlayer player, int itemIndex) {
        super(new ContainerExaltedCrafter(player, itemIndex));
        this.player = player;
        this.itemIndex = itemIndex;

        this.allowUserInput = true;
    }

    @Override
    protected int getBaseYSize() {
        return 225;
    }

    protected void pressButton(int buttonId) {
        ((ContainerExaltedCrafter) inventorySlots).sendPressButton(buttonId);
    }
    
    @Override
    protected void keyTyped(char key, int code) throws IOException {
    	if(code == Keys.EXALTEDCRAFTING.getKeyCode()) {
            pressButton(MinecraftHelpers.isShifted() ? BUTTON_BALANCE : BUTTON_CLEAR);
    	}
    	super.keyTyped(key, code);
    }

    @Override
    public String getGuiTexture() {
        return Reference.TEXTURE_PATH_GUI + "exaltedCrafter_gui.png";
    }

    @SuppressWarnings("unchecked")
	@Override
    public void initGui() {
    	super.initGui();
    	buttonList.add(new GuiButtonExtended(BUTTON_CLEAR,   this.guiLeft + 88,  this.guiTop + 58, 13, 12, "C"));
        buttonList.add(new GuiButtonExtended(BUTTON_BALANCE, this.guiLeft + 103, this.guiTop + 58, 13, 12, "B"));
        buttonList.add(new GuiButtonExtended(BUTTON_TOGGLERETURN, this.guiLeft + 36, this.guiTop + 70, 40, 12, "...") {
            @Override
            public String getDisplayString() {
                return ((ContainerExaltedCrafter) inventorySlots).isReturnToInnerInventory() ? "inner" : "player";
            }
        });
    }
    
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        pressButton(guibutton.id);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
    	super.drawGuiContainerForegroundLayer(x, y);
    	ItemStack itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex);
    	String name = L10NHelpers.localize("gui.exaltedCrafting");
    	if(itemStack.hasDisplayName()) {
    		name = itemStack.getDisplayName();
    	}
        this.fontRendererObj.drawString(name, 28, 6, 4210752);
    }
    
}
