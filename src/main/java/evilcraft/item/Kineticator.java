package evilcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.particle.EntityBlurFX;
import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.ItemHelpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.entity.item.EntityItemUndespawnable;
import evilcraft.fluid.Blood;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;

import java.util.List;
import java.util.Random;

/**
 * Item that can attract items and XP orbs.
 * @author rubensworks
 *
 */
public class Kineticator extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static Kineticator _instance = null;
    
    private static final String NBT_KEY_POWER = "power";
    private static final int POWER_LEVELS = 5;
    private static final int RANGE_PER_LEVEL = 2;
    private static final double USAGE_PER_D = 0.1;
    private static final int CONTAINER_SIZE = FluidContainerRegistry.BUCKET_VOLUME;
    
    @SideOnly(Side.CLIENT)
    private IIcon repellingIcon;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new Kineticator(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Kineticator getInstance() {
        return _instance;
    }

    private Kineticator(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, CONTAINER_SIZE, Blood.getInstance());
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        repellingIcon = iconRegister.registerIcon(getIconString() + "_repelling");
        super.registerIcons(iconRegister);
    }
    
    @Override
    public IIcon getIconFromDamage(int damage) {
    	if(damage == 1) {
    		return repellingIcon;
    	}
    	return super.getIconFromDamage(damage);
    }
    
    protected boolean isRepelling(ItemStack itemStack) {
    	return itemStack.getItemDamage() == 1;
    }
    
    protected void setRepelling(ItemStack itemStack, boolean repelling) {
    	itemStack.setItemDamage(repelling ? 1 : 0);
    }
    
    private int getArea(ItemStack itemStack) {
        return RANGE_PER_LEVEL * (getPower(itemStack) + 1);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            if(player.isSneaking()) {
                ItemHelpers.toggleActivation(itemStack);
            } else {
                int newPower = (getPower(itemStack) + 1) % POWER_LEVELS;
                setPower(itemStack, newPower);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC
                		+ L10NHelpers.localize("item.items.kineticator.setPower", newPower)));
            }
        }
        return itemStack;
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
        		getUnlocalizedName() + ".info" + (isRepelling(itemStack) ? ".repelling" : "") + ".attraction");
        list.add(EnumChatFormatting.BOLD
        		+ L10NHelpers.localize(getUnlocalizedName() + ".info.area", getArea(itemStack)));
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
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof EntityPlayer) {
            kineticate(itemStack, world, entity);
        }
        super.onUpdate(itemStack, world, entity, par4, par5);
    }
    
    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        kineticate(entityItem.getEntityItem(), entityItem.worldObj, entityItem);
        return super.onEntityItemUpdate(entityItem);
    }
    
    @SuppressWarnings("unchecked")
    private void kineticate(ItemStack itemStack, World world, Entity entity) {
        if(ItemHelpers.isActivated(itemStack) &&(getFluid(itemStack) != null ||
                (entity instanceof EntityPlayer && canConsume(1, itemStack, (EntityPlayer) entity))) &&
                (entity == null || !entity.isSneaking())) {
        	boolean repelling = isRepelling(itemStack);
        	
            // Center of the attraction
            double x = entity.posX;
            double y = entity.posY;
            double z = entity.posZ;

            // Not ticking every tick.
            if(0 == world.getWorldTime() % KineticatorConfig.tickHoldoff) {
                // Get items in calculated area.
                int area = getArea(itemStack);
                AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(area, area, area);
                List<EntityItem> entities = world.getEntitiesWithinAABBExcludingEntity(entity, box, new IEntitySelector() {

                    @Override
                    public boolean isEntityApplicable(Entity entity) {
                        return entity instanceof EntityItem
                                || (KineticatorConfig.moveXP && entity instanceof EntityXPOrb);
                    }

                });

                // Move all those items in the direction of the player.
                for(Entity moveEntity : entities) {
                    if(repelling ||
                            (moveEntity instanceof EntityItem && ((EntityItem) moveEntity).delayBeforeCanPickup == 0
                                    && canKineticateItem(((EntityItem) moveEntity).getEntityItem())) ||
                            (moveEntity instanceof EntityXPOrb)) {
                        double dx = moveEntity.posX - x;
                        double dy = moveEntity.posY - (y + (world.isRemote ? -1 : 1));
                        double dz = moveEntity.posZ - z;
                        double strength = -0.1;
                        if (entity instanceof EntityPlayer) {
                            strength = -1;
                        }
                        if (repelling) {
                            strength /= -1;
                            if (entity instanceof EntityPlayer) {
                                strength = 0.3;
                            }
                        }

                        double d = (double) MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
                        int usage = (int) Math.round(d * USAGE_PER_D);
                        if((repelling || d > 0.5D) && (usage == 0 || (this.drain(itemStack, usage, false) != null) ||
                                (entity instanceof EntityPlayer && this.canConsume(usage, itemStack, (EntityPlayer) entity)))) {
                            double m = 1 / (2 * (Math.max(1, d)));
                            dx *= m;
                            dy *= m;
                            dz *= m;
                            if (world.isRemote) {
                                showEntityMoved(world, entity, moveEntity, dx / 10, dy / 10, dz / 10);
                            } else {
                                if (moveEntity instanceof EntityItem && d < 5.0D) {
                                    ((EntityItem) moveEntity).delayBeforeCanPickup = repelling ? 5 : 0;
                                }
                                moveEntity.motionX = dx * strength;
                                moveEntity.motionY = dy * strength;
                                moveEntity.motionZ = dz * strength;
                            }
                            // Not ticking every tick.
                            if(0 == world.getWorldTime() % KineticatorConfig.consumeHoldoff) {
                                if(entity instanceof EntityPlayer) {
                                    this.consume(usage, itemStack, (EntityPlayer) entity);
                                } else {
                                    this.drain(itemStack, usage, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canKineticateItem(ItemStack entityItem) {
        if(entityItem == null) return false;
        for(String name : KineticatorConfig.kineticateBlacklist) {
            if(itemRegistry.getNameForObject(entityItem.getItem()).equals(name)) {
                return false;
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    protected void showEntityMoved(World world, Entity player, Entity entity, double dx, double dy, double dz) {
        Random rand = world.rand;
        float scale = 0.05F;
        float red = rand.nextFloat() * 0.03F + 0.5F;
        float green = rand.nextFloat() * 0.03F + (rand.nextBoolean() ? 0.5F : 0.3F);
        float blue = rand.nextFloat() * 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 2.5D + 10D);

        EntityBlurFX blur = new EntityBlurFX(world, entity.posX, entity.posY, entity.posZ, scale,
                -dx, -dy, -dz,
                red, green, blue, ageMultiplier);
        Minecraft.getMinecraft().effectRenderer.addEffect(blur);
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
    	return new EntityItemUndespawnable(world, (EntityItem) location);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + (isRepelling(itemStack) ? ".repelling" : "");
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	component.getSubItems(item, tab, itemList, fluid, 0);
    	component.getSubItems(item, tab, itemList, fluid, 1);
    }

}
