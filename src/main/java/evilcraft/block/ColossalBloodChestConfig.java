package evilcraft.block;

import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.client.render.tileentity.RenderTileEntityColossalBloodChest;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.item.ItemBlockNBT;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileColossalBloodChest;
import evilcraft.tileentity.TileWorking;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

/**
 * Config for the {@link ColossalBloodChest}.
 * @author rubensworks
 *
 */
public class ColossalBloodChestConfig extends BlockContainerConfig {

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
        ModelBase model = new ModelChest();
        ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "colossalBloodChest.png");
        ClientProxy.TILE_ENTITY_RENDERERS.put(TileColossalBloodChest.class, new RenderTileEntityColossalBloodChest(model, texture));
    }
    
}
