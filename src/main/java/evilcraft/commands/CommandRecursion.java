package evilcraft.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import evilcraft.core.config.ConfigProperty;

/**
 * Command for selecting {@link ConfigProperty}.
 * @author rubensworks
 *
 */
public class CommandRecursion extends CommandEvilCraft {
    
    private static final String NAME = "recursion";
    
    @Override
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(NAME);
        return list;
    }
    
    @Override
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = new HashMap<String, ICommand>();
        map.put(NAME, this);
        return map;
    }
}
