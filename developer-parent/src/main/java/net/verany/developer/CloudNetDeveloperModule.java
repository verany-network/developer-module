package net.verany.developer;

import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.module.NodeCloudNetModule;
import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;

public class CloudNetDeveloperModule extends NodeCloudNetModule {

    public static CloudNetDeveloperModule INSTANCE;
    private VeranyProject module;

    public CloudNetDeveloperModule() {
        INSTANCE = this;
    }

    @ModuleTask(order = 127, event = ModuleLifeCycle.STARTED)
    public void init() {
        module = new VeranyDeveloperModule();
        module.enable();
    }

    @ModuleTask(order = 35, event = ModuleLifeCycle.STOPPED)
    public void disable() {
        module.disable();
    }
}
