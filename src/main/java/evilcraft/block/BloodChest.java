package evilcraft.block;

import evilcraft.client.gui.container.GuiBloodChest;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.inventory.container.ContainerBloodChest;
import evilcraft.tileentity.TileBloodChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Random;

/**
 * A chest that runs on blood and repairs tools.
 * @author rubensworks
 *
 */
public class BloodChest extends ConfigurableBlockContainerGuiTankInfo {

    @BlockProperty(ignore = true)
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    
    private static BloodChest _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodChest getInstance() {
        return _instance;
    }

    public BloodChest(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.wood, TileBloodChest.class);
        
        this.setHardness(2.5F);
        this.setStepSound(soundTypeWood);
        this.setRotatable(true);
        setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return 2;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public String getTankNBTName() {
        return TileBloodChest.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileBloodChest.LIQUID_PER_SLOT;
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerBloodChest.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiBloodChest.class;
    }
}
