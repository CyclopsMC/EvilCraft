package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Undead Plank.
 * @author rubensworks
 *
 */
public class UndeadPlankConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadPlankConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadPlankConfig() {
        super(
                EvilCraft._instance,
        	true,
            "undead_plank",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return (ConfigurableBlock) new ConfigurableBlock(this, Material.WOOD) {
            @Override
            public SoundType getSoundType() {
                return SoundType.WOOD;
            }
        }.setHardness(2.0F);
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODPLANK;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
    	Blocks.FIRE.setFireInfo(getBlockInstance(), 5, 20);
    }
    
}
