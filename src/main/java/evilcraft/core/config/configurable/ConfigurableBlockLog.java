package evilcraft.core.config.configurable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Block that extends from a log that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockLog extends BlockLog implements IConfigurable{

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    private IIcon iconTop;
    private IIcon iconSide;

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockLog(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        this.setBlockName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconSide = iconRegister.registerIcon(getTextureName());
        iconTop = iconRegister.registerIcon(getTextureName() + "_top");
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected IIcon getSideIcon(int par1) {
        return this.iconSide;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected IIcon getTopIcon(int par1) {
        return this.iconTop;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(this, 1, 0));
        return drops;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

}
