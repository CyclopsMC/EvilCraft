package org.cyclops.evilcraft.core.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Torch blockState that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockTorch extends BlockTorch implements IConfigurable {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockState createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty
    public static final IProperty[] _COMPAT = {FACING};

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockTorch(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
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

}
