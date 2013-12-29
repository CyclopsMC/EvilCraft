package evilcraft.blocks;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlock;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.render.EntityBloodSplashFX;

public class BloodStainedBlock extends ConfigurableBlock {
    
    private static BloodStainedBlock _instance = null;
    
    private static final String[] TEXTURE_SUFFIXES = new String[]{"_dirt"}; //, "_grass"
    private static final Block[] INNER_BLOCKS = new Block[]{Block.dirt}; //, Block.grass
    
    private Icon[] icons = new Icon[TEXTURE_SUFFIXES.length];
    private Icon icon = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodStainedBlock(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static BloodStainedBlock getInstance() {
        return _instance;
    }

    private BloodStainedBlock(ExtendedConfig eConfig) {
        super(eConfig, Material.ground);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGravelFootstep);
    }
    
    /**
     * Check the metadata for the given blockid if a stained version exists
     * @param blockId The id to search a stained version for
     * @return metadata for this block or -1 if none can be found.
     */
    public static int getStainedBlockMetadata(int blockID) {
        for(int i = 0; i < INNER_BLOCKS.length; i++) {
            if(INNER_BLOCKS[i].blockID == blockID)
                return i;
        }
        return -1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        for(int i = 0; i < TEXTURE_SUFFIXES.length; i++)
            icons[i] = iconRegister.registerIcon(getTextureName() + TEXTURE_SUFFIXES[i]);
        iconRegister.registerIcon(getTextureName());
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2) {
        return icons[Math.min(TEXTURE_SUFFIXES.length - 1, par2)];
    }
    
    @Override
    public Icon getIcon(int side, int meta, int renderPass) {
        if(renderPass == 1)
            return icon;
        else
            return INNER_BLOCKS[meta].getIcon(side, 0);
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return INNER_BLOCKS[par1].blockID;
    }
    
    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        this.splash(par1World, par2, par3, par4);
        super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        this.splash(par1World, par2, par3, par4);
        super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
    }
    
    private void splash(World world, int x, int y, int z) {
        Random random = new Random();
        EntityBloodSplashFX.spawnParticles(world, x, y + 1, z, 1, 1 + random.nextInt(3));
    }
    
    @Override
    public void fillWithRain(World world, int x, int y, int z) {
        // Transform to regular block when it rains
        world.setBlock(x, y, z, INNER_BLOCKS[world.getBlockMetadata(x, y, z)].blockID);
    }
    
    @Override
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        for (int j = 0; j < TEXTURE_SUFFIXES.length; ++j) {
            list.add(new ItemStack(id, 1, j));
        }
    }

}
