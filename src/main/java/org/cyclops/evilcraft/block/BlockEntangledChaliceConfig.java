package org.cyclops.evilcraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.blockentity.RenderItemStackBlockEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Config for the {@link BlockEntangledChalice}.
 * @author rubensworks
 *
 */
public class BlockEntangledChaliceConfig extends BlockConfig {

    @ConfigurableProperty(category = "machine", comment = "If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.", requiresMcRestart = true)
    public static boolean staticBlockRendering = false;

    public BlockEntangledChaliceConfig() {
        super(
                EvilCraft._instance,
                "entangled_chalice",
                eConfig -> new BlockEntangledChalice(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemEntangledChalice(block, (new Item.Properties())
                        ) {
                    @OnlyIn(Dist.CLIENT)
                    @Override
                    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                        consumer.accept(new RenderItemStackBlockEntityEntangledChalice.ItemRenderProperties());
                    }
                }
        );
    }

    @Override
    protected Collection<ItemStack> defaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((BlockEntangledChalice) getInstance()).fillItemCategory(list);
        return list;
    }
}
