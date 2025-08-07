package org.cyclops.evilcraft.core.fluid;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.persist.world.WorldStorage;

import java.util.Map;
import java.util.Set;

/**
 * @author rubensworks
 */
public class WorldStorageSharedTank extends WorldStorage<WorldStorageSharedTank> {

    private Map<String, FluidStack> tankCache;

    public WorldStorageSharedTank(Map<String, FluidStack> tankCache) {
        this.tankCache = tankCache;
    }

    public FluidStack getFluid(String key) {
        return tankCache.getOrDefault(key, FluidStack.EMPTY);
    }

    public void setFluid(String key, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            tankCache.remove(key);
        } else {
            tankCache.put(key, fluidStack);
        }
        setDirty();
    }

    public Set<Map.Entry<String, FluidStack>> getEntries() {
        return tankCache.entrySet();
    }

    public static class Access extends WorldStorage.Access<WorldStorageSharedTank> {

        public Access(ModBaseNeoForge<?> mod) {
            super(new SavedDataType<>(
                    mod.getModId() + "_shared_tank",
                    (ctx) -> new WorldStorageSharedTank(Maps.newHashMap()),
                    ctx -> RecordCodecBuilder.create(instance -> instance.group(
                            RecordCodecBuilder.point(ctx.levelOrThrow()),
                            Codec.dispatchedMap(Codec.STRING, (key) -> FluidStack.OPTIONAL_CODEC).fieldOf("tank_cache").forGetter(data -> data.tankCache)
                    ).apply(instance, (level, tankCache) -> new WorldStorageSharedTank(Maps.newHashMap(tankCache))))
            ), mod);
        }
    }

}
