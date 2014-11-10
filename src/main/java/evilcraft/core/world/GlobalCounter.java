package evilcraft.core.world;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import java.util.Map;

/**
 * Global counter that is shared over all dimensions, persisted, and consistent over server and clients.
 * @author rubensworks
 */
public class GlobalCounter {

    private static GlobalCounter _instance = null;

    private Map<String, Integer> counters = Maps.newHashMap();

    private GlobalCounter() {

    }

    /**
     * @return The unique instance.
     */
    public static GlobalCounter getInstance() {
        if(_instance == null) {
            _instance = new GlobalCounter();
        }
        return _instance;
    }

    /**
     * Read the counters.
     * @param tag The tag to read from.
     */
    public void readFromNBT(NBTTagCompound tag) {
        if(tag != null) {
            NBTTagList list = tag.getTagList("counters", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound subTag = list.getCompoundTagAt(i);
                counters.put(subTag.getString("key"), subTag.getInteger("value"));
            }
        }
    }

    /**
     * Write the counters.
     * @param tag The tag to write to.
     */
    public void writeToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for(Map.Entry<String, Integer> entry : counters.entrySet()) {
            NBTTagCompound subTag = new NBTTagCompound();
            subTag.setString("key", entry.getKey());
            subTag.setInteger("value", entry.getValue());
            list.appendTag(subTag);
        }
        tag.setTag("counters", list);
    }

    /**
     * Get the next counter value for the given key.
     * @param key the key for the counter.
     * @return The next counter value.
     */
    public int getNext(String key) {
        int next = 0;
        if(counters.containsKey(key)) {
            next = counters.get(key);
        }
        counters.put(key, next + 1);
        return next;
    }

    /**
     * Reset the stored counters.
     */
    public void reset() {
        counters = Maps.newHashMap();
    }

    /**
     * Data holder for the global counter data.
     */
    public static class GlobalCounterData extends WorldSavedData {

        public static String KEY = "GlobalCounterData";

        public NBTTagCompound tag;

        /**
         * Make a new instance.
         * @param key The key for the global counter data.
         */
        public GlobalCounterData(String key) {
            super(key);
            this.tag = new NBTTagCompound();
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            this.tag = tag.getCompoundTag(KEY);
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setTag(KEY, this.tag);
        }

    }

}
