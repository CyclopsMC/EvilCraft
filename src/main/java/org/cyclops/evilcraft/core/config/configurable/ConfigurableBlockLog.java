package org.cyclops.evilcraft.core.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Block that extends from a clog that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockLog extends BlockLog implements IConfigurable {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockState createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty
    public static final IProperty[] _COMPAT = {LOG_AXIS};

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite iconTop;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite iconSide;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockLog(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockStatedata, int fortune) {
        List<ItemStack> drops = new ArrayList<ItemStack>();
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
