package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
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
    @SideOnly(Side.CLIENT)
    private Map<IBroomPart, ResourceLocation> partModels;

    public BroomPartRegistry() {
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
    public <P extends IBroomPart> ItemStack getItemFromPart(P part) {
        return partItems.get(part);
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
        return null;
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
                }
                parts.add(part);
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

    }
}
