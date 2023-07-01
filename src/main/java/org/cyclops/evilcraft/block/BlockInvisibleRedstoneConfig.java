package org.cyclops.evilcraft.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * A config for {@link BlockInvisibleRedstone}.
 * @author rubensworks
 *
 */
public class BlockInvisibleRedstoneConfig extends BlockConfig {

    public BlockInvisibleRedstoneConfig() {
        super(
                EvilCraft._instance,
                "invisible_redstone",
                eConfig -> new BlockInvisibleRedstone(Block.Properties.of()
                        .pushReaction(PushReaction.BLOCK)
                        .air()
                        .strength(5.0F, 10.0F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new BlockItem(block, new Item.Properties())
        );
    }

    @Override
    protected Collection<ItemStack> defaultCreativeTabEntries() {
        return Collections.emptyList();
    }
}
