package evilcraft.items;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.ItemHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.fluids.Blood;

/**
 * Item that can attract items and XP orbs.
 * @author rubensworks
 *
 */
public class Kineticator extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static Kineticator _instance = null;
    
    private static final int TICK_HOLDOFF = 10;
    private static final String NBT_KEY_POWER = "power";
    private static final int POWER_LEVELS = 5;
    private static final int RANGE_PER_LEVEL = 2;
    private static final double USAGE_PER_D = 0.3;
    private static final int CONTAINER_SIZE = FluidContainerRegistry.BUCKET_VOLUME;
    
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
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + StatCollector.translateToLocalFormatted("item.items.kineticator.setPower", new Object[]{newPower})));
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
        list.add(IInformationProvider.INFO_PREFIX+"Shift + Right click to toggle attraction.");
        list.add(IInformationProvider.INFO_PREFIX+"Right click to change area.");
        String attraction = EnumChatFormatting.RESET+"Disabled";
        if(ItemHelpers.isActivated(itemStack)) {
            attraction = EnumChatFormatting.GREEN + "Enabled";
        }
        list.add(EnumChatFormatting.BOLD + "Attraction: " + attraction);
        list.add(EnumChatFormatting.BOLD + "Area: " + getArea(itemStack));
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
        if(ItemHelpers.isActivated(itemStack) && getFluid(itemStack) != null) {
            // Center of the attraction
            double x = entity.posX;
            double y = entity.posY;
            double z = entity.posZ;
            
            // Not ticking every tick.
            if(Math.abs(Math.round(x + y + z)) % TICK_HOLDOFF == world.getWorldTime() % TICK_HOLDOFF) {
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
                    double dx = moveEntity.posX - x;
                    double dy = moveEntity.posY - y;
                    double dz = moveEntity.posZ - z;
                    double strength = -0.1;
                    if(entity instanceof EntityPlayer) {
                        strength = -1;
                    }
                    
                    double d = (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
                    int usage = (int) Math.ceil(d * USAGE_PER_D);
                    if(d > 0.5D && this.drain(itemStack, usage, true) != null) {
                        if(world.isRemote) {
                            showEntityMoved(world, entity, moveEntity, dx, dy, dz);
                        } else {
                            if(moveEntity instanceof EntityItem && d < 5.0D) {
                                ((EntityItem)moveEntity).delayBeforeCanPickup = 0;
                            }
                            moveEntity.motionX += dx * strength;
                            moveEntity.motionY += dy * strength;
                            moveEntity.motionZ += dz * strength;
                        }
                    }
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    protected void showEntityMoved(World world, Entity player, Entity entity, double dx, double dy, double dz) {
        world.spawnParticle("instantSpell", entity.posX, entity.posY, entity.posZ, dx, dy, dz);
    }

}
