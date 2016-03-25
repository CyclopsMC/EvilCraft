package org.cyclops.evilcraft.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BloodStainedBlock;
import org.cyclops.evilcraft.block.DarkOre;
import org.cyclops.evilcraft.block.FluidBlockBlood;

import javax.annotation.Nullable;

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
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkGem getInstance() {
        return _instance;
    }

    public DarkGem(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }
    
    @Override
    public boolean onEntityItemUpdate(final EntityItem entityItem) {
        // This will transform a dark gem into a blood infusion core when it finds 
        // REQUIRED_BLOOD_BLOCKS blood fluid blocks in the neighbourhood.
        if(Configs.isEnabled(BloodInfusionCoreConfig.class) && !entityItem.worldObj.isRemote
        		&& WorldHelpers.efficientTick(entityItem.worldObj, TICK_MODULUS, 
        				(int) entityItem.posX, (int) entityItem.posY, (int) entityItem.posZ)) {
            final BlockPos blockPos = entityItem.getPosition();
            World world = entityItem.worldObj;
            
            int amount = 0;
            if(isValidBlock(world, blockPos)) {
                // For storing REQUIRED_BLOOD_BLOCKS coordinates
                final BlockPos[] visited = new BlockPos[REQUIRED_BLOOD_BLOCKS];
                
                // Save first coordinate
                visited[amount] = blockPos;
                amount++;

                // Search in neighbourhood
                WorldHelpers.foldArea(world, 3, blockPos, new WorldHelpers.WorldFoldingFunction<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer apply(@Nullable Integer amount, World world, BlockPos pos) {
                        if(amount == null || amount == -1) return amount;
                        if(!(pos.getX() == blockPos.getX() && pos.getY() == blockPos.getY() && pos.getZ() == blockPos.getZ()) && isValidBlock(world, pos)) {
                            // Save next coordinate
                            visited[amount] = pos;

                            // Do the transform when REQUIRED_BLOOD_BLOCKS are found
                            if(++amount == REQUIRED_BLOOD_BLOCKS) {
                                // Spawn the new item
                                entityItem.getEntityItem().stackSize--;
                                entityItem.dropItem(DarkPowerGemConfig._instance.getItemInstance(), 1);

                                // Retrace coordinate steps and remove all those blocks + spawn particles
                                for(int restep = 0; restep < amount; restep++) {
                                    world.setBlockToAir(visited[restep]);
                                    if (world.isRemote)
                                        BloodStainedBlock.splash(world, visited[restep].add(0, -1, 0));
                                    world.notifyBlockOfStateChange(visited[restep], Blocks.air);
                                }
                                return -1;
                            }
                        }
                        return amount;
                    }
                }, amount);
            }
        }
        return false;
    }
    
    private boolean isValidBlock(IBlockAccess world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() == FluidBlockBlood.getInstance()
                && FluidBlockBlood.getInstance().isSourceBlock(world, blockPos);
    }

}
