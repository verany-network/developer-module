package net.verany.developer.setup;

import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.common.logging.DefaultLogFormatter;
import de.dytanic.cloudnet.common.logging.IFormatter;
import de.dytanic.cloudnet.console.IConsole;
import de.dytanic.cloudnet.console.animation.questionlist.ConsoleQuestionListAnimation;
import de.dytanic.cloudnet.console.animation.questionlist.QuestionListEntry;
import de.dytanic.cloudnet.console.animation.questionlist.answer.QuestionAnswerTypeInt;
import de.dytanic.cloudnet.console.animation.questionlist.answer.QuestionAnswerTypeString;
import de.dytanic.cloudnet.console.log.ColouredLogFormatter;
import net.verany.developer.DeveloperData;
import net.verany.developer.ServiceData;
import net.verany.developer.VeranyDeveloperModule;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class DeveloperSetup {

    public DeveloperSetup() {
        IConsole console = CloudNet.getInstance().getConsole();
        IFormatter formatter = console.hasColorSupport() ? new ColouredLogFormatter() : new DefaultLogFormatter();
        ConsoleQuestionListAnimation animation = new ConsoleQuestionListAnimation(
                "Developer-Setup",
                () -> CloudNet.getInstance().getQueuedConsoleLogHandler().getCachedQueuedLogEntries().stream().map(formatter::format).collect(Collectors.toList()),
                () -> "Developer setup started",
                () -> "Successfully created developer",
                "&r» &a"
        );
        animation.addEntry(new QuestionListEntry<>(
                "name",
                "Please enter the name of the developer",
                new QuestionAnswerTypeString()
        ));
        animation.addEntry(new QuestionListEntry<>(
                "memory",
                "Please enter the current memory of the developer",
                new QuestionAnswerTypeInt()
        ));
        animation.addEntry(new QuestionListEntry<>(
                "max-memory",
                "Please enter the max memory of the developer",
                new QuestionAnswerTypeInt()
        ));
        animation.addFinishHandler(() -> {
            String name = (String) animation.getResult("name");
            int memory = (int) animation.getResult("memory");
            int maxMemory = (int) animation.getResult("max-memory");
            UUID dev = VeranyDeveloperModule.INSTANCE.getUniqueId(name);
            DeveloperData data = new DeveloperData(dev, false, memory, maxMemory, new ServiceData());
            VeranyDeveloperModule.INSTANCE.createDeveloper(data);
        });
        console.clearScreen();
        console.startAnimation(animation);
    }
}
