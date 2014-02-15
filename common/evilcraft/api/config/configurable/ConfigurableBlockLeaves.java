package evilcraft.api.config.configurable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
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

    private Icon iconOpaque;
    private Icon iconTransparent;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ConfigurableBlockLeaves(ExtendedConfig eConfig) {
        super(eConfig.ID);
        eConfig.ID = this.blockID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
        setBurnProperties(blockID, 30, 60);
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

    @Override
    public abstract int idDropped(int meta, Random random, int zero);

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        if(!world.isRemote) {
            ArrayList<ItemStack> items = getBlockDropped(world, x, y, z, meta, fortune);

            for (ItemStack item : items) {
                if (world.rand.nextFloat() <= chance) {
                    this.dropBlockAsItem_do(world, x, y, z, item);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        return Block.leaves.graphicsLevel ? true : super.shouldSideBeRendered(world, x, y, z, side);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(id, 1, 0));
    }

}
