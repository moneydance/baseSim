package serverpackage;

import java.util.List;
import serverpackage.eventpackage.Event;

public abstract class Server
{
    public abstract int getQueueLength();
    public abstract int getSystemLength();
    public abstract List<Event> arrival(Event event, double clock);
    public abstract Event departure(double clock);

    public String getServerType(){
        return this.getClass().getSimpleName();
    }
}
