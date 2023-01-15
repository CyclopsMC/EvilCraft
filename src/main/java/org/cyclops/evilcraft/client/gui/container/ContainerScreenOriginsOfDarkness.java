package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.infobook.OriginsOfDarknessBook;
import org.cyclops.evilcraft.inventory.container.ContainerOriginsOfDarkness;

/**
 * Gui for the Origins of Darkness book.
 * @author rubensworks
 */
public class ContainerScreenOriginsOfDarkness extends ScreenInfoBook<ContainerOriginsOfDarkness> {

    public static final int X_OFFSET_OUTER = 20;
    public static final int X_OFFSET_INNER = 7;

    public ContainerScreenOriginsOfDarkness(ContainerOriginsOfDarkness container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title, OriginsOfDarknessBook.getInstance());
    }

    @Override
    protected ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "origins_of_darkness_gui.png");
    }

    @Override
    protected int getGuiWidth() {
        return 283;
    }

    @Override
    protected int getGuiHeight() {
        return 180;
    }

    @Override
    protected int getPageWidth() {
        return 142;
    }

    @Override
    protected int getOffsetXForPageBase(int page) {
        return page == 0 ? X_OFFSET_OUTER : X_OFFSET_INNER;
    }

    @Override
    protected int getFootnoteOffsetY() {
        return -16;
    }

    @Override
    public void playPageFlipSound(SoundManager soundHandler) {
        soundHandler.play(SimpleSoundInstance.forUI(EvilCraftSoundEvents.effect_page_flipsingle, 1.0F));
    }

    @Override
    public void playPagesFlipSound(SoundManager soundHandler) {
        soundHandler.play(SimpleSoundInstance.forUI(EvilCraftSoundEvents.effect_page_flipmultiple, 1.0F));
    }
}
