package evilcraft.item;

import evilcraft.Configs;
import evilcraft.block.BloodStainedBlock;
import evilcraft.block.DarkOre;
import evilcraft.block.FluidBlockBlood;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.WorldHelpers;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Gem that drops from {@link DarkOre}.
 * @author rubensworks
 *
 */
public class DarkGem extends ConfigurableItem {
    
    private static DarkGem _instance = null;
    private static final int REQUIRED_BLOOD_BLOCKS = 5;
    private static final int TICK_MODULUS = 5;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkGem(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkGem getInstance() {
        return _instance;
    }

    private DarkGem(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }
    
    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        // This will transform a dark gem into a blood infusion core when it finds 
        // REQUIRED_BLOOD_BLOCKS blood fluid blocks in the neighbourhood.
        if(Configs.isEnabled(BloodInfusionCoreConfig.class) && !entityItem.worldObj.isRemote
        		&& WorldHelpers.efficientTick(entityItem.worldObj, TICK_MODULUS, 
        				(int) entityItem.posX, (int) entityItem.posY, (int) entityItem.posZ)) {
            /*int x = MathHelper.floor_double(entityItem.posX);
            int y = MathHelper.floor_double(entityItem.posY);
            int z = MathHelper.floor_double(entityItem.posZ);*/
            BlockPos blockPos = entityItem.getPosition();
            World world = entityItem.worldObj;
            
            int amount = 0;
            if(isValidBlock(world, blockPos)) {
                // For storing REQUIRED_BLOOD_BLOCKS coordinates
                BlockPos[] visited = new BlockPos[REQUIRED_BLOOD_BLOCKS];
                
                // Save first coordinate
                visited[amount] = blockPos;
                amount++;
                
                // Search in neighbourhood
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        for(int k = -1; k <= 1; k++) {
                            BlockPos loopPos = blockPos.add(i, j, k);
                            if(!(i==0 && j==0 && k==0) && isValidBlock(world, loopPos)) {
                                // Save next coordinate
                                visited[amount] = loopPos;
                                amount++;
                                
                                // Do the transform when REQUIRED_BLOOD_BLOCKS are found
                                if(amount == REQUIRED_BLOOD_BLOCKS) {
                                    // Spawn the new item
                                    entityItem.getEntityItem().stackSize--;
                                    entityItem.dropItem(DarkPowerGemConfig._instance.getItemInstance(), 1);
                                    
                                    // Retrace coordinate step and remove all those blocks + spawn particles
                                    for(int restep = 0; restep < amount; restep++) {
                                        world.setBlockToAir(visited[restep]);
                                        if (world.isRemote)
                                            BloodStainedBlock.splash(world, visited[restep].add(0, -1, 0));
                                        world.notifyNeighborsOfStateChange(visited[restep], Blocks.air);
                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isValidBlock(IBlockAccess world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() == FluidBlockBlood.getInstance()
                && FluidBlockBlood.getInstance().isSourceBlock(world, blockPos);
    }

}
