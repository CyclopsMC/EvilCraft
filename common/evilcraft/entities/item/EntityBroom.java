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
    
    public static ElementType TYPE = ElementType.MOB;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public EntityBroom(World par1World) {
        super(par1World);
        noClip = true;
        preventEntitySpawning = true;
    }
    
    @SideOnly(Side.CLIENT)
    public EntityBroom(World par1World, double par2, double par4, double par6) {
        super(par1World);
        setPosition(par2, par4, par6);
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return true;
    }
    
    public boolean interact(EntityPlayer entityplayer)
    {
        if (!super.interactFirst(entityplayer))
        {
            if (!worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer))
            {
                entityplayer.mountEntity(this);
                Packet39AttachEntity packet = new Packet39AttachEntity(0, entityplayer, this);
                for(Object entity : ModLoader.getMinecraftServerInstance().getConfigurationManager().playerEntityList)
                {
                    EntityPlayerMP player = (EntityPlayerMP)entity;
                    if(entityplayer.entityId == player.entityId)
                        continue;
                    player.playerNetServerHandler.sendPacketToPlayer(packet);
                }
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    //The following methods are for most parts redundant, simplified versions of those in Entity but they also delete unused EMBs.
    public void onUpdate()
    {
        onEntityUpdate();
    }

    public void onEntityUpdate()
    {
        if(riddenByEntity == null || riddenByEntity.isDead)
        {
            setDead();
        }
        /*else if(worldObj.getBlockId(orgBlockPosX, orgBlockPosY, orgBlockPosZ) != orgBlockID)
        {
            interact((EntityPlayer) riddenByEntity);
        }*/
        ticksExisted++;
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
