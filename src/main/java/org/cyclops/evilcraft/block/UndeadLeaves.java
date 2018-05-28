package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.component.IEntityDropParticleFXBlock;
import org.cyclops.cyclopscore.block.component.ParticleDropBlockComponent;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockLeaves;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.Random;

/**
 * Leaves for the Undead Tree.
 * @author rubensworks
 *
 */
public class UndeadLeaves extends ConfigurableBlockLeaves implements IEntityDropParticleFXBlock {
    
    private static UndeadLeaves _instance = null;
    
    private ParticleDropBlockComponent particleDropBlockComponent;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static UndeadLeaves getInstance() {
        return _instance;
    }

    public UndeadLeaves(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig);
        
        setHardness(0.2F);
        setLightOpacity(1);
        
        if (MinecraftHelpers.isClientSide()) {
            particleDropBlockComponent = new ParticleDropBlockComponent(1.0F, 0.0F, 0.0F);
            particleDropBlockComponent.setOffset(0);
            particleDropBlockComponent.setChance(50);
        }
    }

    @Override
    public SoundType getSoundType() {
        return SoundType.GROUND;
    }

    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(Blocks.DEADBUSH);
    }

    @Override
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random rand) {
        particleDropBlockComponent.randomDisplayTick(blockState, world, blockPos, rand);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this);
    }
}
