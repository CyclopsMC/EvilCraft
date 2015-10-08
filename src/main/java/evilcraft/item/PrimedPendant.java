package evilcraft.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.client.gui.GuiHandler;
import evilcraft.client.gui.container.GuiPrimedPendant;
import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.Helpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.inventory.IGuiContainerProvider;
import evilcraft.core.inventory.NBTSimpleInventoryItemHeld;
import evilcraft.core.inventory.NBTSimpleInventoryItemStack;
import evilcraft.fluid.Blood;
import evilcraft.inventory.container.ContainerPrimedPendant;
import evilcraft.modcompat.baubles.BaublesModCompat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * One potion can be inserted to continuously apply it to the player.
 * @author rubensworks
 *
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = Reference.MOD_BAUBLES, striprefs = true)
public class PrimedPendant extends ConfigurableDamageIndicatedItemFluidContainer implements IBauble, IGuiContainerProvider {

    private static final int TICK_MODULUS = MinecraftHelpers.SECOND_IN_TICKS / 2;

    private static PrimedPendant _instance = null;

    private int guiID;

    private Class<? extends Container> container;
    @SideOnly(Side.CLIENT)
    private Class<? extends GuiScreen> gui;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new PrimedPendant(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PrimedPendant getInstance() {
        return _instance;
    }

    private PrimedPendant(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, PrimedPendantConfig.capacity, Blood.getInstance());
        this.setMaxStackSize(1);
        this.guiID = Helpers.getNewId(Helpers.IDType.GUI);

        if (MinecraftHelpers.isClientSide())
            setGUI(GuiPrimedPendant.class);
        setContainer(ContainerPrimedPendant.class);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        ItemStack potionStack = getPotionStack(itemStack);
        if(potionStack != null) {
            List<PotionEffect> potionEffects = Items.potionitem.getEffects(potionStack);
            for(PotionEffect potionEffect : potionEffects) {
                Double multiplier =  PrimedPendantConfig._instance.getMultiplier(potionEffect.getPotionID());
                String striked = multiplier != null && multiplier < 0 ? "§m" : "";
                list.add(L10NHelpers.localize(super.getUnlocalizedName(itemStack) +".potion",
                            striked + L10NHelpers.localize(potionEffect.getEffectName()),
                            StatCollector.translateToLocal("enchantment.level." + (potionEffect.getAmplifier() + 1))));
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
                List<PotionEffect> potionEffects = Items.potionitem.getEffects(potionStack);
                for(PotionEffect potionEffect : potionEffects) {
                    int toDrain = PrimedPendantConfig.usage * (potionEffect.getAmplifier() + 1);
                    Double multiplier = PrimedPendantConfig._instance.getMultiplier(potionEffect.getPotionID());
                    if(multiplier != null) {
                        toDrain *= multiplier;
                    }
                    if((multiplier == null || multiplier >= 0) && canConsume(toDrain, itemStack, player)) {
                        player.addPotionEffect(
                                new PotionEffect(potionEffect.getPotionID(), TICK_MODULUS * 2, potionEffect.getAmplifier(),
                                        !potionEffect.getCurativeItems().isEmpty()));
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

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack, int pass) {
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
    @SideOnly(Side.CLIENT)
    public void setGUI(Class<? extends GuiScreen> gui) {
        this.gui = gui;
    }

    @Override
    public void setContainer(Class<? extends Container> container) {
        this.container = container;
    }

    @Override
    public Class<? extends Container> getContainer() {
        return container;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGUI() {
        return gui;
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

    @Override
    public String getGuiTexture() {
        return getGuiTexture("");
    }

    @Override
    public String getGuiTexture(String suffix) {
        return Reference.TEXTURE_PATH_GUI + eConfig.getNamedId() + "_gui" + suffix + ".png";
    }

    /**
     * Open the gui for a certain item index in the player inventory.
     * @param world The world.
     * @param player The player.
     * @param itemIndex The item index in the player inventory.
     */
    public void openGuiForItemIndex(World world, EntityPlayer player, int itemIndex) {
        GuiHandler.setTemporaryItemIndex(itemIndex);
        if(!world.isRemote || isClientSideOnlyGui()) {
            player.openGui(EvilCraft._instance, getGuiID(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }

    protected boolean isClientSideOnlyGui() {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        openGuiForItemIndex(world, player, player.inventory.currentItem);
        return itemStack;
    }

}
