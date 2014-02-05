package evilcraft.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatMessageComponent;
import evilcraft.api.Helpers;

public class CommandEvilCraft implements ICommand {
    
    private static final String NAME = "evilcraft";
    
    public CommandEvilCraft() {
        
    }
    
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(NAME);
        list.add("evilCraft");
        list.add("EvilCraft");
        return list;
    }
    
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = new HashMap<String, ICommand>();
        map.put("config", new CommandConfig());
        return map;
    }
    
    private List<String> getSubCommands(String cmd) {
        List<String> completions = new LinkedList<String>();
        for(String full : getSubcommands().keySet()) {
            if(full.startsWith(cmd)) {
                completions.add(full);
            }
        }
        return completions;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        String possibilities = "";
        for(String full : getSubcommands().keySet()) {
            possibilities += full + " ";
        }
        return NAME + " " + possibilities;
    }

    @Override
    public List getCommandAliases() {
        return this.getAliases();
    }
    
    protected String[] shortenArgumentList(String[] astring) {
        String[] asubstring = new String[astring.length - 1];
        for(int i = 1; i < astring.length; i++) {
            asubstring[i - 1] = astring[i];
        }
        return asubstring;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if(astring.length == 0) {
            icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Invalid arguments."));
        } else {
            ICommand subcommand = getSubcommands().get(astring[0]);
            if(subcommand != null) {
                String[] asubstring = shortenArgumentList(astring);
                subcommand.processCommand(icommandsender, asubstring);
            } else {
                icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Invalid subcommand."));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
        return Helpers.isOp(icommandsender);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender,
            String[] astring) {
        if(astring.length != 0) {
            ICommand subcommand = getSubcommands().get(astring[0]);
            if(subcommand != null) {
                String[] asubstring = shortenArgumentList(astring);
                return subcommand.addTabCompletionOptions(icommandsender, asubstring);
            } else {
                return getSubCommands(astring[0]);
            }
        } else {
            return getSubCommands("");
        }
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {
        return false;
    }
    
}
