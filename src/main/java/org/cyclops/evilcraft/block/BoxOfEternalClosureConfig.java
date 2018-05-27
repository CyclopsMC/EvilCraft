package org.cyclops.evilcraft.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.model.SingleModelLoader;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.entity.monster.VengeanceSpiritConfig;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

/**
 * Config for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosureConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BoxOfEternalClosureConfig _instance;

    @SideOnly(Side.CLIENT)
    public static ResourceLocation boxModel;
    @SideOnly(Side.CLIENT)
    public static ResourceLocation boxLidModel;
    @SideOnly(Side.CLIENT)
    public static ResourceLocation boxLidRotatedModel;

    /**
     * If this item should be injected in loot tables.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If this item should be injected in loot tables..", requiresMcRestart = true)
    public static boolean injectLootTables = true;

    /**
     * Make a new instance.
     */
    public BoxOfEternalClosureConfig() {
        super(
            EvilCraft._instance,
        	true,
            "box_of_eternal_closure",
            null,
            BoxOfEternalClosure.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }

    @SideOnly(Side.CLIENT)
    public void onRegisteredClient() {
        EvilCraft._instance.getProxy().registerRenderer(TileBoxOfEternalClosure.class,
                new RenderTileEntityBoxOfEternalClosure());

        boxModel = new ResourceLocation(getMod().getModId() + ":block/box");
        boxLidModel = new ResourceLocation(getMod().getModId() + ":block/box_lid");
        boxLidRotatedModel = new ResourceLocation(getMod().getModId() + ":block/box_lid_rotated");

        ModelLoaderRegistry.registerLoader(new SingleModelLoader(
                Reference.MOD_ID, "models/item/box_of_eternal_closure", new ModelBoxOfEternalClosure()));
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/chests/box_of_eternal_closure"),
                    LootTableList.CHESTS_END_CITY_TREASURE,
                    LootTableList.CHESTS_SIMPLE_DUNGEON,
                    LootTableList.CHESTS_ABANDONED_MINESHAFT,
                    LootTableList.CHESTS_STRONGHOLD_LIBRARY);
        }

        BoxOfEternalClosure.boxOfEternalClosureFilled = new ItemStack(BoxOfEternalClosure.getInstance());
        BoxOfEternalClosure.setVengeanceSwarmContent(BoxOfEternalClosure.boxOfEternalClosureFilled);
    }

    @Override
    public void onRegistered() {
        if (MinecraftHelpers.isClientSide()) {
            onRegisteredClient();
        }
    }
    
    @Override
    public boolean isHardDisabled() {
        // Hard dependency on vengeance spirits.
        return !Configs.isEnabled(VengeanceSpiritConfig.class);
    }

}
