package evilcraft.item;

import evilcraft.Reference;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.ItemHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * A ring that allows the player to walk faster with a double step height.
 * @author rubensworks
 *
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = Reference.MOD_BAUBLES, striprefs = true)
public class EffortlessRing extends ConfigurableItem { // TODO: implements IBauble

    private static final int TICK_MODULUS = 1;
    private static final String PLAYER_NBT_KEY = Reference.MOD_ID + ":" + "lastStepSize";

    private static final float SPEED_BONUS = 0.05F;
    private static final float STEP_SIZE = 1F;
    private static final float JUMP_DISTANCE_FACTOR = 0.05F;
    private static final float JUMP_HEIGHT_FACTOR = 0.3F;
    private static final float FALLDISTANCE_REDUCTION = 1.5F;

    private static EffortlessRing _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new EffortlessRing(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EffortlessRing getInstance() {
        return _instance;
    }

    private EffortlessRing(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }

    /**
     * Re-apply the ring effects.
     * @param itemStack The item.
     * @param player The player.
     */
    public void adjustParameters(ItemStack itemStack, EntityPlayer player) {
        // Speed
        if(player.moveForward > 0 && player.onGround) {
            player.moveFlying(0, 1, player.isInWater() ? SPEED_BONUS / 4 : SPEED_BONUS);
        }

        // Step height
        if(!player.getEntityData().hasKey(PLAYER_NBT_KEY)) {
            player.getEntityData().setFloat(PLAYER_NBT_KEY, player.stepHeight);
        }
        player.stepHeight = player.isSneaking() ? 0.5F : STEP_SIZE;

        // Jump distance
        if(!player.onGround) {
            player.jumpMovementFactor = JUMP_DISTANCE_FACTOR;
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if(ItemHelpers.hasPlayerItem(player, this)) {
                // Jump height and fall distance.
                player.motionY += JUMP_HEIGHT_FACTOR;
                player.fallDistance -= FALLDISTANCE_REDUCTION;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        // Reset the step height.
        if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if(player.getEntityData().hasKey(PLAYER_NBT_KEY)) {
                if (!ItemHelpers.hasPlayerItem(player, this)) {
                    player.stepHeight = player.getEntityData().getFloat(PLAYER_NBT_KEY);
                    player.getEntityData().removeTag(PLAYER_NBT_KEY);
                }
            }
        }
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof EntityPlayer) {
            adjustParameters(itemStack, (EntityPlayer) entity);
        }
        super.onUpdate(itemStack, world, entity, par4, par5);
    }

    // TODO
    /*@Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entity) {
        // TODO
        //return BaublesModCompat.canUse();
        return true;
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entity) {
        return true;
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entity) {

    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entity) {

    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entity) {
        // TODO
        if(BaublesModCompat.canUse()) {
            this.onUpdate(itemStack, entity.worldObj, entity, 0, false);
        }
    }*/

}
