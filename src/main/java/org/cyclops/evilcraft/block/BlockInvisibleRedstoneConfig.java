package org.cyclops.evilcraft.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * A config for {@link BlockInvisibleRedstone}.
 * @author rubensworks
 *
 */
public class BlockInvisibleRedstoneConfig extends BlockConfigCommon<IModBase> {

    public BlockInvisibleRedstoneConfig() {
        super(
                EvilCraft._instance,
                "invisible_redstone",
                (eConfig, properties) -> new BlockInvisibleRedstone(properties
                        .pushReaction(PushReaction.BLOCK)
                        .air()
                        .strength(5.0F, 10.0F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new BlockItem(block, eConfig.createDefaultItemProperties())
        );
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        return Collections.emptyList();
    }
}
