package evilcraft.core.config.configurable;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.block.BlockTorch;

/**
 * Torch block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockTorch extends BlockTorch implements IConfigurable{

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockTorch(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        this.setBlockName(eConfig.getUnlocalizedName());
        this.setHardness(0.0F);
        this.setLightLevel(0.9375F);
        this.setStepSound(soundTypeWood);
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
    }

}
