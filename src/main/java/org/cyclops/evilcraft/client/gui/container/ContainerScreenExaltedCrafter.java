package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.client.gui.component.button.ButtonText;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.key.Keys;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;
import org.cyclops.evilcraft.item.ItemExaltedCrafter;
import org.lwjgl.glfw.GLFW;

/**
 * GUI for the {@link ItemExaltedCrafter}.
 * @author rubensworks
 *
 */
public class ContainerScreenExaltedCrafter extends ContainerScreenExtended<ContainerExaltedCrafter> {

    private ButtonText buttonClear;
    private ButtonText buttonBalance;

    public ContainerScreenExaltedCrafter(ContainerExaltedCrafter container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    protected int getBaseYSize() {
        return 225;
    }

    @Override
    public boolean charTyped(char keyCode, int scanCode) {
        return handleKeyCode(keyCode, scanCode) || super.charTyped(keyCode, scanCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
            if (handleKeyCode(keyCode, scanCode)) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean handleKeyCode(int keyCode, int scanCode) {
        InputMappings.Input inputCode = InputMappings.getInputByCode(keyCode, scanCode);
        if (Keys.EXALTEDCRAFTING.isActiveAndMatches(inputCode)) {
            if (MinecraftHelpers.isShifted()) {
                this.buttonBalance.onPress();
            } else {
                this.buttonClear.onPress();
            }
            return true;
        }
        return false;
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "exalted_crafter_gui.png");
    }

    @SuppressWarnings("unchecked")
	@Override
    public void init() {
    	super.init();
    	this.buttonClear = addButton(new ButtonText( this.guiLeft + 88,  this.guiTop + 58, 13, 12,
                L10NHelpers.localize("gui.exalted_crafting.clear"), "C",
                createServerPressable(ContainerExaltedCrafter.BUTTON_CLEAR, (button) -> {}), true));
        this.buttonBalance = addButton(new ButtonText(this.guiLeft + 103, this.guiTop + 58, 13, 12,
                L10NHelpers.localize("gui.exalted_crafting.balance"), "B",
                createServerPressable(ContainerExaltedCrafter.BUTTON_BALANCE, (button) -> {}), true));
        addButton(new ButtonText(this.guiLeft + 36, this.guiTop + 70, 40, 12,
                L10NHelpers.localize("gui.exalted_crafting.toggle_return"), "...",
                createServerPressable(ContainerExaltedCrafter.BUTTON_TOGGLERETURN, (button) -> {}), true) {
            @Override
            public String getText() {
                return container.isReturnToInnerInventory() ? "inner" : "player";
            }
        });
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
    	super.drawGuiContainerForegroundLayer(x, y);
    	ItemStack itemStack = container.getItemStack(playerInventory.player);
    	ITextComponent name = new TranslationTextComponent("gui.exalted_crafting");
    	if(itemStack.hasDisplayName()) {
    		name = itemStack.getDisplayName();
    	}
        this.font.drawString(name.getFormattedText(), 28, 6, 4210752);
    }
    
}
