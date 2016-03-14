package serverpackage.eventpackage;

import serverpackage.Task;
import java.lang.Math;
import java.util.Random;


public abstract class Event implements Comparable<Event>
{
    protected double time_stamp;
    protected Task task;

    public int compareTo(Event o2)
    {
        if (time_stamp > o2.time_stamp)
            return 1;
        else if (time_stamp < o2.time_stamp)
            return -1;
        else
            return 0;
    }

    public int getId(){
        return -1;
    }

    public double getTimeStamp()
    {
        return time_stamp;
    }

    public Task getTask()
    {
        return task;
    }

    public static double nextExponential(double lambda)
    {
        Random RandomGenerator = new Random();
        double Y = RandomGenerator.nextDouble();
        double x = (- Math.log(1.0-Y))/lambda;
        return x;
    }

    public static double nextUniform(double low, double high)
    {
        Random RandomGenerator = new Random();
        return RandomGenerator.nextDouble() * (high-.001) + low;
    }

    private static double Zrand()
    {
        double sum = 0;
        double N = 30;
        for(int i =0; i< N; i++)
        {
            sum += Math.random();
        }
        return (sum - 15.0)/1.58;
    }

    public static double nextNormal(double mean, double std)
    {
       return Zrand() * std + mean;
    }
}
