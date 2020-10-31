package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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

    public ContainerScreenOriginsOfDarkness(ContainerOriginsOfDarkness container, PlayerInventory playerInventory, ITextComponent title) {
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
    public void playPageFlipSound(SoundHandler soundHandler) {
        soundHandler.play(SimpleSound.master(EvilCraftSoundEvents.effect_page_flipsingle, 1.0F));
    }

    @Override
    public void playPagesFlipSound(SoundHandler soundHandler) {
        soundHandler.play(SimpleSound.master(EvilCraftSoundEvents.effect_page_flipmultiple, 1.0F));
    }
}
