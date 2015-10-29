package evilcraft.core;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Level;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Map;

/**
 * Handler for {@link net.minecraftforge.fml.common.event.FMLInterModComms}s.
 * TODO: abstract this to cyclops
 * @author rubensworks
 */
public class IMCHandler {

    private Map<String, IIMCAction> actions = Maps.newHashMap();

    /**
     * Register a new action for messages.
     * @param key The action key which will be used to distinguish messages.
     * @param action The action to execute when messages for that type are received.
     */
    public void registerAction(String key, IIMCAction action) {
        actions.put(key, action);
    }

    public void handle(FMLInterModComms.IMCEvent event) {
        for(FMLInterModComms.IMCMessage message : event.getMessages()) {
            handle(message);
        }
    }

    public void handle(FMLInterModComms.IMCMessage message) {
        EvilCraft.log(String.format("Handling IMC message from %s.", message.getSender()), Level.INFO);
        IIMCAction action = actions.get(message.key);
        if(action != null) {
            if(!action.handle(message)) {
                EvilCraft.log(String.format("The IMC message for key %s was rejected. " +
                        "It may have been incorrectly formatted or has resulted in an error.", message.key), Level.ERROR);
            }
        } else {
            EvilCraft.log(String.format("An IMC message with invalid key %s was received.", message.key), Level.ERROR);
        }
    }

    public static interface IIMCAction {

        /**
         * Handle the given message, corresponds to the given key used with registration.
         * @param message The message.
         * @return If the handling occured without any problems.
         */
        public boolean handle(FMLInterModComms.IMCMessage message);

    }

}
