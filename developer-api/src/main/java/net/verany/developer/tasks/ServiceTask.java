package net.verany.developer.tasks;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ServiceTask {

    private final TaskType taskType;
    private final String data;

    public enum TaskType {
        START,
        STOP,
        RESTART
    }

}
