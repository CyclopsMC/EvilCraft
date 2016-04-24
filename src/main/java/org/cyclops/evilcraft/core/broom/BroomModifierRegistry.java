package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.IBroomModifierRegistry;
import org.cyclops.evilcraft.api.broom.IBroomPart;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Default registry for broom modifiers.
 * @author rubensworks
 */
public class BroomModifierRegistry implements IBroomModifierRegistry {

    private static final String NBT_TAG_NAME = "broom_modifiers_tag";
    private static final String NBT_TAG_KEY = "id";
    private static final String NBT_TAG_VALUE = "value";

    private final Map<ResourceLocation, BroomModifier> broomModifiers = Maps.newHashMap();
    private final Map<BroomModifier, IBroomPart> broomModifierParts = Maps.newHashMap();
    private final Map<ItemStack, Map<BroomModifier, Float>> broomItems = Maps.newHashMap();

    public BroomModifierRegistry() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public BroomModifier registerModifier(BroomModifier modifier) {
        BroomPartModifier broomPart = new BroomPartModifier(modifier);
        overrideDefaultModifierPart(modifier, broomPart);
        return modifier;
    }

    @Override
    public void overrideDefaultModifierPart(BroomModifier modifier, @Nullable IBroomPart broomPart) {
        broomModifierParts.put(modifier, broomPart);
        if (broomPart != null) {
            BroomParts.REGISTRY.registerPart(broomPart);
        }
    }

    @Override
    public @Nullable IBroomPart getModifierPart(BroomModifier modifier) {
        return broomModifierParts.get(modifier);
    }

    @Override
    public void registerModifiersItem(Map<BroomModifier, Float> modifiers, ItemStack item) {
        Objects.requireNonNull(item.getItem());
        broomItems.put(item, modifiers);
    }

    @Override
    public void registerModifiersItem(BroomModifier modifier, float modifierValue, ItemStack item) {
        Map<BroomModifier, Float> map = Maps.newHashMap();
        map.put(modifier, modifierValue);
        registerModifiersItem(map, item);
    }

    @Override
    public Map<BroomModifier, Float> getModifiersFromItem(ItemStack item) {
        for (Map.Entry<ItemStack, Map<BroomModifier, Float>> entry : broomItems.entrySet()) {
            if (ItemStack.areItemsEqual(item, entry.getKey()) && ItemStack.areItemStackTagsEqual(item, entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<BroomModifier> getModifiers() {
        return Collections.unmodifiableCollection(broomModifiers.values());
    }

    @Override
    public Map<BroomModifier, Float> getModifiers(ItemStack broomStack) {
        if(broomStack != null) {
            Map<BroomModifier, Float> modifiers = Maps.newHashMap();

            // Base values
            for (BroomModifier modifier : getModifiers()) {
                if (modifier.isBaseModifier()) {
                    modifiers.put(modifier, modifier.getDefaultValue());
                }
            }

            // Hardcoded values
            if(broomStack.hasTagCompound()) {
                NBTTagList tags = broomStack.getTagCompound().getTagList(NBT_TAG_NAME, MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
                for (int i = 0; i < tags.tagCount(); i++) {
                    NBTTagCompound tag = tags.getCompoundTagAt(i);
                    String id = tag.getString(NBT_TAG_KEY);
                    float value = tag.getFloat(NBT_TAG_VALUE);
                    BroomModifier modifier = broomModifiers.get(new ResourceLocation(id));
                    if (modifier != null) {
                        modifiers.put(modifier, value);
                    }
                }
            }

            return modifiers;
        }
        return Collections.emptyMap();
    }

    @Override
    public void setModifiers(ItemStack broomStack, Map<BroomModifier, Float> modifiers) {
        // Write modifiers
        NBTTagList list = new NBTTagList();
        for (Map.Entry<BroomModifier, Float> entry : modifiers.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString(NBT_TAG_KEY, entry.getKey().getId().toString());
            tag.setFloat(NBT_TAG_VALUE, entry.getValue());
            list.appendTag(tag);
        }
        if(!broomStack.hasTagCompound()) {
            broomStack.setTagCompound(new NBTTagCompound());
        }
        broomStack.getTagCompound().setTag(NBT_TAG_NAME, list);

        // Write corresponding modifier parts
        Collection<IBroomPart> parts = BroomParts.REGISTRY.getBroomParts(broomStack);
        for (BroomModifier modifier : modifiers.keySet()) {
            IBroomPart part = getModifierPart(modifier);
            if (part != null) {
                parts.add(part);
            }
        }
        BroomParts.REGISTRY.setBroomParts(broomStack, parts);
    }

    @SubscribeEvent
    public void onTooltipEvent(ItemTooltipEvent event) {
        Map<BroomModifier, Float> modifiers = getModifiersFromItem(event.itemStack);
        if(modifiers != null) {
            if(MinecraftHelpers.isShifted()) {
                event.toolTip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.modifiers." + Reference.MOD_ID + ".types.name"));
                for (Map.Entry<BroomModifier, Float> entry : modifiers.entrySet()) {
                    event.toolTip.add("  " + L10NHelpers.localize(entry.getKey().getUnlocalizedName()) + ": " + entry.getValue());
                }
            } else {
                event.toolTip.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.modifiers." + Reference.MOD_ID + ".shiftinfo"));
            }
        }
    }
}
