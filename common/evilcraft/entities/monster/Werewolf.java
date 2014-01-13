package evilcraft.entities.monster;

import java.util.Random;

import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.items.WerewolfBoneConfig;

import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class Werewolf extends EntityZombie implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.MOB;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public Werewolf(World par1World) {
        super(par1World);
        this.tasks.addTask(4, new EntityAIWander(this, 0.28F));
    }
    
    /**
     * Returns the block ID for the item the mob drops on death.
     */
    protected int getDropItemId() {
        if(WerewolfBoneConfig._instance.isEnabled())
            return WerewolfBoneConfig._instance.ID;
        else
            return super.getDropItemId();
    }
    
    /**
     * The amount of drops (0 or 1)
     */
    public int quantityDropped(Random par1Random) {
        return par1Random.nextInt(1);
    }

    @Override
    public String getUniqueName() {
        return "entities.monster."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }

}
