package evilcraft.item;

import com.google.common.collect.Maps;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

import java.util.List;
import java.util.Map;

/**
 * Blood reactant.
 * 
 * @author rubensworks
 *
 */
public class PromiseAcceptor extends ConfigurableItem {

    private static PromiseAcceptor _instance = null;
    public static final Map<Integer, Integer> COLORS = Maps.newHashMap();
    static {
        COLORS.put(0, RenderHelpers.RGBToInt(255, 255, 255));
        COLORS.put(1, RenderHelpers.RGBToInt(230, 230, 160));
        COLORS.put(2, RenderHelpers.RGBToInt(150, 250, 200));
    }

    private TextureAtlasSprite overlay;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PromiseAcceptor getInstance() {
        return _instance;
    }

    public PromiseAcceptor(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
        return COLORS.get(itemStack.getItemDamage());
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < COLORS.size(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack) + "." + itemStack.getItemDamage();
    }

}
