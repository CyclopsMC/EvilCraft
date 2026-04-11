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
public class RecipeSerializerBroomPartCombinationConfig extends RecipeConfigCommon<RecipeBroomPartCombination, IModBase> {

    public static final RecipeBroomPartCombination RECIPE = new RecipeBroomPartCombination();
    public static final MapCodec<RecipeBroomPartCombination> CODEC = MapCodec.unit(RECIPE);
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeBroomPartCombination> STREAM_CODEC = StreamCodec.unit(RECIPE);

    public RecipeSerializerBroomPartCombinationConfig() {
        super(EvilCraft._instance,
                "crafting_special_broom_part_combination",
                eConfig -> new RecipeSerializer<>(CODEC, STREAM_CODEC));
    }

}
