package evilcraft.api.degradation.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import evilcraft.Configs;
import evilcraft.api.Coordinate;
import evilcraft.api.block.BlockTypeHolder;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.degradation.StochasticDegradationEffect;
import evilcraft.blocks.NetherfishSpawn;
import evilcraft.blocks.NetherfishSpawnConfig;

/**
 * Degradation effect that will terraform certain blocks into the area to
 * other block.
 * @author rubensworks
 *
 */
public class TerraformDegradation extends StochasticDegradationEffect {
    
    private static Map<Block, BlockTypeHolder> TERRAFORMATIONS = 
            new HashMap<Block, BlockTypeHolder>();
    private static Map<Block, Integer> TERRAFORMATION_CHANCES = 
            new HashMap<Block, Integer>();
    private static final double CHANCE = 0.1D;
    
    private static Random random = new Random();
    
    static{
        TERRAFORMATIONS.put(Block.stone, new BlockTypeHolder(Block.cobblestone));
        
        TERRAFORMATIONS.put(Block.cobblestone, new BlockTypeHolder(Block.sand));
        TERRAFORMATION_CHANCES.put(Block.cobblestone, 10);
        
        TERRAFORMATIONS.put(Block.coalBlock, new BlockTypeHolder(Block.blockDiamond));
        TERRAFORMATION_CHANCES.put(Block.coalBlock, 10000);
        
        TERRAFORMATIONS.put(Block.dirt, new BlockTypeHolder(Block.netherrack));
        TERRAFORMATIONS.put(Block.grass, new BlockTypeHolder(Block.netherrack));
        TERRAFORMATIONS.put(Block.mycelium, new BlockTypeHolder(Block.netherrack));
        
        if(Configs.isEnabled(NetherfishSpawnConfig.class)) {
            TERRAFORMATIONS.put(Block.netherrack,
                    new BlockTypeHolder(NetherfishSpawn.getInstance(),
                            NetherfishSpawn.getInstance().
                                getMetadataFromBlockID(Block.netherrack.blockID)));
            TERRAFORMATION_CHANCES.put(Block.netherrack, 50);
        }
        
        TERRAFORMATIONS.put(Block.sand, new BlockTypeHolder(null));
        
        TERRAFORMATIONS.put(Block.waterStill, new BlockTypeHolder(Block.lavaStill));
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
        BlockTypeHolder holder = TERRAFORMATIONS.get(block);
        if(holder != null) {
            Integer chance = TERRAFORMATION_CHANCES.get(block);
            if(chance == null || random.nextInt(chance) == 0) {
                return holder;
            }
        }
        return null;
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        World world = degradable.getWorld();
        
        Coordinate location = degradable.getLocation();
        int radius = degradable.getRadius();
        int x = location.x - radius + random.nextInt(2 * radius);
        int y = location.y - radius + random.nextInt(2 * radius);
        int z = location.z - radius + random.nextInt(2 * radius);
        
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        BlockTypeHolder replace = getReplacement(block);
        
        if(replace != null) {
            if(replace.getBlock() == null) {
                world.setBlockToAir(x, y, z);
            } else {
                world.setBlock(x, y, z, replace.getBlock().blockID, replace.getMeta(), 1);
            }
        }
    }

}
