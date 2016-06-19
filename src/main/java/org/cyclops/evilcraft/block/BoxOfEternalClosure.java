package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.entity.monster.VengeanceSpiritData;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

import java.util.List;
import java.util.Random;
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
        super(eConfig, Material.IRON, TileBoxOfEternalClosure.class);
        
        this.setHardness(2.5F);
        this.setSoundType(SoundType.METAL);
        this.setRotatable(true);

		MinecraftForge.EVENT_BUS.register(this);
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos blockPos) {
		EnumFacing rotation = BlockHelpers.getSafeBlockStateProperty(source.getBlockState(blockPos), FACING, EnumFacing.NORTH);
		if(rotation == EnumFacing.EAST || rotation == EnumFacing.WEST) {
			return new AxisAlignedBB(0.25F, 0F, 0.0F, 0.75F, 0.43F, 1.0F);
		} else {
			return new AxisAlignedBB(0.0F, 0F, 0.25F, 1.0F, 0.43F, 0.75F);
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

    public String getSpiritNameOrNull(ItemStack itemStack) {
		return hasPlayer(itemStack) ? "Zombie" : getSpiritNameOrNullFromNBTTag(itemStack.getTagCompound());
    }

	private String getSpiritNameOrNullFromNBTTag(NBTTagCompound tag) {
		return TileBoxOfEternalClosure.getSpiritNameOrNullFromNBTTag(tag);
	}
    
    /**
     * Put a vengeance swarm inside the given box.
     * @param itemStack The box.
     */
    public static void setVengeanceSwarmContent(ItemStack itemStack) {
    	NBTTagCompound tag = new NBTTagCompound();
    	NBTTagCompound spiritTag = new NBTTagCompound();

		VengeanceSpiritData spiritData = new VengeanceSpiritData();
		spiritData.setSwarm(true);
		spiritData.setRandomSwarmTier(RANDOM);
		spiritData.writeNBT(spiritTag);

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

		VengeanceSpiritData spiritData = new VengeanceSpiritData();
		spiritData.setPlayerId(playerId.toString());
		spiritData.setPlayerName(FORGOTTEN_PLAYER);
		tag.setString(TileBoxOfEternalClosure.NBTKEY_PLAYERID, spiritData.getPlayerId());
		tag.setString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME, spiritData.getPlayerName());
		spiritData.writeNBT(spiritTag);

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
			String id = getSpiritNameOrNull(itemStack);
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
    public void neighborChanged(IBlockState blockState, World world, BlockPos blockPos, Block block) {
        if(!canPlaceBlockAt(world, blockPos)) {
        	dropBlockAsItem(world, blockPos, blockState, 0);
        	world.setBlockToAir(blockPos);
        }
        super.neighborChanged(blockState, world, blockPos, block);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(world.getTileEntity(blockPos) != null) {
	    	TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
			if(tile.isClosed()) {
				tile.open();
	    		return true;
	    	}
    	}
    	return super.onBlockActivated(world, blockPos, state, entityplayer, hand, heldItem, side, hitX, hitY, hitZ);
    }

	private float randomFloat(Random random, float min, float delta) {
		return min + random.nextFloat() * delta;
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
            if(tile.hasSpirit()) {
                return 15;
            }
        }
        return super.getComparatorInputOverride(blockState, world, blockPos);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState blockState, World worldObj, RayTraceResult target, ParticleManager particleManager) {
		if(target != null) {
			RenderHelpers.addBlockHitEffects(particleManager, worldObj, Blocks.OBSIDIAN.getDefaultState(), target.getBlockPos(), target.sideHit);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager particleManager) {
		RenderHelpers.addBlockHitEffects(particleManager, world, Blocks.OBSIDIAN.getDefaultState(), pos, EnumFacing.UP);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addLandingEffects(IBlockState blockState, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
		RenderHelpers.addBlockHitEffects(Minecraft.getMinecraft().effectRenderer, worldObj, Blocks.OBSIDIAN.getDefaultState(), blockPosition, EnumFacing.UP);
		return true;
	}
}
