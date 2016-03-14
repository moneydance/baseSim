package servermultipackage;

import serverpackage.*;
import java.lang.Math;

public class MultiServerStatistics extends Statistics
{
    private int death_event_count;
    private double mean_system_time;
    private double mean_system_time_pow2;
    private double z_score1;
    private double z_score2;

    public MultiServerStatistics(String server_type, boolean collect_logs)
    {
        super(server_type, collect_logs);
        death_event_count = 0;
        mean_system_time = 0.0;
        mean_system_time_pow2 = 0;
        z_score1 = 1.96; // 95%
        z_score2 = 2.33; // 98%
    }

    public void recordTimes(Task task)
    {
        double total_time = task.getWaitTime();
        mean_system_time += total_time;
        mean_system_time_pow2 += Math.pow(total_time, 2);
        death_event_count++;
    }

    public void recordLengths(int queue_length, int system_length)
    {
        monitor_event_count++;
    }

    public void printStats(double clock, double max_time)
    {
        System.out.println("===================================================");
        System.out.println("clock: " + (clock - max_time));
        System.out.println("total system time: " + mean_system_time);
        System.out.println("qt: " + mean_system_time/death_event_count);
        double qt_stdev = computeStdev(mean_system_time, mean_system_time_pow2, death_event_count);
        System.out.println("qt stdev: " + qt_stdev);
        System.out.println("qt 95% confidence interval: " + confidenceInterval(qt_stdev, z_score1, death_event_count));
        System.out.println("qt 98% confidence interval: " + confidenceInterval(qt_stdev, z_score2, death_event_count));
        System.out.println("tasks through system: " + death_event_count);
        System.out.println("===================================================");
    }
}



