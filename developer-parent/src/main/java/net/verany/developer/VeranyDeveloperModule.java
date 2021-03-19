package net.verany.developer;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ProcessConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceEnvironmentType;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.developer.task.CheckTask;
import net.verany.developer.tasks.ServiceTask;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@VeranyModule(name = "Developer", version = "2021.3.1", authors = {"tylix"}, user = "tylix", host = "159.69.63.105", password = "RxNqA18HB56SS7GW", databases = {"developer"})
public class VeranyDeveloperModule extends VeranyProject {

    public static VeranyDeveloperModule INSTANCE;

    @Override
    public void enable() {
        INSTANCE = this;

        Verany.loadModule(this);

        init();
    }

    private void init() {
        Document testData = getConnection().getCollection("developers").find().first();
        if(testData == null)
            getConnection().getCollection("developers").insertOne(new Gson().fromJson(new Gson().toJson(new DeveloperData("tylix", true, 1014, new ServiceData())), Document.class));

        Verany.addTask(new CheckTask(1000, this));
    }

    @Override
    public void disable() {
        getConnection().disconnect();
    }

    public void runTask(DeveloperData data, ServiceTask task) {
        switch (task.getTaskType()) {
            case START: {
                if (data.getServiceData().isOnline()) return;
                ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceFactory().createCloudService(
                        data.getName(),
                        "jvm",
                        true,
                        data.getServiceData().isStaticService(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        Arrays.asList(data.getName(), "Global"),
                        new ProcessConfiguration(
                                ServiceEnvironmentType.MINECRAFT_SERVER,
                                data.getMemory(),
                                new ArrayList<>()
                        ),
                        JsonDocument.newDocument(),
                        null
                );
                serviceInfoSnapshot.provider().start();
                data.getServiceData().setInfoSnapshot(serviceInfoSnapshot);
                data.getServiceData().setStarting(true);
                data.getServiceData().setOnline(true);
                break;
            }
            case STOP: {
                if (!data.getServiceData().isOnline()) return;
                data.getServiceData().getInfoSnapshot().provider().stop();
                data.getServiceData().setOnline(false);
                break;
            }
        }
        data.getServiceData().getTasks().remove(task);
        updateData(data);
    }

    public void updateData(DeveloperData data) {
        getConnection().getCollection("developers").replaceOne(new BasicDBObject("name", data.getName()), new Gson().fromJson(new Gson().toJson(data), Document.class));
    }
}
