package org.cyclops.evilcraft.item;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.event.RenderOverlayEventHook;
import org.cyclops.evilcraft.fluid.Blood;
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
public class Broom extends ConfigurableDamageIndicatedItemFluidContainer implements IBroom {

    protected static final ResourceLocation OVERLAY = new ResourceLocation(Reference.MOD_ID, "textures/gui/overlay.png");

    private static Broom _instance = null;
    
    private static final float Y_SPAWN_OFFSET = 1.5f;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Broom getInstance() {
        return _instance;
    }

    public Broom(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, 10 * FluidContainerRegistry.BUCKET_VOLUME, Blood.getInstance());
        this.maxStackSize = 1;
        MinecraftForge.EVENT_BUS.register(this);
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && player.ridingEntity == null && !player.isSneaking()) {
            player.posY += Y_SPAWN_OFFSET;
            
            EntityBroom entityBroom = new EntityBroom(world, player.posX, player.posY, player.posZ);
            entityBroom.setBroomStack(stack);
            entityBroom.rotationYaw = player.rotationYaw;
            // Spawn and mount the broom
            world.spawnEntityInWorld(entityBroom);
            entityBroom.mountEntity(player);
            
            stack.stackSize--;
        }
        
        return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if (!world.isRemote && player.isSneaking()) {
            EntityBroom entityBroom = new EntityBroom(world, blockPos.getX() + 0.5, blockPos.getY() + Y_SPAWN_OFFSET, blockPos.getZ() + 0.5);
            entityBroom.setBroomStack(stack);
            entityBroom.rotationYaw = player.rotationYaw;
    		world.spawnEntityInWorld(entityBroom);
    		
    		// We don't consume the broom when in creative mode
    		if (!player.capabilities.isCreativeMode)
    		    stack.stackSize--;
    		
    		return true;
    	}
    	
    	return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        int maxRarity = 0;
        for (IBroomPart part : getBroomParts(itemStack)) {
            maxRarity = Math.max(maxRarity, part.getRarity().ordinal());
        }
        return EnumRarity.values()[maxRarity];
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
    public boolean canConsumeBroomEnergy(int amount, ItemStack itemStack, @Nullable EntityLivingBase entityLiving) {
        return canConsume(amount, itemStack, entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null);
    }

    @Override
    public int consumeBroom(int amount, ItemStack itemStack, @Nullable EntityLivingBase entityLiving) {
        return FluidHelpers.getAmount(consume(amount, itemStack, entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null));
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(MinecraftHelpers.isShifted()) {
            list.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.parts." + Reference.MOD_ID + ".types.name"));
            Map<BroomModifier, Float> baseModifiers = BroomParts.REGISTRY.getBaseModifiersFromBroom(itemStack);
            Map<BroomModifier, Float> modifiers = getBroomModifiers(itemStack);
            Set<BroomModifier> modifierTypes = Sets.newHashSet();
            modifierTypes.addAll(baseModifiers.keySet());
            modifierTypes.addAll(modifiers.keySet());
            for (IBroomPart part : getBroomParts(itemStack)) {
                String line = part.getTooltipLine("  ");
                if (line != null) {
                    list.add(line);
                }
            }
            Pair<Integer, Integer> modifiersAndMax = getModifiersAndMax(modifiers, baseModifiers);
            int modifierCount = modifiersAndMax.getLeft();
            int maxModifiers = modifiersAndMax.getRight();
            list.add(EnumChatFormatting.ITALIC + L10NHelpers.localize(
                    "broom.modifiers." + Reference.MOD_ID + ".types.nameparam", modifierCount, maxModifiers));
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
            list.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom." + Reference.MOD_ID + ".shiftinfo"));
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

    @SubscribeEvent
    public void onFovEvent(FOVUpdateEvent event) {
        if(event.entity.ridingEntity instanceof EntityBroom) {
            EntityBroom broom = (EntityBroom) event.entity.ridingEntity;
            double speed = broom.getLastPlayerSpeed();
            event.newfov += speed / 10;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOverlayEvent(RenderGameOverlayEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player.ridingEntity instanceof EntityBroom
                && event.type == RenderGameOverlayEvent.ElementType.ALL) {
            EntityBroom broom = (EntityBroom) player.ridingEntity;
            ItemStack broomStack = broom.getBroomStack();
            ScaledResolution resolution = event.resolution;
            int height = 21;
            int width = 21;
            RenderOverlayEventHook.OverlayPosition overlayPosition = RenderOverlayEventHook.OverlayPosition.values()[
                    MathHelper.clamp_int(BroomConfig.guiOverlayPosition, 0, RenderOverlayEventHook.OverlayPosition.values().length - 1)];
            int x = overlayPosition.getX(resolution, width, height) + BroomConfig.guiOverlayPositionOffsetX;
            int y = overlayPosition.getY(resolution, width, height) + BroomConfig.guiOverlayPositionOffsetY;

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelpers.bindTexture(OVERLAY);

            // Render slot
            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, 11, 0, 24, 24);

            // Render item
            GlStateManager.enableLighting();
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(broomStack, x + 3, y + 3);
            Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(
                    Minecraft.getMinecraft().ingameGUI.getFontRenderer(), broomStack, x + 3, y + 3, "");
            RenderHelper.enableStandardItemLighting();
            GlStateManager.disableLighting();

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
