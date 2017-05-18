package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockStairs;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Reinforced Undead Plank Stairs.
 * @author rubensworks
 *
 */
public class ReinforcedUndeadPlankStairsConfig extends BlockConfig {

    /**
     * The unique instance.
     */
    public static ReinforcedUndeadPlankStairsConfig _instance;

    /**
     * Make a new instance.
     */
    public ReinforcedUndeadPlankStairsConfig() {
        super(
                EvilCraft._instance,
                true,
                "reinforced_undead_plank_stairs",
                null,
                null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableBlockStairs block = (ConfigurableBlockStairs) new ConfigurableBlockStairs(this, ReinforcedUndeadPlank.getInstance().getDefaultState()) {
            @SuppressWarnings("deprecation")
            @Override
            public SoundType getSoundType() {
                return SoundType.WOOD;
            }
        }.setHardness(1.5F);
        block.setHarvestLevel("pickaxe", 0);
        return block;
    }

}
