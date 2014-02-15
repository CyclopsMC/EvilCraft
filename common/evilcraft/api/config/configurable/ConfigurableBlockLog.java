package evilcraft.api.config.configurable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

/**
 * Block that extends from a log that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockLog extends BlockLog implements Configurable{

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.BLOCK;

    private Icon iconTop;
    private Icon iconSide;

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ConfigurableBlockLog(ExtendedConfig eConfig) {
        super(eConfig.ID);
        eConfig.ID = this.blockID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
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
        iconSide = iconRegister.registerIcon(getTextureName());
        iconTop = iconRegister.registerIcon(getTextureName() + "_top");
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected Icon getSideIcon(int par1) {
        return this.iconSide;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected Icon getEndIcon(int par1) {
        return this.iconTop;
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(blockID, 1, 0));
        return drops;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(id, 1, 0));
    }

}
