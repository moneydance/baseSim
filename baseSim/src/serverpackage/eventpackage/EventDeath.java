package serverpackage.eventpackage;
import serverpackage.Task;

public class EventDeath extends Event
{
    public EventDeath(Task task, double service_time, double clock)
    {
        task.updateServiceTime(service_time);
        this.time_stamp = clock + service_time;
        this.task = task;
    }
}
