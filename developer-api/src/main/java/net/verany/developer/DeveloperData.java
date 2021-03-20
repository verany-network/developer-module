package net.verany.developer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class DeveloperData {

    private final UUID uniqueId;
    private final boolean hasService;
    private final int memory;
    private final int maxMemory;
    private ServiceData serviceData;

}
