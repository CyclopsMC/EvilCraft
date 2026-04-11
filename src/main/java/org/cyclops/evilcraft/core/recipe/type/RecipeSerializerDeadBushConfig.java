package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeDeadBush}.
 * @author rubensworks
 */
public class RecipeSerializerDeadBushConfig extends RecipeConfigCommon<RecipeDeadBush, IModBase> {

    public static final RecipeDeadBush RECIPE = new RecipeDeadBush();
    public static final MapCodec<RecipeDeadBush> CODEC = MapCodec.unit(RECIPE);
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeDeadBush> STREAM_CODEC = StreamCodec.unit(RECIPE);

    public RecipeSerializerDeadBushConfig() {
        super(EvilCraft._instance,
                "crafting_special_dead_bush",
                eConfig -> new RecipeSerializer<>(CODEC, STREAM_CODEC));
    }

}
