package evilcraft.api.config.configurable;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Door block that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableBlockDoor extends BlockDoor implements Configurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.BLOCK;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ConfigurableBlockDoor(ExtendedConfig eConfig, Material material) {
        super(eConfig.ID, material);
        eConfig.ID = this.blockID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return false;
    }

}
