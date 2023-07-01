package org.cyclops.evilcraft.loot.modifier;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class LootModifierInjectBoxOfEternalClosure extends LootModifier {
    public static final Supplier<Codec<LootModifierInjectBoxOfEternalClosure>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(
            Codec.list(Codec.STRING).fieldOf("loot_tables").forGetter(LootModifierInjectBoxOfEternalClosure::getLootTables)
    ).apply(inst, LootModifierInjectBoxOfEternalClosure::new)));

    private static final List<Pair<UUID, String>> PLAYERS = Lists.newArrayList(
            Pair.of(UUID.fromString("068d4de0-3a75-4c6a-9f01-8c37e16a394c"), "kroeserr"),
            Pair.of(UUID.fromString("e1dc75c6-dcf9-4e0c-8fbf-9c6e5e44527c"), "_EeB_"),
            Pair.of(UUID.fromString("777e7aa3-9373-4511-8d75-f99d23ebe252"), "Davivs69"),
            Pair.of(UUID.fromString("3e13f558-fb72-4949-a842-07879924bc49"), "Jona"),
            Pair.of(UUID.fromString("b5c31e33-8224-4f96-a4bf-73721be9d2ec"), "dodo3231"),
            Pair.of(UUID.fromString("b2faeaab-fc87-4f91-98d3-836024f268ae"), "_KillaH229_"),
            Pair.of(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch"),
            Pair.of(UUID.fromString("853c80ef-3c37-49fd-aa49-938b674adae6"), "jeb_"),
            Pair.of(UUID.fromString("61699b2e-d327-4a01-9f1e-0ea8c3f06bc6"), "Dinnerbone"),
            Pair.of(UUID.fromString("bbb87dbe-690f-4205-bdc5-72ffb8ebc29d"), "direwolf20"),
            Pair.of(UUID.fromString("0b7509f0-2458-4160-9ce1-2772b9a45ac2"), "iChun")
    );

    private final List<String> lootTables;

    public LootModifierInjectBoxOfEternalClosure(LootItemCondition[] conditionsIn, List<String> lootTables) {
        super(conditionsIn);
        this.lootTables = lootTables;
    }

    public List<String> getLootTables() {
        return lootTables;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (getLootTables().contains(context.getQueriedLootTableId().toString())) {
            ItemStack stack = new ItemStack(RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE);
            if (context.getRandom().nextBoolean()) {
                BlockBoxOfEternalClosure.setVengeanceSwarmContent(stack);
            } else {
                Pair<UUID, String> entry = PLAYERS.get(context.getRandom().nextInt(PLAYERS.size()));
                BlockBoxOfEternalClosure.setPlayerContent(stack, entry.left(), entry.right());
            }
            generatedLoot.add(stack);
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
