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
        this.experienceValue = 10;
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
    public boolean attackEntityAsMob(Entity entity) {
        // Ignite the attacked entity for a certain duration with a certain chance.
        if(this.rand.nextFloat() < FIRE_CHANCE)
            entity.setFire(this.rand.nextInt(MAX_FIRE_DURATION));
        return super.attackEntityAsMob(entity);
    }
    
    @Override
    public void livingTick() {
        if(this.world.isRemote() && rand.nextInt(30) == 0) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.FLAME, this.getPosX() + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), this.getPosY() + this.rand.nextDouble() * (double)this.getHeight(), this.getPosZ() + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
        super.livingTick();
    }

    class AIHideInStone extends RandomWalkingGoal {

        private Direction field_179483_b;
        private boolean field_179484_c;
        private static final String __OBFID = "CL_00002205";

        public AIHideInStone()
        {
            super(EntityNetherfish.this, 1.0D, 10);
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (EntityNetherfish.this.getAttackTarget() != null) {
                return false;
            } else if (!EntityNetherfish.this.getNavigator().noPath()) {
                return false;
            } else {
                Random random = EntityNetherfish.this.getRNG();

                if (random.nextInt(10) == 0) {
                    this.field_179483_b = Direction.getRandomDirection(random);
                    BlockPos blockpos = (new BlockPos(EntityNetherfish.this.getPosX(), EntityNetherfish.this.getPosY() + 0.5D, EntityNetherfish.this.getPosZ())).offset(this.field_179483_b);
                    BlockInfestedNether.Type type = BlockInfestedNether.unwrapBlock(EntityNetherfish.this.world.getBlockState(blockpos));
                    if (type != null) {
                        this.field_179484_c = true;
                        return true;
                    }
                }

                this.field_179484_c = false;
                return super.shouldExecute();
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return this.field_179484_c ? false : super.shouldContinueExecuting();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            if (!this.field_179484_c) {
                super.startExecuting();
            } else {
                BlockPos blockpos = (new BlockPos(EntityNetherfish.this.getPosX(), EntityNetherfish.this.getPosY() + 0.5D, EntityNetherfish.this.getPosZ())).offset(this.field_179483_b);
                BlockInfestedNether.Type type = BlockInfestedNether.unwrapBlock(EntityNetherfish.this.world.getBlockState(blockpos));
                if (type != null) {
                    EntityNetherfish.this.world.setBlockState(blockpos, BlockInfestedNether.wrapBlock(type).getDefaultState());
                    EntityNetherfish.this.spawnExplosionParticle();
                    EntityNetherfish.this.remove();
                }
            }
        }

    }
    
}
