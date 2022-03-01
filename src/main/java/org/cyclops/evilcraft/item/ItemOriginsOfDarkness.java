package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.inventory.container.NamedContainerProviderItem;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.inventory.container.ContainerOriginsOfDarkness;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;

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
    public MenuProvider getContainer(Level world, Player playerEntity, int itemIndex, InteractionHand hand, ItemStack itemStack) {
        return new NamedContainerProviderItem(itemIndex, hand,
                new TranslatableComponent("gui.cyclopscore.infobook"), ContainerOriginsOfDarkness::new);
    }

    @Override
    public Class<? extends AbstractContainerMenu> getContainerClass(Level world, Player playerEntity, ItemStack itemStack) {
        return ContainerOriginsOfDarkness.class;
    }

}
