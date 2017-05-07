package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockStairs;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dark Blood Brick Stairs.
 * @author rubensworks
 *
 */
public class DarkBloodBrickStairsConfig extends BlockConfig {

    /**
     * The unique instance.
     */
    public static DarkBloodBrickStairsConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkBloodBrickStairsConfig() {
        super(
                EvilCraft._instance,
                true,
                "darkBloodBrickStairs",
                null,
                null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableBlockStairs block = (ConfigurableBlockStairs) new ConfigurableBlockStairs(this, DarkBrickConfig._instance.getBlockInstance().getDefaultState()) {
            @SuppressWarnings("deprecation")
            @Override
            public SoundType getSoundType() {
                return SoundType.STONE;
            }
        }.setHardness(5.0F);
        block.setHarvestLevel("pickaxe", 2);
        return block;
    }

}
