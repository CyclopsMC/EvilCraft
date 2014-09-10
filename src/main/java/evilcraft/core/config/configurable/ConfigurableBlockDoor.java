package evilcraft.core.config.configurable;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;

/**
 * Door block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockDoor extends BlockDoor implements IConfigurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockDoor(ExtendedConfig eConfig, Material material) {
        super(material);
        this.setConfig(eConfig);
        this.setBlockName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
    }

}
