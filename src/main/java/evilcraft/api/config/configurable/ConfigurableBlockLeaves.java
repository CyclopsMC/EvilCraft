package evilcraft.api.config.configurable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Block that extends from BlockLeaves that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockLeaves extends BlockLeaves implements Configurable{

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.BLOCK;

    private IIcon iconOpaque;
    private IIcon iconTransparent;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockLeaves(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        this.setBlockName(this.getUniqueName());
    }

    @Override
    public void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }

    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconTransparent = iconRegister.registerIcon(getTextureName());
        iconOpaque = iconRegister.registerIcon(getTextureName() + "_opaque");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return !field_150121_P? iconTransparent : iconOpaque;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return !field_150121_P? iconTransparent : iconOpaque;
    }

    @Override
    public abstract Item getItemDropped(int meta, Random random, int zero);

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        if(!world.isRemote) {
            ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, fortune);

            for (ItemStack item : items) {
                if (world.rand.nextFloat() <= chance) {
                    this.dropBlockAsItem(world, x, y, z, item);
                }
            }
        }
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
    	if (side > 7 || field_150121_P) {
            return super.shouldSideBeRendered(world, x, y, z, side);
        } else {
            return true;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }
    
    @Override
    public boolean isLeaves (IBlockAccess world, int x, int y, int z) {
        return true;
    }

}
