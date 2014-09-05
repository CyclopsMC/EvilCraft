package evilcraft.core.config.configurable;

import net.minecraft.world.biome.BiomeGenBase;
import evilcraft.core.config.BiomeConfig;
import evilcraft.core.config.ElementType;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.helpers.L10NHelpers;

/**
 * A simple configurable for Biomes, will auto-register itself after construction.
 * @author rubensworks
 *
 */
public class ConfigurableBiome extends BiomeGenBase implements Configurable {

    protected BiomeConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.BIOME;
    
    /**
     * Make a new Biome instance
     * @param eConfig Config for this enchantment.
     */
    protected ConfigurableBiome(BiomeConfig eConfig) {
        super(eConfig.ID);
        this.setConfig(eConfig);
        this.setBiomeName(getLocalizedName());
        
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = (BiomeConfig)eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "biomes."+eConfig.NAMEDID;
    }
    
    /**
     * Get localized name of this biome.
     * @return Localized name.
     */
    public String getLocalizedName() {
        return L10NHelpers.localize("biomes." + eConfig.NAMEDID);
    }
    
    @Override
    public boolean isEntity() {
        return false;
    }

}
