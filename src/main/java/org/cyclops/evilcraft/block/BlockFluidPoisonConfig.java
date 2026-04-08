package org.cyclops.evilcraft.block;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for {@link BlockFluidPoison}.
 * @author rubensworks
 *
 */
public class BlockFluidPoisonConfig extends BlockConfigCommon<IModBase> {

    public BlockFluidPoisonConfig() {
        super(
                EvilCraft._instance,
            "poison",
                (eConfig, properties) -> new BlockFluidPoison(properties
                        .liquid()
                        .noCollision()
                        .strength(100.0F)
                        .randomTicks()
                        .replaceable()),
                (eConfig, block) -> new BlockItem(block, eConfig.createDefaultItemProperties())
        );
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        return Collections.emptyList();
    }

}
