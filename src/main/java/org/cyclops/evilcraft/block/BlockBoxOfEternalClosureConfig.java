package org.cyclops.evilcraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.DefaultDataComponentsBoundEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritData;
import org.cyclops.evilcraft.item.ItemBlockBoxOfEternalClosure;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Config for the {@link BlockBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BlockBoxOfEternalClosureConfig extends BlockConfigCommon<ModBaseNeoForge<?>> {

    public BlockBoxOfEternalClosureConfig() {
        super(
            EvilCraft._instance,
            "box_of_eternal_closure",
                (eConfig, properties) -> new BlockBoxOfEternalClosure(properties
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new ItemBlockBoxOfEternalClosure(block, eConfig.createDefaultItemProperties())
        );
        NeoForge.EVENT_BUS.addListener(this::onDefaultDataComponentsBound);
    }

    private void onDefaultDataComponentsBound(DefaultDataComponentsBoundEvent event) {
        if (event.shouldUpdateStaticData()) {
            // Initialize the filled box template with a zombie spirit inside
            EntityVengeanceSpiritData spiritData = new EntityVengeanceSpiritData();
            spiritData.setInnerEntityType(EntityType.ZOMBIE);
            CompoundTag spiritTag = IModHelpers.get().getMinecraftHelpers().valueOutputToNbt(spiritData::writeNBT);
            ItemStack filledStack = new ItemStack(RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.get().asItem());
            filledStack.set(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA, spiritTag);
            BlockBoxOfEternalClosure.boxOfEternalClosureFilled = ItemStackTemplate.fromNonEmptyStack(filledStack);
        }
    }

    @Override
    public BlockClientConfig<ModBaseNeoForge<?>> constructBlockClientConfig() {
        return new BlockBoxOfEternalClosureConfigClient(this);
    }

    @Override
    public Collection<Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        NonNullList<Supplier<ItemStack>> list = NonNullList.create();
        ((BlockBoxOfEternalClosure) getInstance()).fillItemCategory(list);
        return list;
    }

}
