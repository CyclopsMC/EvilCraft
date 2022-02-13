package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.inventory.container.NamedContainerProviderItem;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.inventory.container.ContainerOriginsOfDarkness;

import javax.annotation.Nullable;

import net.minecraft.item.Item.Properties;

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
    public INamedContainerProvider getContainer(World world, PlayerEntity playerEntity, int itemIndex, Hand hand, ItemStack itemStack) {
        return new NamedContainerProviderItem(itemIndex, hand,
                new TranslationTextComponent("gui.cyclopscore.infobook"), ContainerOriginsOfDarkness::new);
    }

    @Override
    public Class<? extends Container> getContainerClass(World world, PlayerEntity playerEntity, ItemStack itemStack) {
        return ContainerOriginsOfDarkness.class;
    }

}
