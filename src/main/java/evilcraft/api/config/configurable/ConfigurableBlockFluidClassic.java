package evilcraft.api.config.configurable;

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
import evilcraft.api.blockcomponents.EntityDropParticleFXBlockComponent;
import evilcraft.api.blockcomponents.IEntityDropParticleFXBlock;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.events.TextureStitchEventHook;

/**
 * Block that represents an in-world fluid that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockFluidClassic extends BlockFluidClassic implements Configurable, IEntityDropParticleFXBlock{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.BLOCK;
    
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
        this.setBlockName(this.getUniqueName());
        fluid.setBlock(this);
        TextureStitchEventHook.fluidMap.put(fluid, this);
    }

    @Override
    public void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
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
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = new IIcon[] { iconRegister.registerIcon(getTextureName()+"_still"), iconRegister.registerIcon(Reference.MOD_ID+":"+eConfig.NAMEDID+"_flow") };
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
    
    @Override
    public boolean isEntity() {
        return false;
    }

}
