package evilcraft.tileentity.tickaction.spiritfurnace;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import evilcraft.EvilCraft;
import evilcraft.block.SpiritFurnaceConfig;
import evilcraft.core.helper.MathHelpers;
import evilcraft.core.helper.WeightedItemStack;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.core.world.FakeWorldItemDelegator;
import evilcraft.tileentity.TileSpiritFurnace;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.mutable.MutableDouble;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * {@link ITickAction} that is able to cook boxes with spirits.
 * @author rubensworks
 *
 */
public class BoxCookTickAction implements ITickAction<TileSpiritFurnace> {

    public static Map<Class<? extends EntityLivingBase>, List<WeightedItemStack>> MOBDROP_OVERRIDES = Maps.newHashMap();
    public static Map<UUID, List<WeightedItemStack>> PLAYERDROP_OVERRIDES = Maps.newHashMap();

    static {
        if(SpiritFurnaceConfig.villagerDropEmeraldChance > 0) {
            overrideMobDrop(EntityVillager.class, Sets.newHashSet(
                    new WeightedItemStack(new ItemStack(Items.emerald), 1),
                    new WeightedItemStack(null, SpiritFurnaceConfig.villagerDropEmeraldChance - 1)
            ));
        }
        overridePlayerDrop("068d4de0-3a75-4c6a-9f01-8c37e16a394c", new ItemStack(Items.emerald)); // kroeserr
        overridePlayerDrop("e1dc75c6-dcf9-4e0c-8fbf-9c6e5e44527c", new ItemStack(Items.wooden_sword)); // _EeB_
        overridePlayerDrop("777e7aa3-9373-4511-8d75-f99d23ebe252", new ItemStack(Items.dye, 1, 3).setStackDisplayName("Lekkere Stront")); // Davivs69
        overridePlayerDrop("3e13f558-fb72-4949-a842-07879924bc49", new ItemStack(Items.quartz)); // Jona
        overridePlayerDrop("b5c31e33-8224-4f96-a4bf-73721be9d2ec", new ItemStack(Blocks.cobblestone)); // dodo3231
        overridePlayerDrop("b2faeaab-fc87-4f91-98d3-836024f268ae", new ItemStack(Blocks.furnace).setStackDisplayName("Fuurnas")); // _KillaH229_
        overridePlayerDrop("069a79f4-44e9-4726-a5be-fca90e38aaf5", new ItemStack(Items.golden_apple, 1, 1)); // Notch
        overridePlayerDrop("853c80ef-3c37-49fd-aa49-938b674adae6", new ItemStack(Items.spawn_egg, 1, 91).setStackDisplayName("jeb_")); // jeb_
        overridePlayerDrop("61699b2e-d327-4a01-9f1e-0ea8c3f06bc6", new ItemStack(Items.spawn_egg, 1, 91).setStackDisplayName("Dinnerbone")); // Dinnerbone
        overridePlayerDrop("bbb87dbe-690f-4205-bdc5-72ffb8ebc29d", new ItemStack(Blocks.cobblestone, 45).setStackDisplayName("direwolf20")); // direwolf20
        overridePlayerDrop("0b7509f0-2458-4160-9ce1-2772b9a45ac2", new ItemStack(Items.porkchop)); // iChun
    }

    /**
     * Override an entity's drops inside the spirit furnace.
     * @param entity The entity class.
     * @param drops A map of drops to relative frequency, with the second pair of the map key representing the min-max
     *              amount of drops (both inclusive)
     */
    public static void overrideMobDrop(Class<? extends EntityLivingBase> entity, Set<WeightedItemStack> drops) {
        MOBDROP_OVERRIDES.put(entity, WeightedItemStack.createWeightedList(drops));
    }

    /**
     * Override a player's drops inside the spirit furnace.
     * @param playerId The player id.
     * @param drop The drop
     */
    public static void overridePlayerDrop(String playerId, ItemStack drop) {
        PLAYERDROP_OVERRIDES.put(UUID.fromString(playerId), WeightedItemStack.createWeightedList(Sets.newHashSet(new WeightedItemStack(drop, 1))));
    }
    
