package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
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
                eConfig -> new Block(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(3.0F, 5.0F)
                        .sound(SoundType.METAL)) {
                    @Override
                    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                        return 5;
                    }

                    @Override
                    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                        return 5;
                    }
                },
                getDefaultItemConstructor(EvilCraft._instance)
        );
        MinecraftForge.EVENT_BUS.addListener(this::onFurnaceFuelBurnTimeEvent);
    }

    public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().getItem() == this.getItemInstance()) {
            event.setBurnTime(32000);
        }
    }
}
