package WMABroadcast;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Starbuck
 * Date: 3/31/13
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BroadcastTask extends BukkitRunnable
{
    private int lastMessage = 0;
    private WMABroadcast plugin;

    public BroadcastTask(WMABroadcast plugin) {
        this.plugin = plugin;
    }
    @Override
    public void run()
    {
        List<String> broadcastMessages = plugin.getConfig().getStringList("messages");
        if (broadcastMessages.size() == 0) {
            return;
        }

        if (broadcastMessages.size() <= lastMessage) {
            lastMessage = 0;
        }

        broadcastMessage(broadcastMessages.get(lastMessage));
        lastMessage = lastMessage + 1;
        plugin.reloadConfig();
    }

    private void broadcastMessage(String message) {
        String prefix = plugin.getBroadcastPrefix();

        String finalMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);
        plugin.getServer().broadcastMessage(finalMessage);
    }

}
