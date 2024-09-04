package me.scarday.whitelistManager.discord.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import lombok.val;
import me.scarday.whitelistManager.WhitelistManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.necko1.whitelist.api.WhitelistAPI;
import org.necko1.whitelist.data.WhitelistData;

import java.util.stream.Collectors;

@Command(name = "whitelist")
public class WhitelistCommand {
    WhitelistAPI whitelistAPI;

    public WhitelistCommand(WhitelistManager plugin) {
        this.whitelistAPI = plugin.getWhitelist();
    }

    @Execute(name = "add")
    public void add(@Context User user, @Context SlashCommandInteractionEvent event,
                        @Arg(value = "nickname") String userName
    ) {
        boolean added = whitelistAPI.add(userName);

        if (!added) {
            event.reply(":eyes:Похоже что игрок итак был добавлен!").queue();
            return;
        }

        event.reply(":white_check_mark:Игрок " + userName + " успешно добавлен!").queue();
        whitelistAPI.save(true);
    }

    @Execute(name = "remove")
    public void remove(@Context User user, @Context SlashCommandInteractionEvent event,
                       @Arg(value = "nickname") String userName) {
        boolean removed = whitelistAPI.remove(userName);

        if (!removed) {
            event.reply(":eyes:Похоже игрока нету в вайтлисте!").queue();
            return;
        }

        event.reply(":white_check_mark:Игрок " + userName + " успешно удалён!").queue();
        whitelistAPI.save(true);
    }

    @Execute(name = "list")
    public void list(@Context User user, @Context SlashCommandInteractionEvent event) {
        val list = whitelistAPI.getList();

        val builder = new StringBuilder();

        if (list.isEmpty()) {
            event.reply(":eyes:Игроков нету!").queue();
            return;
        }

        builder.append(":information_source:Всего игроков (").append(list.size()).append("): ");

        builder.append(list.stream()
                .map(WhitelistData::getName)
                .collect(Collectors.joining(", "))
        );

        event.reply(builder.toString()).queue();
    }
}
