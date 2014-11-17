package evilcraft.item;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.core.tileentity.upgrade.Upgrades;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;

/**
 * Promise item singleton.
 * Used for machine upgrades.
 * 
 * @author rubensworks
 *
 */
public class Promise extends ConfigurableItem {

    private static Promise _instance = null;
    private static final Upgrades.Upgrade[] UPGRADES = new Upgrades.Upgrade[]{
            WorkingTileEntity.UPGRADE_TIER1,
            WorkingTileEntity.UPGRADE_TIER2,
            WorkingTileEntity.UPGRADE_TIER3,
            WorkingTileEntity.UPGRADE_SPEED,
            WorkingTileEntity.UPGRADE_EFFICIENCY
    };
    private static final Map<Upgrades.Upgrade, Integer> MAIN_COLORS = Maps.newHashMap();
    private static final Map<Upgrades.Upgrade, Integer> SECONDARY_COLORS = Maps.newHashMap();
    static {
        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_TIER1, RenderHelpers.RGBToInt(220, 220, 220));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_TIER1, RenderHelpers.RGBToInt(255, 255, 255));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_TIER2, RenderHelpers.RGBToInt(234, 238, 87));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_TIER2, RenderHelpers.RGBToInt(230, 230, 160));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_TIER3, RenderHelpers.RGBToInt(51, 235, 203));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_TIER3, RenderHelpers.RGBToInt(150, 250, 200));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_SPEED, RenderHelpers.RGBToInt(200, 90, 80));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_SPEED, RenderHelpers.RGBToInt(240, 150, 140));

        MAIN_COLORS.put(WorkingTileEntity.UPGRADE_EFFICIENCY, RenderHelpers.RGBToInt(80, 70, 200));
        SECONDARY_COLORS.put(WorkingTileEntity.UPGRADE_EFFICIENCY, RenderHelpers.RGBToInt(150, 140, 150));
    }

    private IIcon overlay;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new Promise(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Promise getInstance() {
        return _instance;
    }

    private Promise(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public int getItemStackLimit(ItemStack itemStack) {
        if(itemStack.getItemDamage() <= 2) { // All the 'tier' upgrades can only have stacksize 1.
            return 1;
        }
        return super.getItemStackLimit(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack, int pass) {
        return pass == 0;
    }

        @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @Override
    public int getRenderPasses(int metadata) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
        Upgrades.Upgrade upgrade = getUpgrade(itemStack);
        return renderPass == 0 ? SECONDARY_COLORS.get(upgrade) : MAIN_COLORS.get(upgrade);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(EnumChatFormatting.DARK_GREEN + "Usable in: TODO");// TODO
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        overlay = iconRegister.registerIcon(getIconString() + "_overlay");
    }
    
    @Override
    public IIcon getIconFromDamageForRenderPass(int meta, int renderpass) {
        return renderpass == 0 ? this.overlay : super.getIconFromDamageForRenderPass(meta, renderpass);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < UPGRADES.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack) + "." + getUpgrade(itemStack).getId();
    }

    /**
     * Get the upgrade for given damage.
     * @param itemStack The item.
     * @return The upgrade instance.
     */
    public Upgrades.Upgrade getUpgrade(ItemStack itemStack) {
        return UPGRADES[Math.min(UPGRADES.length - 1, itemStack.getItemDamage())];
    }

    /**
     * @param itemStack The item.
     * @return If the upgrade is a tier upgrade.
     */
    public boolean isTierUpgrade(ItemStack itemStack) {
        return itemStack != null && itemStack.getItemDamage() <= 2;
    }

}
