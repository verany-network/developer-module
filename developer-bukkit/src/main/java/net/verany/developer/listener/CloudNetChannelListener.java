package net.verany.developer.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStartEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStopEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.verany.api.Verany;
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
                }
                break;
            }
        }
    }

}
