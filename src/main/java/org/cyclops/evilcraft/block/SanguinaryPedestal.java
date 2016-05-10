package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.tileentity.TileSanguinaryPedestal;

import java.util.List;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class SanguinaryPedestal extends ConfigurableBlockContainer implements IInformationProvider, IBlockRarityProvider {

    @BlockProperty
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 1);

    private static SanguinaryPedestal _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SanguinaryPedestal getInstance() {
        return _instance;
    }

    public SanguinaryPedestal(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileSanguinaryPedestal.class);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int j = 0; j <= 1; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }

    @Override
    public int damageDropped(IBlockState blockState) {
        return (Integer) blockState.getValue(TIER);
    }

    @Override
    public String getInfo(ItemStack itemStack) {
        if(itemStack.getItemDamage() == 1) {
            return TextFormatting.GRAY + L10NHelpers.localize(this.getUnlocalizedName() + ".boost");
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
                                   EntityPlayer entityPlayer, List list, boolean par4) {

    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return itemStack.getItemDamage() == 1 ? EnumRarity.UNCOMMON : EnumRarity.COMMON;
    }
}
