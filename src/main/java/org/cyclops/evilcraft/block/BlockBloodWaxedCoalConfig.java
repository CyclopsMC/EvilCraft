package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Waxed Coal.
 * @author rubensworks
 *
 */
public class BlockBloodWaxedCoalConfig extends BlockConfigCommon<IModBase> {

    public BlockBloodWaxedCoalConfig() {
        super(
            EvilCraft._instance,
            "blood_waxed_coal_block",
                (eConfig, properties) -> new Block(properties
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
                getDefaultItemConstructor(EvilCraft._instance, properties -> properties
                        .component(DataComponents.COOKING_FUEL, new CookingFuel(new ResolvableInt.Constant(32000),
                                ResolvableFloat.fromKey(ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER))))
        );
    }

}
