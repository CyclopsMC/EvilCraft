package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;
import org.cyclops.evilcraft.item.BroomConfig;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Default registry for broom parts.
 * @author rubensworks
 */
public class BroomPartRegistry implements IBroomPartRegistry {

    private static final String NBT_TAG_NAME = "broom_parts_tag";

    private final Map<ResourceLocation, IBroomPart> parts = Maps.newLinkedHashMap();
    private final Multimap<IBroomPart, ItemStack> partItems = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();
    private final Multimap<IBroomPart.BroomPartType, IBroomPart> partsByType = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();
    private final Map<IBroomPart, Map<BroomModifier, Float>> baseModifiers = Maps.newHashMap();
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
    public <P extends IBroomPart> void registerPartItem(@Nullable P part, ItemStack item) {
        if (part != null) {
            Objects.requireNonNull(item.getItem());
            partItems.put(part, item);
        }
    }

    @Override
    public <P extends IBroomPart> void registerBaseModifiers(@Nullable P part, Map<BroomModifier, Float> modifiers) {
        if (part != null) {
            baseModifiers.put(part, modifiers);
        }
    }

    @Override
    public <P extends IBroomPart> void registerBaseModifiers(@Nullable P part, BroomModifier modifier, float modifierValue) {
        Map<BroomModifier, Float> map = Maps.newHashMap();
        map.put(modifier, modifierValue);
        registerBaseModifiers(part, map);
    }

    @Override
    public <P extends IBroomPart> Map<BroomModifier, Float> getBaseModifiersFromPart(P part) {
        if(baseModifiers.containsKey(part)) {
            return baseModifiers.get(part);
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
    public <P extends IBroomPart> Collection<ItemStack> getItemsFromPart(P part) {
        return Collections.unmodifiableCollection(partItems.get(part));
    }

    @Override
    public <P extends IBroomPart> P getPartFromItem(ItemStack item) {
        for (Map.Entry<IBroomPart, ItemStack> entry : partItems.entries()) {
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
        if(broomStack != null) {
            List<IBroomPart> parts = Lists.newArrayList();
            if(broomStack.hasTagCompound()) {
                NBTTagList tags = broomStack.getTagCompound().getTagList(NBT_TAG_NAME, MinecraftHelpers.NBTTag_Types.NBTTagString.ordinal());
                for (int i = 0; i < tags.tagCount(); i++) {
                    String id = tags.getStringTagAt(i);
                    IBroomPart part = getPart(new ResourceLocation(id));
                    if (part == null) {
                        // TODO: fallback to default
                    } else {
                        parts.add(part);
                    }
                }
            }

            if(parts.isEmpty()) {
                return Lists.newArrayList(BroomParts.BRUSH_WHEAT,
                        BroomParts.CAP_GEM_DARK != null ? BroomParts.CAP_GEM_DARK : BroomParts.CAP_GEM_QUARTZ, BroomParts.ROD_WOOD);
            }

            return parts;
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
    @SideOnly(Side.CLIENT)
    public void onTooltipEvent(ItemTooltipEvent event) {
        if (BroomConfig.broomPartTooltips) {
            IBroomPart part = getPartFromItem(event.getItemStack());
            if (part != null) {
                Map<BroomModifier, Float> modifiers = getBaseModifiersFromPart(part);
                if (!modifiers.isEmpty()) {
                    if (MinecraftHelpers.isShifted()) {
                        event.getToolTip().add(TextFormatting.ITALIC + L10NHelpers.localize("broom.modifiers." + Reference.MOD_ID + ".types.name"));
                        for (Map.Entry<BroomModifier, Float> entry : modifiers.entrySet()) {
                            event.getToolTip().add(entry.getKey().getTooltipLine("  ", entry.getValue(), 0, false));
                        }
                    } else {
                        event.getToolTip().add(TextFormatting.ITALIC + L10NHelpers.localize("broom.parts." + Reference.MOD_ID + ".shiftinfo"));
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void beforeItemsRegistered(RegistryEvent<Item> event) {
        // The block registry even is called before the items event
        parts.clear();
        partItems.clear();
        partsByType.clear();
        baseModifiers.clear();
        if(MinecraftHelpers.isClientSide()) {
            partModels.clear();
        }
    }
}
