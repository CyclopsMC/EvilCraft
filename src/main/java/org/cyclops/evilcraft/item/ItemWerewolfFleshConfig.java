package org.cyclops.evilcraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemWerewolfFlesh}
 * @author rubensworks
 *
 */
public class ItemWerewolfFleshConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "Humanoid flesh will drop in a 1/X chance.", isCommandable = true)
    public static int humanoidFleshDropChance = 5;

    public ItemWerewolfFleshConfig(boolean humanoid) {
        super(
                EvilCraft._instance,
                humanoid ? "flesh_humanoid" : "flesh_werewolf",
                eConfig -> new ItemWerewolfFlesh(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .maxStackSize(16), humanoid)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        Minecraft.getInstance().getItemColors().register(new ItemWerewolfFlesh.ItemColor(), getInstance());
    }

    /*
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_FLESH; TODO
    }*/
}
