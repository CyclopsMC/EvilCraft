package org.cyclops.evilcraft.block;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.List;

/**
 * Config for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
public class BlockBloodChestConfig extends BlockConfigCommon<ModBaseNeoForge<?>> {

    @ConfigurablePropertyCommon(category = "machine", comment = "The 1/x chance the Blood Chest should add random bad enchants with a small chance to repairing items. Disabled by setting to a negative value.", isCommandable = true)
    public static int randomBadEnchantsChance = 10000;

    @ConfigurablePropertyCommon(category = "machine", comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int mBPerDamage = 5;

    @ConfigurablePropertyCommon(category = "machine", comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;

    @ConfigurablePropertyCommon(category = "machine", comment = "Item names that can not be repaired. Regular expressions are allowed.", isCommandable = true)
    public static List<String> itemBlacklist = Lists.newArrayList(
            "minecraft:stick"
    );

    public BlockBloodChestConfig() {
        super(
                EvilCraft._instance,
                "blood_chest",
                (eConfig, properties) -> new BlockBloodChest(properties
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.WOOD)
                        .isValidSpawn((_, _, _, _) -> false)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, eConfig.createDefaultItemProperties())
        );
    }

    @Override
    public BlockClientConfig<ModBaseNeoForge<?>> constructBlockClientConfig() {
        return new BlockBloodChestConfigClient(this);
    }
}
