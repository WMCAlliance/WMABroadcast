package WMABroadcast;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Starbuck
 * Date: 3/31/13
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BroadcastTask extends BukkitRunnable
{
    private Random rand;
    private WMABroadcast plugin;
    private String worldName;
    int lastMessage;
    int lastLastMessage;
    int lastLastLastMessage;

    public BroadcastTask(WMABroadcast plugin, String worldName) {
        this.plugin = plugin;
        this.worldName = worldName;
        rand = new Random();
    }

    @Override
    public void run()
    {
        Path globalPath =Paths.get(plugin.getDataFolder().getPath(), "global.txt");
        if (!Files.exists(globalPath)) {
            try
            {
                Files.copy(plugin.getResource("global.txt"), globalPath);
            } catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        List<String> broadcastMessages = readLinesFromFile("global.txt");



        String worldPath = worldName.toLowerCase() + ".txt";
        if (Files.exists(Paths.get(plugin.getDataFolder().getPath(), worldPath))) {
            broadcastMessages.addAll(readLinesFromFile(worldPath));
        }


        if (broadcastMessages.size() == 0) {
            return;
        }

        int random = rand.nextInt(broadcastMessages.size());

        while (true) {
            if (random != lastMessage && random != lastLastMessage && random != lastLastLastMessage) {
                lastLastLastMessage = lastLastMessage;
                lastLastMessage = lastMessage;
                lastMessage = random;
                break;
            }

            random = rand.nextInt(broadcastMessages.size());
        }

        WorldBroadcastEvent broadcastEvent = new WorldBroadcastEvent(broadcastMessages.get(random), worldName);

        plugin.getServer().getPluginManager().callEvent(broadcastEvent);

        if (broadcastEvent.isCancelled()) {
            return;
        } else {
            World world = plugin.getServer().getWorld(broadcastEvent.getWorldName());

            if (world == null) { return; }

            broadcastMessage(broadcastEvent.getMessage());
        }


    }

    private void broadcastMessage(String message) {
        String prefix = plugin.getBroadcastPrefix();

        String finalMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);

        World world = plugin.getServer().getWorld(worldName);

        if (world == null) { return; }

        worldBroadcast(world, finalMessage);
    }

    private List<String> readLinesFromFile(String fileName) {
        Path path = new File(plugin.getDataFolder(), fileName).toPath();
        try
        {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }



    private void worldBroadcast(World world, String message) {
        for (Player player : world.getPlayers()) {
            if (player == null) { continue; }

            player.sendMessage(message);
        }
    }

}
