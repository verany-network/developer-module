package net.verany.developer.task;

import com.google.gson.Gson;
import net.verany.api.module.VeranyProject;
import net.verany.api.task.AbstractTask;
import net.verany.developer.DeveloperData;
import net.verany.developer.ServiceData;
import net.verany.developer.VeranyDeveloperModule;
import net.verany.developer.tasks.ServiceTask;
import org.bson.Document;

public class CheckTask extends AbstractTask {

    private final VeranyProject project;

    public CheckTask(long waitTime, VeranyProject project) {
        super(waitTime);
        this.project = project;
    }

    @Override
    public void run() {
        for (Document document : project.getConnection().getCollection("developers").find()) {
            DeveloperData data = new Gson().fromJson(document.toJson(), DeveloperData.class);
            ServiceData serviceData = data.getServiceData();
            if (serviceData == null) continue;
            for (int i = 0; i < serviceData.getTasks().size(); i++) {
                ServiceTask task = serviceData.getTasks().get(i);
                VeranyDeveloperModule.INSTANCE.runTask(data, task);
            }
        }
    }
}
