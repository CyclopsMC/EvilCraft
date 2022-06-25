package org.cyclops.evilcraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.container.NamedContainerProviderItem;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.inventory.container.ContainerOriginsOfDarkness;

import javax.annotation.Nullable;

/**
 * A simple orb that can be filled with blood.
 * @author rubensworks
 *
 */
public class ItemOriginsOfDarkness extends ItemGui {

    public ItemOriginsOfDarkness(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public MenuProvider getContainer(Level world, Player playerEntity, ItemLocation itemLocation) {
        return new NamedContainerProviderItem(itemLocation,
                Component.translatable("gui.cyclopscore.infobook"), ContainerOriginsOfDarkness::new);
    }

    @Override
    public Class<? extends AbstractContainerMenu> getContainerClass(Level world, Player playerEntity, ItemStack itemStack) {
        return ContainerOriginsOfDarkness.class;
    }

}
