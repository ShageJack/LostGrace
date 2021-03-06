package shagejack.lostgrace.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import shagejack.lostgrace.contents.grace.GlobalGraceSet;
import shagejack.lostgrace.contents.grace.Grace;

import java.util.Set;

public class CommandListAllGraces {

    public CommandListAllGraces(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("listallgraces").requires(cs -> cs.hasPermission(2)).executes(this::listAllGraces));
    }

    private int listAllGraces(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Set<Grace> graceSet = GlobalGraceSet.getGraceSet();

        int count = 0;

        for (Grace grace : graceSet) {
            context.getSource().sendSuccess(new TextComponent("[" + count + "]" + grace.toString()), true);
            count++;
        }

        context.getSource().sendSuccess(new TextComponent(count + " graces found."), true);

        return 1;
    }

}