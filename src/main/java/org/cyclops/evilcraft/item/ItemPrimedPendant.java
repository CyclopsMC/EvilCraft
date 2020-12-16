package org.cyclops.evilcraft.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stat;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemHeld;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemStack;
import org.cyclops.cyclopscore.inventory.container.NamedContainerProviderItem;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.inventory.container.ContainerPrimedPendant;

import javax.annotation.Nullable;
import java.util.List;

/**
 * One potion can be inserted to continuously apply it to the player.
 * @author rubensworks
 *
 */
public class ItemPrimedPendant extends ItemBloodContainer {

    private static final int TICK_MODULUS = MinecraftHelpers.SECOND_IN_TICKS / 2;

    public ItemPrimedPendant(Item.Properties properties) {
        super(properties, ItemPrimedPendantConfig.capacity);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        ItemStack potionStack = getPotionStack(itemStack);
        if(!potionStack.isEmpty()) {
            List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(potionStack);
            for(EffectInstance potionEffect : potionEffects) {
                TranslationTextComponent textComponent = new TranslationTextComponent(super.getTranslationKey(itemStack) + ".potion",
                        new TranslationTextComponent(potionEffect.getEffectName()),
                        new TranslationTextComponent("enchantment.level." + (potionEffect.getAmplifier() + 1)));
                Double multiplier =  ItemPrimedPendantConfig.getMultiplier(potionEffect.getPotion());
                if (multiplier != null && multiplier < 0) {
                    textComponent.applyTextStyle(TextFormatting.STRIKETHROUGH);
                }
                list.add(textComponent);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof PlayerEntity
                && world.getGameTime() % TICK_MODULUS == 0) {
            PlayerEntity player = (PlayerEntity) entity;
            ItemStack potionStack = getPotionStack(itemStack);
            if(!potionStack.isEmpty()) {
                List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(potionStack);
                for(EffectInstance potionEffect : potionEffects) {
                    int toDrain = ItemPrimedPendantConfig.usage * (potionEffect.getAmplifier() + 1);
                    Double multiplier = ItemPrimedPendantConfig.getMultiplier(potionEffect.getPotion());
                    if(multiplier != null) {
                        toDrain *= multiplier;
                    }
                    if((multiplier == null || multiplier >= 0) && canConsume(toDrain, itemStack, player)) {
                        player.addPotionEffect(
                                new EffectInstance(potionEffect.getPotion(), TICK_MODULUS * 27, potionEffect.getAmplifier(),
                                        !potionEffect.getCurativeItems().isEmpty(), true));
                        consume(toDrain, itemStack, player);
                    }
                }
            }
        }
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }

    public boolean hasPotionStack(ItemStack itemStack) {
        return !getPotionStack(itemStack).isEmpty();
    }

    public ItemStack getPotionStack(ItemStack itemStack) {
        IInventory inventory = getSupplementaryInventory(itemStack);
        return inventory.getStackInSlot(0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return hasPotionStack(itemStack);
    }

    /**
     * Get the supplementary inventory of the item.
     * @param player The player using the item.
     * @param itemStack The item stack.
     * @param itemIndex The item index.
     * @param hand The hand the item is in.
     * @return The inventory.
     */
    public IInventory getSupplementaryInventory(PlayerEntity player, ItemStack itemStack, int itemIndex, Hand hand) {
        return new NBTSimpleInventoryItemHeld(player, itemIndex, hand, 1, 64, "inventoryItem");
    }

    /**
     * Get the supplementary inventory of the item.
     * @param itemStack The item stack.
     * @return The inventory.
     */
    public IInventory getSupplementaryInventory(ItemStack itemStack) {
        return new NBTSimpleInventoryItemStack(itemStack, 1, 64, "inventoryItem");
    }

    // --- TODO: copy of ItemGui methods, clean this up with Cyclops for 1.8 ---

    @Nullable
    public INamedContainerProvider getContainer(World world, PlayerEntity playerEntity, int itemIndex, Hand hand, ItemStack itemStack) {
        return new NamedContainerProviderItem(itemIndex, hand,
                itemStack.getDisplayName(), ContainerPrimedPendant::new);
    }

    public Class<? extends Container> getContainerClass(World world, PlayerEntity playerEntity, ItemStack itemStack) {
        return ContainerPrimedPendant.class;
    }

    public boolean onDroppedByPlayer(ItemStack itemstack, PlayerEntity player) {
        if (!itemstack.isEmpty() && player instanceof ServerPlayerEntity && player.openContainer != null && player.openContainer.getClass() == this.getContainerClass(player.world, player, itemstack)) {
            player.closeScreen();
        }

        return super.onDroppedByPlayer(itemstack, player);
    }

    public void openGuiForItemIndex(World world, ServerPlayerEntity player, int itemIndex, Hand hand) {
        if (!world.isRemote()) {
            INamedContainerProvider containerProvider = this.getContainer(world, player, itemIndex, hand, player.getHeldItem(hand));
            if (containerProvider != null) {
                NetworkHooks.openGui(player, containerProvider, (packetBuffer) -> {
                    this.writeExtraGuiData(packetBuffer, world, player, itemIndex, hand);
                });
                Stat<ResourceLocation> openStat = this.getOpenStat();
                if (openStat != null) {
                    player.addStat(openStat);
                }
            }
        }

    }

    public void writeExtraGuiData(PacketBuffer packetBuffer, World world, ServerPlayerEntity player, int itemIndex, Hand hand) {
        packetBuffer.writeInt(itemIndex);
        packetBuffer.writeBoolean(hand == Hand.MAIN_HAND);
    }

    @Nullable
    protected Stat<ResourceLocation> getOpenStat() {
        return null;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player instanceof FakePlayer) {
            return new ActionResult(ActionResultType.FAIL, itemStack);
        } else {
            if (player instanceof ServerPlayerEntity) {
                this.openGuiForItemIndex(world, (ServerPlayerEntity)player, player.inventory.currentItem, hand);
            }

            return new ActionResult(ActionResultType.SUCCESS, itemStack);
        }
    }

}