    @Override
    public boolean canTick(TileSpiritFurnace tile, ItemStack itemStack, int slot, int tick) {
        if(!tile.isForceHalt() && !tile.isCaughtError() && tile.canWork()
                && tile.getTank().getFluidAmount() >= getRequiredMb(tile, 0)
        		&& getCookStack(tile) != null && tile.canConsume(getCookStack(tile))) {
        	for(int slotId : tile.getProduceSlots()) {
	        	ItemStack production = tile.getInventory().getStackInSlot(slotId);
	            if(production == null || production.stackSize < production.getMaxStackSize()) {
	            	return tile.isSizeValidForEntity();
	            }
            }
        }
        return false;
    }
    
    protected ItemStack getCookStack(TileSpiritFurnace tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }

    protected ItemStack getPlayerSkull(String playerName) {
        ItemStack itemStack = new ItemStack(Items.skull, 1, 3);
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setString("SkullOwner", playerName);
        return itemStack;
    }
    
    protected void doNextDrop(TileSpiritFurnace tile) {
    	EntityLiving entity = tile.getEntity();
    	if(entity != null) {
    		FakeWorldItemDelegator world = FakeWorldItemDelegator.getInstance();
			world.setItemDropListener(tile);
			
			// Send sound to client
			String deathSound = ObfuscationHelpers.getDeathSound(entity);
            if(SpiritFurnaceConfig.mobDeathSounds) {
                EvilCraft.proxy.sendSoundMinecraft(tile.xCoord + 0.5D, tile.yCoord + 0.5D,
                        tile.zCoord + 0.5D, deathSound, 0.5F + world.rand.nextFloat() * 0.2F, 1.0F);
            }

            if(tile.isPlayer()) {
                UUID playerUuid = UUID.fromString(tile.getPlayerId());
                List<WeightedItemStack> possibleDrops = PLAYERDROP_OVERRIDES.get(playerUuid);
                possibleDrops.add(new WeightedItemStack(getPlayerSkull(tile.getPlayerName()), 1));
                WeightedItemStack weightedItemStack = WeightedItemStack.getRandomWeightedItemStack(possibleDrops, world.rand);
                ItemStack drop = weightedItemStack.getItemStackWithRandomizedSize(world.rand);
                if (drop != null) {
                    tile.onItemDrop(drop);
                }
            } else {
                if (MOBDROP_OVERRIDES.containsKey(entity.getClass())) {
                    List<WeightedItemStack> possibleDrops = MOBDROP_OVERRIDES.get(entity.getClass());
                    WeightedItemStack weightedItemStack = WeightedItemStack.getRandomWeightedItemStack(possibleDrops, world.rand);
                    ItemStack drop = weightedItemStack.getItemStackWithRandomizedSize(world.rand);
                    if (drop != null) {
                        tile.onItemDrop(drop);
                    }
                } else {
                    // To make sure the entity actually will drop something.
                    ObfuscationHelpers.setRecentlyHit(entity, 100);

                    try {
                        // Kill the entity to get the drops
                        entity.onDeath(DamageSource.generic);
                    } catch (Exception e) { // Gotta catch 'em all
                        tile.caughtError();
                    }
                }
            }
		}
    }

    protected int getRequiredMb(TileSpiritFurnace tile, int tick) {
        MutableDouble drain = new MutableDouble(SpiritFurnaceConfig.mBPerTick);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(drain, TileSpiritFurnace.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), tick);
    }

	@Override
	public void onTick(TileSpiritFurnace tile, ItemStack itemStack, int slot,
			int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(getRequiredMb(tile, tick), true);
		if(tick >= getRequiredTicks(tile, slot, tick)) {
			doNextDrop(tile);
		}
	}

	@Override
	public float getRequiredTicks(TileSpiritFurnace tile, int slot, int tick) {
        int requiredTicksBase;
		EntityLivingBase entity = tile.getEntity();
		if(entity == null) {
            requiredTicksBase = SpiritFurnaceConfig.requiredTicksPerHp;
		} else {
            requiredTicksBase = (int) ((entity.getHealth() + entity.getTotalArmorValue()) * SpiritFurnaceConfig.requiredTicksPerHp);
        }
        MutableDouble duration = new MutableDouble(requiredTicksBase);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(duration, TileSpiritFurnace.UPGRADEEVENT_SPEED));
        return (int) (double) duration.getValue();
	}
    
}
