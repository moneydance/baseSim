package serverpackage;

import java.util.List;

public abstract class Server
{
    public abstract int getQueueLength();
    public abstract int getSystemLength();
    public abstract List<Event> arrival(Event event);
    public abstract EventDeath departure();

    public String getServerType(){
        return this.getClass().getSimpleName();
    }
}
