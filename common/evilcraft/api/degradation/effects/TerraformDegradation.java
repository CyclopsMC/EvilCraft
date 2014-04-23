package evilcraft.api.degradation.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import evilcraft.api.Coordinate;
import evilcraft.api.block.BlockTypeHolder;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.degradation.StochasticDegradationEffect;

/**
 * Degradation effect that will terraform certain blocks into the area to
 * other block.
 * @author rubensworks
 *
 */
public class TerraformDegradation extends StochasticDegradationEffect {
    
    private static Map<Block, BlockTypeHolder> TERRAFORMATIONS = 
            new HashMap<Block, BlockTypeHolder>();
    private static final double CHANCE = 0.1D;
    static{
        TERRAFORMATIONS.put(Block.stone, new BlockTypeHolder(Block.cobblestone));
    }
    
    /**
     * Make a new instance.
     */
    public TerraformDegradation() {
        super(CHANCE);
    }

    @Override
    public boolean canRun(IDegradable degradable) {
        return true;
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        
    }
    
    protected BlockTypeHolder getReplacement(Block block) {
        return TERRAFORMATIONS.get(block);
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        World world = degradable.getWorld();
        Random random = world.rand;
        
        Coordinate location = degradable.getLocation();
        int radius = degradable.getRadius();
        int x = location.x + random.nextInt(radius);
        int y = location.y + random.nextInt(radius);
        int z = location.z + random.nextInt(radius);
        
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        BlockTypeHolder replace = getReplacement(block);
        
        world.setBlock(x, y, z, replace.getBlock().blockID, replace.getMeta(), 2);
    }

}
