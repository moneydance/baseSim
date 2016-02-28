package serverpackage;

public class EventDeath extends Event
{
    public EventDeath(Task task)
    {
        double service_time = genTimeStamp(Simulate.MU);
        task.updateServiceTime(service_time);
        this.time_stamp = Simulate.CLOCK + service_time;
        this.task = task;
    }
}
