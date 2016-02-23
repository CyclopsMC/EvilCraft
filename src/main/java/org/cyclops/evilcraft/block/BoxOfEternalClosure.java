package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.evilcraft.core.world.FakeWorld;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

import java.util.List;
import java.util.UUID;

/**
 * A box that can hold beings from higher dimensions.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosure extends ConfigurableBlockContainer implements IInformationProvider, IBlockRarityProvider {

	public static final String FORGOTTEN_PLAYER = "Forgotten Player";
	private static final int LIGHT_LEVEL = 6;

	@BlockProperty(ignore = true)
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
    private static BoxOfEternalClosure _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BoxOfEternalClosure getInstance() {
        return _instance;
    }

    public BoxOfEternalClosure(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileBoxOfEternalClosure.class);
        
        this.setHardness(2.5F);
        this.setStepSound(soundTypePiston);
        this.setRotatable(true);

		MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos blockPos) {
    	EnumFacing rotation = world.getBlockState(blockPos).getValue(FACING);
        if(rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
        	setBlockBounds(0.25F, 0F, 0.0F, 0.75F, 0.43F, 1.0F);
        } else {
        	setBlockBounds(0.0F, 0F, 0.25F, 1.0F, 0.43F, 0.75F);
        }
    }
    
    @Override
    public int getRenderType() {
    	return -1;
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean isNormalCube() {
    	return false;
    }
    
    /**
     * Get the ID of an inner spirit, can be -1.
     * @param itemStack The item stack.
     * @return The ID or -1.
     */
    public int getSpiritID(ItemStack itemStack) {
		if(hasPlayer(itemStack)) {
			return -1;
		}
    	NBTTagCompound tag = itemStack.getTagCompound();
		if(tag != null) {
			NBTTagCompound spiritTag = tag.getCompoundTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT);
			if(spiritTag != null) {
				String innerEntity = spiritTag.getString(VengeanceSpirit.NBTKEY_INNER_SPIRIT);
				if(innerEntity != null && !innerEntity.isEmpty()) {
					try {
						Class<?> clazz = Class.forName(innerEntity);
                        if(!VengeanceSpirit.canSustainClass(clazz)) return -1;
						Integer ret = ObfuscationHelpers.getClassToID().get(clazz);
						if(ret == null) {
							return -1;
						} else {
							return ret;
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return -1;
    }
    
    /**
     * Get the name of an inner spirit, can be null.
     * @param itemStack The item stack.
     * @return The name.
     */
    public String getSpiritName(ItemStack itemStack) {
		if(hasPlayer(itemStack)) {
			return "Zombie";
		}
    	NBTTagCompound tag = itemStack.getTagCompound();
		if(tag != null) {
			NBTTagCompound spiritTag = tag.getCompoundTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT);
			if(spiritTag != null && !spiritTag.hasNoTags()) {
				String innerEntity = spiritTag.getString(VengeanceSpirit.NBTKEY_INNER_SPIRIT);
				if(innerEntity != null && !innerEntity.isEmpty()) {
					try {
						Class<?> clazz = Class.forName(innerEntity);
						if(!VengeanceSpirit.canSustainClass(clazz)) return null;
						return (String) EntityList.classToStringMapping.get(clazz);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					return VengeanceSpirit.DEFAULT_L10N_KEY;
				}
			}
		}
		return null;
    }
    
    /**
     * Put a vengeance swarm inside the given box.
     * @param itemStack The box.
     */
    public static void setVengeanceSwarmContent(ItemStack itemStack) {
    	NBTTagCompound tag = new NBTTagCompound();
    	NBTTagCompound spiritTag = new NBTTagCompound();
    	
    	VengeanceSpirit spirit = new VengeanceSpirit(FakeWorld.getInstance());
    	spirit.setGlobalVengeance(true);
    	spirit.setIsSwarm(true);
    	spirit.writeToNBT(spiritTag);
    	String entityId = EntityList.getEntityString(spirit);
    	
		spiritTag.setString(EntityHelpers.NBTTAG_ID, entityId);
		tag.setTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT, spiritTag);
		itemStack.setTagCompound(tag);
    }

	/**
	 * Put a player inside the given box.
	 * @param itemStack The box.
	 * @param playerId The player id to set.
	 */
	public static void setPlayerContent(ItemStack itemStack, UUID playerId) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound spiritTag = new NBTTagCompound();

		VengeanceSpirit spirit = new VengeanceSpirit(FakeWorld.getInstance());
		spirit.setPlayerId(playerId.toString());
		spirit.setPlayerName(FORGOTTEN_PLAYER);
		tag.setString(TileBoxOfEternalClosure.NBTKEY_PLAYERID, spirit.getPlayerId());
		tag.setString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME, spirit.getPlayerName());
		spirit.setGlobalVengeance(true);
		spirit.writeToNBT(spiritTag);
		String entityId = EntityList.getEntityString(spirit);

		spiritTag.setString(EntityHelpers.NBTTAG_ID, entityId);
		tag.setTag(TileBoxOfEternalClosure.NBTKEY_SPIRIT, spiritTag);
		itemStack.setTagCompound(tag);
	}

	public String getPlayerName(ItemStack itemStack) {
		if(itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME, MinecraftHelpers.NBTTag_Types.NBTTagString.ordinal())) {
			return itemStack.getTagCompound().getString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME);
		}
		return "";
	}

	public String getPlayerId(ItemStack itemStack) {
		if(itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(TileBoxOfEternalClosure.NBTKEY_PLAYERID, MinecraftHelpers.NBTTag_Types.NBTTagString.ordinal())) {
			return itemStack.getTagCompound().getString(TileBoxOfEternalClosure.NBTKEY_PLAYERID);
		}
		return "";
	}

	public boolean hasPlayer(ItemStack itemStack) {
		return !getPlayerId(itemStack).isEmpty();
	}

	@Override
	public String getInfo(ItemStack itemStack) {
		String content = EnumChatFormatting.ITALIC + L10NHelpers.localize("general." + Reference.MOD_ID + ".info.empty");
		if(hasPlayer(itemStack)) {
			content = getPlayerName(itemStack);
		} else {
			String id = getSpiritName(itemStack);
			if (id != null) {
				content = L10NHelpers.getLocalizedEntityName(id);
			}
		}
		return EnumChatFormatting.BOLD + L10NHelpers.localize(getUnlocalizedName() + ".info.content",
				EnumChatFormatting.RESET + content);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void provideInformation(ItemStack itemStack,
			EntityPlayer entityPlayer, List list, boolean par4) {
		
	}

    @Override
	public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
    	return World.doesBlockHaveSolidTopSurface(world, blockPos.add(0, -1, 0));
    }
    
    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState blockState, Block block) {
        if(!canPlaceBlockAt(world, blockPos)) {
        	dropBlockAsItem(world, blockPos, blockState, 0);
        	world.setBlockToAir(blockPos);
        }
        super.onNeighborBlockChange(world, blockPos, blockState, block);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(world.getTileEntity(blockPos) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
	    	if(tile.getSpiritInstance() != null) {
	    		world.playSoundEffect(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, "random.chestopen",
	    				0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
	    		if(!world.isRemote) {
	    			tile.releaseSpirit();
	    		}
	    		return true;
	    	}
    	}
    	return super.onBlockActivated(world, blockPos, state, entityplayer, side, hitX, hitY, hitZ);
    }
    
    @Override
    public int getLightValue(IBlockAccess world, BlockPos blockPos) {
    	if(world.getTileEntity(blockPos) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
	    	if(tile.getLidAngle() > 0) {
	    		return LIGHT_LEVEL;
	    	}
    	}
        return super.getLightValue(world, blockPos);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item));
        ItemStack swarmStack = new ItemStack(item);
        setVengeanceSwarmContent(swarmStack);
        list.add(swarmStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return hasPlayer(itemStack) ? EnumRarity.RARE : EnumRarity.UNCOMMON;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        if(world.getTileEntity(blockPos) != null) {
            TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
            if(tile.getSpiritInstance() != null) {
                return 15;
            }
        }
        return super.getComparatorInputOverride(world, blockPos);
    }

}
