package org.cyclops.evilcraft.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.inventory.IGuiContainerProviderConfigurable;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemHeld;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemStack;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.gui.container.GuiPrimedPendant;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.inventory.container.ContainerPrimedPendant;
import org.cyclops.evilcraft.modcompat.baubles.BaublesModCompat;

import java.util.List;

/**
 * One potion can be inserted to continuously apply it to the player.
 * @author rubensworks
 *
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = Reference.MOD_BAUBLES, striprefs = true)
public class PrimedPendant extends ConfigurableDamageIndicatedItemFluidContainer implements IBauble, IGuiContainerProviderConfigurable {

    private static final int TICK_MODULUS = MinecraftHelpers.SECOND_IN_TICKS / 2;

    private static PrimedPendant _instance = null;

    private int guiID;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PrimedPendant getInstance() {
        return _instance;
    }

    public PrimedPendant(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, PrimedPendantConfig.capacity, Blood.getInstance());
        this.setMaxStackSize(1);
        this.guiID = Helpers.getNewId(EvilCraft._instance, Helpers.IDType.GUI);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        ItemStack potionStack = getPotionStack(itemStack);
        if(potionStack != null) {
            List<PotionEffect> potionEffects = PotionUtils.getEffectsFromStack(potionStack);
            for(PotionEffect potionEffect : potionEffects) {
                Double multiplier =  PrimedPendantConfig._instance.getMultiplier(potionEffect.getPotion());
                String striked = multiplier != null && multiplier < 0 ? "§m" : "";
                list.add(L10NHelpers.localize(super.getUnlocalizedName(itemStack) + ".potion",
                        striked + L10NHelpers.localize(potionEffect.getEffectName()),
                        I18n.translateToLocal("enchantment.level." + (potionEffect.getAmplifier() + 1))));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof EntityPlayer
                && world.getWorldTime() % TICK_MODULUS == 0) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack potionStack = getPotionStack(itemStack);
            if(potionStack != null) {
                List<PotionEffect> potionEffects = PotionUtils.getEffectsFromStack(potionStack);
                for(PotionEffect potionEffect : potionEffects) {
                    int toDrain = PrimedPendantConfig.usage * (potionEffect.getAmplifier() + 1);
                    Double multiplier = PrimedPendantConfig._instance.getMultiplier(potionEffect.getPotion());
                    if(multiplier != null) {
                        toDrain *= multiplier;
                    }
                    if((multiplier == null || multiplier >= 0) && canConsume(toDrain, itemStack, player)) {
                        player.addPotionEffect(
                                new PotionEffect(potionEffect.getPotion(), TICK_MODULUS * 27, potionEffect.getAmplifier(),
                                        !potionEffect.getCurativeItems().isEmpty(), true));
                        consume(toDrain, itemStack, player);
                    }
                }
            }
        }
        super.onUpdate(itemStack, world, entity, par4, par5);
    }

    public boolean hasPotionStack(ItemStack itemStack) {
        return getPotionStack(itemStack) != null;
    }

    public ItemStack getPotionStack(ItemStack itemStack) {
        IInventory inventory = getSupplementaryInventory(itemStack);
        return inventory.getStackInSlot(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return hasPotionStack(itemStack);
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entity) {
        return BaublesModCompat.canUse();
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entity) {
        return true;
    }

    @Optional.Method(modid = Reference.MOD_BAUBLES)
    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.AMULET;
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
        if(BaublesModCompat.canUse()) {
            this.onUpdate(itemStack, entity.worldObj, entity, 0, false);
        }
    }

    /**
     * Get the supplementary inventory of the given crafter.
     * @param player The player using the crafter.
     * @param itemStack The item stack.
     * @param itemIndex The item index.
     * @return The inventory.
     */
    public IInventory getSupplementaryInventory(EntityPlayer player, ItemStack itemStack, int itemIndex) {
        return new NBTSimpleInventoryItemHeld(player, itemIndex, 1, 64);
    }

    /**
     * Get the supplementary inventory of the item.
     * @param itemStack The item stack.
     * @return The inventory.
     */
    public IInventory getSupplementaryInventory(ItemStack itemStack) {
        return new NBTSimpleInventoryItemStack(itemStack, 1, 64);
    }

    // --- TODO: copy of ItemGui methods, clean this up with Cyclops for 1.8 ---

    @Override
    public int getGuiID() {
        return this.guiID;
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerPrimedPendant.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiPrimedPendant.class;
    }

    @Override
    public ModBase getMod() {
        return EvilCraft._instance;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
        if(itemstack != null
                && player instanceof EntityPlayerMP
                && player.openContainer != null
                && player.openContainer.getClass() == getContainer()) {
            ((EntityPlayerMP) player).closeScreen();
        }
        return super.onDroppedByPlayer(itemstack, player);
    }

    /**
     * Open the gui for a certain item index in the player inventory.
     * @param world The world.
     * @param player The player.
     * @param itemIndex The item index in the player inventory.
     */
    public void openGuiForItemIndex(World world, EntityPlayer player, int itemIndex) {
        EvilCraft._instance.getGuiHandler().setTemporaryData(GuiHandler.GuiType.ITEM, itemIndex);
        if(!world.isRemote || isClientSideOnlyGui()) {
            player.openGui(EvilCraft._instance, getGuiID(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }

    protected boolean isClientSideOnlyGui() {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        openGuiForItemIndex(world, player, player.inventory.currentItem);
        return MinecraftHelpers.successAction(itemStack);
    }

}
