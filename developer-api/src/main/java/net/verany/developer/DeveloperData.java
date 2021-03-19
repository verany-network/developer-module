package net.verany.developer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DeveloperData {

    private final String name;
    private final boolean hasService;
    private final int memory;
    private ServiceData serviceData;

}
