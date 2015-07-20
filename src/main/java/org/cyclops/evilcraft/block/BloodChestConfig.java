package org.cyclops.evilcraft.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityBloodChest;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If the Blood Chest should add random bad enchants with a small chance to repairing items.", isCommandable = true)
    public static boolean addRandomBadEnchants = true;
    
    /**
     * The amount Blood mB required for repairing one damage value.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int mBPerDamage = 5;
    
    /**
     * The amount of ticks required for repairing one damage value.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;
    
    /**
     * If the Blood Chest should be able to repair tools from Tinkers' Construct (if that mod is available).
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If the Blood Chest should be able to repair tools from Tinkers' Construct", isCommandable = true)
    public static boolean repairTConstructTools = true;

    /**
     * Make a new instance.
     */
    public BloodChestConfig() {
        super(
            EvilCraft._instance,
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
        super.onRegistered();
        if (MinecraftHelpers.isClientSide()) {
        	registerClientSide();
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void registerClientSide() {
    	ModelBase model = new ModelChest();
    	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "bloodChest.png");
        getMod().getProxy().registerRenderer(TileBloodChest.class, new RenderTileEntityBloodChest(model, texture));
    }
    
}
