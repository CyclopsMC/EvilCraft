package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.client.gui.container.GuiBloodInfuser;
import evilcraft.client.particle.EntityBloodBubbleFX;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.inventory.container.ContainerBloodInfuser;
import evilcraft.tileentity.TileBloodInfuser;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.recipe.custom.api.IMachine;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.api.ISuperRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BloodInfuser extends ConfigurableBlockContainerGuiTankInfo implements IMachine<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> {

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    @BlockProperty
    public static final PropertyBool ON = PropertyBool.create("on");

    private static BloodInfuser _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodInfuser getInstance() {
        return _instance;
    }

    public BloodInfuser(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileBloodInfuser.class);
        this.setStepSound(soundTypeStone);
        this.setRotatable(true);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public String getTankNBTName() {
        return TileBloodInfuser.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileBloodInfuser.LIQUID_PER_SLOT;
    }

    @Override
    public IRecipeRegistry<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> getRecipeRegistry() {
        return EvilCraft._instance.getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipeRegistry(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState state, Random random) {
        EntityBloodBubbleFX.randomDisplayTick((WorkingTileEntity) world.getTileEntity(blockPos), world, blockPos, random);
        super.randomDisplayTick(world, blockPos, state, random);
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos blockPos) {
        TileBloodInfuser tile = (TileBloodInfuser) world.getTileEntity(blockPos);
        return tile.isVisuallyWorking() ? 4 : super.getLightValue(world, blockPos);
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerBloodInfuser.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiBloodInfuser.class;
    }
}
