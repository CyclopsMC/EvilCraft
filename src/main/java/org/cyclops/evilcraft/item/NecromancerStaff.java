package org.cyclops.evilcraft.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHead;
import org.cyclops.evilcraft.fluid.Blood;

/**
 * A staff that can summon evil creatures that will target another entity.
 * The evil creatures will die instantly when the target is dead and will not drop items.
 * The creatures have less HP than in their regular form.
 * @author rubensworks
 *
 */
public class NecromancerStaff extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static NecromancerStaff _instance = null;
    
    private static final int CONTAINER_SIZE = NecromancerStaffConfig.capacity;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static NecromancerStaff getInstance() {
        return _instance;
    }

    public NecromancerStaff(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, CONTAINER_SIZE, Blood.getInstance());
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    protected void throwNecromancersHead(EntityLivingBase entityLiving, Class<? extends EntityLiving> mobType) {
    	EntityNecromancersHead head = new EntityNecromancersHead(entityLiving.worldObj, entityLiving);
    	if(!entityLiving.worldObj.isRemote) {
    		head.setMobType(mobType);
            // Last three params: pitch offset, velocity, inaccuracy
            head.func_184538_a(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, -20.0F, 0.5F, 1.0F);
    		entityLiving.worldObj.spawnEntityInWorld(head);
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		if(canConsume(NecromancerStaffConfig.usage, itemStack, player)) {
			consume(NecromancerStaffConfig.usage, itemStack, player);
			Class<? extends EntityLiving> mobType = EntityZombie.class; // Other types might be allowed in the future.
			throwNecromancersHead(player, mobType);
			return MinecraftHelpers.successAction(itemStack);
		}
        return super.onItemRightClick(itemStack, world, player, hand);
    }

}
