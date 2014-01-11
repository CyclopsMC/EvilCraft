package evilcraft.blocks;
import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.world.World;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.fluids.Blood;

public class FluidBlockBlood extends ConfigurableBlockFluidClassic {
    
    private static final int CHANCE_HARDEN = 10;

    private static FluidBlockBlood _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new FluidBlockBlood(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static FluidBlockBlood getInstance() {
        return _instance;
    }

    private FluidBlockBlood(ExtendedConfig eConfig) {
        super(eConfig, Blood.getInstance(), Material.water);
        this.setParticleColor(1.0F, 0.0F, 0.0F);
        this.setTickRandomly(true);
    }
    
    @Override
    public int tickRate(World par1World) {
        return 100;
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if(isSourceBlock(world, x, y, z) && !world.isRaining() && random.nextInt(CHANCE_HARDEN) == 0) {
            world.setBlock(x, y, z, HardenedBloodConfig._instance.ID);
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
        } else {
            super.updateTick(world, x, y, z, random);
        }
    }

}
