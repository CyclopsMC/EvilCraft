package evilcraft.block;

import evilcraft.api.RegistryManager;
import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipeRegistry;
import evilcraft.api.recipes.custom.ISuperRecipeRegistry;
import evilcraft.client.gui.container.GuiBloodInfuser;
import evilcraft.client.particle.EntityBloodBubbleFX;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.inventory.container.ContainerBloodInfuser;
import evilcraft.tileentity.TileBloodInfuser;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BloodInfuser extends ConfigurableBlockContainerGuiTankInfo implements IMachine<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> {
    
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
        return RegistryManager.getRegistry(ISuperRecipeRegistry.class).getRecipeRegistry(this);
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
