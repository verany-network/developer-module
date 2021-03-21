package net.verany.developer.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStartEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStopEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.verany.api.Verany;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CloudNetChannelListener {

    @EventListener
    public void handle(CloudServiceStartEvent event) {
        for (IPlayerInfo playerInfo : Verany.getOnlinePlayers()) {
            Player player = playerInfo.getPlayer();
            if (player == null) continue;
            if (player.hasPermission("verany.service.notify")) {
                playerInfo.sendKey(playerInfo.getPrefix("VeranyCloud"), "service.started", new Placeholder("%service%", event.getServiceInfo().getServiceId().getName()));
            }
        }
    }

    @EventListener
    public void handle(CloudServiceStopEvent event) {
        for (IPlayerInfo playerInfo : Verany.getOnlinePlayers()) {
            Player player = playerInfo.getPlayer();
            if (player == null) continue;
            if (player.hasPermission("verany.service.notify")) {
                playerInfo.sendKey(playerInfo.getPrefix("VeranyCloud"), "service.stop", new Placeholder("%service%", event.getServiceInfo().getServiceId().getName()));
            }
        }
    }

    @EventListener
    public void handleChannelMessage(ChannelMessageReceiveEvent event) {
        switch (event.getChannel()) {
            case "dev_module": {
                if (event.getMessage().equals("start")) {
                    UUID dev = event.getData().get("uuid", UUID.class);
                    Player player = Bukkit.getPlayer(dev);
                    if (player == null) return;
                    player.sendMessage("Your server will now start.");
                } else if (event.getMessage().equals("offline")) {
                    UUID dev = event.getData().get("uuid", UUID.class);
                    Player player = Bukkit.getPlayer(dev);
                    if (player == null) return;
                    player.sendMessage("Your server has been stopped.");
                }
                break;
            }
            case "dev_server": {
                if (event.getMessage().equals("online")) {
                    UUID dev = event.getData().get("uuid", UUID.class);
                    Player player = Bukkit.getPlayer(dev);
                    if (player == null) return;
                    player.sendMessage("Your server is now online.");
                    Verany.getPlayer(player).sendMessage(new AbstractComponentBuilder("Click here to connect") {
                        @Override
                        public void onCreate() {
                            setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dev"));
                            setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to connect to your server").create()));
                        }
                    });
                }
                break;
            }
        }
    }

}
