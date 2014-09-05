package evilcraft.block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.fluid.Poison;

/**
 * A block for the {@link Poison} fluid.
 * @author rubensworks
 *
 */
public class FluidBlockPoison extends ConfigurableBlockFluidClassic {

    private static FluidBlockPoison _instance = null;
    
    private static final int POISON_DURATION = 5;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new FluidBlockPoison(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static FluidBlockPoison getInstance() {
        return _instance;
    }

    private FluidBlockPoison(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Poison.getInstance(), Material.water);
        
        if (MinecraftHelpers.isClientSide())
            this.setParticleColor(0.0F, 1.0F, 0.0F);
        this.setTickRandomly(true);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
        }
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }

}
