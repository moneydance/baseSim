package servermultipackage.multieventpackage;

import serverpackage.eventpackage.EventDeath;
import serverpackage.Task;

public class MultiEventDeath extends EventDeath
{
    private int id;
    public MultiEventDeath(Task task, double time_step, double clock, int id)
    {
        super(task, time_step, clock);
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
