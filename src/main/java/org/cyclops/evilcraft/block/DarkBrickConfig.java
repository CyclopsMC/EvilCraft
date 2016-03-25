package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Dark Brick.
 * @author rubensworks
 *
 */
public class DarkBrickConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static DarkBrickConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkBrickConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkBrick",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        Block block = new ConfigurableBlock(this, Material.rock) {
            @Override
            public SoundType getStepSound() {
                return SoundType.STONE;
            }
        }.setHardness(5.0F);
        block.setHarvestLevel("pickaxe", 2);
        return (ConfigurableBlock) block;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
