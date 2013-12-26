package evilcraft.blocks;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlock;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.monster.Netherfish;

public class NetherfishSpawn extends ConfigurableBlock {
    
    private static NetherfishSpawn _instance = null;
    
    public static final Block[] SPAWN_BLOCKS = new Block[]{Block.netherrack, Block.netherBrick, Block.slowSand};
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new NetherfishSpawn(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static NetherfishSpawn getInstance() {
        return _instance;
    }

    private NetherfishSpawn(ExtendedConfig eConfig) {
        super(eConfig, Material.clay);
        this.setHardness(0.0F);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2) {
        return SPAWN_BLOCKS[par2].getBlockTextureFromSide(par1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {}
    
    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
        if (!par1World.isRemote) {
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
    
    @Override
    protected ItemStack createStackedBlock(int par1) {
        return new ItemStack(SPAWN_BLOCKS[par1]);
    }
    
    @Override
    public int getDamageValue(World par1World, int par2, int par3, int par4){
        return par1World.getBlockMetadata(par2, par3, par4);
    }
    
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int j = 0; j < SPAWN_BLOCKS.length; ++j) {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }
    
    /**
     * Gets the blockID of the block this block is pretending to be according to this block's metadata.
     */
    public static boolean getPosingIdByMetadata(int par0)
    {
        for (int j = 0; j < SPAWN_BLOCKS.length; ++j) {
            if(par0 == SPAWN_BLOCKS[j].blockID) return true;
        }
        return false;
    }

    /**
     * Returns the metadata to use when a Netherfish hides in the block. Sets the block to NetherfishSpawn with this
     * metadata. It changes the displayed texture client side to look like a normal block.
     */
    public static int getMetadataForBlockType(int par0)
    {
        for (int j = 0; j < SPAWN_BLOCKS.length; ++j) {
            if(par0 == SPAWN_BLOCKS[j].blockID) return j;
        }
        return 0;
    }

}
