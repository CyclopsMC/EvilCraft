package org.cyclops.evilcraft.client.gui.container;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.client.gui.component.button.ButtonText;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;
import org.cyclops.cyclopscore.helper.IModHelpers;
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

    public ContainerScreenExaltedCrafter(ContainerExaltedCrafter container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }

    @Override
    protected int getBaseYSize() {
        return 225;
    }

    @Override
    public boolean charTyped(CharacterEvent evt) {
        return handleKeyCode(new KeyEvent(evt.codepoint(), 0, evt.modifiers())) || super.charTyped(evt);
    }

    @Override
    public boolean keyPressed(KeyEvent evt) {
        if (evt.key() != GLFW.GLFW_KEY_ESCAPE) {
            if (handleKeyCode(evt)) {
                return true;
            }
        }
        return super.keyPressed(evt);
    }

    protected boolean handleKeyCode(KeyEvent evt) {
        InputConstants.Key inputCode = InputConstants.getKey(evt);
        if (Keys.EXALTEDCRAFTING.isActiveAndMatches(inputCode)) {
            if (IModHelpers.get().getMinecraftClientHelpers().isShifted()) {
                this.buttonBalance.onPress(evt);
            } else {
                this.buttonClear.onPress(evt);
            }
            return true;
        }
        return false;
    }

    @Override
    public ResourceLocation constructGuiTexture() {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "exalted_crafter_gui.png");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        super.init();
        this.buttonClear = addRenderableWidget(new ButtonText( this.leftPos + 88,  this.topPos + 58, 12, 12,
                Component.translatable("gui.exalted_crafting.clear"), Component.literal("C"),
                createServerPressable(ContainerExaltedCrafter.BUTTON_CLEAR, (button) -> {}), true));
        this.buttonBalance = addRenderableWidget(new ButtonText(this.leftPos + 103, this.topPos + 58, 12, 12,
                Component.translatable("gui.exalted_crafting.balance"), Component.literal("B"),
                createServerPressable(ContainerExaltedCrafter.BUTTON_BALANCE, (button) -> {}), true));
        addRenderableWidget(new ButtonText(this.leftPos + 36, this.topPos + 70, 40, 12,
                Component.translatable("gui.exalted_crafting.toggle_return"), Component.literal("..."),
                createServerPressable(ContainerExaltedCrafter.BUTTON_TOGGLERETURN, (button) -> {}), true) {
            @Override
            public Component getText() {
                return Component.literal(container.isReturnToInnerInventory() ? "inner" : "player");
            }
        });
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        // super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        ItemStack itemStack = container.getItemStack(getMinecraft().player);
        Component name = Component.translatable("gui.exalted_crafting");
        if(itemStack.has(DataComponents.CUSTOM_NAME)) {
            name = itemStack.getHoverName();
        }
        guiGraphics.drawString(font, name, 28, 6, 4210752, false);
    }

}
