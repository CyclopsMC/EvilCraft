package evilcraft.item;

import evilcraft.entity.item.EntityBroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Item for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class Broom extends ConfigurableItem {
    
    private static Broom _instance = null;
    
    private static final float Y_SPAWN_OFFSET = 1.5f;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Broom getInstance() {
        return _instance;
    }

    public Broom(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && player.ridingEntity == null) {
            player.posY += Y_SPAWN_OFFSET;
            
            EntityBroom broom = new EntityBroom(world, player.posX, player.posY, player.posZ);
            
            // Spawn and mount the broom
            world.spawnEntityInWorld(broom);
            broom.mountEntity(player);
            
            stack.stackSize--;
        }
        
        return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if (!world.isRemote && player.isSneaking()) {
    		world.spawnEntityInWorld(new EntityBroom(world, blockPos.getX() + 0.5, blockPos.getY() + Y_SPAWN_OFFSET, blockPos.getZ() + 0.5));
    		
    		// We don't consume the broom when in creative mode
    		if (!player.capabilities.isCreativeMode)
    		    stack.stackSize--;
    		
    		return true;
    	}
    	
    	return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.RARE;
    }
    
}
