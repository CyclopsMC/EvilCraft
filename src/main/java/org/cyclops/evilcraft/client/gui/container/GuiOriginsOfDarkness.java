package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.infobook.OriginsOfDarknessBook;

/**
 * Gui for the Origins of Darkness book.
 * @author rubensworks
 */
public class GuiOriginsOfDarkness extends GuiInfoBook {

    protected static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "originsOfDarkness_gui.png");

    public static final int X_OFFSET_OUTER = 20;
    public static final int X_OFFSET_INNER = 7;

    public GuiOriginsOfDarkness(EntityPlayer player, int itemIndex) {
        super(player, itemIndex, OriginsOfDarknessBook.getInstance(), texture);
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
    public void playPageFlipSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.getMasterRecord(EvilCraftSoundEvents.effect_page_flipsingle, 1.0F));
    }

    @Override
    public void playPagesFlipSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.getMasterRecord(EvilCraftSoundEvents.effect_page_flipmultiple, 1.0F));
    }
}
