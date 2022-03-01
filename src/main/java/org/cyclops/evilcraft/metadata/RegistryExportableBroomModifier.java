package org.cyclops.evilcraft.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.metadata.IRegistryExportable;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;

import java.util.Map;

/**
 * Broom modifier recipe exporter.
 */
public class RegistryExportableBroomModifier implements IRegistryExportable {

    @Override
    public JsonObject export() {
        JsonObject root = new JsonObject();

        for (BroomModifier modifier : BroomModifiers.REGISTRY.getModifiers()) {
            JsonObject object = new JsonObject();
            JsonArray items = new JsonArray();
            for (Map.Entry<ItemStack, Float> entry : BroomModifiers.REGISTRY.getItemsFromModifier(modifier).entrySet()) {
                JsonObject item = new JsonObject();
                item.add("item", IRegistryExportable.serializeItemStack(entry.getKey()));
                item.addProperty("modifier", entry.getValue());
                items.add(item);
            }
            object.add("items", items);
            object.addProperty("name", modifier.getTranslationKey());
            root.add(Reference.MOD_ID + ":" + modifier.getName(), object);
        }

        return root;
    }

    @Override
    public String getName() {
        return "broom_modifier";
    }
}
