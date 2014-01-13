package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockWithInnerBlocks;
import evilcraft.entities.monster.Netherfish;
import evilcraft.entities.monster.NetherfishConfig;

public class NetherfishSpawn extends ConfigurableBlockWithInnerBlocks {
    
    private static NetherfishSpawn _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new NetherfishSpawn(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static NetherfishSpawn getInstance() {
        return _instance;
    }

    private NetherfishSpawn(ExtendedConfig eConfig) {
        super(eConfig, Material.clay);
        this.setHardness(0.0F);
    }
    
    @Override
    protected Block[] makeInnerBlockList() {
        return new Block[]{
                Block.netherrack,
                Block.netherBrick, 
                Block.slowSand
                };
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {}
    
    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
        if (!par1World.isRemote && NetherfishConfig._instance.isEnabled()) {
            Netherfish netherfish = new Netherfish(par1World);
            netherfish.setLocationAndAngles((double)par2 + 0.5D, (double)par3, (double)par4 + 0.5D, 0.0F, 0.0F);
            par1World.spawnEntityInWorld(netherfish);
            netherfish.spawnExplosionParticle();
        }

        super.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);
    }
    
    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
    
    /**
     * Does the given metadata correspond to an inner block?
     * @param meta Metadata for the (inner) block
     * @return if the metadata corresponds to an inner block.
     */
    public boolean getPosingIdByMetadata(int meta)
    {
        return getMetadataFromBlockID(meta) > -1;
    }

}
