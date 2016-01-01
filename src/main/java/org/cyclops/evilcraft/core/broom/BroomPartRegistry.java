package org.cyclops.evilcraft.core.broom;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.api.broom.IBroomPartRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Default registry for broom parts.
 * @author rubensworks
 */
public class BroomPartRegistry implements IBroomPartRegistry {

    private final Set<IBroomPart> parts = Sets.newHashSet();
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
        parts.add(part);
        partsByType.put(part.getType(), part);
        return part;
    }

    @Override
    public Collection<IBroomPart> getParts() {
        return Collections.unmodifiableCollection(parts);
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
}
