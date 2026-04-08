package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeDeadBush}.
 * @author rubensworks
 */
public class RecipeSerializerDeadBushConfig extends RecipeConfigCommon<RecipeDeadBush, IModBase> {

    public static final MapCodec<RecipeDeadBush> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(r -> CraftingBookCategory.MISC)
                    )
                    .apply(builder, RecipeDeadBush::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeDeadBush> STREAM_CODEC = StreamCodec.unit(new RecipeDeadBush(CraftingBookCategory.MISC));

    public RecipeSerializerDeadBushConfig() {
        super(EvilCraft._instance,
                "crafting_special_dead_bush",
                eConfig -> new RecipeSerializer<>(CODEC, STREAM_CODEC));
    }

}
