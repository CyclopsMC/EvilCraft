package evilcraft.api.config.configurable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Block that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableBlockLeaves extends BlockLeaves implements Configurable{

    protected ExtendedConfig eConfig = null;

    public static ElementType TYPE = ElementType.BLOCK;

    private Icon iconOpaque;
    private Icon iconTransparent;
    
    public ConfigurableBlockLeaves(ExtendedConfig eConfig) {
        super(eConfig.ID);
        eConfig.ID = this.blockID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }

    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }

    public boolean isEntity() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        iconTransparent = iconRegister.registerIcon(getTextureName());
        iconOpaque = iconRegister.registerIcon(getTextureName() + "_opaque");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return Block.leaves.graphicsLevel ? iconTransparent : iconOpaque;
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return Block.leaves.graphicsLevel ? iconTransparent : iconTransparent;
    }

    @Override
    public boolean isOpaqueCube() {
        return !Block.leaves.graphicsLevel;
    }

    public abstract int idDropped(int par1, Random par2Random, int par3);

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        if(!par1World.isRemote) {
            ArrayList<ItemStack> items = getBlockDropped(par1World, par2, par3, par4, par5, par7);

            for (ItemStack item : items) {
                if (par1World.rand.nextFloat() <= par6) {
                    this.dropBlockAsItem_do(par1World, par2, par3, par4, item);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side) {
        return Block.leaves.graphicsLevel ? true : super.shouldSideBeRendered(iba, x, y, z, side);
    }

    @Override
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(id, 1, 0));
    }

}
