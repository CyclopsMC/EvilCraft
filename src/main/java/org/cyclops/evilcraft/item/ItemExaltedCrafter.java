package org.cyclops.evilcraft.item;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemHeld;
import org.cyclops.cyclopscore.inventory.container.NamedContainerProviderItem;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A portable crafting table with a built-in ender chest.
 * @author rubensworks
 *
 */
public class ItemExaltedCrafter extends ItemGui implements IItemEmpowerable {

    private static final String NBT_RETURNTOINNER = "returnToInner";
    private static final String NBT_INVENTORY = "inventory";

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
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack itemStack){
        return isEmpowered(itemStack) ? Rarity.UNCOMMON : super.getRarity(itemStack);
    }

    @Override
    public boolean isEmpowered(ItemStack itemStack) {
        return empowered;
    }

    @Override
    public ItemStack empower(ItemStack itemStack) {
        ItemStack newStack = new ItemStack(wooden ? RegistryEntries.ITEM_EXALTED_CRAFTER_WOODEN_EMPOWERED : RegistryEntries.ITEM_EXALTED_CRAFTER_EMPOWERED);
        if (itemStack.hasTag()) {
            newStack.setTag(itemStack.getTag().copy());
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
            return new NBTSimpleInventoryItemHeld(player, itemLocation, 27, 64, ItemExaltedCrafter.NBT_INVENTORY);
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
        if(itemStack.hasTag()) {
            itemStack.getTag().putBoolean(NBT_RETURNTOINNER, returnToInner);
        }
    }

    public static boolean isReturnToInner(ItemStack itemStack) {
        if(itemStack.hasTag()) {
            return itemStack.getTag().getBoolean(NBT_RETURNTOINNER);
        }
        return false;
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide()) {
            world.playSound(player, player.getX(), player.getY(), player.getZ(),
                    this.wooden ? SoundEvents.CHEST_OPEN : SoundEvents.ENDER_CHEST_OPEN,
                    SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        return super.use(world, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag nbt) {
        final IItemHandler itemHandler = new ItemHandler(stack);
        return new ICapabilityProvider() {
            @Override
            public <T> LazyOptional getCapability(Capability<T> capability, Direction facing) {
                return ItemExaltedCrafter.this.wooden && capability == ForgeCapabilities.ITEM_HANDLER
                        ? LazyOptional.of(() -> itemHandler) : LazyOptional.empty();
            }
        };
    }

    public class ItemHandler implements IItemHandlerModifiable {

        protected ItemStack itemStack;

        public ItemHandler(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        protected NonNullList<ItemStack> getItemList() {
            NonNullList<ItemStack> itemStacks = NonNullList.withSize(getSlots(), ItemStack.EMPTY);
            CompoundTag rootTag = itemStack.getTag();
            if (rootTag != null && rootTag.contains(ItemExaltedCrafter.NBT_INVENTORY, Tag.TAG_LIST)) {
                ListTag nbttaglist = rootTag.getList(ItemExaltedCrafter.NBT_INVENTORY, Tag.TAG_COMPOUND);
                for (int j = 0; j < nbttaglist.size(); ++j) {
                    CompoundTag slot = nbttaglist.getCompound(j);
                    int index;
                    if (slot.contains("index")) {
                        index = slot.getInt("index");
                    } else {
                        index = slot.getByte("Slot");
                    }
                    if (index >= 0 && index < getSlots()) {
                        itemStacks.set(index, ItemStack.of(slot));
                    }
                }
            }
            return itemStacks;
        }

        protected void setItemList(NonNullList<ItemStack> itemStacks) {
            CompoundTag rootTag = itemStack.getOrCreateTag();
            ListTag slots = new ListTag();
            for (byte index = 0; index < getSlots(); ++index) {
                ItemStack itemStack = itemStacks.get(index);
                if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                    CompoundTag slot = new CompoundTag();
                    slots.add(slot);
                    slot.putByte("Slot", index);
                    itemStack.save(slot);
                }
            }
            rootTag.put(ItemExaltedCrafter.NBT_INVENTORY, slots);
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            NonNullList<ItemStack> itemStacks = getItemList();
            itemStacks.set(slot, stack);
            setItemList(itemStacks);
        }

        @Override
        public int getSlots() {
            return 27;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return getItemList().get(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            NonNullList<ItemStack> itemStacks = getItemList();
            ItemStack existingStack = itemStacks.get(slot);

            int maxStackSize;
            if (!existingStack.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existingStack))
                    return stack;

                maxStackSize = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - existingStack.getCount();

                if (stack.getCount() <= maxStackSize) {
                    if (!simulate) {
                        ItemStack copy = stack.copy();
                        copy.grow(existingStack.getCount());
                        setStackInSlot(slot, copy);
                    }

                    return ItemStack.EMPTY;
                } else  {
                    // copy the stack to not modify the original one
                    stack = stack.copy();
                    if (!simulate) {
                        ItemStack copy = stack.split(maxStackSize);
                        copy.grow(existingStack.getCount());
                        setStackInSlot(slot, copy);
                        return stack;
                    }  else {
                        stack.shrink(maxStackSize);
                        return stack;
                    }
                }
            } else {
                maxStackSize = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
                if (maxStackSize < stack.getCount()) {
                    // copy the stack to not modify the original one
                    stack = stack.copy();
                    if (!simulate) {
                        setStackInSlot(slot, stack.split(maxStackSize));
                        return stack;
                    } else {
                        stack.shrink(maxStackSize);
                        return stack;
                    }
                } else {
                    if (!simulate) {
                        setStackInSlot(slot, stack);
                    }
                    return ItemStack.EMPTY;
                }
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0)
                return ItemStack.EMPTY;

            ItemStack existingStack = getStackInSlot(slot);

            if (existingStack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            ItemStack extracted = existingStack.split(amount);
            if (!simulate) {
                setStackInSlot(slot, existingStack);
            }
            return extracted;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return true;
        }
    }
}
