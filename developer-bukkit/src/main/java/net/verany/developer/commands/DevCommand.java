package net.verany.developer.commands;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.verany.api.Verany;
import net.verany.api.inventory.InventoryBuilder;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.player.IPlayerInfo;
import net.verany.developer.Developer;
import net.verany.volcano.round.ServerRoundData;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DevCommand implements CommandExecutor {

    private final Integer[] contentSlot = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.getPlayer(player);

        if (strings.length == 0) {
            ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(player.getName() + "-1");
            if (serviceInfoSnapshot == null) {
                player.sendMessage("You currently don't have a service!");
                return false;
            }
            if (isMultiRoundServer(serviceInfoSnapshot)) {
                String roundData = serviceInfoSnapshot.getProperties().getString("round_data");
                List<Document> documents = Verany.GSON.fromJson(roundData, ServerRoundData.class).getDocuments();

                Inventory inventory = InventoryBuilder.builder().size(9 * 6).title("Rounds").event(event -> {
                    event.setCancelled(true);

                    if (event.getCurrentItem().getType().equals(Material.ARMOR_STAND)) {
                        String name = event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1];

                        String server = ChatColor.stripColor(name.split("#")[0]);
                        String id = ChatColor.stripColor(name.split("#")[1]);

                        ICloudPlayer cloudPlayer = playerInfo.getCloudPlayer();
                        cloudPlayer.getProperties().append("round-id", id);
                        CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).updateOnlinePlayer(cloudPlayer);

                        playerInfo.sendOnServer(server);
                    }
                }).onClose(event -> {
                    Developer.INSTANCE.removeMetadata(player, "rounds_inventory");
                }).build().fillCycle(new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setNoName().build()).buildAndOpen(player);

                for (int i = 0; i < documents.size(); i++) {
                    Document document = documents.get(i);
                    String id = document.getString("id");
                    String difficulty = document.getString("difficulty");
                    int maxPlayers = (int) Math.round(document.getDouble("max_players"));
                    List<String> players = document.getList("players", String.class);

                    inventory.setItem(contentSlot[i], new ItemBuilder(Material.ARMOR_STAND).setAmount(players.isEmpty() ? 1 : players.size()).setDisplayName(player.getName() + "-1#" + id).build());
                }
                return false;
            }
            playerInfo.sendOnServer(player.getName() + "-1");
        } else if (strings.length == 1) {
            Verany.PROFILE_OBJECT.getPlayer(strings[0]).ifPresentOrElse(iPlayerInfo -> {
                ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(iPlayerInfo.getName() + "-1");
                if (serviceInfoSnapshot == null) {
                    player.sendMessage("This player doesn't have a server!");
                    return;
                }
                if (!serviceInfoSnapshot.getProperties().contains("joinPermission")) return;
                String permission = serviceInfoSnapshot.getProperties().getString("joinPermission");
                if (!player.hasPermission(permission)) {
                    player.sendMessage("You are not allowed to join this server!");
                    return;
                }
                playerInfo.sendOnServer(iPlayerInfo.getName() + "-1");
            }, () -> {
                player.sendMessage("This player doesn't exist!");
            });
        }
        return false;
    }

    private boolean isMultiRoundServer(ServiceInfoSnapshot serviceInfoSnapshot) {
        return serviceInfoSnapshot.getProperties().contains("round_data");
    }
}
