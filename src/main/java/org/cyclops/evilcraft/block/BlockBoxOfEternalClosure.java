package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritData;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * A box that can hold beings from higher dimensions.
 * @author rubensworks
 *
 */
public class BlockBoxOfEternalClosure extends BlockTile implements IInformationProvider, IBlockRarityProvider {

	public static final String FORGOTTEN_PLAYER = "Forgotten Player";
	private static final int LIGHT_LEVEL = 6;

	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

	public static final VoxelShape SHAPE_EW = Block.makeCuboidShape(0.25F * 16F, 0F, 0.0F, 0.75F * 16F, 0.43F * 16F, 1.0F * 16F);
	public static final VoxelShape SHAPE_NS = Block.makeCuboidShape(0.0F, 0F, 0.25F * 16F, 1.0F * 16F, 0.43F * 16F, 0.75F * 16F);

	public static ItemStack boxOfEternalClosureFilled;

	public BlockBoxOfEternalClosure(Block.Properties properties) {
        super(properties, TileBoxOfEternalClosure::new);

		this.setDefaultState(this.stateContainer.getBaseState()
				.with(FACING, Direction.NORTH));

		MinecraftForge.EVENT_BUS.register(this);
    }

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction rotation = state.get(FACING);
		return rotation == Direction.EAST || rotation == Direction.WEST ? SHAPE_EW : SHAPE_NS;
	}
    
    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
    	return BlockRenderType.MODEL;
    }

    public static EntityType<?> getSpiritType(ItemStack itemStack) {
		return hasPlayer(itemStack) ? EntityType.ZOMBIE : getSpiritTypeOrNull(itemStack.getTag());
    }

    @Nullable
	public static EntityType<?> getSpiritTypeOrNull(@Nullable CompoundNBT tag) {
		return TileBoxOfEternalClosure.getSpiritType(tag);
	}
    
    /**
     * Put a vengeance swarm inside the given box.
     * @param itemStack The box.
     */
    public static void setVengeanceSwarmContent(ItemStack itemStack) {
    	CompoundNBT tag = new CompoundNBT();
    	CompoundNBT spiritTag = new CompoundNBT();

		EntityVengeanceSpiritData spiritData = new EntityVengeanceSpiritData();
		spiritData.setSwarm(true);
		spiritData.setRandomSwarmTier(new Random());
		spiritData.writeNBT(spiritTag);

		tag.put(TileBoxOfEternalClosure.NBTKEY_SPIRIT, spiritTag);
		itemStack.setTag(tag);
    }

	/**
	 * Put a player inside the given box.
	 * @param itemStack The box.
	 * @param playerId The player id to set.
	 */
	public static void setPlayerContent(ItemStack itemStack, UUID playerId) {
		CompoundNBT tag = new CompoundNBT();
		CompoundNBT spiritTag = new CompoundNBT();

		EntityVengeanceSpiritData spiritData = new EntityVengeanceSpiritData();
		spiritData.setPlayerId(playerId.toString());
		spiritData.setPlayerName(FORGOTTEN_PLAYER);
		tag.putString(TileBoxOfEternalClosure.NBTKEY_PLAYERID, spiritData.getPlayerId());
		tag.putString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME, spiritData.getPlayerName());
		spiritData.writeNBT(spiritTag);

		tag.put(TileBoxOfEternalClosure.NBTKEY_SPIRIT, spiritTag);
		itemStack.setTag(tag);
	}

	public static String getPlayerName(ItemStack itemStack) {
		if(itemStack.hasTag() && itemStack.getTag().contains(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME, Constants.NBT.TAG_STRING)) {
			return itemStack.getTag().getString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME);
		}
		return "";
	}

	public static String getPlayerId(ItemStack itemStack) {
		if(itemStack.hasTag() && itemStack.getTag().contains(TileBoxOfEternalClosure.NBTKEY_PLAYERID, Constants.NBT.TAG_STRING)) {
			return itemStack.getTag().getString(TileBoxOfEternalClosure.NBTKEY_PLAYERID);
		}
		return "";
	}

	public static boolean hasPlayer(ItemStack itemStack) {
		return !getPlayerId(itemStack).isEmpty();
	}

	@Override
	public ITextComponent getInfo(ItemStack itemStack) {
		ITextComponent content = new TranslationTextComponent("general." + Reference.MOD_ID + ".info.empty")
				.applyTextStyle(TextFormatting.ITALIC);
		if(hasPlayer(itemStack)) {
			content = new StringTextComponent(getPlayerName(itemStack));
		} else {
			content = getSpiritType(itemStack).getName();
		}
		return new TranslationTextComponent(getTranslationKey() + ".info.content")
				.applyTextStyle(TextFormatting.BOLD)
				.appendSibling(content);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void provideInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {

	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return BlockHelpers.doesBlockHaveSolidTopSurface(worldIn, pos);
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!worldIn.isAreaLoaded(pos, 1))
			return;
		if (!state.isValidPosition(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		}
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (!stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult blockRayTraceResult) {
		return TileHelpers.getSafeTile(worldIn, pos, TileBoxOfEternalClosure.class)
				.map(tile -> {
					if (tile.isClosed()) {
						tile.open();
						return ActionResultType.SUCCESS;
					}
					return super.onBlockActivated(state, worldIn, pos, player, handIn, blockRayTraceResult);
				})
				.orElseGet(() -> super.onBlockActivated(state, worldIn, pos, player, handIn, blockRayTraceResult));
	}

	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return TileHelpers.getSafeTile(world, pos, TileBoxOfEternalClosure.class)
				.map(tile -> tile.getLidAngle() > 0 ? LIGHT_LEVEL : super.getLightValue(state, world, pos))
				.orElse(0);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this));
		items.add(BlockBoxOfEternalClosure.boxOfEternalClosureFilled);
	}

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return hasPlayer(itemStack) ? Rarity.RARE : Rarity.UNCOMMON;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos blockPos) {
        if(world.getTileEntity(blockPos) != null) {
            TileBoxOfEternalClosure tile = (TileBoxOfEternalClosure) world.getTileEntity(blockPos);
            if(tile.hasSpirit()) {
                return 15;
            }
        }
        return super.getComparatorInputOverride(blockState, world, blockPos);
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean addHitEffects(BlockState blockState, World world, RayTraceResult target, ParticleManager particleManager) {
		if(target != null) {
			RenderHelpers.addBlockHitEffects(particleManager, world, Blocks.OBSIDIAN.getDefaultState(), ((BlockRayTraceResult) target).getPos(), ((BlockRayTraceResult) target).getFace());
		}
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager particleManager) {
		RenderHelpers.addBlockHitEffects(particleManager, world, Blocks.OBSIDIAN.getDefaultState(), pos, Direction.UP);
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean addLandingEffects(BlockState blockState, ServerWorld world, BlockPos blockPosition, BlockState iblockstate, LivingEntity entity, int numberOfParticles) {
		RenderHelpers.addBlockHitEffects(Minecraft.getInstance().particles, world, Blocks.OBSIDIAN.getDefaultState(), blockPosition, Direction.UP);
		return true;
	}
}
