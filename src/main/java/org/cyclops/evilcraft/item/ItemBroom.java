package org.cyclops.evilcraft.item;

import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.entity.item.EntityBroom;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Item for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class ItemBroom extends ItemBloodContainer implements IBroom {

    private static final float Y_SPAWN_OFFSET = 1.5f;

    public ItemBroom(Item.Properties properties) {
        super(properties, 10 * IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume());
        if (IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            NeoForge.EVENT_BUS.addListener(this::onFovEvent);
        }
    }

    @Override
    public boolean isPlaceFluids() {
        return false;
    }

    @Override
    public boolean isPickupFluids() {
        return false;
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide() && player.getVehicle() == null && !player.isCrouching()) {
            player.setPos(player.getX(), player.getY() + Y_SPAWN_OFFSET, player.getZ());

            EntityBroom entityBroom = new EntityBroom(world, player.getX(), player.getY(), player.getZ());
            entityBroom.setBroomStack(stack);
            entityBroom.setYRot(player.getYRot());
            // Spawn and mount the broom
            world.addFreshEntity(entityBroom);
            player.startRiding(entityBroom);

            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!context.getLevel().isClientSide() && context.getPlayer().isCrouching()) {
            BlockPos blockPos = context.getClickedPos();
            if (!IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(context.getLevel(), blockPos, context.getClickedFace(), net.neoforged.neoforge.capabilities.Capabilities.Fluid.BLOCK).isPresent()
                    && context.getLevel().isEmptyBlock(blockPos.offset(0, (int) Y_SPAWN_OFFSET, 0))) {
                EntityBroom entityBroom = new EntityBroom(context.getLevel(), blockPos.getX() + 0.5, blockPos.getY() + Y_SPAWN_OFFSET, blockPos.getZ() + 0.5);
                entityBroom.setBroomStack(stack);
                entityBroom.setYRot(context.getPlayer().getYRot());
                context.getLevel().addFreshEntity(entityBroom);

                // We don't consume the broom when in creative mode
                if (!context.getPlayer().isCreative())
                    stack.shrink(1);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public Collection<IBroomPart> getBroomParts(ItemStack itemStack) {
        return BroomParts.REGISTRY.getBroomParts(itemStack);
    }

    @Override
    public Map<BroomModifier, Float> getBroomModifiers(ItemStack itemStack) {
        return BroomModifiers.REGISTRY.getModifiers(itemStack);
    }

    @Override
    public boolean canConsumeBroomEnergy(int amount, ItemStack itemStack, @Nullable LivingEntity entityLiving) {
        return canConsume(amount, itemStack, entityLiving instanceof Player ? (Player) entityLiving : null);
    }

    @Override
    public int consumeBroom(int amount, ItemStack itemStack, @Nullable LivingEntity entityLiving) {
        return IModHelpersNeoForge.get().getFluidHelpers().getAmount(consume(amount, itemStack, entityLiving instanceof Player ? (Player) entityLiving : null));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, tooltipDisplay, list, flag);
        if(IModHelpers.get().getMinecraftClientHelpers().isShifted()) {
            list.accept(Component.translatable("broom.parts." + Reference.MOD_ID + ".types")
                    .withStyle(ChatFormatting.ITALIC));
            Map<BroomModifier, Float> baseModifiers = BroomParts.REGISTRY.getBaseModifiersFromBroom(itemStack);
            Map<BroomModifier, Float> modifiers = getBroomModifiers(itemStack);
            Set<BroomModifier> modifierTypes = Sets.newHashSet();
            modifierTypes.addAll(baseModifiers.keySet());
            modifierTypes.addAll(modifiers.keySet());
            for (IBroomPart part : getBroomParts(itemStack)) {
                Component line = part.getTooltipLine("  ");
                if (line != null) {
                    list.accept(line);
                }
            }
            Pair<Integer, Integer> modifiersAndMax = getModifiersAndMax(modifiers, baseModifiers);
            int modifierCount = modifiersAndMax.getLeft();
            int maxModifiers = modifiersAndMax.getRight();
            list.accept(Component.translatable(
                    "broom.modifiers." + Reference.MOD_ID + ".types.nameparam", modifierCount, maxModifiers)
                    .withStyle(ChatFormatting.ITALIC));
            for (BroomModifier modifier : modifierTypes) {
                if(modifier.showTooltip()) {
                    Float value = modifiers.get(modifier);
                    Float baseValue = baseModifiers.get(modifier);
                    list.accept(modifier.getTooltipLine("  ",
                            value     == null ? 0 : value,
                            baseValue == null ? 0 : baseValue));
                }
            }

        } else {
            list.accept(Component.translatable("broom." + Reference.MOD_ID + ".shiftinfo")
                    .withStyle(ChatFormatting.ITALIC));
        }
    }

    private Pair<Integer, Integer> getModifiersAndMax(Map<BroomModifier, Float> broomModifiers,
                                                      Map<BroomModifier, Float> baseModifiers) {
        int baseMaxModifiers = 0;
        if(baseModifiers.containsKey(BroomModifiers.MODIFIER_COUNT)) {
            baseMaxModifiers = (int) (float) baseModifiers.get(BroomModifiers.MODIFIER_COUNT);
        }
        int maxModifiers = baseMaxModifiers;
        int modifiers = 0;
        for (Map.Entry<BroomModifier, Float> entry : broomModifiers.entrySet()) {
            int tier = BroomModifier.getTier(entry.getKey(), entry.getValue());
            if(entry.getKey() == BroomModifiers.MODIFIER_COUNT) {
                maxModifiers += (int) (float) entry.getValue();
            } else {
                modifiers += tier;
            }
        }
        return Pair.of(modifiers, maxModifiers);
    }

    public void onFovEvent(ComputeFovModifierEvent event) {
        if(event.getPlayer().getVehicle() instanceof EntityBroom) {
            EntityBroom broom = (EntityBroom) event.getPlayer().getVehicle();
            double speed = broom.getLastPlayerSpeed();
            event.setNewFovModifier((float) (event.getNewFovModifier() + speed / 10));
        }
    }
}
