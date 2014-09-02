package evilcraft.entities.item;

import java.util.List;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.api.item.grenades.IGrenadeType;
import evilcraft.items.Grenade;

/**
 * An entity for a {@link Grenade}.
 * @author immortaleeb
 *
 */
public class EntityGrenade extends EntityThrowable implements Configurable {

    protected ExtendedConfig<?> eConfig = null;

    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ENTITY;

    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityGrenade(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link net.minecraft.entity.EntityLivingBase}.
     * @param world The world.
     * @param entityLivingBase The {@link net.minecraft.entity.EntityLivingBase} that placed this {@link net.minecraft.entity.Entity}.
     */
    public EntityGrenade(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
    }

    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    @SideOnly(Side.CLIENT)
    public EntityGrenade(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void entityInit() {
        super.entityInit();

        DataWatcher dw = getDataWatcher();
        dw.addObject(Grenade.DATA_WATCHER_TYPES_ID, 0);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig( ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }

    private List<IGrenadeType> getGrenadeTypes() {
        int types = getDataWatcher().getWatchableObjectInt(Grenade.DATA_WATCHER_TYPES_ID);
        
        return Grenade.deserializeGrenadeTypes(types);
    }

    @Override
    protected void onImpact(MovingObjectPosition pos) {
        int i = 0;
        boolean done = false;
        List<IGrenadeType> grenadeTypes = getGrenadeTypes();

        // Loop through all grenade types, or until one grenade type returns true on impact
        while (i < grenadeTypes.size() && !done) {
            done = grenadeTypes.get(i++).onImpact(worldObj, getThrower(), this, pos, rand);
        }

        this.setDead();
    }
}
