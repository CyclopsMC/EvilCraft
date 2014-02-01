package evilcraft.blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.fluids.Poison;

public class FluidBlockPoison extends ConfigurableBlockFluidClassic {

    private static FluidBlockPoison _instance = null;
    
    private static final int POISON_DURATION = 5;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new FluidBlockPoison(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static FluidBlockPoison getInstance() {
        return _instance;
    }

    private FluidBlockPoison(ExtendedConfig eConfig) {
        super(eConfig, Poison.getInstance(), Material.water);
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
