package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlock;
import evilcraft.items.DarkGemConfig;

public class DarkOre extends ConfigurableBlock {
    
    private static DarkOre _instance = null;
    private static final int MINIMUM_DROPS = 1; // Minimum amount of drops when mining this block
    private static final int INCREASE_DROPS = 3; // Amount that can be increased at random for drops
    private static final int INCREASE_XP = 5; // Amount of XP that can be gained from mining this block
    
    private static final int GLOWINGMETA = 1;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new DarkOre(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static DarkOre getInstance() {
        return _instance;
    }

    private DarkOre(ExtendedConfig eConfig) {
        super(eConfig, Material.rock);
        this.setTickRandomly(true);
        this.setHardness(10.0F);
        this.setStepSound(Block.soundStoneFootstep);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2); // Iron tier
    }
    
    public int idDropped(int par1, Random random, int zero) {
        if(DarkGemConfig._instance.isEnabled())
            return DarkGemConfig._instance.ID;
        else
            return zero;
    }
    
    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        return this.quantityDropped(par2Random) + par2Random.nextInt(par1 + 1);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return MINIMUM_DROPS + par1Random.nextInt(INCREASE_DROPS);
    }
    
    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

        if (this.idDropped(par5, par1World.rand, par7) != this.blockID)
        {
            int j1 = 1 + par1World.rand.nextInt(INCREASE_XP);
            this.dropXpOnBlockBreak(par1World, par2, par3, par4, j1);
        }
    }
    
    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int par1)
    {
        return new ItemStack(DarkOre._instance);
    }
    
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 30;
    }
    
    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        this.glow(par1World, par2, par3, par4);
        super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        this.glow(par1World, par2, par3, par4);
        super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
    }
    
    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        this.glow(par1World, par2, par3, par4);
        return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
    }
    
    private boolean isGlowing(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == GLOWINGMETA;
    }

    /**
     * The darkore glows.
     */
    private void glow(World world, int x, int y, int z)
    {
        this.sparkle(world, x, y, z);

        if (!isGlowing(world, x, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, GLOWINGMETA, 2);// Flag=2 causes client update
            world.scheduleBlockUpdate(x,y,z,blockID,tickRate(world));
        }
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (isGlowing(world, x, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);// Flag=2 causes client update
        }
    }
    
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (isGlowing(world, x, y, z)) {
            this.sparkle(world, x, y, z);
        }
    }
    
    /**
     * The darkore sparkles.
     */
    private void sparkle(World par1World, int par2, int par3, int par4)
    {
        Random random = par1World.rand;
        double d0 = 0.0625D;

        for (int l = 0; l < 6; ++l) {
            double d1 = (double)((float)par2 + random.nextFloat());
            double d2 = (double)((float)par3 + random.nextFloat());
            double d3 = (double)((float)par4 + random.nextFloat());

            if (l == 0 && !par1World.isBlockOpaqueCube(par2, par3 + 1, par4)) {
                d2 = (double)(par3 + 1) + d0;
            }

            if (l == 1 && !par1World.isBlockOpaqueCube(par2, par3 - 1, par4)) {
                d2 = (double)(par3 + 0) - d0;
            }

            if (l == 2 && !par1World.isBlockOpaqueCube(par2, par3, par4 + 1)) {
                d3 = (double)(par4 + 1) + d0;
            }

            if (l == 3 && !par1World.isBlockOpaqueCube(par2, par3, par4 - 1)) {
                d3 = (double)(par4 + 0) - d0;
            }

            if (l == 4 && !par1World.isBlockOpaqueCube(par2 + 1, par3, par4)) {
                d1 = (double)(par2 + 1) + d0;
            }

            if (l == 5 && !par1World.isBlockOpaqueCube(par2 - 1, par3, par4)) {
                d1 = (double)(par2 + 0) - d0;
            }

            if (d1 < (double)par2
                    || d1 > (double)(par2 + 1)
                    || d2 < 0.0D
                    || d2 > (double)(par3 + 1)
                    || d3 < (double)par4
                    || d3 > (double)(par4 + 1)) {
                par1World.spawnParticle("smoke", d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
    @Override
    public boolean canSilkHarvest() {
        return true;
    }

}
