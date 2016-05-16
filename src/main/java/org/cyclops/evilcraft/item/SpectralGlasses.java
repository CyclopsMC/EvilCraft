package org.cyclops.evilcraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableItemArmor;

/**
 * Glasses that make you see spirits.
 * @author rubensworks
 *
 */
public class SpectralGlasses extends ConfigurableItemArmor {

    public static ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("SPECTRAL",
            Reference.MOD_ID + ":" + "spectralGlasses",
            15, new int[]{1, 4, 5, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);

    private static SpectralGlasses _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpectralGlasses getInstance() {
        return _instance;
    }

    public SpectralGlasses(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, MATERIAL, EntityEquipmentSlot.HEAD);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.HEAD;
    }
}
