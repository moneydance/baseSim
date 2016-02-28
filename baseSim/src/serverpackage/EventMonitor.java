package serverpackage;

public class EventMonitor extends Event
{
    public EventMonitor()
    {
        this.time_stamp = Simulate.CLOCK + genTimeStamp(Simulate.MONITOR_RATE);
    }
}
