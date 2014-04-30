package evilcraft.api.degradation.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import evilcraft.Configs;
import evilcraft.api.Coordinate;
import evilcraft.api.Helpers;
import evilcraft.api.block.BlockTypeHolder;
import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.api.config.ExtendedConfig;
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
    
private static TerraformDegradation _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<DegradationEffectConfig> eConfig) {
        if(_instance == null)
            _instance = new TerraformDegradation(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static TerraformDegradation getInstance() {
        return _instance;
    }
    
    private static Map<Block, Map<BlockTypeHolder, Integer>> TERRAFORMATIONS = 
            new HashMap<Block, Map<BlockTypeHolder, Integer>>();
    private static final double CHANCE = 0.1D;
    
    private static Random random = new Random();
    
    static{
        // Default replacement
        putReplacement(null, new BlockTypeHolder(Block.cobblestone), 30);
        
        putReplacement(Block.stone, new BlockTypeHolder(Block.cobblestone));
        
        putReplacement(Block.cobblestone, new BlockTypeHolder(Block.dirt), 10);
        putReplacement(Block.cobblestone, new BlockTypeHolder(Block.lavaStill), 30);
        
        putReplacement(Block.coalBlock, new BlockTypeHolder(Block.blockDiamond), 10000);
        
        putReplacement(Block.dirt, new BlockTypeHolder(Block.netherrack), 30);
        putReplacement(Block.grass, new BlockTypeHolder(Block.netherrack), 20);
        putReplacement(Block.mycelium, new BlockTypeHolder(Block.netherrack), 5);
        putReplacement(Block.dirt, new BlockTypeHolder(Block.sand));
        putReplacement(Block.grass, new BlockTypeHolder(Block.sand));
        putReplacement(Block.mycelium, new BlockTypeHolder(Block.sand));
        putReplacement(Block.dirt, new BlockTypeHolder(Block.blockClay), 20);
        putReplacement(Block.grass, new BlockTypeHolder(Block.sand), 20);
        putReplacement(Block.mycelium, new BlockTypeHolder(Block.sand), 20);
        
        if(Configs.isEnabled(NetherfishSpawnConfig.class)) {
            putReplacement(Block.netherrack,
                    new BlockTypeHolder(NetherfishSpawn.getInstance(),
                            NetherfishSpawn.getInstance().
                                getMetadataFromBlockID(Block.netherrack.blockID)), 50);
        }
        
        putReplacement(Block.sand, new BlockTypeHolder(null));
        
        putReplacement(Block.waterStill, new BlockTypeHolder(null));
    }
    
    private static final void putReplacement(Block key, BlockTypeHolder value) {
        putReplacement(key, value, 0);
    }
    
    private static final void putReplacement(Block key, BlockTypeHolder value, int chance) {
        Map<BlockTypeHolder, Integer> mapValue = TERRAFORMATIONS.get(key);
        if(mapValue == null) {
            mapValue = new HashMap<BlockTypeHolder, Integer>();
            TERRAFORMATIONS.put(key, mapValue);
        }
        mapValue.put(value, chance);
    }
    
    private TerraformDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig, CHANCE);
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        
    }
    
    protected BlockTypeHolder getReplacement(Block block) {
        Map<BlockTypeHolder, Integer> mapValue = TERRAFORMATIONS.get(block);
        if(mapValue == null) { // Fetch the default replacement.
            mapValue = TERRAFORMATIONS.get(null);
        }
        if(mapValue != null) {
            Object[] keys = mapValue.keySet().toArray();
            BlockTypeHolder holder = (BlockTypeHolder) keys[random.nextInt(keys.length)];
            Integer chance = mapValue.get(holder);
            if(chance == null || chance == 0 || random.nextInt(chance) == 0) {
                return holder;
            }
        }
        return null;
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        World world = degradable.getWorld();
        
        Coordinate blockPoint = Helpers.getRandomPointInSphere(
                degradable.getLocation(), degradable.getRadius());
        int x = blockPoint.x;
        int y = blockPoint.y;
        int z = blockPoint.z;
        
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        BlockTypeHolder replace = getReplacement(block);
        
        if(replace != null
                && !degradable.getLocation().equals(blockPoint)
                && world.getBlockTileEntity(x, y, z) == null) {
            if(replace.getBlock() == null) {
                world.setBlockToAir(x, y, z);
            } else if(replace.getBlock().getBlockHardness(world, x, y, z) > 0) {
                world.setBlock(x, y, z, replace.getBlock().blockID, replace.getMeta(), 3);
            }
        }
    }

}
