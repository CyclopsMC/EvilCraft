package org.cyclops.evilcraft.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemHeld;
import org.cyclops.cyclopscore.inventory.container.NamedContainerProviderItem;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;

import javax.annotation.Nullable;

/**
 * A portable crafting table with a built-in ender chest.
 * @author rubensworks
 *
 */
public class ItemExaltedCrafter extends ItemGui implements IItemEmpowerable {

    private final boolean wooden;
    private final boolean empowered;

    public ItemExaltedCrafter(Properties properties, boolean wooden, boolean empowered) {
        super(properties);
        this.wooden = wooden;
        this.empowered = empowered;
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return isEmpowered(itemStack);
    }

    @Override
    public boolean isEmpowered(ItemStack itemStack) {
        return empowered;
    }

    @Override
    public ItemStack empower(ItemStack itemStack) {
        ItemStack newStack = new ItemStack(wooden ? RegistryEntries.ITEM_EXALTED_CRAFTER_WOODEN_EMPOWERED : RegistryEntries.ITEM_EXALTED_CRAFTER_EMPOWERED);
        for (DataComponentType<?> componentType : itemStack.getComponents().keySet()) {
            newStack.set((DataComponentType) componentType, itemStack.get(componentType));
        }
        return newStack;
    }

    /**
     * Get the supplementary inventory of the given crafter.
     * @param player The player using the crafter.
     * @param itemLocation The item location.
     * @return The inventory.
     */
    public Container getSupplementaryInventory(Player player, ItemLocation itemLocation) {
        if(this.wooden) {
            return new NBTSimpleInventoryItemHeld(player, itemLocation, 27, 64, "default");
        }
        return player.getEnderChestInventory();
    }

    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
        return true;
    }

    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemStack) {
        return new EntityItemEmpowerable(world, (ItemEntity) location);
    }

    public static void setReturnToInner(ItemStack itemStack, boolean returnToInner) {
        itemStack.set(RegistryEntries.COMPONENT_EXALTED_CRAFTER_RETURN_TO_INNER, returnToInner);
    }

    public static boolean isReturnToInner(ItemStack itemStack) {
        return itemStack.getOrDefault(RegistryEntries.COMPONENT_EXALTED_CRAFTER_RETURN_TO_INNER, false);
    }

    @Nullable
    @Override
    public MenuProvider getContainer(Level world, Player playerEntity, ItemLocation itemLocation) {
        return new NamedContainerProviderItem(itemLocation, itemLocation.getItemStack(playerEntity).getHoverName(), ContainerExaltedCrafter::new);
    }

    @Override
    public Class<? extends AbstractContainerMenu> getContainerClass(Level world, Player playerEntity, ItemStack itemStack) {
        return ContainerExaltedCrafter.class;
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide()) {
            world.playSound(player, player.getX(), player.getY(), player.getZ(),
                    this.wooden ? SoundEvents.CHEST_OPEN : SoundEvents.ENDER_CHEST_OPEN,
                    SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        return super.use(world, player, hand);
    }
}
