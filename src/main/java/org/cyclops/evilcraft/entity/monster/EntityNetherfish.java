package org.cyclops.evilcraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockInfestedNether;

import java.util.EnumSet;
import java.util.Random;

/**
 * A silverfish for the nether.
 * @author rubensworks
 *
 */
public class EntityNetherfish extends SilverfishEntity {
    
    private static final int MAX_FIRE_DURATION = 3;
    private static final double FIRE_CHANCE = 0.5;

    public EntityNetherfish(EntityType<? extends EntityNetherfish> typeIn, World worldIn) {
        super(typeIn, worldIn);
        this.xpReward = 10;
    }

    public EntityNetherfish(World world) {
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
        if(this.level.isClientSide() && random.nextInt(30) == 0) {
            for (int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.FLAME, this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), this.getY() + this.random.nextDouble() * (double)this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
        super.aiStep();
    }

    class AIHideInStone extends RandomWalkingGoal {

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
                Random random = EntityNetherfish.this.getRandom();

                if (random.nextInt(10) == 0) {
                    this.selectedDirection = Direction.getRandom(random);
                    BlockPos blockpos = (new BlockPos(EntityNetherfish.this.getX(), EntityNetherfish.this.getY() + 0.5D, EntityNetherfish.this.getZ())).relative(this.selectedDirection);
                    BlockInfestedNether.Type type = BlockInfestedNether.unwrapBlock(EntityNetherfish.this.level.getBlockState(blockpos));
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
                BlockPos blockpos = (new BlockPos(EntityNetherfish.this.getX(), EntityNetherfish.this.getY() + 0.5D, EntityNetherfish.this.getZ())).relative(this.selectedDirection);
                BlockInfestedNether.Type type = BlockInfestedNether.unwrapBlock(EntityNetherfish.this.level.getBlockState(blockpos));
                if (type != null) {
                    EntityNetherfish.this.level.setBlockAndUpdate(blockpos, BlockInfestedNether.wrapBlock(type).defaultBlockState());
                    EntityNetherfish.this.spawnAnim();
                    EntityNetherfish.this.remove();
                }
            }
        }

    }
    
}
