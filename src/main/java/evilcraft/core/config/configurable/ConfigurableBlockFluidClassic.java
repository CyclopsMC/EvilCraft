package evilcraft.core.config.configurable;

import evilcraft.Reference;
import evilcraft.core.block.component.EntityDropParticleFXBlockComponent;
import evilcraft.core.block.component.IEntityDropParticleFXBlock;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.event.TextureStitchEventHook;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Block that represents an in-world fluid that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockFluidClassic extends BlockFluidClassic implements IConfigurable, IEntityDropParticleFXBlock{
    
	private Fluid fluid;
	
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    @SideOnly(Side.CLIENT)
    protected EntityDropParticleFXBlockComponent entityDropParticleFXBlockComponent;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param fluid The fluid this blockState has to represent
     * @param material Material of this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockFluidClassic(ExtendedConfig eConfig, Fluid fluid, Material material) {
        super(fluid, material);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        fluid.setBlock(this);
        TextureStitchEventHook.fluidMap.put(fluid, this);
        this.fluid = fluid;
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }
    
    /**
     * Set the drop particle color.
     * @param particleRed Red color.
     * @param particleGreen Green color.
     * @param particleBlue Blue color.
     * @return This instance of the blockState.
     */
    @SideOnly(Side.CLIENT)
    public ConfigurableBlockFluidClassic setParticleColor(float particleRed, float particleGreen, float particleBlue) {
        entityDropParticleFXBlockComponent = new EntityDropParticleFXBlockComponent(particleRed, particleGreen, particleBlue);
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * For the particle effects underneath a blockState that has the liquid on top.
     */
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState blockState, Random rand) {
        super.randomDisplayTick(world, blockPos, blockState, rand);
        if(entityDropParticleFXBlockComponent != null)
            entityDropParticleFXBlockComponent.randomDisplayTick(world, blockPos, rand);
    }

    /**
     * Get the still icon.
     * @param map The texture map.
     * @return The icon.
     */
    public TextureAtlasSprite getIconLocationStill(TextureMap map) {
        return map.getAtlasSprite(Reference.MOD_ID + ":" + "blocks/" + getConfig().getNamedId() + "_still");
    }

    /**
     * Get the flow icon.
     * @param map The texture map.
     * @return The icon.
     */
    public TextureAtlasSprite getIconLocationFlow(TextureMap map) {
        return map.getAtlasSprite(Reference.MOD_ID + ":" + "blocks/" + getConfig().getNamedId() + "_flow");
    }

}
