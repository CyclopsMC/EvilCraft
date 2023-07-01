package org.cyclops.evilcraft.block;

import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.blockentity.RenderItemStackBlockEntityBloodChest;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.List;
import java.util.function.Consumer;

/**
 * Config for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
public class BlockBloodChestConfig extends BlockConfig {

    @ConfigurableProperty(category = "machine", comment = "If the Blood Chest should add random bad enchants with a small chance to repairing items.", isCommandable = true)
    public static boolean addRandomBadEnchants = true;

    @ConfigurableProperty(category = "machine", comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int mBPerDamage = 5;

    @ConfigurableProperty(category = "machine", comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;

    @ConfigurableProperty(category = "machine", comment = "Item names that can not be repaired. Regular expressions are allowed.", isCommandable = true)
    public static List<String> itemBlacklist = Lists.newArrayList(
            "minecraft:stick"
    );

    public BlockBloodChestConfig() {
        super(
                EvilCraft._instance,
                "blood_chest",
                eConfig -> new BlockBloodChest(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.WOOD)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        ) {
                    @OnlyIn(Dist.CLIENT)
                    @Override
                    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                        consumer.accept(new RenderItemStackBlockEntityBloodChest.ItemRenderProperties());
                    }
                }
        );
    }

}
