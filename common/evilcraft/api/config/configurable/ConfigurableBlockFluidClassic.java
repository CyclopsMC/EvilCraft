package evilcraft.api.config.configurable;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.blockcomponents.EntityDropParticleFXBlockComponent;
import evilcraft.api.blockcomponents.IEntityDropParticleFXBlock;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.render.particle.ExtendedEntityDropParticleFX;

/**
 * Block that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableBlockFluidClassic extends BlockFluidClassic implements Configurable, IEntityDropParticleFXBlock{
    
    protected ExtendedConfig eConfig = null;
    public static ElementType TYPE = ElementType.BLOCK;
    
    @SideOnly(Side.CLIENT)
    protected Icon[] icon;
    
    private Fluid fluid;
    
    @SideOnly(Side.CLIENT)
    protected EntityDropParticleFXBlockComponent entityDropParticleFXBlockComponent;
    
    public ConfigurableBlockFluidClassic(ExtendedConfig eConfig, Fluid fluid, Material material) {
        super(eConfig.ID, fluid, material);
        eConfig.ID = this.blockID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
        fluid.setBlockID(this);
        this.fluid = fluid;
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.icon = new Icon[] { iconRegister.registerIcon(getTextureName()+"_still"), iconRegister.registerIcon(Reference.MOD_ID+":"+eConfig.NAMEDID+"_flow") };
    }
    
    @Override
    public Icon getIcon(int side, int meta) {
        return side != 0 && side != 1 ? this.icon[1] : this.icon[0];
    }
    
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
