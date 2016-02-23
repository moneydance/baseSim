package serverpackage;

import java.lang.Math;

public class Event implements Comparable<Event>
{
    protected double time_stamp;
    protected Task task;

    public Event()
    {

    }

    public Event(double time_stamp)
    {
        this.time_stamp = time_stamp;
    }

    public int compareTo(Event o2)
    {
        if (time_stamp > o2.time_stamp)
            return 1;
        else if (time_stamp < o2.time_stamp)
            return -1;
        else
            return 0;
    }

    public double getTimeStamp()
    {
        return time_stamp;
    }

    public Task getTask()
    {
        return task;
    }

    protected static double genTimeStamp(double lambda)
    {
        double Y = Drand48.nextDouble();
        double x = (- Math.log(1.0-Y))/lambda;
        return x;
    }
}
