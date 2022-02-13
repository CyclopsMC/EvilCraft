package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootTables;
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
                    LootTables.END_CITY_TREASURE,
                    LootTables.SIMPLE_DUNGEON,
                    LootTables.ABANDONED_MINESHAFT,
                    LootTables.STRONGHOLD_LIBRARY);
        }

        BlockBoxOfEternalClosure.boxOfEternalClosureFilled = new ItemStack(getItemInstance());
        BlockBoxOfEternalClosure.setVengeanceSwarmContent(BlockBoxOfEternalClosure.boxOfEternalClosureFilled);
    }

}
