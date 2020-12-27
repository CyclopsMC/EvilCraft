package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Waxed Coal.
 * @author rubensworks
 *
 */
public class BlockBloodWaxedCoalConfig extends BlockConfig {

    public BlockBloodWaxedCoalConfig() {
        super(
            EvilCraft._instance,
            "blood_waxed_coal_block",
                eConfig -> new Block(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(3.0F, 5.0F)
                        .sound(SoundType.METAL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
        MinecraftForge.EVENT_BUS.addListener(this::onFurnaceFuelBurnTimeEvent);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        BlockHelpers.setFireInfo(getInstance(), 5, 5);
    }

    public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().getItem() == this.getItemInstance()) {
            event.setBurnTime(32000);
        }
    }
}
