package org.cyclops.evilcraft.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStained;
import org.cyclops.evilcraft.block.BlockDarkOre;

import javax.annotation.Nullable;

/**
 * Gem that drops from {@link BlockDarkOre}.
 * @author rubensworks
 *
 */
public class ItemDarkGem extends Item {

    private static final int REQUIRED_BLOOD_BLOCKS = 5;
    private static final int TICK_MODULUS = 5;

    public ItemDarkGem(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
        // This will transform a dark gem into a blood infusion core when it finds 
        // REQUIRED_BLOOD_BLOCKS blood fluid blocks in the neighbourhood.
        if (!entityItem.world.isRemote()
        		&& WorldHelpers.efficientTick(entityItem.world, TICK_MODULUS,
        				(int) entityItem.getPosX(), (int) entityItem.getPosY(), (int) entityItem.getPosZ())) {
            final BlockPos blockPos = entityItem.getPosition();
            World world = entityItem.world;
            
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
                                entityItem.getItem().shrink(1);
                                entityItem.entityDropItem(new ItemStack(RegistryEntries.ITEM_DARK_POWER_GEM));

                                // Retrace coordinate steps and remove all those blocks + spawn particles
                                for(int restep = 0; restep < amount; restep++) {
                                    world.removeBlock(visited[restep], false);
                                    if (world.isRemote())
                                        BlockBloodStained.splash(world, visited[restep].add(0, -1, 0));
                                    world.notifyNeighborsOfStateChange(visited[restep], Blocks.AIR);
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
    
    private boolean isValidBlock(World world, BlockPos blockPos) {
        return world.getFluidState(blockPos).getFluid() == RegistryEntries.FLUID_BLOOD
                && world.getFluidState(blockPos).isSource();
    }

}
