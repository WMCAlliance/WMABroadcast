package WMABroadcast;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class WMABroadcast extends JavaPlugin
{
    private BroadcastTask broadcastTask;

    public void onEnable() {

        saveDefaultConfig();

        broadcastTask = new BroadcastTask(this);
        broadcastTask.runTaskTimer(this, 20, getTime());
    }
    public String getBroadcastPrefix() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "&b[&aWMA&b] &6"));
    }

    public long getTime() {
        String interval = getConfig().getString("interval", "40s");
        String timeNoUnit = interval.substring(0, interval.length() - 1);
        int timeNoUnitInt = Integer.valueOf(timeNoUnit);
        if (interval.endsWith("s")) {
            return timeNoUnitInt  * 20;
        } else if (interval.endsWith("m")) {
            return timeNoUnitInt * 20 * 60;
        } else {
            return 5 * 20 * 60;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (command.getName().equalsIgnoreCase("reloadbroadcast")) {
            if (sender.hasPermission("wmabroadcast.reload")) {
                broadcastTask.cancel();
                broadcastTask = new BroadcastTask(this);

                reloadConfig();

                broadcastTask.runTaskTimer(this, 20, getTime());
                sender.sendMessage(ChatColor.DARK_AQUA + "Your broadcast messages have been reloaded.");
            } else {
                sender.sendMessage(ChatColor.RED + "Sorry, but you have insufficient permissions.");
            }
        }


        return true;
    }

}
