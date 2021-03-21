package net.verany.developer.tasks;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.verany.api.task.AbstractTask;
import net.verany.developer.Developer;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UpdateTask extends AbstractTask {

    public UpdateTask(long waitTime) {
        super(waitTime);
    }

    @Override
    public void run() {
        if (Developer.INSTANCE.getLastPlayerOnline() + TimeUnit.MINUTES.toMillis(10) < System.currentTimeMillis())
            Bukkit.shutdown();

        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("dev_server", "serviceInfoSnapshot", JsonDocument.newDocument().append("uuid", Developer.INSTANCE.getServiceProperties().get("uuid", UUID.class)).append("infoSnapshot", Wrapper.getInstance().getCurrentServiceInfoSnapshot()));
    }
}
