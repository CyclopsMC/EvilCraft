package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.MinecraftHelpers;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

/**
 * Config for the Garmonbozia.
 * @author rubensworks
 *
 */
public class GarmonboziaConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static GarmonboziaConfig _instance;

    /**
     * Make a new instance.
     */
    public GarmonboziaConfig() {
        super(
            true,
            "garmonbozia",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this) {
            @Override
            public EnumRarity getRarity(ItemStack itemStack) {
                return EnumRarity.epic;
            }

            @Override
            public boolean hasEffect(ItemStack par1ItemStack, int pass) {
                return true;
            }
        };
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        for(String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
                    getItemInstance(), 0, 1, 3, 2));
        }
    }
    
}
