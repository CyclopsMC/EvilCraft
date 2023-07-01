package org.cyclops.evilcraft.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockInfestedNether;

import java.util.EnumSet;

/**
 * A silverfish for the nether.
 * @author rubensworks
 *
 */
public class EntityNetherfish extends Silverfish {

    private static final int MAX_FIRE_DURATION = 3;
    private static final double FIRE_CHANCE = 0.5;

    public EntityNetherfish(EntityType<? extends EntityNetherfish> typeIn, Level worldIn) {
        super(typeIn, worldIn);
        this.xpReward = 10;
    }

    public EntityNetherfish(Level world) {
        this(RegistryEntries.ENTITY_NETHERFISH, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new EntityNetherfish.AIHideInStone());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        // Ignite the attacked entity for a certain duration with a certain chance.
        if(this.random.nextFloat() < FIRE_CHANCE)
            entity.setSecondsOnFire(this.random.nextInt(MAX_FIRE_DURATION));
        return super.doHurtTarget(entity);
    }

    @Override
    public void aiStep() {
        if(this.level().isClientSide() && random.nextInt(30) == 0) {
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), this.getY() + this.random.nextDouble() * (double)this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
        super.aiStep();
    }

    class AIHideInStone extends RandomStrollGoal {

        private Direction selectedDirection;
        private boolean doMerge;
        private static final String __OBFID = "CL_00002205";

        public AIHideInStone()
        {
            super(EntityNetherfish.this, 1.0D, 10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canUse()
        {
            if (EntityNetherfish.this.getTarget() != null) {
                return false;
            } else if (!EntityNetherfish.this.getNavigation().isDone()) {
                return false;
            } else {
                RandomSource random = EntityNetherfish.this.getRandom();

                if (random.nextInt(10) == 0) {
                    this.selectedDirection = Direction.getRandom(random);
                    BlockPos blockpos = (BlockPos.containing(EntityNetherfish.this.getX(), EntityNetherfish.this.getY() + 0.5D, EntityNetherfish.this.getZ())).relative(this.selectedDirection);
                    BlockInfestedNether.Type type = BlockInfestedNether.unwrapBlock(EntityNetherfish.this.level().getBlockState(blockpos));
                    if (type != null) {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.canUse();
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse()
        {
            return this.doMerge ? false : super.canContinueToUse();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            if (!this.doMerge) {
                super.start();
            } else {
                BlockPos blockpos = (BlockPos.containing(EntityNetherfish.this.getX(), EntityNetherfish.this.getY() + 0.5D, EntityNetherfish.this.getZ())).relative(this.selectedDirection);
                BlockInfestedNether.Type type = BlockInfestedNether.unwrapBlock(EntityNetherfish.this.level().getBlockState(blockpos));
                if (type != null) {
                    EntityNetherfish.this.level().setBlockAndUpdate(blockpos, BlockInfestedNether.wrapBlock(type).defaultBlockState());
                    EntityNetherfish.this.spawnAnim();
                    EntityNetherfish.this.remove(RemovalReason.DISCARDED);
                }
            }
        }

    }

}
