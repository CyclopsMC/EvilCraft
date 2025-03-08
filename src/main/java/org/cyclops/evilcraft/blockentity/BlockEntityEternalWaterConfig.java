package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityEternalWater}.
 * @author rubensworks
 *
 */
public class BlockEntityEternalWaterConfig extends BlockEntityConfigCommon<BlockEntityEternalWater, IModBase> {

    public BlockEntityEternalWaterConfig() {
        super(
                EvilCraft._instance,
                "eternal_water",
                (eConfig) -> new BlockEntityType<>(BlockEntityEternalWater::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ETERNAL_WATER.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityEternalWater.CapabilityRegistrar(this::getInstance)::register);
    }

}
