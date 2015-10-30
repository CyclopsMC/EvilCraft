package org.cyclops.evilcraft.block;

import com.google.common.collect.Sets;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityColossalBloodChest;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileWorking;

import java.util.Set;

/**
 * Config for the {@link ColossalBloodChest}.
 * @author rubensworks
 *
 */
public class ColossalBloodChestConfig extends UpgradableBlockContainerConfig {

    /**
     * The unique instance.
     */
    public static ColossalBloodChestConfig _instance;

    /**
     * The amount Blood mB required for repairing one damage value.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The amount Blood mB required for repairing one damage value.", isCommandable = true)
    public static int baseMBPerDamage = 10;

    /**
     * The amount of ticks required for repairing one damage value.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The amount of ticks required for repairing one damage value.", isCommandable = true)
    public static int ticksPerDamage = 2;

    /**
     * The base amount of concurrent items that need to be available before efficiency can rise.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The base amount of concurrent items that need to be available before efficiency can rise.", isCommandable = true)
    public static int baseConcurrentItems = 4;

    /**
     * Make a new instance.
     */
    public ColossalBloodChestConfig() {
        super(
                EvilCraft._instance,
        	true,
            "colossalBloodChest",
            null,
            ColossalBloodChest.class
        );
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }

    @Override
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet(
                TileWorking.UPGRADE_EFFICIENCY,
                TileWorking.UPGRADE_SPEED,
                TileWorking.UPGRADE_TIER1,
                TileWorking.UPGRADE_TIER2,
                TileWorking.UPGRADE_TIER3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        ModelChest model = new ModelChest();
        ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "colossalBloodChest.png");
        EvilCraft._instance.getProxy().registerRenderer(TileColossalBloodChest.class, new RenderTileEntityColossalBloodChest(model, texture));
    }
    
}
