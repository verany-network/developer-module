package net.verany.developer;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.wrapper.Wrapper;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.developer.commands.DevCommand;
import net.verany.developer.listener.CloudNetChannelListener;
import net.verany.developer.listener.PlayerQuitListener;
import net.verany.developer.tasks.UpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
@Setter
@VeranyModule(name = "Developer", maxRounds = -1, prefix = "", version = "2021.3.1", authors = {"tylix"}, user = "tylix", host = "159.69.63.105", password = "RxNqA18HB56SS7GW", databases = {"developer", "network"})
public class Developer extends VeranyProject {

    public static Developer INSTANCE;

    private long lastPlayerOnline = System.currentTimeMillis();

    @Override
    public void onEnable() {
        INSTANCE = this;

        Verany.loadModule(this);

        init();
    }

    @Override
    public void init() {
        super.init();

        CloudNetDriver.getInstance().getEventManager().registerListener(new CloudNetChannelListener());

        this.getCommand("dev").setExecutor(new DevCommand());

        if(getServiceProperties().contains("uuid")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    CloudNetDriver.getInstance().getMessenger().sendChannelMessage("dev_server", "online", JsonDocument.newDocument().append("uuid", getServiceProperties().get("uuid", UUID.class)));
                }
            }.runTask(this);
            Verany.addTask(new UpdateTask(1000));

            new PlayerQuitListener(this);

            Bukkit.getWhitelistedPlayers().add(Bukkit.getOfflinePlayer(getServiceProperties().get("uuid", UUID.class)));
        }
    }

    @Override
    public void onDisable() {
        if(getServiceProperties().contains("uuid"))
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("dev_server", "offline", JsonDocument.newDocument().append("uuid", getServiceProperties().get("uuid", UUID.class)));
        getConnection().disconnect();
    }

    public JsonDocument getServiceProperties() {
        return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getProperties();
    }
}
