package evilcraft.command;

import net.minecraft.command.ICommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Command for doing some recursion.
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
