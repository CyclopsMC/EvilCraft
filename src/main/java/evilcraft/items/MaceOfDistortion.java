package evilcraft.items;
import java.util.List;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.ExtendedDamageSource;
import evilcraft.api.IInformationProvider;
import evilcraft.api.ItemHelpers;
import evilcraft.api.L10NHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.fluids.Blood;
import evilcraft.render.particle.EntityDistortFX;

/**
 * A powerful magical mace.
 * The power of it can be changed by Shift + Right clicking.
 * It can be used as primary weapon by just right clicking on entities, it will however
 * use up some blood for that and become unusable when the tank is empty.
 * It can also be used as secondary weapon to do a distortion effect in a certain area
 * with the area increasing depending on how long the item is being charged, this
 * area is smaller with a larger power level, but more powerful.
 * @author rubensworks
 *
 */
public class MaceOfDistortion extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static MaceOfDistortion _instance = null;
    
    /**
     * The amount of ticks that should go between each update of the area of effect particles.
     */
    public static final int AOE_TICK_UPDATE = 20;
    
    private static final String NBT_KEY_POWER = "power";
    
    private static final int MAXIMUM_CHARGE = 100;
    private static final float MELEE_DAMAGE = 7.0F;
    private static final float RADIAL_DAMAGE = 3.0F;
    private static final int CONTAINER_SIZE = FluidContainerRegistry.BUCKET_VOLUME * 4;
    private static final int HIT_USAGE = 5;
    private static final int POWER_LEVELS = 5;
    
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
            this.drain(itemStack, HIT_USAGE, true);
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
        return MAXIMUM_CHARGE * (POWER_LEVELS - getPower(itemStack));
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(player.isSneaking()) {
            if(!world.isRemote) {
                int newPower = (getPower(itemStack) + 1) % POWER_LEVELS;
                setPower(itemStack, newPower);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC
                		+ L10NHelpers.localize("item.items.maceOfDistortion.setPower",
                				new Object[]{newPower})));
            }
            return itemStack;
        } else {
            if(isUsable(itemStack)) {
                player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
            } else {
                if(world.isRemote) {
                    animateOutOfEnergy(world, player);
                }
            }
        }
        return itemStack;
    }
    
    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int duration) {
        World world = player.worldObj;
        if(world.isRemote && duration % AOE_TICK_UPDATE == 0) {
            showUsingItemTick(world, itemStack, player, duration);
        }
        super.onUsingTick(itemStack, player, duration);
    }
    
    @SideOnly(Side.CLIENT)
    protected void showUsingItemTick(World world, ItemStack itemStack, EntityPlayer player, int duration) {
        int itemUsedCount = getMaxItemUseDuration(itemStack) - duration;
        double area = getArea(itemUsedCount);
        int points = (int) (Math.pow(area, 0.55)) * 2 + 1;
        int particleChance = 5 * (POWER_LEVELS - getPower(itemStack));
        for(double point = -points; point <= points; point++) {
            for(double pointHeight = -points; pointHeight <= points; pointHeight+=0.5F) {
                if(itemRand.nextInt(particleChance) == 0) {
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
                                    particleMotionX, particleMotionY, particleMotionZ, (float) area * 3)
                            );
                }
            }
        }
    }
    
    /**
     * The area of effect for the given in use count (counting up per tick).
     * @param itemUsedCount The amount of ticks the item was active.
     * @return The area of effect.
     */
    private double getArea(int itemUsedCount) {
        return itemUsedCount / 5 + 2.0D;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int itemInUseCount) {
        // Actual usage length
        int itemUsedCount = getMaxItemUseDuration(itemStack) - itemInUseCount;
        
        // Calculate how much blood to drain
        int toDrain = itemUsedCount * getCapacity(itemStack) * (getPower(itemStack) + 1)
                / (getMaxItemUseDuration(itemStack) * POWER_LEVELS);
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
            
            // This will perform a distortion effect to entities in a certain area,
            // depending on the itemUsedCount.
            distortEntities(world, player, itemUsedCount, getPower(itemStack));
        } else if(world.isRemote) {
            animateOutOfEnergy(world, player);
        }
    }
    
    @SuppressWarnings("unchecked")
    protected void distortEntities(World world, EntityPlayer player, int itemUsedCount, int power) {
        // Center of the knockback
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        
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
        
        for(Entity entity : entities) {
            distortEntity(world, player, entity, x, y, z, itemUsedCount, power);
        }
    }
    
    /**
     * Distort an entity.
     * @param world The world.
     * @param player The player distoring the entity, can be null.
     * @param entity The distorted entity.
     * @param x Center X coordinate.
     * @param y Center Y coordinate.
     * @param z Center Z coordinate.
     * @param itemUsedCount The distortion usage power.
     * @param power The current power.
     */
    public static void distortEntity(World world, EntityPlayer player, Entity entity, double x, double y, double z, int itemUsedCount, int power) {
        double inverseStrength = entity.getDistance(x, y, z) / (itemUsedCount + 1);
        double knock = power + itemUsedCount / 200 + 1.0D;

        double dx = entity.posX - x;
        double dy = entity.posY + (double)entity.getEyeHeight() - y;
        double dz = entity.posZ - z;
        double d = (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);

        // No knockback is possible when the absolute distance is zero.
        if (d != 0.0D) {
            dx /= d;
            dy /= d;
            dz /= d;
            double strength = (1.0D - inverseStrength) * knock;
            if(entity instanceof EntityLivingBase) {
                // Attack the entity with the current power level.
                DamageSource damageSource = null;
                if(player == null) {
                    damageSource = ExtendedDamageSource.distorted;
                } else {
                    damageSource = DamageSource.causePlayerDamage(player);
                }
                entity.attackEntityFrom(damageSource, (float) (RADIAL_DAMAGE * power));
                
                if(world.isRemote) {
                    showEntityDistored(world, player, entity, power);
                }
            }
            strength /= 2;
            entity.motionX += dx * strength;
            entity.motionY += dy * strength;
            entity.motionZ += dz * strength;
        }
    }
    
    @SideOnly(Side.CLIENT)
    protected static void showEntityDistored(World world, EntityPlayer player, Entity entity, int power) {
        // Play a nice sound with the volume depending on the power.
        world.playSoundAtEntity(entity, "random.explode", (float)(power + 1) / (float)POWER_LEVELS, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if(player != null) {
            world.playSoundAtEntity(player, "random.explode", (float)(power + 1) / (float)POWER_LEVELS, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }
        // Fake explosion effect.
        world.spawnParticle("largeexplode", entity.posX, entity.posY + itemRand.nextFloat(), entity.posZ, 1.0D, 0.0D, 0.0D);
    }
    
    @SideOnly(Side.CLIENT)
    protected void animateOutOfEnergy(World world, EntityPlayer player) {
        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;
        
        double particleX = xCoord;
        double particleY = yCoord;
        double particleZ = zCoord;

        float particleMotionX = world.rand.nextFloat() * 0.2F - 0.1F;
        float particleMotionY = 0.2F;
        float particleMotionZ = world.rand.nextFloat() * 0.2F - 0.1F;
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                new EntitySmokeFX(world, particleX, particleY, particleZ,
                        particleMotionX, particleMotionY, particleMotionZ)
                );
        
        world.playSoundAtEntity(player, "note.bd", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    }
    
    @Override
    public int getItemEnchantability() {
        return 15;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Multimap getAttributeModifiers(ItemStack itemStack) {
        Multimap multimap = super.getAttributeModifiers(itemStack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)MELEE_DAMAGE, 0));
        return multimap;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        list.add(IInformationProvider.INFO_PREFIX + L10NHelpers.getLocalizedInfo(this, ".main.1"));
        list.add(IInformationProvider.INFO_PREFIX + L10NHelpers.getLocalizedInfo(this, ".main.2"));
        list.add(EnumChatFormatting.BOLD
        		+ L10NHelpers.localize(getUnlocalizedName() + ".info.power",
        				new Object[]{getPower(itemStack)}));
    }
    
    /**
     * Get the power level of the given ItemStack.
     * @param itemStack The item to check.
     * @return The power this Mace currently has.
     */
    public static int getPower(ItemStack itemStack) {
        return ItemHelpers.getNBTInt(itemStack, NBT_KEY_POWER);
    }
    
    /**
     * Set the power level of the given ItemStack.
     * @param itemStack The item to change.
     * @param power The new power level.
     */
    public static void setPower(ItemStack itemStack, int power) {
        ItemHelpers.setNBTInt(itemStack, power, NBT_KEY_POWER);
    }

}
