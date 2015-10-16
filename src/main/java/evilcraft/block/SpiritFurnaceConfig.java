package evilcraft.block;

import com.google.common.collect.Sets;
import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.EvilCraft;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.core.IMCHandler;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.IChangedCallback;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.ItemHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.WeightedItemStack;
import evilcraft.core.item.ItemBlockNBT;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.item.BiomeExtractConfig;
import evilcraft.tileentity.TileWorking;
import evilcraft.tileentity.tickaction.spiritfurnace.BoxCookTickAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.logging.log4j.Level;

import java.util.Set;
import java.util.UUID;

/**
 * Config for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class SpiritFurnaceConfig extends BlockContainerConfig {

    private static final String DELIMITER = "\\|";
    
    /**
     * The unique instance.
     */
    public static SpiritFurnaceConfig _instance;
    
    /**
     * How much mB per tick this furnace should consume.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "How much mB per tick this furnace should consume.")
    public static int mBPerTick = 25;

    /**
     * How much mB per tick this furnace should consume for player spirit.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "How much mB per tick this furnace should consume for player spirit.")
    public static int playerMBPerTick = mBPerTick * 4;
    
    /**
     * The required amount of ticks for each HP for cooking an entity.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The required amount of ticks for each HP for cooking an entity.")
    public static int requiredTicksPerHp = 10;

    /**
     * If the machine should play mob death sounds.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If the machine should play mob death sounds.")
    public static boolean mobDeathSounds = true;

    /**
     * The 1/X chance for villagers to drop emeralds. 0 means no drops.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The 1/X chance for villagers to drop emeralds. 0 means no drops.")
    public static int villagerDropEmeraldChance = 20;

    /**
     * Custom player drops. Maps player UUID to an itemstack. Expects the format domain:itemname:amount:meta for items where amount and meta are optional.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE,
            comment = "Custom player drops. Maps player UUID to an itemstack. Expects the format domain:itemname:amount:meta for items where amount and meta are optional.",
            changedCallback = OverridePlayerDrop.class)
    public static String[] playerDrops = new String[]{
            "93b459be-ce4f-4700-b457-c1aa91b3b687|minecraft:stone_slab", // Etho's Slab
    };

    /**
     * Make a new instance.
     */
    public SpiritFurnaceConfig() {
        super(
        	true,
            "spiritFurnace",
            null,
            SpiritFurnace.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }

    @Override
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet(
                TileWorking.UPGRADE_EFFICIENCY,
                TileWorking.UPGRADE_SPEED,
                TileWorking.UPGRADE_TIER1,
                TileWorking.UPGRADE_TIER2,
                TileWorking.UPGRADE_TIER3);
    }

    @Override
    public void onInit(IInitListener.Step step) {
        super.onInit(step);
        if(step == IInitListener.Step.INIT) {
            EvilCraft.IMC_HANDLER.registerAction(Reference.IMC_OVERRIDE_SPIRITFURNACE_DROPS, new IMCHandler.IIMCAction() {

                @Override
                public boolean handle(FMLInterModComms.IMCMessage message) {
                    if(!message.isNBTMessage()) return false;
                    try {
                        Class<EntityLivingBase> clazz = (Class<EntityLivingBase>) Class.forName(message.getNBTValue().getString("entityClass"));
                        Set<WeightedItemStack> itemStacks = Sets.newHashSet();
                        NBTTagList list = message.getNBTValue().getTagList("items", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
                        if(list == null || list.tagCount() == 0) {
                            EvilCraft.log("IMC override mob drop has no items.", Level.ERROR);
                            return false;
                        }
                        for(int i = 0; i < list.tagCount(); i++) {
                            NBTTagCompound tag = list.getCompoundTagAt(i);
                            if(!tag.hasKey("item")) {
                                EvilCraft.log("IMC override mob drop has no item in the list.", Level.ERROR);
                                return false;
                            }
                            ItemStack itemStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item"));
                            if(!tag.hasKey("weight")) {
                                EvilCraft.log("IMC override mob drop has no weight in the list.", Level.ERROR);
                                return false;
                            }
                            int weight = tag.getInteger("weight");
                            itemStacks.add(new WeightedItemStack(itemStack, weight));
                        }
                        BoxCookTickAction.overrideMobDrop(clazz, itemStacks);
                    } catch (ClassNotFoundException e) {
                        EvilCraft.log("IMC override mob drop did not provide an existing entity class.", Level.ERROR);
                        return false;
                    } catch (ClassCastException e) {
                        EvilCraft.log("IMC override mob drop did not provide an entity class of type EntityLivingBase.",
                                Level.ERROR);
                        return false;
                    }
                    return true;
                }
            });
        }
    }

    /**
     * Callback for when the player drops property is changed.
     * @author rubensworks
     *
     */
    public static class OverridePlayerDrop implements IChangedCallback {

        private static boolean calledOnce = false;

        @Override
        public void onChanged(Object value) {
            if(calledOnce) {
                for(String line : SpiritFurnaceConfig.playerDrops) {
                    String[] split = line.split(DELIMITER);
                    if(split.length != 2) {
                        throw new IllegalArgumentException("Invalid line '" + line + "' found for "
                                + "a Spirit Furnace player drop config.");
                    }
                    try {
                        String playerId = split[0];
                        boolean validId = true;
                        try {
                            UUID.fromString(playerId);
                        } catch (IllegalArgumentException e) {
                            validId = false;
                        }
                        if(!validId) {
                            EvilCraft.log("Invalid line '" + line + "' found for "
                                    + "a Spirit Furnace player drop config: " + split[0] + " does not refer to a valid player UUID; skipping.");
                        }
                        try {
                            ItemStack itemStack = ItemHelpers.parseItemStack(split[1]);
                            BoxCookTickAction.overridePlayerDrop(playerId, itemStack);
                        } catch (IllegalArgumentException e) {
                            EvilCraft.log("Invalid item '" + split[1] + "' in "
                                    + "a Spirit Furnace player drop config; skipping:" + e.getMessage(), Level.ERROR);
                        }
                    } catch (NumberFormatException e) {
                        EvilCraft.log("Invalid line '" + line + "' found for "
                                + "a Spirit Furnace player drop config: " + split[0] + " is not a number; skipping.");
                    }
                }
            }
            calledOnce = true;
        }

        @Override
        public void onRegisteredPostInit(Object value) {
            onChanged(value);
        }

    }
    
}
