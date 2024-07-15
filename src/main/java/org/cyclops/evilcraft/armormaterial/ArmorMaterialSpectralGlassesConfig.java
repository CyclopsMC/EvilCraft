package org.cyclops.evilcraft.armormaterial;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.cyclops.cyclopscore.config.extendedconfig.ArmorMaterialConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.EnumMap;
import java.util.List;

/**
 * Config for spectral glasses material.
 * @author rubensworks
 */
public class ArmorMaterialSpectralGlassesConfig extends ArmorMaterialConfig {
    public ArmorMaterialSpectralGlassesConfig() {
        super(EvilCraft._instance, "spectral_glasses", eConfig -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                    p_323384_.put(ArmorItem.Type.BOOTS, 1);
                    p_323384_.put(ArmorItem.Type.LEGGINGS, 2);
                    p_323384_.put(ArmorItem.Type.CHESTPLATE, 3);
                    p_323384_.put(ArmorItem.Type.HELMET, 1);
                    p_323384_.put(ArmorItem.Type.BODY, 3);
                }),
                15,
                SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.of(RegistryEntries.ITEM_DARK_GEM_CRUSHED.get()),
                List.of(
                        new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "spectral_glasses"), "", false)
                ),
                0.0F,
                0.0F
        ));
    }
}
