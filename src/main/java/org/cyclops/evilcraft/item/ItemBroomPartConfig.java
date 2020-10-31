package org.cyclops.evilcraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBroomPart}.
 * @author rubensworks
 *
 */
public class ItemBroomPartConfig extends ItemConfig {

    public ItemBroomPartConfig() {
        super(
            EvilCraft._instance,
            "broom_part",
                eConfig -> new ItemBroomPart(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        Minecraft.getInstance().getItemColors().register(new ItemBroomPart.ItemColor(), getInstance());
    }
    
}
