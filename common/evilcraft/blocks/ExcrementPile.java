package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
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
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlock;
import evilcraft.items.Broom;
import evilcraft.items.BroomConfig;

public class ExcrementPile extends ConfigurableBlock {
    
    private static ExcrementPile _instance = null;
    
    private static final int CHANCE_DESPAWN = 25; // 1/CHANCE_DESPAWN per random tick chance for despawning and potentially triggering bonemeal event
    private static final int CHANCE_BONEMEAL = 3; // 1/CHANCE_BONEMEAL for ground below to be bonemealed or turn into grass from dirt
    private static final int POISON_DURATION = 3;
    private static final int PIG_BOOST_DURATION = 3;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new ExcrementPile(eConfig);
        else
            eConfig.showDoubleInitError();
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
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
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return player.getCurrentEquippedItem() != null && BroomConfig._instance.isEnabled() && player.getCurrentEquippedItem().itemID == BroomConfig._instance.ID;
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
                            } else {
                                ItemDye.applyBonemeal(new ItemStack(Item.dyePowder, 1), world, xr, y, zr, null);
                            }
                        }
                    }
                }
            }
            world.setBlockToAir(x, y, z);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if(random.nextInt(100) == 0) {
            int meta = world.getBlockMetadata(x, y, z);
            float height = (float)(meta + 1) / 8.0F;
            double d0 = (double)x + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)y + 0.0625F + height);
            double d2 = (double)z + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            
            float f1 = 0.1F-random.nextFloat()*0.2F;
            float f2 = 0.1F-random.nextFloat()*0.2F;
            float f3 = 0.1F-random.nextFloat()*0.2F;
            world.spawnParticle("happyVillager", d0, d1, d2, (double)f1, (double)f2, (double)f3);
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

    @Override
    public boolean isBlockReplaceable(World world, int x, int y, int z) {
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourBlockID)
    {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            // Pigs love excrement
            if(entity instanceof EntityPig) {
                if(isChanceWithHeight(world, x, y, z)) {
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.heal.id, PIG_BOOST_DURATION * 20, 1));
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, PIG_BOOST_DURATION * 20, 1));
                }
            } else if(entity instanceof EntityPlayer || ExcrementPileConfig.hurtEntities) {
                // Poison player or mob with a chance depending on the height of the pile.
                if(isChanceWithHeight(world, x, y, z))
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
            }
        }
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }
    
    private boolean isChanceWithHeight(World world, int x, int y, int z) {
        float height = ((float) (world.getBlockMetadata(x, y, z) & 7) + 1) * 0.125F;
        return world.rand.nextFloat() * 10 < height;
    }
    
    /**
     * If the height of this block can be increased.
     * @param world the World
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public static boolean canHeightenPileAt(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta < 7;
    }

    /**
     * Set the height of a pile.
     * @param world the World
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public static void heightenPileAt(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if(meta < 7)
            world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    }

}
