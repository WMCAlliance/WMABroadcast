package im.wma.dev.WMABroadcast;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created with IntelliJ IDEA.
 * User: Starbuck
 * Date: 4/2/13
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldBroadcastEvent extends Event implements Cancellable
{
    private String message;
    private String worldName;
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled = false;


    public WorldBroadcastEvent(String message, String worldName) {
        this.message = message;
        this.worldName = worldName;
    }

    public String getMessage() {
        return message;
    }

    public String getWorldName() {
        return worldName;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    @Override
    public boolean isCancelled()
    {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b)
    {
        canceled = b;
    }
}
