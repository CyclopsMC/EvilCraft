package org.cyclops.evilcraft.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Config for the {@link ItemBroomPart}.
 * @author rubensworks
 *
 */
public class ItemBroomPartConfig extends ItemConfigCommon<ModBaseNeoForge<?>> {

    public ItemBroomPartConfig() {
        super(
            EvilCraft._instance,
            "broom_part",
                (eConfig, properties) -> new ItemBroomPart(properties)
        );
    }

    @Override
    public @Nullable ItemClientConfig<ModBaseNeoForge<?>> constructItemClientConfig() {
        return new ItemBroomPartConfigClient(this);
    }

    @Override
    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        ((ItemBroomPart) getInstance()).fillItemCategory(list);
        return list;
    }

}
