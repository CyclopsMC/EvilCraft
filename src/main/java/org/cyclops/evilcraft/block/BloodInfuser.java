package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.recipe.custom.api.IMachine;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.api.ISuperRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.gui.container.GuiBloodInfuser;
import org.cyclops.evilcraft.client.particle.EntityBloodBubbleFX;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.inventory.container.ContainerBloodInfuser;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BloodInfuser extends ConfigurableBlockContainerGuiTankInfo implements IMachine<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> {

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
        this.setStepSound(SoundType.STONE);
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
    public IRecipeRegistry<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> getRecipeRegistry() {
        return EvilCraft._instance.getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipeRegistry(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos blockPos, Random random) {
        EntityBloodBubbleFX.randomDisplayTick((WorkingTileEntity) world.getTileEntity(blockPos), world, blockPos,
                random, BlockHelpers.getSafeBlockStateProperty(state, FACING, EnumFacing.NORTH));
        super.randomDisplayTick(state, world, blockPos, random);
    }

    @Override
    public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
        TileBloodInfuser tile = TileHelpers.getSafeTile(world, blockPos, TileBloodInfuser.class);
        return tile != null && tile.isVisuallyWorking() ? 4 : super.getLightValue(blockState, world, blockPos);
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
