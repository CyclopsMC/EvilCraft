package evilcraft.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.common.config.Configuration;
import evilcraft.api.Helpers;
import evilcraft.api.config.ConfigProperty;

/**
 * Command that can define {@link ConfigProperty}.
 * @author rubensworks
 *
 */
public class CommandConfigSet extends CommandEvilCraft {
    
    private ConfigProperty config;
    private String name;
    /**
     * The configuration file that is used in this mod.
     */
    public static Configuration CONFIGURATION;

    /**
     * Make a new command for a {@link ConfigProperty}.
     * @param name The identifier.
     * @param config The config to be defined.
     */
    public CommandConfigSet(String name, ConfigProperty config) {
        this.name = name;
        this.config = config;
    }
    
    @Override
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(name);
        return list;
    }
    
    @Override
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = new HashMap<String, ICommand>();
        return map;
    }
    
    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if(astring.length == 0 || astring.length > 1) {
            icommandsender.addChatMessage(new ChatComponentText("Please define one new value."));
        } else {
            Object newValue = Helpers.tryParse(astring[0], config.getValue());
            if(newValue != null) {
                CONFIGURATION.load();
                config.setValue(newValue);
                config.save(CONFIGURATION, true);
                CONFIGURATION.save();
                icommandsender.addChatMessage(new ChatComponentText("Updated "+name+" to: "+newValue.toString() + " (not updated in config file)"));
                // TODO: Why not updated in config? Config not changeable at runtime?
            } else {
                icommandsender.addChatMessage(new ChatComponentText("Invalid new value."));
            }
        }
    }

}
