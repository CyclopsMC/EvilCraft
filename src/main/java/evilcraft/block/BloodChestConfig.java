package evilcraft.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.client.render.item.RenderItemBloodChest;
import evilcraft.client.render.tileentity.RenderTileEntityBloodChest;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemBlockNBT;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileBloodChest;

/**
 * Config for the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class BloodChestConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BloodChestConfig _instance;
    
    /**
     * If the Blood Chest should add random bad enchants.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If the Blood Chest should add random bad enchants with a small chance to repairing items.", isCommandable = true)
    public static boolean addRandomBadEnchants = true;
    
    /**
     * The amount Blood mB required for repairing one damage value.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int mBPerDamage = 5;
    
    /**
     * The amount of ticks required for repairing one damage value.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;
    
    /**
     * If the Blood Chest should be able to repair tools from Tinkers' Construct (if that mod is available).
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If the Blood Chest should be able to repair tools from Tinkers' Construct", isCommandable = true)
    public static boolean repairTConstructTools = true;

    /**
     * Make a new instance.
     */
    public BloodChestConfig() {
        super(
        	true,
            "bloodChest",
            null,
            BloodChest.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }
    
    @Override
    public void onRegistered() {
        if (MinecraftHelpers.isClientSide()) {
        	registerClientSide();
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void registerClientSide() {
    	ModelBase model = new ModelChest();
    	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "bloodChest.png");
        ClientProxy.TILE_ENTITY_RENDERERS.put(TileBloodChest.class, new RenderTileEntityBloodChest(model, texture));
        ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(BloodChest.getInstance()), new RenderItemBloodChest(model, texture));
    }
    
}
