package net.verany.developer.tasks;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.verany.api.task.AbstractTask;
import net.verany.developer.Developer;

import java.util.UUID;

public class UpdateTask extends AbstractTask {

    public UpdateTask(long waitTime) {
        super(waitTime);
    }

    @Override
    public void run() {
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("dev_server", "serviceInfoSnapshot", JsonDocument.newDocument().append("uuid", Developer.INSTANCE.getServiceProperties().get("uuid", UUID.class)).append("infoSnapshot", Wrapper.getInstance().getCurrentServiceInfoSnapshot()));
    }
}
