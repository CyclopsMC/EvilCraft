package org.cyclops.evilcraft.loot.modifier;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class LootModifierInjectBroom extends LootModifier {
    public static final Supplier<Codec<LootModifierInjectBroom>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(
            Codec.list(Codec.STRING).fieldOf("loot_tables").forGetter(LootModifierInjectBroom::getLootTables)
    ).apply(inst, LootModifierInjectBroom::new)));

    private final List<String> lootTables;

    public LootModifierInjectBroom(LootItemCondition[] conditionsIn, List<String> lootTables) {
        super(conditionsIn);
        this.lootTables = lootTables;
    }

    public List<String> getLootTables() {
        return lootTables;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (getLootTables().contains(context.getQueriedLootTableId().toString())) {
            ItemStack stack = new ItemStack(RegistryEntries.ITEM_BROOM);

            // Fill with blood
            IFluidHandlerItemCapacity fluidHanderFull = FluidHelpers.getFluidHandlerItemCapacity(stack).orElse((IFluidHandlerItemCapacity) null);
            fluidHanderFull.fill(new FluidStack(RegistryEntries.FLUID_BLOOD, fluidHanderFull.getCapacity() / 2 + context.getRandom().nextInt(fluidHanderFull.getCapacity() / 2)), IFluidHandler.FluidAction.EXECUTE);
            stack = fluidHanderFull.getContainer();

            // Add random parts
            ArrayList<IBroomPart> parts = Lists.newArrayList(BroomParts.REGISTRY.getParts());
            IBroomPart partRod = getRandomPart("rod", parts);
            IBroomPart partBrush = getRandomPart("brush", parts);
            IBroomPart partCap = getRandomPart("cap", parts);
            BroomParts.REGISTRY.setBroomParts(stack, Lists.newArrayList(partRod, partBrush, partCap));

            // Add random modifiers
            ArrayList<BroomModifier> modifiers = Lists.newArrayList(BroomModifiers.REGISTRY.getModifiers());
            BroomModifier modifier1 = modifiers.get(context.getRandom().nextInt(modifiers.size()));
            BroomModifier modifier2 = modifiers.get(context.getRandom().nextInt(modifiers.size()));
            Map<BroomModifier, Float> modifiersMap = Maps.newHashMap();
            modifiersMap.put(modifier1, modifier1.getTierValue());
            modifiersMap.put(modifier2, modifier2.getTierValue());
            BroomModifiers.REGISTRY.setModifiers(stack, modifiersMap);

            generatedLoot.add(stack);
        }
        return generatedLoot;
    }

    private IBroomPart getRandomPart(String type, List<IBroomPart> parts) {
        Random random = new Random();
        IBroomPart part = null;
        while (part == null) {
            part = parts.get(random.nextInt(parts.size()));
            if (!part.getId().getPath().startsWith(type)) {
                part = null;
            }
        }
        return part;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
