package org.cyclops.evilcraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.Collection;

/**
 * Config for the {@link BlockDarkTank}.
 * @author rubensworks
 *
 */
public class BlockDarkTankConfig extends BlockConfig {

    @ConfigurableProperty(category = "machine", comment = "The maximum tank size visible in the creative tabs. (Make sure that you do not cross the max int size.)")
    public static int maxTankCreativeSize = 4096000;

    @ConfigurableProperty(category = "machine", comment = "If creative versions for all fluids should be added to the creative tab.")
    public static boolean creativeTabFluids = true;

    @ConfigurableProperty(category = "machine", comment = "If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.", requiresMcRestart = true)
    public static boolean staticBlockRendering = false;

    @ConfigurableProperty(category = "item", comment = "If held buckets should be autofilled when enabled.", isCommandable = true)
    public static boolean autoFillBuckets = false;

    public BlockDarkTankConfig() {
        super(
                EvilCraft._instance,
            "dark_tank",
                eConfig -> new BlockDarkTank(Block.Properties.of()
                        .strength(0.5F)
                        .sound(SoundType.GLASS)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        )
        );
    }

    @Override
    protected Collection<ItemStack> defaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((BlockDarkTank) getInstance()).fillItemCategory(list);
        return list;
    }
}
