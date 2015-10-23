package evilcraft.item;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.block.FluidBlockPoison;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.fluid.PoisonConfig;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Bottle that is retrieved when right-clicking a poison source.
 * @author rubensworks
 *
 */
public class PoisonBottle extends ConfigurableItem {

    private static PoisonBottle _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new PoisonBottle(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PoisonBottle getInstance() {
        return _instance;
    }

    private PoisonBottle(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setPotionEffect(PotionHelper.spiderEyeEffect);
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return Items.potionitem.getIconFromDamage(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
        return Items.potionitem.getIconFromDamageForRenderPass(meta, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass) {
        return pass == 0 ? RenderHelpers.RGBToInt(77, 117, 15) : super.getColorFromItemStack(itemStack, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        // Do nothing
    }

    @SubscribeEvent
    public void onPoisonRightClick(PlayerInteractEvent event) {
        // Return poison bottle instead of water bottle when right clicking poison fluid source with empty bottle.
        if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getHeldItem() != null) {
            MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(event.world, event.entityPlayer, true);
            if(pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = pos.blockX;
                int y = pos.blockY;
                int z = pos.blockZ;
                if(event.world.canMineBlock(event.entityPlayer, x, y, z) &&
                        event.entityPlayer.canPlayerEdit(x, y, z, pos.sideHit, event.entityPlayer.getHeldItem()) &&
                        event.world.getBlock(x, y, z).getMaterial() == Material.water) {
                    if(event.entityPlayer.getHeldItem().getItem() == Items.glass_bottle && Configs.isEnabled(PoisonConfig.class) &&
                            event.world.getBlock(x, y, z) == FluidBlockPoison.getInstance()) {
                        InventoryHelpers.tryReAddToStack(event.entityPlayer, event.entityPlayer.getHeldItem(), new ItemStack(this));
                        event.world.setBlockToAir(x, y, z);
                    }
                }
            }
        }
    }

}
