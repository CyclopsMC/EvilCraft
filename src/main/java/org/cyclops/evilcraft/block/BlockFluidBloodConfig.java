package org.cyclops.evilcraft.block;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for {@link BlockFluidBlood}.
 * @author rubensworks
 *
 */
public class BlockFluidBloodConfig extends BlockConfigCommon<IModBase> {

    public BlockFluidBloodConfig() {
        super(
                EvilCraft._instance,
            "blood",
                (eConfig, properties) -> new BlockFluidBlood(properties
                        .liquid()
                        .noCollision()
                        .strength(100.0F)
                        .randomTicks()
                        .replaceable()),
                (eConfig, block) -> new BlockItem(block, eConfig.createDefaultItemProperties())
        );
    }

    @Override
    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        return Collections.emptyList();
    }
}
