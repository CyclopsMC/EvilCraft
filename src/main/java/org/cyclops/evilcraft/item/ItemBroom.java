package org.cyclops.evilcraft.item;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
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
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote() && player.getRidingEntity() == null && !player.isCrouching()) {
            player.setPosition(player.getPosX(), player.getPosY() + Y_SPAWN_OFFSET, player.getPosZ());
            
            EntityBroom entityBroom = new EntityBroom(world, player.getPosX(), player.getPosY(), player.getPosZ());
            entityBroom.setBroomStack(stack);
            entityBroom.rotationYaw = player.rotationYaw;
            // Spawn and mount the broom
            world.addEntity(entityBroom);
            player.startRiding(entityBroom);

            stack.shrink(1);
        }
        
        return MinecraftHelpers.successAction(stack);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
    	if (!context.getWorld().isRemote() && context.getPlayer().isCrouching()) {
            BlockPos blockPos = context.getPos();
            if (!TileHelpers.getCapability(context.getWorld(), blockPos, context.getFace(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()
                    && context.getWorld().isAirBlock(blockPos.add(0, Y_SPAWN_OFFSET, 0))) {
                EntityBroom entityBroom = new EntityBroom(context.getWorld(), blockPos.getX() + 0.5, blockPos.getY() + Y_SPAWN_OFFSET, blockPos.getZ() + 0.5);
                entityBroom.setBroomStack(stack);
                entityBroom.rotationYaw = context.getPlayer().rotationYaw;
                context.getWorld().addEntity(entityBroom);

                // We don't consume the broom when in creative mode
                if (!context.getPlayer().isCreative())
                    stack.shrink(1);

                return ActionResultType.SUCCESS;
            }
    	}
    	
    	return ActionResultType.PASS;
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
        return canConsume(amount, itemStack, entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null);
    }

    @Override
    public int consumeBroom(int amount, ItemStack itemStack, @Nullable LivingEntity entityLiving) {
        return FluidHelpers.getAmount(consume(amount, itemStack, entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        if(MinecraftHelpers.isShifted()) {
            list.add(new TranslationTextComponent("broom.parts." + Reference.MOD_ID + ".types")
                    .applyTextStyle(TextFormatting.ITALIC));
            Map<BroomModifier, Float> baseModifiers = BroomParts.REGISTRY.getBaseModifiersFromBroom(itemStack);
            Map<BroomModifier, Float> modifiers = getBroomModifiers(itemStack);
            Set<BroomModifier> modifierTypes = Sets.newHashSet();
            modifierTypes.addAll(baseModifiers.keySet());
            modifierTypes.addAll(modifiers.keySet());
            for (IBroomPart part : getBroomParts(itemStack)) {
                ITextComponent line = part.getTooltipLine("  ");
                if (line != null) {
                    list.add(line);
                }
            }
            Pair<Integer, Integer> modifiersAndMax = getModifiersAndMax(modifiers, baseModifiers);
            int modifierCount = modifiersAndMax.getLeft();
            int maxModifiers = modifiersAndMax.getRight();
            list.add(new TranslationTextComponent(
                    "broom.modifiers." + Reference.MOD_ID + ".types.nameparam", modifierCount, maxModifiers)
                    .applyTextStyle(TextFormatting.ITALIC));
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
            list.add(new TranslationTextComponent("broom." + Reference.MOD_ID + ".shiftinfo")
                    .applyTextStyle(TextFormatting.ITALIC));
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
    public void onFovEvent(FOVUpdateEvent event) {
        if(event.getEntity().getRidingEntity() instanceof EntityBroom) {
            EntityBroom broom = (EntityBroom) event.getEntity().getRidingEntity();
            double speed = broom.getLastPlayerSpeed();
            event.setNewfov((float) (event.getFov() + speed / 10));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void onRenderOverlayEvent(RenderGameOverlayEvent.Post event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.getRidingEntity() instanceof EntityBroom
                && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            EntityBroom broom = (EntityBroom) player.getRidingEntity();
            ItemStack broomStack = broom.getBroomStack();
            MainWindow resolution = event.getWindow();
            int height = 21;
            int width = 21;
            RenderOverlayEventHook.OverlayPosition overlayPosition = RenderOverlayEventHook.OverlayPosition.values()[
                    MathHelper.clamp(ItemBroomConfig.guiOverlayPosition, 0, RenderOverlayEventHook.OverlayPosition.values().length - 1)];
            int x = overlayPosition.getX(resolution, width, height) + ItemBroomConfig.guiOverlayPositionOffsetX;
            int y = overlayPosition.getY(resolution, width, height) + ItemBroomConfig.guiOverlayPositionOffsetY;

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelpers.bindTexture(OVERLAY);

            // Render slot
            Minecraft.getInstance().ingameGUI.blit(x, y, 11, 0, 24, 24);

            // Render item
            GlStateManager.enableLighting();
            RenderHelper.enableStandardItemLighting();
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(broomStack, x + 3, y + 3);
            Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(
                    Minecraft.getInstance().ingameGUI.getFontRenderer(), broomStack, x + 3, y + 3, "");
            RenderHelper.enableStandardItemLighting();
            GlStateManager.disableLighting();

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
