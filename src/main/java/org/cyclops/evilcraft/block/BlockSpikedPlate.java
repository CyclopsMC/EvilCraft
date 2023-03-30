package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.ExtendedDamageSources;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryPedestal;
import org.cyclops.evilcraft.core.config.configurable.BlockPressurePlate;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Logs for the Undead Tree.
 * @author rubensworks
 *
 */
public class BlockSpikedPlate extends BlockPressurePlate {

    public BlockSpikedPlate(Block.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos blockPos) {
        return super.canSurvive(state, world, blockPos)
                || world.getBlockState(blockPos.offset(0, -1, 0)).getBlock() instanceof org.cyclops.evilcraft.block.BlockSanguinaryPedestal;
    }

    /**
     * Damage the given entity.
     * @param world The world.
     * @param entity The entity.
     * @param blockPos The position.
     * @return If the given entity was damaged.
     */
    protected boolean damageEntity(ServerLevel world, Entity entity, BlockPos blockPos) {
        if(!(entity instanceof Player) && entity instanceof LivingEntity) {
            float damage = (float) BlockSpikedPlateConfig.damage;

            // To make sure the entity actually will drop something.
            ((LivingEntity) entity).lastHurtByPlayerTime = 100;

            if(entity.hurt(ExtendedDamageSources.spikedDamage(world), damage)) {
                BlockEntity tile = world.getBlockEntity(blockPos.offset(0, -1, 0));
                if(tile != null && tile instanceof BlockEntitySanguinaryPedestal) {
                    int amount = Mth.floor(damage * (float) BlockSpikedPlateConfig.mobMultiplier);
                    ((BlockEntitySanguinaryPedestal) tile).getBonusFluidHandler()
                            .fill(new FluidStack(RegistryEntries.FLUID_BLOOD, amount), IFluidHandler.FluidAction.EXECUTE);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected int getSignalStrength(Level world, BlockPos blockPos) {
        VoxelShape shape = this.getShape(world.getBlockState(blockPos), world, blockPos, CollisionContext.empty());
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, shape.bounds().expandTowards(0, 1, 1).move(blockPos));

        int ret = 0;

        if (!world.isClientSide() && !list.isEmpty()) {
            for (LivingEntity entity : list) {
                    if (!entity.isIgnoringBlockTriggers() && damageEntity((ServerLevel) world, entity, blockPos)) {
                    ret = 15;
                }
            }
        }

        return ret;
    }

    protected int getSignalForState(BlockState blockState) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, POWERED, false) ? 15 : 0;
    }

    protected BlockState setSignalForState(BlockState blockState, int meta) {
        return blockState.setValue(POWERED, meta > 0);
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, @Nullable EntityType<?> entityType) {
        return true;
    }

}
