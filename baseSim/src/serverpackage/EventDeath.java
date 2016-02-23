package serverpackage;

public class EventDeath extends Event
{
    public EventDeath(Task task)
    {
        super();
        double service_time = genTimeStamp(Simulate.MU);
        this.time_stamp = Simulate.CLOCK + service_time;
        task.updateServiceTime(service_time);
        this.task = task;
    }
}
