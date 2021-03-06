package im.wma.dev.WMABroadcast;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class WMABroadcast extends JavaPlugin
{
    private List<BroadcastTask> broadcastTasks;
    private PerWorldListener worldListener;
    private Map<String, Boolean> debugMode = new HashMap<String, Boolean>();


    public void onEnable() {

        saveDefaultConfig();

        broadcastTasks = new ArrayList<BroadcastTask>();

        worldListener = new PerWorldListener(this);
        getServer().getPluginManager().registerEvents(worldListener, this);
        scheduleTasks();
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
                reloadConfig();
                scheduleTasks();
                sender.sendMessage(ChatColor.DARK_AQUA + "Your broadcast messages have been reloaded.");
            } else {
                sender.sendMessage(ChatColor.RED + "Sorry, but you have insufficient permissions.");
            }
        } else if (command.getName().equalsIgnoreCase("broadcastdebug")) {
            if (sender.hasPermission("wmabroadcast.debug")) {
                if (sender instanceof Player) {
                    if (debugMode.containsKey(sender.getName())) {
                        debugMode.put(sender.getName(), debugMode.get(sender.getName()));
                    } else {
                        debugMode.put(sender.getName(), true);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Sorry only players can use this command.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Sorry, but you have insufficient permissions.");
            }
        }


        return true;
    }

    public void scheduleTasks() {
        for (BroadcastTask task : broadcastTasks) {
            task.cancel();
        }

        broadcastTasks.clear();

        for (World w : getServer().getWorlds()) {
            if (!Files.exists(Paths.get(getDataFolder().getPath(), w.getName().toLowerCase() + ".txt"))) { continue; }
            BroadcastTask task = new BroadcastTask(this, w.getName());
            task.runTaskTimer(this, 0, getTime());
            broadcastTasks.add(task);
        }
    }

    public boolean isDebug(Player player) {
        if (!debugMode.containsKey(player.getName())) {
            return false;
        }

        return debugMode.get(player.getName());
    }

}
