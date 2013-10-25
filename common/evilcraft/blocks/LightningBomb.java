package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlock;
import evilcraft.api.config.ExtendedConfig;

public class LightningBomb extends ConfigurableBlock {
    
    private static LightningBomb _instance = null;
    
    private Icon blockIconTop;
    private Icon blockIconBottom;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new LightningBomb(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static LightningBomb getInstance() {
        return _instance;
    }

    private LightningBomb(ExtendedConfig eConfig) {
        super(eConfig, Material.tnt);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundPowderFootstep);
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourID)
    {
        if (world.isBlockIndirectlyGettingPowered(x, y, z))
        {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            world.setBlockToAir(x, y, z);
            explode(world, x, y, z);
        }
    }
    
    private void explode(World world, int x, int y, int z) {
        Random rand = new Random();
        for (int i = 0; i < 32; ++i) {
            world.spawnParticle("magicCrit", x, y + rand.nextDouble() * 2.0D, z, rand.nextGaussian(), 0.0D, rand.nextGaussian());
        }

        if (!world.isRemote) {             
            world.addWeatherEffect(new EntityLightningBolt(world, x, y, z));
        }
    }
    
    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return par1 == 0 ? this.blockIconBottom : (par1 == 1 ? this.blockIconTop : this.blockIcon);
    }
    
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(this.getTextureName() + "_side");
        this.blockIconTop = par1IconRegister.registerIcon(this.getTextureName() + "_top");
        this.blockIconBottom = par1IconRegister.registerIcon(this.getTextureName() + "_bottom");
    }

}
