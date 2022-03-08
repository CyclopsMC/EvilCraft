package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.item.ItemBlockBoxOfEternalClosure;

/**
 * Config for the {@link BlockBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BlockBoxOfEternalClosureConfig extends BlockConfig {

    @ConfigurableProperty(category = "item", comment = "If this item should be injected in loot tables..", requiresMcRestart = true)
    public static boolean injectLootTables = true;

    public BlockBoxOfEternalClosureConfig() {
        super(
            EvilCraft._instance,
            "box_of_eternal_closure",
                eConfig -> new BlockBoxOfEternalClosure(Block.Properties.of(Material.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.METAL)),
                (eConfig, block) -> new ItemBlockBoxOfEternalClosure(block, (new Item.Properties())
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/chests/box_of_eternal_closure"),
                    BuiltInLootTables.END_CITY_TREASURE,
                    BuiltInLootTables.SIMPLE_DUNGEON,
                    BuiltInLootTables.ABANDONED_MINESHAFT,
                    BuiltInLootTables.STRONGHOLD_LIBRARY);
        }

        BlockBoxOfEternalClosure.boxOfEternalClosureFilled = new ItemStack(getItemInstance());
        BlockBoxOfEternalClosure.setVengeanceSwarmContent(BlockBoxOfEternalClosure.boxOfEternalClosureFilled);
    }

}
