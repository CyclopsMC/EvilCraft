package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.*;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
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

	public static ItemStack boxOfEternalClosureFilled;
    
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
        this.setStepSound(SoundType.METAL);
        this.setRotatable(true);

		MinecraftForge.EVENT_BUS.register(this);
    }

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World world, BlockPos blockPos) {
		EnumFacing rotation = world.getBlockState(blockPos).getValue(FACING);
		if(rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
			return new AxisAlignedBB(0.25F, 0F, 0.0F, 0.75F, 0.43F, 1.0F).offset(blockPos);
		} else {
			return new AxisAlignedBB(0.0F, 0F, 0.25F, 1.0F, 0.43F, 0.75F).offset(blockPos);
		}
	}
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState blockState) {
    	return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
    	return false;
    }
    
    @Override
    public boolean isNormalCube(IBlockState blockState) {
    	return false;
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
		String content = TextFormatting.ITALIC + L10NHelpers.localize("general." + Reference.MOD_ID + ".info.empty");
		if(hasPlayer(itemStack)) {
			content = getPlayerName(itemStack);
		} else {
			String id = getSpiritName(itemStack);
			if (id != null) {
				content = L10NHelpers.getLocalizedEntityName(id);
			}
		}
		return TextFormatting.BOLD + L10NHelpers.localize(getUnlocalizedName() + ".info.content",
				TextFormatting.RESET + content);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void provideInformation(ItemStack itemStack,
			EntityPlayer entityPlayer, List list, boolean par4) {
		
	}

    @Override
	public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
		return BlockHelpers.doesBlockHaveSolidTopSurface(world, blockPos);
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
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(world.getTileEntity(blockPos) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
	    	if(tile.getSpiritInstance() != null) {
				EvilCraft.proxy.playSound(
						(double) blockPos.getX() + 0.5D,
						(double) blockPos.getY() + 0.5D,
						(double) blockPos.getZ() + 0.5D,
						SoundEvents.block_chest_open,
						SoundCategory.BLOCKS,
						0.5F,
						world.rand.nextFloat() * 0.1F + 0.5F
				);
	    		if(!world.isRemote) {
	    			tile.releaseSpirit();
	    		}
	    		return true;
	    	}
    	}
    	return super.onBlockActivated(world, blockPos, state, entityplayer, hand, heldItem, side, hitX, hitY, hitZ);
    }
    
    @Override
    public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
    	if(world.getTileEntity(blockPos) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
	    	if(tile.getLidAngle() > 0) {
	    		return LIGHT_LEVEL;
	    	}
    	}
        return super.getLightValue(blockState, world, blockPos);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item));
        list.add(BoxOfEternalClosure.boxOfEternalClosureFilled);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return hasPlayer(itemStack) ? EnumRarity.RARE : EnumRarity.UNCOMMON;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos blockPos) {
        if(world.getTileEntity(blockPos) != null) {
            TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
            if(tile.getSpiritInstance() != null) {
                return 15;
            }
        }
        return super.getComparatorInputOverride(blockState, world, blockPos);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState blockState, World worldObj, RayTraceResult target, EffectRenderer effectRenderer) {
		if(target != null) {
			RenderHelpers.addBlockHitEffects(effectRenderer, worldObj, Blocks.obsidian.getDefaultState(), target.getBlockPos(), target.sideHit);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
		RenderHelpers.addBlockHitEffects(effectRenderer, world, Blocks.obsidian.getDefaultState(), pos, EnumFacing.UP);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addLandingEffects(IBlockState blockState, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
		RenderHelpers.addBlockHitEffects(Minecraft.getMinecraft().effectRenderer, worldObj, Blocks.obsidian.getDefaultState(), blockPosition, EnumFacing.UP);
		return true;
	}
}
