package org.cyclops.evilcraft.core.blockentity;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.tickaction.TickComponent;

import java.util.List;

/**
 * A BlockEntity with Tank and Inventory that can tick.
 * It uses a list of {@link TickComponent} that are able to tick.
 * And these components will contain a collection of {@link ITickAction}
 * that can perform specific actions depending on the condition of the {@link TickComponent}.
 * @author rubensworks
 * @param <T> The subclass of {@link BlockEntityTankInventory}, will be in
 * most cases just the extension class.
 * @see TickComponent
 */
public abstract class BlockEntityTickingTankInventory<T extends BlockEntityTankInventory> extends BlockEntityTankInventory {

    private List<TickComponent<T, ITickAction<T>>> tickers;
    protected int currentState = -1;
    protected int previousState = -1;

    public BlockEntityTickingTankInventory(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState, int inventorySize, int stackSize, int tankSize, Fluid acceptedFluid) {
        super(type, blockPos, blockState, inventorySize, stackSize, tankSize, acceptedFluid);
        tickers = Lists.newArrayList();
    }

    /**
     * Add a new ticker.
     * @param ticker The ticker to (try) run every tick.
     * @return The index of the newly added ticker in the ticker list.
     */
    protected int addTicker(TickComponent<T, ITickAction<T>> ticker) {
        tickers.add(ticker);
        return tickers.size() - 1;
    }

    /**
     * Get the tickers this TileEntity has.
     * @return List of added tickers.
     */
    public List<TickComponent<T, ITickAction<T>>> getTickers() {
        return this.tickers;
    }

    @Override
    public void read(CompoundTag data) {
        super.read(data);
        currentState = data.getInt("currentState");
        ListTag tickerList = data.getList("tickers", 10);
        for(int i = 0; i < tickers.size(); i++) {
            TickComponent<T, ITickAction<T>> ticker = tickers.get(i);
            if(tickerList.size() > i) {
                CompoundTag tag = tickerList.getCompound(i);
                ticker.setTick(tag.getInt("tick"));
                ticker.setRequiredTicks(tag.getFloat("requiredTicks"));
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        data.putInt("currentState", currentState);
        ListTag tickerList = new ListTag();
        for(TickComponent<T, ITickAction<T>> ticker : tickers) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("tick", ticker.getTick());
            tag.putFloat("requiredTicks", ticker.getRequiredTicks());
            tickerList.add(tag);
        }
        data.put("tickers", tickerList);
    }

    protected boolean hasJustWorked() {
        return false;
    }

    /**
     * Get the new (numerical) state for this tile entity.
     * @return The new state.
     */
    public abstract int getNewState();
    /**
     * What needs to happen when the (numerical) state is changed.
     */
    public abstract void onStateChanged();

    /**
     * Get the current (numerical) state.
     * @return The current tile entity state.
     */
    public int getCurrentState() {
        return currentState;
    }

    public static class TickerServer<T extends BlockEntityTickingTankInventory<T>> extends BlockEntityTickerDelayed<T> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, T blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            // Update tickers
            boolean redstone = level.hasNeighborSignal(pos);
            for(TickComponent<T, ITickAction<T>> ticker : blockEntity.getTickers()) {
                if(!(ticker.isRedstoneDisableable() && redstone)) {
                    ticker.tick(blockEntity.getInventory().getItem(ticker.getSlot()), ticker.getSlot());
                }
            }

            // Update state server->clients.
            int newState = blockEntity.getNewState();
            if (newState != blockEntity.currentState) {
                blockEntity.currentState = newState;
                blockEntity.onStateChanged();
            }
        }
    }

    public static class TickerClient<T extends BlockEntityTickingTankInventory<T>> extends BlockEntityTickerDelayed<T> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, T blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            // Update internal state for client.
            if (blockEntity.previousState != blockEntity.currentState) {
                blockEntity.previousState = blockEntity.currentState;
                blockEntity.onStateChanged();
            }
        }
    }
}
