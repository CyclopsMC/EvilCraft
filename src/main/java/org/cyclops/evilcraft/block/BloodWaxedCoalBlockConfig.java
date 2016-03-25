package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

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
            EvilCraft._instance,
        	true,
            "bloodWaxedCoalBlock",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return (ConfigurableBlock) new ConfigurableBlock(this, Material.rock) {
            @Override
            public SoundType getStepSound() {
                return SoundType.METAL;
            }
        }.setHardness(3.0F).setResistance(5.0F);
    }

    @Override
    public boolean isMultipartEnabled() {
        return true;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
    	Blocks.fire.setFireInfo(getBlockInstance(), 5, 5);
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
