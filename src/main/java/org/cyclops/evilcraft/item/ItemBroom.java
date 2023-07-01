package org.cyclops.evilcraft.item;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.event.RenderOverlayEventHook;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Item for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class ItemBroom extends ItemBloodContainer implements IBroom {

    protected static final ResourceLocation OVERLAY = new ResourceLocation(Reference.MOD_ID, "textures/gui/overlay.png");

    private static final float Y_SPAWN_OFFSET = 1.5f;

    public ItemBroom(Item.Properties properties) {
        super(properties, 10 * FluidHelpers.BUCKET_VOLUME);
        if (MinecraftHelpers.isClientSide()) {
            MinecraftForge.EVENT_BUS.addListener(this::onFovEvent);
            MinecraftForge.EVENT_BUS.addListener(this::onRenderOverlayEvent);
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide() && player.getVehicle() == null && !player.isCrouching()) {
            player.setPos(player.getX(), player.getY() + Y_SPAWN_OFFSET, player.getZ());

            EntityBroom entityBroom = new EntityBroom(world, player.getX(), player.getY(), player.getZ());
            entityBroom.setBroomStack(stack);
            entityBroom.setYRot(player.getYRot());
            // Spawn and mount the broom
            world.addFreshEntity(entityBroom);
            player.startRiding(entityBroom);

            stack.shrink(1);
        }

        return MinecraftHelpers.successAction(stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!context.getLevel().isClientSide() && context.getPlayer().isCrouching()) {
            BlockPos blockPos = context.getClickedPos();
            if (!BlockEntityHelpers.getCapability(context.getLevel(), blockPos, context.getClickedFace(), ForgeCapabilities.FLUID_HANDLER).isPresent()
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
    public Rarity getRarity(ItemStack itemStack) {
        int maxRarity = 0;
        for (IBroomPart part : getBroomParts(itemStack)) {
            maxRarity = Math.max(maxRarity, part.getRarity().ordinal());
        }
        return Rarity.values()[maxRarity];
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
        return FluidHelpers.getAmount(consume(amount, itemStack, entityLiving instanceof Player ? (Player) entityLiving : null));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        if(MinecraftHelpers.isShifted()) {
            list.add(Component.translatable("broom.parts." + Reference.MOD_ID + ".types")
                    .withStyle(ChatFormatting.ITALIC));
            Map<BroomModifier, Float> baseModifiers = BroomParts.REGISTRY.getBaseModifiersFromBroom(itemStack);
            Map<BroomModifier, Float> modifiers = getBroomModifiers(itemStack);
            Set<BroomModifier> modifierTypes = Sets.newHashSet();
            modifierTypes.addAll(baseModifiers.keySet());
            modifierTypes.addAll(modifiers.keySet());
            for (IBroomPart part : getBroomParts(itemStack)) {
                Component line = part.getTooltipLine("  ");
                if (line != null) {
                    list.add(line);
                }
            }
            Pair<Integer, Integer> modifiersAndMax = getModifiersAndMax(modifiers, baseModifiers);
            int modifierCount = modifiersAndMax.getLeft();
            int maxModifiers = modifiersAndMax.getRight();
            list.add(Component.translatable(
                    "broom.modifiers." + Reference.MOD_ID + ".types.nameparam", modifierCount, maxModifiers)
                    .withStyle(ChatFormatting.ITALIC));
            for (BroomModifier modifier : modifierTypes) {
                if(modifier.showTooltip()) {
                    Float value = modifiers.get(modifier);
                    Float baseValue = baseModifiers.get(modifier);
                    list.add(modifier.getTooltipLine("  ",
                            value     == null ? 0 : value,
                            baseValue == null ? 0 : baseValue));
                }
            }

        } else {
            list.add(Component.translatable("broom." + Reference.MOD_ID + ".shiftinfo")
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

    @OnlyIn(Dist.CLIENT)
    public void onFovEvent(ComputeFovModifierEvent event) {
        if(event.getPlayer().getVehicle() instanceof EntityBroom) {
            EntityBroom broom = (EntityBroom) event.getPlayer().getVehicle();
            double speed = broom.getLastPlayerSpeed();
            event.setNewFovModifier((float) (event.getNewFovModifier() + speed / 10));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void onRenderOverlayEvent(RenderGuiEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player.getVehicle() instanceof EntityBroom) {
            EntityBroom broom = (EntityBroom) player.getVehicle();
            ItemStack broomStack = broom.getBroomStack();
            Window resolution = event.getWindow();
            int height = 21;
            int width = 21;
            RenderOverlayEventHook.OverlayPosition overlayPosition = RenderOverlayEventHook.OverlayPosition.values()[
                    Mth.clamp(ItemBroomConfig.guiOverlayPosition, 0, RenderOverlayEventHook.OverlayPosition.values().length - 1)];
            int x = overlayPosition.getX(resolution, width, height) + ItemBroomConfig.guiOverlayPositionOffsetX;
            int y = overlayPosition.getY(resolution, width, height) + ItemBroomConfig.guiOverlayPositionOffsetY;

            event.getGuiGraphics().pose().pushPose();
            GlStateManager._enableBlend();
            GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            // Render slot
            event.getGuiGraphics().blit(OVERLAY, x, y, 11, 0, 24, 24);

            // Render item
            Lighting.setupFor3DItems();
            event.getGuiGraphics().renderItem(broomStack, x + 3, y + 3);
            event.getGuiGraphics().renderItemDecorations(
                    Minecraft.getInstance().gui.getFont(), broomStack, x + 3, y + 3, "");
            Lighting.setupForFlatItems();

            GlStateManager._disableBlend();
            event.getGuiGraphics().pose().popPose();
        }
    }
}
