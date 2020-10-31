package org.cyclops.evilcraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBowlOfPromises}.
 * @author rubensworks
 *
 */
public class ItemBowlOfPromisesConfig extends ItemConfig {

    public ItemBowlOfPromisesConfig(ItemBowlOfPromises.Type type) {
        super(
                EvilCraft._instance,
            "bowl_of_promises",
                eConfig -> new ItemBowlOfPromises(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()), type)
        );
    }

    /*
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for(int tier = 0; tier < getTiers(); tier++) {
            for(int i = tier; i < getTiers(); i++) {
                OreDictionary.registerOre(getBaseDictionaryName() + tier, new ItemStack(BowlOfPromises.getInstance(), 1, 2 + i));
            }
        }
    }
    TODO
     */

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        Minecraft.getInstance().getItemColors().register(new ItemBiomeExtract.ItemColor(), getInstance());
    }
}
