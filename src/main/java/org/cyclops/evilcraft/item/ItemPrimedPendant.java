package org.cyclops.evilcraft.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        ItemStack potionStack = getPotionStack(itemStack);
        if(!potionStack.isEmpty()) {
            List<MobEffectInstance> potionEffects = PotionUtils.getMobEffects(potionStack);
            for(MobEffectInstance potionEffect : potionEffects) {
                TranslatableComponent textComponent = new TranslatableComponent(super.getDescriptionId(itemStack) + ".potion",
                        new TranslatableComponent(potionEffect.getDescriptionId()),
                        new TranslatableComponent("enchantment.level." + (potionEffect.getAmplifier() + 1)));
                Double multiplier =  ItemPrimedPendantConfig.getMultiplier(potionEffect.getEffect());
                if (multiplier != null && multiplier < 0) {
                    textComponent.withStyle(ChatFormatting.STRIKETHROUGH);
                }
                list.add(textComponent);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int par4, boolean par5) {
        if(entity instanceof Player
                && world.getGameTime() % TICK_MODULUS == 0) {
            Player player = (Player) entity;
            ItemStack potionStack = getPotionStack(itemStack);
            if(!potionStack.isEmpty()) {
                List<MobEffectInstance> potionEffects = PotionUtils.getMobEffects(potionStack);
                for(MobEffectInstance potionEffect : potionEffects) {
                    int toDrain = ItemPrimedPendantConfig.usage * (potionEffect.getAmplifier() + 1);
                    Double multiplier = ItemPrimedPendantConfig.getMultiplier(potionEffect.getEffect());
                    if(multiplier != null) {
                        toDrain *= multiplier;
                    }
                    if((multiplier == null || multiplier >= 0) && canConsume(toDrain, itemStack, player)) {
                        player.addEffect(
                                new MobEffectInstance(potionEffect.getEffect(), TICK_MODULUS * 27, potionEffect.getAmplifier(),
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
        Container inventory = getSupplementaryInventory(itemStack);
        return inventory.getItem(0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemStack) {
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
    public Container getSupplementaryInventory(Player player, ItemStack itemStack, int itemIndex, InteractionHand hand) {
        return new NBTSimpleInventoryItemHeld(player, itemIndex, hand, 1, 64, "inventoryItem");
    }

    /**
     * Get the supplementary inventory of the item.
     * @param itemStack The item stack.
     * @return The inventory.
     */
    public Container getSupplementaryInventory(ItemStack itemStack) {
        return new NBTSimpleInventoryItemStack(itemStack, 1, 64, "inventoryItem");
    }

    // --- TODO: copy of ItemGui methods, clean this up with Cyclops for 1.8 ---

    @Nullable
    public MenuProvider getContainer(Level world, Player playerEntity, int itemIndex, InteractionHand hand, ItemStack itemStack) {
        return new NamedContainerProviderItem(itemIndex, hand,
                itemStack.getHoverName(), ContainerPrimedPendant::new);
    }

    public Class<? extends AbstractContainerMenu> getContainerClass(Level world, Player playerEntity, ItemStack itemStack) {
        return ContainerPrimedPendant.class;
    }

    public boolean onDroppedByPlayer(ItemStack itemstack, Player player) {
        if (!itemstack.isEmpty() && player instanceof ServerPlayer && player.containerMenu != null && player.containerMenu.getClass() == this.getContainerClass(player.level, player, itemstack)) {
            player.closeContainer();
        }

        return super.onDroppedByPlayer(itemstack, player);
    }

    public void openGuiForItemIndex(Level world, ServerPlayer player, int itemIndex, InteractionHand hand) {
        if (!world.isClientSide()) {
            MenuProvider containerProvider = this.getContainer(world, player, itemIndex, hand, player.getItemInHand(hand));
            if (containerProvider != null) {
                NetworkHooks.openGui(player, containerProvider, (packetBuffer) -> {
                    this.writeExtraGuiData(packetBuffer, world, player, itemIndex, hand);
                });
                Stat<ResourceLocation> openStat = this.getOpenStat();
                if (openStat != null) {
                    player.awardStat(openStat);
                }
            }
        }

    }

    public void writeExtraGuiData(FriendlyByteBuf packetBuffer, Level world, ServerPlayer player, int itemIndex, InteractionHand hand) {
        packetBuffer.writeInt(itemIndex);
        packetBuffer.writeBoolean(hand == InteractionHand.MAIN_HAND);
    }

    @Nullable
    protected Stat<ResourceLocation> getOpenStat() {
        return null;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player instanceof FakePlayer) {
            return new InteractionResultHolder(InteractionResult.FAIL, itemStack);
        } else {
            if (player instanceof ServerPlayer) {
                this.openGuiForItemIndex(world, (ServerPlayer)player, player.getInventory().selected, hand);
            }

            return new InteractionResultHolder(InteractionResult.SUCCESS, itemStack);
        }
    }

}
