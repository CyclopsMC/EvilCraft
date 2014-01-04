package evilcraft.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.command.ICommand;
import evilcraft.api.config.ExtendedConfig.ConfigProperty;

public class CommandConfig extends CommandEvilCraft{
    
    private static final String NAME = "config";
    
    public static final Map<String, ConfigProperty> PROPERTIES = new HashMap<String, ConfigProperty>();
    
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(NAME);
        return list;
    }
    
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = new HashMap<String, ICommand>();
        for(Entry<String, ConfigProperty> entry : PROPERTIES.entrySet()) {
            String name = entry.getValue().getName();
            map.put(name, new CommandConfigSet(name, entry.getValue()));
        }
        return map;
    }
}
