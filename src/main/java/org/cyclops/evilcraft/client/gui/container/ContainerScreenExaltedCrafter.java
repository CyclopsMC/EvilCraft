package org.cyclops.evilcraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.client.gui.component.button.ButtonText;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;
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
        InputMappings.Input inputCode = InputMappings.getKey(keyCode, scanCode);
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
    	this.buttonClear = addButton(new ButtonText( this.leftPos + 88,  this.topPos + 58, 13, 12,
                new TranslationTextComponent("gui.exalted_crafting.clear"), new StringTextComponent("C"),
                createServerPressable(ContainerExaltedCrafter.BUTTON_CLEAR, (button) -> {}), true));
        this.buttonBalance = addButton(new ButtonText(this.leftPos + 103, this.topPos + 58, 13, 12,
                new TranslationTextComponent("gui.exalted_crafting.balance"), new StringTextComponent("B"),
                createServerPressable(ContainerExaltedCrafter.BUTTON_BALANCE, (button) -> {}), true));
        addButton(new ButtonText(this.leftPos + 36, this.topPos + 70, 40, 12,
                new TranslationTextComponent("gui.exalted_crafting.toggle_return"), new StringTextComponent("..."),
                createServerPressable(ContainerExaltedCrafter.BUTTON_TOGGLERETURN, (button) -> {}), true) {
            @Override
            public ITextComponent getText() {
                return new StringTextComponent(container.isReturnToInnerInventory() ? "inner" : "player");
            }
        });
    }
    
    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    	// super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    	ItemStack itemStack = container.getItemStack(inventory.player);
    	ITextComponent name = new TranslationTextComponent("gui.exalted_crafting");
    	if(itemStack.hasCustomHoverName()) {
    		name = itemStack.getHoverName();
    	}
        // MCP: drawString
        this.font.draw(matrixStack, name, 28, 6, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        // TODO: rm
    }

}
