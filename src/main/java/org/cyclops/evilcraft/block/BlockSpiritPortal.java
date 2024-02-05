package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.ExtendedDamageSources;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritPortal;
import org.cyclops.evilcraft.core.algorithm.RegionIterator;

import javax.annotation.Nullable;

/**
 * Portal for throwing in your book and stuff.
 * @author rubensworks
 *
 */
public class BlockSpiritPortal extends BlockWithEntity {

    public static final VoxelShape SHAPE = Block.box(0.4F * 16F, 0.4F * 16F, 0.4F * 16F, 0.6F * 16F, 0.6F * 16F, 0.6F * 16F);

    public BlockSpiritPortal(Block.Properties properties) {
        super(properties, BlockEntitySpiritPortal::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_SPIRIT_PORTAL, new BlockEntitySpiritPortal.Ticker());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    protected static boolean canReplaceBlock(BlockState blockState, LevelReader world, BlockPos pos) {
        return blockState != null && (blockState.isAir()|| blockState.canBeReplaced());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void palingDeath(LivingDeathEvent event) {
        if(event.getSource().type()
                .equals(event.getEntity().level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ExtendedDamageSources.DAMAGE_TYPE_PALING).value())) {
            tryPlacePortal(event.getEntity().level(), event.getEntity().blockPosition().offset(0, 1, 0));
        }
    }

    public boolean tryPlacePortal(Level world, BlockPos blockPos) {
        int attempts = 9;
        for(RegionIterator it = new RegionIterator(blockPos, 1, true); it.hasNext() && attempts >= 0;) {
            BlockPos location = it.next();
            if(canReplaceBlock(world.getBlockState(location), world, blockPos)) {
                world.setBlock(location, RegistryEntries.BLOCK_SPIRIT_PORTAL.defaultBlockState(),
                        MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                return true;
            }
            attempts--;
        }
        return false;
    }
}
