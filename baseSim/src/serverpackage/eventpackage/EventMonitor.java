package serverpackage.eventpackage;

public class EventMonitor extends Event
{
    public EventMonitor(double Lambda, double clock)
    {
        this.time_stamp = clock + nextExponential(Lambda);
    }
}
