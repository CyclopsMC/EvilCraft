package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.blockcomponents.EntityDropParticleFXBlockComponent;
import evilcraft.api.blockcomponents.IEntityDropParticleFXBlock;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockLeaves;

public class UndeadLeaves extends ConfigurableBlockLeaves implements IEntityDropParticleFXBlock {
    
    private static UndeadLeaves _instance = null;
    
    private EntityDropParticleFXBlockComponent entityDropParticleFXBlockComponent;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new UndeadLeaves(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static UndeadLeaves getInstance() {
        return _instance;
    }

    private UndeadLeaves(ExtendedConfig eConfig) {
        super(eConfig);
        
        if (Helpers.isClientSide()) {
            entityDropParticleFXBlockComponent = new EntityDropParticleFXBlockComponent(1.0F, 0.0F, 0.0F);
            entityDropParticleFXBlockComponent.setOffset(0);
            entityDropParticleFXBlockComponent.setChance(50);
        }
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return Block.deadBush.blockID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        super.randomDisplayTick(world, x, y, z, rand);
        entityDropParticleFXBlockComponent.randomDisplayTick(world, x, y, z, rand);
    }

}
