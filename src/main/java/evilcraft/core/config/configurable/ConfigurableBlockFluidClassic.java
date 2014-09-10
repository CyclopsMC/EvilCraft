package evilcraft.core.config.configurable;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.block.component.EntityDropParticleFXBlockComponent;
import evilcraft.core.block.component.IEntityDropParticleFXBlock;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.event.TextureStitchEventHook;

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
    protected IIcon[] icon;
    
    @SideOnly(Side.CLIENT)
    protected EntityDropParticleFXBlockComponent entityDropParticleFXBlockComponent;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param fluid The fluid this block has to represent
     * @param material Material of this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockFluidClassic(ExtendedConfig eConfig, Fluid fluid, Material material) {
        super(fluid, material);
        this.setConfig(eConfig);
        this.setBlockName(eConfig.getUnlocalizedName());
        fluid.setBlock(this);
        TextureStitchEventHook.fluidMap.put(fluid, this);
        this.fluid = fluid;
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = new IIcon[] { iconRegister.registerIcon(getTextureName()+"_still"), iconRegister.registerIcon(Reference.MOD_ID+":"+eConfig.getNamedId()+"_flow") };
        fluid.setIcons(icon[0], icon[1]);
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        return side != 0 && side != 1 ? this.icon[1] : this.icon[0];
    }
    
    /**
     * Set the drop particle color.
     * @param particleRed Red color.
     * @param particleGreen Green color.
     * @param particleBlue Blue color.
     * @return This instance of the block.
     */
    @SideOnly(Side.CLIENT)
    public ConfigurableBlockFluidClassic setParticleColor(float particleRed, float particleGreen, float particleBlue) {
        entityDropParticleFXBlockComponent = new EntityDropParticleFXBlockComponent(particleRed, particleGreen, particleBlue);
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * For the particle effects underneath a block that has the liquid on top.
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        super.randomDisplayTick(world, x, y, z, rand);
        if(entityDropParticleFXBlockComponent != null)
            entityDropParticleFXBlockComponent.randomDisplayTick(world, x, y, z, rand);
    }

}
