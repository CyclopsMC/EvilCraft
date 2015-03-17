package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.tileentity.TileEternalWaterBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Config for the {@link evilcraft.block.BloodChest}.
 * @author rubensworks
 *
 */
public class EternalWaterBlockConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static EternalWaterBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public EternalWaterBlockConfig() {
        super(
        	true,
            "eternalWaterBlock",
            null,
            null
        );
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlock.class;
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableBlockContainer(this, Material.water, TileEternalWaterBlock.class) {

            @Override
            public boolean saveNBTToDroppedItem() {
                return false;
            }

            @Override
            public float getBlockHardness(World world, BlockPos blockPos) {
                return 0.5F;
            }

            @Override
            public boolean isReplaceable(World world, BlockPos blockPos) {
                return false;
            }

            @Override
            public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side,
                                            float xp, float yp, float zp) {
                ItemStack itemStack = player.inventory.getCurrentItem();
                if(itemStack != null) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                    if (fluidStack == null) {
                        ItemStack filledItem = FluidContainerRegistry.fillFluidContainer(TileEternalWaterBlock.WATER, itemStack);
                        if (filledItem != null && !player.capabilities.isCreativeMode) {
                            InventoryHelpers.tryReAddToStack(player, itemStack, filledItem);
                        }
                    }
                    if(itemStack.getItem() instanceof IFluidContainerItem) {
                        IFluidContainerItem containerItem = ((IFluidContainerItem) itemStack.getItem());
                        containerItem.fill(itemStack, TileEternalWaterBlock.WATER, true);
                    }
                }
                return true;
            }

        };
    }
    
}
