package evilcraft.blocks;
import java.util.Random;

import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlock;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.render.EntityBloodSplashFX;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BloodStainedDirt extends ConfigurableBlock {
    
    private static BloodStainedDirt _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodStainedDirt(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static BloodStainedDirt getInstance() {
        return _instance;
    }

    private BloodStainedDirt(ExtendedConfig eConfig) {
        super(eConfig, Material.ground);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGravelFootstep);
    }
    
    public int idDropped(int par1, Random random, int zero) {
        return Block.dirt.blockID;
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

}
