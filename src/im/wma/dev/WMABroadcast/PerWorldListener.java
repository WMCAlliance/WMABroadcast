package im.wma.dev.WMABroadcast;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: Starbuck
 * Date: 4/2/13
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class PerWorldListener implements Listener
{
    private WMABroadcast plugin;

    public PerWorldListener(WMABroadcast plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWMABroadcast(WorldBroadcastEvent event) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (plugin.isDebug(p)) {
                p.sendMessage(ChatColor.GRAY + event.getWorldName() + ": " + event.getMessage() + " is " + (event.isCancelled() ? "" : "not") + "canceled.");
            }
        }
    }
}