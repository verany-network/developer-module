package net.verany.developer.listener;

import com.google.gson.Gson;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import net.verany.developer.DeveloperData;
import net.verany.developer.ServiceData;
import net.verany.developer.VeranyDeveloperModule;
import org.bson.Document;

import java.util.UUID;

public class CloudNetChannelListener {

    @EventListener
    public void handle(ChannelMessageReceiveEvent event) {
        switch (event.getChannel()) {
            case "dev_server": {
                UUID dev = event.getData().get("uuid", UUID.class);
                DeveloperData data = VeranyDeveloperModule.INSTANCE.getDevData(dev);

                switch (event.getMessage()) {
                    case "online": {
                        data.getServiceData().setOnline(true);
                        data.getServiceData().setStarting(false);
                        VeranyDeveloperModule.INSTANCE.updateData(data);
                        break;
                    }
                    case "offline": {
                        data.getServiceData().setOnline(false);
                        data.getServiceData().setInfoSnapshot(null);
                        VeranyDeveloperModule.INSTANCE.updateData(data);
                        break;
                    }
                    case "serviceInfoSnapshot": {
                        ServiceInfoSnapshot serviceInfoSnapshot = event.getData().get("infoSnapshot", ServiceInfoSnapshot.class);
                        data.getServiceData().setInfoSnapshot(serviceInfoSnapshot);
                        VeranyDeveloperModule.INSTANCE.updateData(data);
                        break;
                    }
                }
                break;
            }
            case "hub_server": {

                break;
            }
            case "core": {
                UUID dev = event.getData().get("uuid", UUID.class);
                if (event.getMessage().equals("createDev")) {
                    if (VeranyDeveloperModule.INSTANCE.getDevData(dev) != null) return;
                    VeranyDeveloperModule.INSTANCE.createDeveloper(new DeveloperData(dev, false, 512, 1024, new ServiceData()));
                }
                break;
            }
        }
    }

}
