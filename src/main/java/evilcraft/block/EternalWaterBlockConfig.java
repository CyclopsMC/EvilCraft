package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.tileentity.TileEternalWaterBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
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
            public float getBlockHardness(World world, int x, int y, int z) {
                return 0.5F;
            }

            private IIcon topIcon;
            private IIcon bottomIcon;

            @Override
            @SideOnly(Side.CLIENT)
            public void registerBlockIcons(IIconRegister iconRegister) {
                super.registerBlockIcons(iconRegister);
                topIcon = iconRegister.registerIcon(getTextureName() + "_top");
                bottomIcon = iconRegister.registerIcon(getTextureName() + "_bottom");
            }

            @Override
            public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
                if(side == ForgeDirection.UP.ordinal()) return topIcon;
                if(side == ForgeDirection.DOWN.ordinal()) return bottomIcon;
                return super.getIcon(world, x, y, z, side);
            }

            @Override
            public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
                return false;
            }

            public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
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
