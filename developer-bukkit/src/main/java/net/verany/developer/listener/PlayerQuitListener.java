package net.verany.developer.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyProject;
import net.verany.developer.Developer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends AbstractListener {

    public PlayerQuitListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, PlayerQuitEvent.class, event -> {
            Player player = event.getPlayer();

            if(Verany.getOnlinePlayers().isEmpty())
                Developer.INSTANCE.setLastPlayerOnline(System.currentTimeMillis());
        });
    }
}
