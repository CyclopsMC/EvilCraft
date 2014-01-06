package evilcraft.items;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableItem;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.item.EntityBroom;

public class Broom extends ConfigurableItem {
    
    private static Broom _instance = null;
    
    private static final int SLOW_DURATION = 5;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new Broom(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static Broom getInstance() {
        return _instance;
    }

    private Broom(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    	
    	if (!world.isRemote) {
    		
    		EvilCraft.log("spawned broom on " + x + " " + y + " " + z);
    		
    		world.spawnEntityInWorld(new EntityBroom(world, x + 0.5, y + 1.5, z + 0.5));
    		return true;
    	}
    	
    	return false;
    }
    
}
