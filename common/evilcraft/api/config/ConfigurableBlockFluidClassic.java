package evilcraft.api.config;

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
import evilcraft.render.ExtendedEntityDropParticleFX;

/**
 * Block that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableBlockFluidClassic extends BlockFluidClassic implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    public static ElementType TYPE = ElementType.BLOCK;
    
    @SideOnly(Side.CLIENT)
    protected Icon[] icon;
    
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    
    public ConfigurableBlockFluidClassic(ExtendedConfig eConfig, Fluid fluid, Material material) {
        super(eConfig.ID, fluid, material);
        eConfig.ID = this.blockID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.icon = new Icon[] { iconRegister.registerIcon(Reference.MOD_ID+":"+eConfig.NAMEDID+"_still"), iconRegister.registerIcon(Reference.MOD_ID+":"+eConfig.NAMEDID+"_flow") };
    }
    
    @Override
    public Icon getIcon(int side, int meta) {
        return side != 0 && side != 1 ? this.icon[1] : this.icon[0];
    }
    
    public ConfigurableBlockFluidClassic setParticleColor(float particleRed, float particleGreen, float particleBlue) {
        this.particleRed = particleRed;
        this.particleGreen = particleGreen;
        this.particleBlue = particleBlue;
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * For the particle effects underneath a block that has the liquid on top.
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        super.randomDisplayTick(world, x, y, z, rand);

        if (rand.nextInt(10) == 0 && world.doesBlockHaveSolidTopSurface(x, y - 1, z) && !world.getBlockMaterial(x, y - 2, z).blocksMovement()) {
            double px = (double) ((float) x + rand.nextFloat());
            double py = (double) y - 1.05D;
            double pz = (double) ((float) z + rand.nextFloat());

            EntityFX fx = new ExtendedEntityDropParticleFX(world, px, py, pz, particleRed, particleGreen, particleBlue);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }
    
    public boolean isEntity() {
        return false;
    }

}
