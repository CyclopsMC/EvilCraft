package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockRarityProvider;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.tileentity.TileSanguinaryPedestal;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class SanguinaryPedestal extends ConfigurableBlockContainer implements IInformationProvider, IBlockRarityProvider {
    
    private static SanguinaryPedestal _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SanguinaryPedestal(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SanguinaryPedestal getInstance() {
        return _instance;
    }

    private SanguinaryPedestal(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileSanguinaryPedestal.class);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.02F, 0F, 0.02F, 0.98F, 0.98F, 0.98F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        setBlockBoundsForItemRender();
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB area, List list, Entity entity) {
        setBlockBounds(0, 0, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, x, y, z, area, list, entity);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        // This is ONLY used for the block breaking/broken particles
        // Since the anvil looks very similar, we use that icon.
        return Blocks.anvil.getIcon(0, 0);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock() {
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
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public String getInfo(ItemStack itemStack) {
        if(itemStack.getItemDamage() == 1) {
            return EnumChatFormatting.GRAY + L10NHelpers.localize(this.getUnlocalizedName() + ".boost");
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
        return itemStack.getItemDamage() == 1 ? EnumRarity.uncommon : EnumRarity.common;
    }
}
