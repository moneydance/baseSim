package serverpackage;

public class EventBirth extends Event
{
    public EventBirth()
    {
        super();
        this.time_stamp = Simulate.CLOCK + genTimeStamp(Simulate.LAMBDA);
        this.task = new Task(time_stamp);
    }
}
