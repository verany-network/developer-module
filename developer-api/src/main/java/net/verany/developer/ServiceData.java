package net.verany.developer;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.verany.api.Verany;
import net.verany.developer.tasks.ServiceTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@Getter
@Setter
public class ServiceData {

    private final String id = Verany.generate(10);
    private boolean online;
    private boolean starting;
    private boolean staticService;
    private ServiceInfoSnapshot infoSnapshot;
    private final List<String> plugins = new ArrayList<>();
    private final List<ServiceTask> tasks = new ArrayList<>();

}
