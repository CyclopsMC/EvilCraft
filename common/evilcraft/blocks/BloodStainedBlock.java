package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlockWithInnerBlocks;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.render.MultiPassBlockRenderer;
import evilcraft.render.EntityBloodSplashFX;

public class BloodStainedBlock extends ConfigurableBlockWithInnerBlocks {
    
    private static BloodStainedBlock _instance = null;
    
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
    
    @Override
    protected Block[] makeInnerBlockList() {
        return new Block[]{
                Block.grass,
                Block.dirt,
                Block.stone,
                Block.stoneBrick,
                Block.cobblestone,
                Block.sand
                };
    }
    
    @Override
    public int getRenderType() {
        return MultiPassBlockRenderer.ID;
    }
    
    @Override
    public int getRenderPasses() {
        return 2;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return getIcon(side, meta, pass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta, int renderPass) {
        if(renderPass < 0)
            return null;
        else if(renderPass == 1)
            return blockIcon;
        else
            return getBlockFromMetadata(meta).getIcon(side, 0);
    }
    
    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        this.splash(par1World, par2, par3, par4);
        super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
    }

    @Override
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
        world.setBlock(x, y, z, getBlockFromMetadata(world.getBlockMetadata(x, y, z)).blockID);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        if(getBlockFromMetadata(world.getBlockMetadata(x, y, z)).blockID == Block.grass.blockID) {
            return Block.grass.colorMultiplier(world, x, y, z);
        } else {
            return super.colorMultiplier(world, x, y, z);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
        if(getBlockFromMetadata(meta).blockID == Block.grass.blockID)
            return Block.grass.getBlockColor();
        else
            return super.getRenderColor(meta);
    }

}
