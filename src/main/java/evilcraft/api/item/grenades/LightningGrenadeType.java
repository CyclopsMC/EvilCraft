package evilcraft.api.item.grenades;

import evilcraft.api.helpers.EntityHelpers;
import evilcraft.api.helpers.L10NHelpers;
import evilcraft.entities.item.EntityGrenade;
import evilcraft.items.WeatherContainer;
import evilcraft.items.WeatherContainer.WeatherContainerTypes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Implements the functionality of a lightning grenade; When it hits the
 * ground, it spawns a lightning strike on that position.
 * @author immortaleeb
 *
 */
public class LightningGrenadeType extends AbstractGrenadeType {
	
	/**
	 * The unique name used to register this grenade type and find its icon location.
	 */
	public static final String UNIQUE_NAME = "lightningGrenade";

	/**
	 * Creates a new lightning grenade type. You should NEVER call this constructor unless you know what you're doing!!
	 * Call {@link GrenadeTypeRegistry#registerGrenadeType(String, Class)} and {@link GrenadeTypeRegistry#getGrenadeType(String)}
	 * instead.
	 * @param id The unique id for this grenade type.
	 */
	public LightningGrenadeType(int id) { 
		super(id);
    }

    @Override
    public boolean onImpact(World world, EntityLivingBase thrower, EntityGrenade grenade,
                            MovingObjectPosition pos, Random random) {
        if (pos.entityHit != null) {
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(grenade, thrower), 0.0F);
        }

        for (int i = 0; i < 32; ++i) {
            world.spawnParticle("magicCrit",
                    pos.blockX + 0.5, pos.blockY + random.nextDouble() * 2.0D, pos.blockZ + 0.5,
                    random.nextGaussian(), 0.0D, random.nextGaussian());
        }

        if (!world.isRemote) {
            if (thrower != null && thrower instanceof EntityPlayerMP) {
                EntityHelpers.onEntityCollided(world, pos.blockX, pos.blockY, pos.blockZ, grenade);
                world.addWeatherEffect(new EntityLightningBolt(world, pos.blockX, pos.blockY, pos.blockZ));
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return UNIQUE_NAME;
    }
    
    @Override
	public void addInformation(List list) {
		list.add(EnumChatFormatting.YELLOW + L10NHelpers.localize("grenade.types.lightning.info"));
	}

	@Override
	public ItemStack getCraftingItem() {
		return WeatherContainer.createItemStack(WeatherContainerTypes.LIGHTNING, 1);
	}
}
