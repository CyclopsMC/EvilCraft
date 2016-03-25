package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.fluid.Poison;

/**
 * A blockState for the {@link Poison} fluid.
 * @author rubensworks
 *
 */
public class FluidBlockPoison extends ConfigurableBlockFluidClassic {

    private static FluidBlockPoison _instance = null;
    
    private static final int POISON_DURATION = 5;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static FluidBlockPoison getInstance() {
        return _instance;
    }

    public FluidBlockPoison(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Poison.getInstance(), Material.water);
        
        if (MinecraftHelpers.isClientSide())
            this.setParticleColor(0.0F, 1.0F, 0.0F);
        this.setTickRandomly(true);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
        if(entity instanceof EntityLivingBase && WorldHelpers.efficientTick(world, (POISON_DURATION / 2) * 20)) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.poison, POISON_DURATION * 20, 1));
        }
        super.onEntityCollidedWithBlock(world, blockPos, entity);
    }

}
