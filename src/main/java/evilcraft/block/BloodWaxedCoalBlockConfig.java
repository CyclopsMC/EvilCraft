package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlock;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Config for the Undead Plank.
 * @author rubensworks
 *
 */
public class BloodWaxedCoalBlockConfig extends BlockConfig implements IFuelHandler {

    /**
     * The unique instance.
     */
    public static BloodWaxedCoalBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodWaxedCoalBlockConfig() {
        super(
        	true,
            "bloodWaxedCoalBlock",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return (ConfigurableBlock) new ConfigurableBlock(this, Material.rock).
                setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston);
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        // setFireInfo
    	Blocks.fire.func_180686_a(getBlockInstance(), 5, 5);
        GameRegistry.registerFuelHandler(this);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if(Item.getItemFromBlock(getBlockInstance()) == fuel.getItem()) {
            return 32000;
        }
        return 0;
    }
    
}
