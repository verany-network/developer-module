package net.verany.developer;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
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

import java.util.*;

@VeranyModule(name = "Developer", version = "2021.3.1", authors = {"tylix"}, user = "tylix", host = "159.69.63.105", password = "RxNqA18HB56SS7GW", databases = {"developer", "network"})
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
        if (testData == null)
            getConnection().getCollection("developers").insertOne(new Gson().fromJson(new Gson().toJson(new DeveloperData(getUniqueId("tylix"), false, 1024, 1024, new ServiceData())), Document.class));


        Verany.addTask(new CheckTask(1000, this));
    }

    @Override
    public void disable() {
        getConnection().disconnect();
    }

    public void runTask(DeveloperData data, ServiceTask task) {
        executeTask(data, task.getTaskType());

        data.getServiceData().getTasks().remove(task);
        updateData(data);
    }

    private void executeTask(DeveloperData data, ServiceTask.TaskType taskType) {
        switch (taskType) {
            case START: {
                if (data.getServiceData().isOnline()) return;
                ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceFactory().createCloudService(
                        getName(data.getUniqueId()),
                        "jvm",
                        true,
                        data.getServiceData().isStaticService(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        Arrays.asList(getName(data.getUniqueId()), "Global", "Devs"),
                        new ProcessConfiguration(
                                ServiceEnvironmentType.MINECRAFT_SERVER,
                                data.getMemory(),
                                new ArrayList<>()
                        ),
                        JsonDocument.newDocument().append("uuid", data.getUniqueId()),
                        null
                );
                serviceInfoSnapshot.provider().start();
                CloudNetDriver.getInstance().getMessenger().sendChannelMessage("dev_module", "start", JsonDocument.newDocument().append("uuid", data.getUniqueId()));
                data.getServiceData().setInfoSnapshot(serviceInfoSnapshot);
                data.getServiceData().setStarting(true);
                break;
            }
            case STOP: {
                if (!data.getServiceData().isOnline()) return;
                data.getServiceData().getInfoSnapshot().provider().stop();
                data.getServiceData().setInfoSnapshot(null);
                data.getServiceData().setOnline(false);
                break;
            }
            case RESTART: {
                executeTask(data, ServiceTask.TaskType.STOP);
                executeTask(data, ServiceTask.TaskType.START);
                break;
            }
        }
    }

    public void updateData(DeveloperData data) {
        getConnection().getCollection("developers").replaceOne(new BasicDBObject("uniqueId", data.getUniqueId().toString()), new Gson().fromJson(new Gson().toJson(data), Document.class));
    }

    public String getName(UUID uuid) {
        return getConnection().getCollection("network", "players").find().filter(Filters.eq("uuid", uuid.toString())).first().getString("name");
    }

    public UUID getUniqueId(String name) {
        return UUID.fromString(getConnection().getCollection("network", "players").find().filter(Filters.eq("name", name)).first().getString("uuid"));
    }

    public DeveloperData getDevData(UUID developer) {
        return new Gson().fromJson(getConnection().getCollection("developers").find().filter(Filters.eq("uniqueId", developer.toString())).first().toJson(), DeveloperData.class);
    }

    public void createDeveloper(DeveloperData data) {
        VeranyDeveloperModule.INSTANCE.getConnection().getCollection("developers").insertOne(new Gson().fromJson(new Gson().toJson(data), Document.class));
    }
}
