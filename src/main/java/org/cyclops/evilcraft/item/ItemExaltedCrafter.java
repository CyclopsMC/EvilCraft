package org.cyclops.evilcraft.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemHeld;
import org.cyclops.cyclopscore.inventory.container.NamedContainerProviderItem;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
    public boolean hasEffect(ItemStack itemStack){
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
        newStack.setTag(itemStack.getTag().copy());
        return newStack;
    }
    
    /**
     * Get the supplementary inventory of the given crafter.
     * @param player The player using the crafter.
     * @param itemStack The item stack.
     * @param itemIndex The item index.
     * @param hand The used hand.
     * @return The inventory.
     */
    public IInventory getSupplementaryInventory(PlayerEntity player, ItemStack itemStack, int itemIndex, Hand hand) {
    	if(this.wooden) {
    		return new NBTSimpleInventoryItemHeld(player, itemIndex, hand, 27, 64, ItemExaltedCrafter.NBT_INVENTORY);
    	}
    	return player.getInventoryEnderChest();
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
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
    public INamedContainerProvider getContainer(World world, PlayerEntity playerEntity, int itemIndex, Hand hand, ItemStack itemStack) {
        return new NamedContainerProviderItem(itemIndex, hand, itemStack.getDisplayName(), ContainerExaltedCrafter::new);
    }

    @Override
    public Class<? extends Container> getContainerClass(World world, PlayerEntity playerEntity, ItemStack itemStack) {
        return ContainerExaltedCrafter.class;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote()) {
            world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(),
                    this.wooden ? SoundEvents.BLOCK_CHEST_OPEN : SoundEvents.BLOCK_ENDER_CHEST_OPEN,
                    SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundNBT nbt) {
        final IItemHandler itemHandler = new ItemHandler(stack);
        return new ICapabilityProvider() {
            @Override
            public <T> LazyOptional getCapability(Capability<T> capability, Direction facing) {
                return ItemExaltedCrafter.this.wooden && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
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
            CompoundNBT rootTag = itemStack.getTag();
            if (rootTag != null && rootTag.contains(ItemExaltedCrafter.NBT_INVENTORY, Constants.NBT.TAG_LIST)) {
                ListNBT nbttaglist = rootTag.getList(ItemExaltedCrafter.NBT_INVENTORY, Constants.NBT.TAG_COMPOUND);
                for (int j = 0; j < nbttaglist.size(); ++j) {
                    CompoundNBT slot = nbttaglist.getCompound(j);
                    int index;
                    if (slot.contains("index")) {
                        index = slot.getInt("index");
                    } else {
                        index = slot.getByte("Slot");
                    }
                    if (index >= 0 && index < getSlots()) {
                        itemStacks.set(index, ItemStack.read(slot));
                    }
                }
            }
            return itemStacks;
        }

        protected void setItemList(NonNullList<ItemStack> itemStacks) {
            CompoundNBT rootTag = itemStack.getOrCreateTag();
            ListNBT slots = new ListNBT();
            for (byte index = 0; index < getSlots(); ++index) {
                ItemStack itemStack = itemStacks.get(index);
                if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                    CompoundNBT slot = new CompoundNBT();
                    slots.add(slot);
                    slot.putByte("Slot", index);
                    itemStack.write(slot);
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
