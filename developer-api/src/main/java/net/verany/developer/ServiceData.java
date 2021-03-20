package net.verany.developer;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.Verany;
import net.verany.developer.tasks.ServiceTask;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServiceData {

    private final String id = Verany.generate(10);
    private boolean online = false;
    private boolean starting = false;
    private boolean staticService = true;
    private ServiceInfoSnapshot infoSnapshot;
    private final List<ServiceTask> tasks = new ArrayList<>();

}
