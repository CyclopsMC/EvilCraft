package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;
import org.cyclops.evilcraft.item.Broom;

import java.util.*;

/**
 * Default registry for broom parts.
 * @author rubensworks
 */
public class BroomPartRegistry implements IBroomPartRegistry {

    private static final String NBT_TAG_NAME = "broom_parts_tag";

    private final Map<ResourceLocation, IBroomPart> parts = Maps.newHashMap();
    private final Map<IBroomPart, ItemStack> partItems = Maps.newHashMap();
    private final Multimap<IBroomPart.BroomPartType, IBroomPart> partsByType = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();
    private final Map<IBroomPart, Map<BroomModifier, Float>> baseModifers = Maps.newHashMap();
    @SideOnly(Side.CLIENT)
    private Map<IBroomPart, ResourceLocation> partModels;

    public BroomPartRegistry() {
        MinecraftForge.EVENT_BUS.register(this);
        if(MinecraftHelpers.isClientSide()) {
            partModels = Maps.newHashMap();
        }
    }

    @Override
    public <P extends IBroomPart> P registerPart(P part) {
        parts.put(part.getId(), part);
        partsByType.put(part.getType(), part);
        return part;
    }

    @Override
    public <P extends IBroomPart> void registerPartItem(P part, ItemStack item) {
        Objects.requireNonNull(item.getItem());
        partItems.put(part, item);
    }

    @Override
    public <P extends IBroomPart> void registerBaseModifiers(Map<BroomModifier, Float> modifiers, P part) {
        baseModifers.put(part, modifiers);
    }

    @Override
    public <P extends IBroomPart> void registerBaseModifiers(BroomModifier modifier, float modifierValue, P part) {
        Map<BroomModifier, Float> map = Maps.newHashMap();
        map.put(modifier, modifierValue);
        registerBaseModifiers(map, part);
    }

    @Override
    public <P extends IBroomPart> Map<BroomModifier, Float> getBaseModifiersFromPart(P part) {
        if(baseModifers.containsKey(part)) {
            return baseModifers.get(part);
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<BroomModifier, Float> getBaseModifiersFromBroom(ItemStack broomStack) {
        Map<BroomModifier, Float> baseModifiers = Maps.newHashMap();
        for (IBroomPart part : getBroomParts(broomStack)) {
            for (Map.Entry<BroomModifier, Float> entry : BroomParts.REGISTRY.getBaseModifiersFromPart(part).entrySet()) {
                BroomModifier modifier = entry.getKey();
                if(baseModifiers.containsKey(modifier)) {
                    baseModifiers.put(modifier, entry.getValue() + baseModifiers.get(modifier));
                } else{
                    baseModifiers.put(modifier, entry.getValue());
                }
            }
        }
        return baseModifiers;
    }

    @Override
    public <P extends IBroomPart> ItemStack getItemFromPart(P part) {
        ItemStack result = partItems.get(part);
        if (result == null) {
            return null;
        }
        return result.copy();
    }

    @Override
    public <P extends IBroomPart> P getPartFromItem(ItemStack item) {
        for (Map.Entry<IBroomPart, ItemStack> entry : partItems.entrySet()) {
            if (ItemStack.areItemsEqual(item, entry.getValue()) && ItemStack.areItemStackTagsEqual(item, entry.getValue())) {
                return (P) entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Collection<IBroomPart> getParts() {
        return Collections.unmodifiableCollection(parts.values());
    }

    @Override
    public IBroomPart getPart(ResourceLocation partId) {
        return parts.get(partId);
    }

    @Override
    public Collection<IBroomPart> getParts(IBroomPart.BroomPartType type) {
        return Collections.unmodifiableCollection(partsByType.get(type));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerPartModel(IBroomPart part, ResourceLocation modelLocation) {
        partModels.put(part, modelLocation);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getPartModel(IBroomPart part) {
        return partModels.get(part);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Collection<ResourceLocation> getPartModels() {
        return Collections.unmodifiableCollection(partModels.values());
    }

    @Override
    public Collection<IBroomPart> getBroomParts(ItemStack broomStack) {
        if(broomStack != null && broomStack.hasTagCompound()) {
            List<IBroomPart> parts = Lists.newLinkedList();
            NBTTagList tags = broomStack.getTagCompound().getTagList(NBT_TAG_NAME, MinecraftHelpers.NBTTag_Types.NBTTagString.ordinal());
            for(int i = 0; i < tags.tagCount(); i++) {
                String id = tags.getStringTagAt(i);
                IBroomPart part = getPart(new ResourceLocation(id));
                if(part == null) {
                    // TODO: fallback to default
                } else {
                    parts.add(part);
                }
            }
            return parts;
        }

        // Backwards compatibility: the "old" broom
        if(broomStack != null && broomStack.getItem() == Broom.getInstance() && !broomStack.hasTagCompound()) {
            return Lists.newArrayList(BroomParts.BRUSH_WHEAT, BroomParts.CAP_DARKGEM, BroomParts.ROD_WOOD);
        }

        return Collections.emptyList();
    }

    @Override
    public void setBroomParts(ItemStack broomStack, Collection<IBroomPart> broomParts) {
        NBTTagList list = new NBTTagList();
        for (IBroomPart broomPart : broomParts) {
            list.appendTag(new NBTTagString(broomPart.getId().toString()));
        }
        if(!broomStack.hasTagCompound()) {
            broomStack.setTagCompound(new NBTTagCompound());
        }
        broomStack.getTagCompound().setTag(NBT_TAG_NAME, list);
    }

    @SubscribeEvent
    public void onTooltipEvent(ItemTooltipEvent event) {
        IBroomPart part = getPartFromItem(event.itemStack);
        if(part != null) {
            if(MinecraftHelpers.isShifted()) {
                event.toolTip.add(part.getTooltipLine(""));
                event.toolTip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.modifiers." + Reference.MOD_ID + ".types.name"));
                Map<BroomModifier, Float> modifiers = getBaseModifiersFromPart(part);
                for (Map.Entry<BroomModifier, Float> entry : modifiers.entrySet()) {
                    event.toolTip.add("  " + L10NHelpers.localize(entry.getKey().getUnlocalizedName()) + ": " + entry.getValue());
                }
            } else {
                event.toolTip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.parts." + Reference.MOD_ID + ".shiftinfo"));
            }
        }
    }
}
