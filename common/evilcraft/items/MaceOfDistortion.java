package evilcraft.items;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.fluids.Blood;
import evilcraft.render.particle.EntityDistortFX;

/**
 * A powerful magical mace.
 * @author rubensworks
 *
 */
public class MaceOfDistortion extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static MaceOfDistortion _instance = null;
    
    /**
     * The amount of ticks that should go between each update of the area of effect particles.
     */
    public static final int AOE_TICK_UPDATE = 20;
    
    private static final int MAXIMUM_CHARGE = 1000;
    private static final float MELEE_DAMAGE = 7.0F;
    private static final float RADIAL_DAMAGE = 3.0F;
    private static final int CONTAINER_SIZE = FluidContainerRegistry.BUCKET_VOLUME / 4;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new MaceOfDistortion(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static MaceOfDistortion getInstance() {
        return _instance;
    }

    private MaceOfDistortion(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, CONTAINER_SIZE, Blood.getInstance());
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    private boolean isUsable(ItemStack itemStack) {
        return getFluid(itemStack) != null;
    }
    
    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase attacked, EntityLivingBase attacker) {
        if(isUsable(itemStack)) {
            this.drain(itemStack, 1, true);
        }
        return true;
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer player, Entity entity) {
        return !isUsable(itemStack);
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.bow;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return MAXIMUM_CHARGE;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(isUsable(itemStack))
            player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        return itemStack;
    }
    
    @Override
    public void onUsingItemTick(ItemStack itemStack, EntityPlayer player, int duration) {
        // TODO: fancy growing particles, pixel noise particle?
        World world = player.worldObj;
        if(world.isRemote && duration % AOE_TICK_UPDATE == 0) {
            int itemUsedCount = getMaxItemUseDuration(itemStack) - duration;
            double area = getArea(itemUsedCount);
            int points = (int) (Math.pow(area, 0.55)) * 2 + 1;
            for(double point = -points; point <= points; point++) {
                for(double pointHeight = -points; pointHeight <= points; pointHeight+=0.5F) {
                    double u = Math.PI * (point / points);
                    double v = -2 * Math.PI * (pointHeight / points);
                    
                    double xOffset = Math.cos(u) * Math.sin(v) * area;
                    double yOffset = Math.sin(u) * area;
                    double zOffset = Math.cos(v) * area;
                    
                    double xCoord = player.posX;
                    double yCoord = player.posY;
                    double zCoord = player.posZ;
                    
                    double particleX = xCoord + xOffset + world.rand.nextFloat() - 0.5F;
                    double particleY = yCoord + yOffset + world.rand.nextFloat() - 0.5F;
                    double particleZ = zCoord + zOffset + world.rand.nextFloat() - 0.5F;
        
                    float particleMotionX = (float) (xOffset * 10);
                    float particleMotionY = (float) (yOffset * 10);
                    float particleMotionZ = (float) (zOffset * 10);
        
                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                            new EntityDistortFX(world, particleX, particleY, particleZ,
                                    particleMotionX, particleMotionY, particleMotionZ, (float) area)
                            );
                }
            }
        }
        super.onUsingItemTick(itemStack, player, duration);
    }
    
    /**
     * The area of effect for the given in use count (counting up per tick).
     * @param itemUsedCount The amount of ticks the item was active.
     * @return The area of effect.
     */
    private double getArea(int itemUsedCount) {
        return itemUsedCount / 5 + 2.0D;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int itemInUseCount) {
        // TODO: balance knockback & damage
        // Center of the knockback
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        
        // Actual usage length
        int itemUsedCount = getMaxItemUseDuration(itemStack) - itemInUseCount;
        
        // Calculate how much blood to drain
        int toDrain = itemUsedCount * getCapacity(itemStack) / getMaxItemUseDuration(itemStack);
        FluidStack fluidStack = getFluid(itemStack);
        int amount = 0;
        if(fluidStack != null) amount = fluidStack.amount;
        toDrain = Math.min(toDrain, amount);
        
        // Recalculate the itemUsedCount depending on how much blood is available
        itemUsedCount = toDrain * getMaxItemUseDuration(itemStack) / getCapacity(itemStack);
        
        // Only do something if there is some blood left
        if(toDrain > 0) {
            // Drain the calculate blood
            this.drain(itemStack, toDrain, true);
            
            // Get the entities in the given area
            double area = getArea(itemUsedCount);
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(area, area, area);
            List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, box, new IEntitySelector() {
    
                @Override
                public boolean isEntityApplicable(Entity entity) {
                    return true;
                }
                
            });
            
            // Do knockback and damage to the list of entities
            double knock = itemUsedCount / 20 + 1.0D;
            Vec3 vec3 = world.getWorldVec3Pool().getVecFromPool(x, y, z);
            for(Entity entity : entities) {
                double inverseStrength = entity.getDistance(x, y, z) / (itemUsedCount + 1);
    
                double dx = entity.posX - x;
                double dy = entity.posY + (double)entity.getEyeHeight() - y;
                double dz = entity.posZ - z;
                double d = (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
    
                // No knockback is possible when the absolute distance is zero.
                if (d != 0.0D) {
                    dx /= d;
                    dy /= d;
                    dz /= d;
                    double percentageBlocks = (double)world.getBlockDensity(vec3, entity.boundingBox);
                    double strength = (1.0D - inverseStrength) * percentageBlocks * knock;
                    if(entity instanceof EntityLivingBase)
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) (RADIAL_DAMAGE * strength));
                    strength /= 2;
                    entity.motionX += dx * strength;
                    entity.motionY += dy * strength;
                    entity.motionZ += dz * strength;
                }
            }
        } else {
            // TODO: play FAIL sound + smoke particles
        }
    }
    
    @Override
    public int getItemEnchantability() {
        return 15;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)MELEE_DAMAGE, 0));
        return multimap;
    }

}
