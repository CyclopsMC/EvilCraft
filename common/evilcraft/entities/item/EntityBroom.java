package evilcraft.entities.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet39AttachEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.Configurable;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

public class EntityBroom extends Entity implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ENTITY_NO_GLOBAL_ID;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public EntityBroom(World world) {
        super(world);
    }
    
    @SideOnly(Side.CLIENT)
    public EntityBroom(World world, double x, double y, double z) {
        super(world);
        setPosition(x, y, z);
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return true;
    }
    
    @Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}
    
    @Override
    public boolean interactFirst(EntityPlayer player) {
    	if (!worldObj.isRemote && riddenByEntity == null) {
    		player.mountEntity(this);
    	}
    	
    	return true;
    }

    @Override
    protected void entityInit() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        // TODO Auto-generated method stub
        
    }
    
    
}
