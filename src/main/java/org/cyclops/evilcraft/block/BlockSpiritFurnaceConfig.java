package org.cyclops.evilcraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.List;
import java.util.Set;

/**
 * Config for the {@link BlockSpiritFurnace}.
 * @author rubensworks
 *
 */
public class BlockSpiritFurnaceConfig extends UpgradableBlockContainerConfig {

    public static final String DELIMITER = "\\|";

    @ConfigurableProperty(category = "machine", comment = "How much mB per tick this furnace should consume.")
    public static int mBPerTick = 25;

    @ConfigurableProperty(category = "machine", comment = "How much mB per tick this furnace should consume for player spirit.")
    public static int playerMBPerTick = mBPerTick * 4;

    @ConfigurableProperty(category = "machine", comment = "How much mB per tick this furnace should consume for boss mob spirit.")
    public static int bossMBPerTick = mBPerTick * 10;

    @ConfigurableProperty(category = "machine", comment = "The required amount of ticks for each HP for cooking an entity.")
    public static int requiredTicksPerHp = 10;

    @ConfigurableProperty(category = "machine", comment = "If the machine should play mob death sounds.")
    public static boolean mobDeathSounds = true;

    @ConfigurableProperty(category = "machine", comment = "The 1/X chance for villagers to drop emeralds. 0 means no drops.")
    public static int villagerDropEmeraldChance = 20;

    @ConfigurableProperty(category = "machine",
            comment = "Custom player drops. Maps player UUID to an itemstack. Expects the format domain:itemname:amount:meta for items where amount and meta are optional.")
    public static List<String> playerDrops = Lists.newArrayList(
            "93b459be-ce4f-4700-b457-c1aa91b3b687|minecraft:stone_slab" // Etho's Slab
    );
    @ConfigurableProperty(category = "machine",
            comment = "Custom mob drops. Maps entity names to a loot table resource location. Expects the format entityname|loottable. For example: 'minecraft:pig|minecraft:entities/sheep'")
    public static List<String> mobDrops = Lists.newArrayList(
            //"minecraft:pig|minecraft:entities/sheep",
    );

    public BlockSpiritFurnaceConfig() {
        super(
                EvilCraft._instance,
            "spirit_furnace",
                eConfig -> new BlockSpiritFurnace(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(5.0F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        )
        );
    }

    @Override
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet(
                Upgrades.UPGRADE_EFFICIENCY,
                Upgrades.UPGRADE_SPEED,
                Upgrades.UPGRADE_TIER1,
                Upgrades.UPGRADE_TIER2,
                Upgrades.UPGRADE_TIER3);
    }

}
