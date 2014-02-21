package evilcraft.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import evilcraft.VersionStats;
import evilcraft.api.config.ConfigProperty;

/**
 * Command for showing current version.
 * @author rubensworks
 *
 */
public class CommandVersion extends CommandEvilCraft{
    
    private static final String NAME = "version";
    
    /**
     * Map with command identifier to {@link ConfigProperty} that can be configured.
     */
    public static final Map<String, ConfigProperty> PROPERTIES = new HashMap<String, ConfigProperty>();
    
    @Override
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(NAME);
        return list;
    }
    
    @Override
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = new HashMap<String, ICommand>();
        return map;
    }
    
    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
    	icommandsender.addChatMessage(new ChatComponentText(VersionStats.getVersion()));
    }
}
