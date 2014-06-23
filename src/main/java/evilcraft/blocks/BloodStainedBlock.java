package evilcraft.blocks;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.L10NHelpers;
import evilcraft.api.RenderHelpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockWithInnerBlocks;
import evilcraft.api.render.AlternatingBlockIconComponent;
import evilcraft.render.particle.EntityBloodSplashFX;

/**
 * Multiple block types (defined by metadata) that have blood stains.
 * @author rubensworks
 *
 */
public class BloodStainedBlock extends ConfigurableBlockWithInnerBlocks {
    
    private static BloodStainedBlock _instance = null;
    private AlternatingBlockIconComponent alternatingBlockIconComponent = new AlternatingBlockIconComponent(getAlternateIconsAmount());
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodStainedBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodStainedBlock getInstance() {
        return _instance;
    }

    private BloodStainedBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.ground);
        this.setHardness(0.5F);
        this.setStepSound(soundTypeGravel);
    }
    
    @Override
    protected Block[] makeInnerBlockList() {
        return new Block[]{
                Blocks.grass,
                Blocks.dirt,
                Blocks.stone,
                Blocks.stonebrick,
                Blocks.cobblestone,
                Blocks.sand
                };
    }
    
    /**
     * Get the amount of alternative icons for the blood stains.
     * @return The amount of icons.
     */
    public int getAlternateIconsAmount() {
        return 3;
    }
    
    @Override
    public int getRenderPasses() {
        return 2;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        alternatingBlockIconComponent.registerIcons(getTextureName(), iconRegister);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return this.getIcon(side, world.getBlockMetadata(x, y, z), pass, alternatingBlockIconComponent.getAlternateIcon(world, x, y, z, side));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        // Only for inventory blocks
        return getIcon(side, meta, pass, alternatingBlockIconComponent.getBaseIcon());
    }
    
    /**
     * Get the icon.
     * @param side The side to render.
     * @param meta The metadata for the block to render.
     * @param renderPass The renderpass.
     * @param defaultIcon The default icon to render if none needs to be rendered.
     * @return The icon.
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta, int renderPass, IIcon defaultIcon) {
        if(renderPass < 0) {
            return RenderHelpers.EMPTYICON;
        } else if(renderPass == 1) {
            if(side != ForgeDirection.UP.ordinal())
                return RenderHelpers.EMPTYICON;
            return defaultIcon;
        } else {
            return getBlockFromMetadata(meta).getIcon(side, 0);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
        splash(par1World, par2, par3, par4);
        super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {
        splash(par1World, par2, par3, par4);
        super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
    }
    
    /**
     * Spawn particles.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    @SideOnly(Side.CLIENT)
    public static void splash(World world, int x, int y, int z) {
        EntityBloodSplashFX.spawnParticles(world, x, y + 1, z, 1, 1 + world.rand.nextInt(3));
    }
    
    @Override
    public void fillWithRain(World world, int x, int y, int z) {
        // Transform to regular block when it rains
        world.setBlock(x, y, z, getBlockFromMetadata(world.getBlockMetadata(x, y, z)));
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return "Block: "+EnumChatFormatting.ITALIC+getBlockFromMetadata(itemStack.getItemDamage()).getLocalizedName();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        list.add(L10NHelpers.getLocalizedInfo(this));
    }

}
