package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;
import org.cyclops.evilcraft.item.ItemBroomConfig;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Default registry for broom parts.
 * @author rubensworks
 */
public class BroomPartRegistry implements IBroomPartRegistry {

    private static final String NBT_TAG_NAME = "broom_parts_tag";

    private final Map<ResourceLocation, IBroomPart> parts = Maps.newLinkedHashMap();
    private final Multimap<IBroomPart, Supplier<ItemStack>> partItems = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();
    private final Multimap<IBroomPart.BroomPartType, IBroomPart> partsByType = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();
    private final Map<IBroomPart, Map<BroomModifier, Float>> baseModifiers = Maps.newHashMap();
    @OnlyIn(Dist.CLIENT)
    private Map<IBroomPart, ResourceLocation> partModels;

    public BroomPartRegistry() {
        EvilCraft._instance.getModEventBus().addListener(EventPriority.HIGHEST, this::beforeItemsRegistered);
        NeoForge.EVENT_BUS.addListener(this::onTooltipEvent);
        if(MinecraftHelpers.isClientSide()) {
            partModels = Maps.newHashMap();
        }
    }

    @Override
    public <P extends IBroomPart> P registerPart(P part) {
        Objects.requireNonNull(part);
        parts.put(part.getId(), part);
        partsByType.put(part.getType(), part);
        return part;
    }

    @Override
    public <P extends IBroomPart> void registerPartItem(@Nullable P part, Supplier<ItemStack> item) {
        if (part != null) {
            Objects.requireNonNull(item);
            partItems.put(part, () -> {
                ItemStack itemStack = item.get();
                itemStack.set(DataComponents.RARITY, part.getRarity());
                return itemStack;
            });
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
        return partItems.get(part).stream().map(Supplier::get).toList();
    }

    @Override
    public <P extends IBroomPart> P getPartFromItem(ItemStack item) {
        for (Map.Entry<IBroomPart, Supplier<ItemStack>> entry : partItems.entries()) {
            if (ItemStack.isSameItemSameComponents(item, entry.getValue().get())) {
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
    @OnlyIn(Dist.CLIENT)
    public void registerPartModel(IBroomPart part, ResourceLocation modelLocation) {
        partModels.put(part, modelLocation);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getPartModel(IBroomPart part) {
        return partModels.get(part);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Collection<ResourceLocation> getPartModels() {
        return Collections.unmodifiableCollection(partModels.values());
    }

    @Override
    public Collection<IBroomPart> getBroomParts(ItemStack broomStack) {
        if(!broomStack.isEmpty()) {
            List<IBroomPart> parts = Lists.newArrayList();
            if(broomStack.has(RegistryEntries.COMPONENT_BROOM_PARTS)) {
                parts.addAll(broomStack.get(RegistryEntries.COMPONENT_BROOM_PARTS).getParts());
            }

            if(parts.isEmpty()) {
                // BroomParts.BRUSH_WHEAT can be null during mod loading
                if (BroomParts.BRUSH_WHEAT != null) {
                    return Lists.newArrayList(BroomParts.BRUSH_WHEAT, BroomParts.CAP_GEM_DARK, BroomParts.ROD_WOOD);
                }
            }

            return parts;
        }
        return Collections.emptyList();
    }

    @Override
    public void setBroomParts(ItemStack broomStack, Collection<IBroomPart> broomParts) {
        broomStack.set(RegistryEntries.COMPONENT_BROOM_PARTS, new BroomPartsContents(Lists.newArrayList(broomParts)));
        broomStack.set(DataComponents.RARITY, getRarity(broomParts));
    }

    protected Rarity getRarity(Collection<IBroomPart> broomParts) {
        int maxRarity = 0;
        for (IBroomPart part : broomParts) {
            maxRarity = Math.max(maxRarity, part.getRarity().ordinal());
        }
        return Rarity.values()[maxRarity];
    }

    public void onTooltipEvent(ItemTooltipEvent event) {
        if (ItemBroomConfig.broomPartTooltips) {
            IBroomPart part = getPartFromItem(event.getItemStack());
            if (part != null) {
                Map<BroomModifier, Float> modifiers = getBaseModifiersFromPart(part);
                if (!modifiers.isEmpty()) {
                    if (MinecraftHelpers.isShifted()) {
                        event.getToolTip().add(Component.translatable("broom.modifiers." + Reference.MOD_ID + ".types")
                                .withStyle(ChatFormatting.ITALIC));
                        for (Map.Entry<BroomModifier, Float> entry : modifiers.entrySet()) {
                            event.getToolTip().add(entry.getKey().getTooltipLine("  ", entry.getValue(), 0, false));
                        }
                    } else {
                        event.getToolTip().add(Component.translatable("broom.parts." + Reference.MOD_ID + ".shiftinfo")
                                .withStyle(ChatFormatting.ITALIC));
                    }
                }
            }
        }
    }

    public void beforeItemsRegistered(RegisterEvent event) {
        // The block registry even is called before the items event
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            parts.clear();
            partItems.clear();
            partsByType.clear();
            baseModifiers.clear();
            if (MinecraftHelpers.isClientSide()) {
                partModels.clear();
            }
        }
    }
}
