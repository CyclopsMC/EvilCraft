package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.IBroomModifierRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Default registry for broom modifiers.
 * @author rubensworks
 */
public class BroomModifierRegistry implements IBroomModifierRegistry {

    private static final String NBT_TAG_NAME = "broom_modifiers_tag";
    private static final String NBT_TAG_KEY = "id";
    private static final String NBT_TAG_VALUE = "value";

    private final Map<ResourceLocation, BroomModifier> broomModifiers = Maps.newHashMap();

    @Override
    public BroomModifier registerModifier(BroomModifier modifier) {
        broomModifiers.put(modifier.getId(), modifier);
        return modifier;
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
        NBTTagList list = new NBTTagList();
        for (Map.Entry<BroomModifier, Float> entry : modifiers.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString(NBT_TAG_NAME, entry.getKey().getId().toString());
            tag.setFloat(NBT_TAG_VALUE, entry.getValue());
            list.appendTag(tag);
        }
        if(!broomStack.hasTagCompound()) {
            broomStack.setTagCompound(new NBTTagCompound());
        }
        broomStack.getTagCompound().setTag(NBT_TAG_NAME, list);
    }
}
