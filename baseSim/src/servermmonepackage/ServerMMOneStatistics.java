package servermmonepackage;

import serverpackage.*;
import java.lang.Math;

public class ServerMMOneStatistics extends Statistics
{
    private int death_event_count;
    private double mean_queue_length;
    private double mean_queue_time;
    private double mean_system_time;
    private double mean_system_length;
    private double mean_queue_length_pow2;
    private double mean_queue_time_pow2;
    private double mean_system_time_pow2;
    private double mean_system_length_pow2;
    private double total_service_time;
    private double z_score;

    public ServerMMOneStatistics(String server_type)
    {
        super(server_type);
        death_event_count = 0;
        monitor_event_count = 0;
        mean_queue_length = 0.0;
        mean_queue_time = 0.0;
        mean_system_time = 0.0;
        mean_system_length = 0.0;
        total_service_time = 0.0;
        mean_queue_length_pow2 = 0;
        mean_system_time_pow2 = 0;
        mean_queue_length_pow2 = 0;
        mean_system_length_pow2 = 0;
        z_score = 3.31;
    }

    public void recordTimes(Task task)
    {
        double wait_time = task.getWaitTime();
        double service_time = task.getServiceTime();
        double total_time = service_time + wait_time;
        mean_system_time += total_time;
        mean_queue_time += wait_time;
        mean_system_time_pow2 += Math.pow(total_time, 2);
        mean_queue_time_pow2 += Math.pow(wait_time, 2);
        total_service_time += service_time;
        death_event_count++;
    }

    public void recordLengths(int queue_length, int system_length)
    {
        mean_queue_length += queue_length;
        mean_system_length += system_length;
        mean_queue_length_pow2 += Math.pow(queue_length, 2);
        mean_system_length_pow2 += Math.pow(system_length, 2);
        monitor_event_count++;
    }

    public void printStats()
    {
        System.out.println("===================================================");
        System.out.println("CLOCK: " + Simulate.CLOCK/2);
        System.out.println("W: " + mean_queue_length/(double)monitor_event_count);
        System.out.println("Q: " + mean_system_length/(double)monitor_event_count);
        System.out.println("Wt: " + mean_queue_time/death_event_count);
        System.out.println("Qt: " + mean_system_time/death_event_count);
        System.out.println("Rho: " + total_service_time/(Simulate.CLOCK/2.0));
        double w_stdev = computeStdev((double)mean_queue_length, (double)mean_queue_length_pow2, monitor_event_count);
        double q_stdev = computeStdev((double)mean_system_length, (double)mean_system_length_pow2, monitor_event_count);
        double wt_stdev = computeStdev(mean_queue_time, mean_queue_time_pow2, death_event_count);
        double qt_stdev = computeStdev(mean_system_time, mean_system_time_pow2, death_event_count);
        System.out.println("W stdev: " + w_stdev);
        System.out.println("Q stdev: " + q_stdev);
        System.out.println("Wt stdev: " + wt_stdev);
        System.out.println("Qt stdev: " + qt_stdev);
        System.out.println("W Confidence Interval: " + confidenceInterval(w_stdev, z_score, monitor_event_count));
        System.out.println("Q Confidence Interval: " + confidenceInterval(q_stdev, z_score, monitor_event_count));
        System.out.println("Wt Confidence Interval: " + confidenceInterval(wt_stdev, z_score, death_event_count));
        System.out.println("Qt Confidence Interval: " + confidenceInterval(qt_stdev, z_score, death_event_count));
        System.out.println("Monitors through system: " + monitor_event_count);
        System.out.println("Tasks through system: " + death_event_count);
        System.out.println("===================================================");
    }
}
