package com.cloudcraftgaming.module.command;

import com.cloudcraftgaming.internal.calendar.EventCreator;
import com.cloudcraftgaming.internal.calendar.EventMessageFormatter;
import com.cloudcraftgaming.utils.Message;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nova Fox on 1/3/2017.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCal
 */
@SuppressWarnings("Duplicates")
public class EventCommand implements ICommand {
    @Override
    public String getCommand() {
        return "event";
    }

    @Override
    public Boolean issueCommand(String[] args, MessageReceivedEvent event, IDiscordClient client) {
        if (args.length < 1) {
            Message.sendMessage("Please specify the function you would like to execute.", event, client);
        } else if (args.length == 1) {
            String function = args[0];
            if (function.equalsIgnoreCase("create")) {
                Message.sendMessage("Please specify a name for the event!", event, client);
            }
        } else if (args.length == 2) {
            String function = args[0];
            String guildId = event.getMessage().getGuild().getID();
            if (function.equalsIgnoreCase("create")) {
                if (EventCreator.getCreator().hasPreEvent(guildId)) {
                    Message.sendMessage("Event Creator already started!", event, client);
                } else {
                    EventCreator.getCreator().init(event, args[1]);
                    Message.sendMessage("Event Creator initiated! Please specify event summery.", event, client);
                }
            } else if (function.equalsIgnoreCase("date")) {
                if (EventCreator.getCreator().hasPreEvent(guildId)) {
                    String dateRaw = args[1].trim();
                    if (dateRaw.length() > 10) {
                        try {
                            //Do a lot of date shuffling to get to proper formats and shit like that.
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
                            Date dateObj = sdf.parse(dateRaw);
                            DateTime dateTime = new DateTime(dateObj);
                            EventDateTime eventDateTime = new EventDateTime();
                            eventDateTime.setDateTime(dateTime);

                            //Date shuffling done, now actually apply all that damn stuff here.
                            EventCreator.getCreator().getPreEvent(guildId).setDateTime(eventDateTime);
                            Message.sendMessage("Event date (yyyy/MM/dd) set to: " + EventMessageFormatter.getHumanReadableDate(eventDateTime), event, client);
                            Message.sendMessage("Event start time (HH:mm, military) set to: " + EventMessageFormatter.getHumanReadableTime(eventDateTime), event, client);
                        } catch (ParseException e) {
                            Message.sendMessage("Invalid Date & Time specified!", event, client);
                        }
                    } else {
                        try {
                            //Do a lot of date shuffling to get to proper formats and shit like that.
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            Date dateObj = sdf.parse(dateRaw);
                            DateTime dateTime = new DateTime(dateObj);
                            EventDateTime eventDateTime = new EventDateTime();
                            eventDateTime.setDate(dateTime);

                            //Date shuffling done, now actually apply all that damn stuff here.
                            EventCreator.getCreator().getPreEvent(guildId).setDateTime(eventDateTime);
                            Message.sendMessage("Event date (yyyy/MM/dd) set to: " + EventMessageFormatter.getHumanReadableDate(eventDateTime), event, client);
                        } catch (ParseException e) {
                            Message.sendMessage("Invalid Date specified!", event, client);
                        }
                    }
                } else {
                    Message.sendMessage("Event Creator has not been initialized! Create an event to initialize!", event, client);
                }
            } else if (function.equalsIgnoreCase("summery")) {
                if (EventCreator.getCreator().hasPreEvent(guildId)) {
                    String content = getContent(args);
                    EventCreator.getCreator().getPreEvent(guildId).setSummery(content);
                    Message.sendMessage("Event summery set to: '" + content + "'", event, client);
                    Message.sendMessage("Please specify the event description!", event, client);
                } else {
                    Message.sendMessage("Event Creator has not been initialized! Create an event to initialize!", event, client);
                }
            } else if (function.equalsIgnoreCase("description")) {
                if (EventCreator.getCreator().hasPreEvent(guildId)) {
                    String content = getContent(args);
                    EventCreator.getCreator().getPreEvent(guildId).setDescription(content);
                    Message.sendMessage("Event description set to: '" + content + "'", event, client);
                    Message.sendMessage("Please specify one of the following: ", event, client);
                    Message.sendMessage("For an ALL DAY event, please specify date in yyyy/MM/dd format!", event, client);
                    Message.sendMessage("For a TIMED EVENT event, please specify date & starting time(military) in yyyy/MM/dd-HH:mm:ss format!", event, client);
                } else {
                    Message.sendMessage("Event Creator has not been initialized! Create an event to initialize!", event, client);
                }
            } else {
                Message.sendMessage("Invalid function!", event, client);
            }
        } else {
            String function = args[0];
            String guildId = event.getMessage().getGuild().getID();
            if (function.equalsIgnoreCase("create")) {
                Message.sendMessage("Event name can only be one(1) word!", event, client);
            } else if (function.equalsIgnoreCase("summery")) {
                if (EventCreator.getCreator().hasPreEvent(guildId)) {
                    String content = getContent(args);
                    EventCreator.getCreator().getPreEvent(guildId).setSummery(content);
                    Message.sendMessage("Event summery set to: '" + content + "'", event, client);
                    Message.sendMessage("Please specify the event description!", event, client);
                } else {
                    Message.sendMessage("Event Creator has not been initialized! Create an event to initialize!", event, client);
                }
            } else if (function.equalsIgnoreCase("description")) {
                if (EventCreator.getCreator().hasPreEvent(guildId)) {
                    String content = getContent(args);
                    EventCreator.getCreator().getPreEvent(guildId).setDescription(content);
                    Message.sendMessage("Event description set to: '" + content + "'", event, client);
                    Message.sendMessage("Please specify one of the following: ", event, client);
                    Message.sendMessage("For an ALL DAY event, please specify date in yyyy/MM/dd format!", event, client);
                    Message.sendMessage("For a TIMED EVENT event, please specify date & starting time(military) in yyyy/MM/dd-HH:mm:ss format!", event, client);
                } else {
                    Message.sendMessage("Event Creator has not been initialized! Create an event to initialize!", event, client);
                }
            }
        }

        return false;
    }

    private String getContent(String[] args) {
        String content = "";
        for (int i = 1; i < args.length; i++) {
            content = content + args[i] + " ";
        }
        return content.trim();
    }
}
