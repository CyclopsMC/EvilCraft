package evilcraft.blocks;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.Coordinate;
import evilcraft.api.Helpers;
import evilcraft.api.config.ConfigurableBlock;
import evilcraft.api.config.ExtendedConfig;

public class ExcrementPile extends ConfigurableBlock {
    
    private static ExcrementPile _instance = null;
    
    private static final int CHANCE_DESPAWN = 25; // 1/CHANCE_DESPAWN per random tick chance for despawning and potentially triggering bonemeal event
    private static final int CHANCE_BONEMEAL = 3; // 1/CHANCE_BONEMEAL for ground below to be bonemealed or turn into grass from dirt
    private static final int POISON_DURATION = 3;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new ExcrementPile(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static ExcrementPile getInstance() {
        return _instance;
    }

    private ExcrementPile(ExtendedConfig eConfig) {
        super(eConfig, Material.clay);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickRandomly(true);
        this.setBlockBoundsForPileDepth(0);
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return ExcrementPileConfig._instance.ID;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4) & 7;
        float f = 0.125F;
        return AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)((float)par3 + (float)l * f), (double)par4 + this.maxZ);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBoundsForPileDepth(0);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        this.setBlockBoundsForPileDepth(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
    
    protected void setBlockBoundsForPileDepth(int par1) {
        int j = par1 & 7;
        float f = (float)(2 * (1 + j)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        int l = par1World.getBlockId(par2, par3 - 1, par4);        
        Block block = Block.blocksList[l];
        if (block == null) return false;
        if (block == this && (par1World.getBlockMetadata(par2, par3 - 1, par4) & 7) == 7) return true;
        if (!block.isLeaves(par1World, par2, par3 - 1, par4) && !Block.blocksList[l].isOpaqueCube()) return false;
        return par1World.getBlockMaterial(par2, par3 - 1, par4).blocksMovement();
    }

    @Override
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
        par1World.setBlockToAir(par3, par4, par5);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }
    
    @Override
    public int tickRate(World par1World) {
        return 100;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        this.poisonEffect(world, x, y, z);
        if(random.nextInt(CHANCE_DESPAWN) == 0) {
            if(random.nextInt(CHANCE_BONEMEAL) == 0) {
                for(int xr = x - 1; xr <= x + 1; xr++) {
                    for(int zr = z - 1; zr <= z + 1; zr++) {
                        if(random.nextInt(9) == 0) {
                            int blockIDBelow = world.getBlockId(xr, y - 1, zr);
                            if(blockIDBelow == Block.dirt.blockID) {
                                world.setBlock(xr, y - 1, zr, Block.grass.blockID);
                            } else if(blockIDBelow == Block.grass.blockID) {
                                ItemDye.applyBonemeal(new ItemStack(Item.dyePowder, 1), world, xr, y - 1, zr, null);
                            }
                        }
                    }
                }
            }
            world.setBlockToAir(x, y, z);
        }
    }

    private void poisonEffect(World world, int x, int y, int z) {
        // TODO: this is not working for some reason...
        Random random = world.rand;
        double d0 = 0.0625D;
        
        Iterator<Coordinate> it = Helpers.getSideIterator(x, y, z);
        while(it.hasNext()) {
            Coordinate c = it.next();
            double d1 = (double)((float)x + random.nextFloat());
            double d2 = (double)((float)y + random.nextFloat());
            double d3 = (double)((float)z + random.nextFloat());
            
            if(c.x > x && !world.isBlockOpaqueCube(c.x, c.y, c.z)) {
                d1 = (double)(x + 1) + d0;
            }
            if(c.x < x && !world.isBlockOpaqueCube(c.x, c.y, c.z)) {
                d1 = (double)(x + 0) - d0;
            }
            if(c.y > y && !world.isBlockOpaqueCube(c.x, c.y, c.z)) {
                d2 = (double)(y + 1) + d0;
            }
            if(c.y < y && !world.isBlockOpaqueCube(c.x, c.y, c.z)) {
                d2 = (double)(y + 0) - d0;
            }
            if(c.z > z && !world.isBlockOpaqueCube(c.x, c.y, c.z)) {
                d3 = (double)(z + 1) + d0;
            }
            if(c.z < z && !world.isBlockOpaqueCube(c.x, c.y, c.z)) {
                d3 = (double)(z + 0) - d0;
            }
            if (d1 < (double)x
                    || d1 > (double)(x + 1)
                    || d2 < 0.0D
                    || d2 > (double)(y + 1)
                    || d3 < (double)z
                    || d3 > (double)(z + 1)) {
                world.spawnParticle("bubble", d1, d2, d3, 0.2D, 1.0D, 0.1D);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return par5 == 1 ? true : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return (meta & 7) + 1;
    }

    /**
     * Determines if a new block can be replace the space occupied by this one,
     * Used in the player's placement code to make the block act like water, and lava.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is replaceable by another block
     */
    @Override
    public boolean isBlockReplaceable(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return (meta >= 7 ? false : blockMaterial.isReplaceable());
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            if(entity instanceof EntityPlayer || ExcrementPileConfig.hurtEntities) {
                // Poison player or mob with a chance depending on the height of the pile.
                float height = ((float) (world.getBlockMetadata(x, y, z) & 7) + 1) * 0.125F;
                if(world.rand.nextFloat() * 10 < height)
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
            }
        }
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }

}
