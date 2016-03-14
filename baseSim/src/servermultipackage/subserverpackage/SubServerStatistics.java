package servermultipackage.subserverpackage;

import serverpackage.*;
import java.lang.Math;

public class SubServerStatistics extends Statistics
{
    private double mean_queue_length;
    private double mean_system_length;
    private double mean_queue_length_pow2;
    private double mean_system_length_pow2;
    private double total_service_time;
    private double z_score1;
    private double z_score2;
    private int total_requests;

    public SubServerStatistics(String server_type, boolean collect_logs)
    {
        super(server_type, collect_logs);
        mean_queue_length = 0.0;
        mean_system_length = 0.0;
        total_service_time = 0.0;
        mean_queue_length_pow2 = 0;
        mean_system_length_pow2 = 0;
        total_requests= 0;
        z_score1 = 1.96;
        z_score2 = 2.33;
    }

    public void recordTs(double ts){
        this.total_service_time += ts;
    }

    public void recordRequests(int total_requests){
        this.total_requests = total_requests;
    }

    public void recordTimes(Task task)
    {
    }

    public void recordLengths(int queue_length, int system_length)
    {
        mean_queue_length += queue_length;
        mean_system_length += system_length;
        mean_queue_length_pow2 += Math.pow(queue_length, 2);
        mean_system_length_pow2 += Math.pow(system_length, 2);
        monitor_event_count++;
    }

    public void printStats(double clock, double max_time)
    {
        System.out.println("===================================================");
        System.out.println("CLOCK: " + (clock - max_time));
        System.out.println("W: " + mean_queue_length/(double)monitor_event_count);
        System.out.println("Q: " + mean_system_length/(double)monitor_event_count);
        System.out.println("Rho: " + total_service_time/(clock));
        System.out.println("Lambda: " + total_requests/(clock));
        double w_stdev = computeStdev((double)mean_queue_length, (double)mean_queue_length_pow2, monitor_event_count);
        double q_stdev = computeStdev((double)mean_system_length, (double)mean_system_length_pow2, monitor_event_count);
        System.out.println("W stdev: " + w_stdev);
        System.out.println("Q stdev: " + q_stdev);
        System.out.println("W 95% Confidence Interval: " + confidenceInterval(w_stdev, z_score1, monitor_event_count));
        System.out.println("W 98% Confidence Interval: " + confidenceInterval(w_stdev, z_score2, monitor_event_count));
        System.out.println("Q 95% Confidence Interval: " + confidenceInterval(q_stdev, z_score1, monitor_event_count));
        System.out.println("Q 98% Confidence Interval: " + confidenceInterval(q_stdev, z_score2, monitor_event_count));
        System.out.println("Monitors through system: " + monitor_event_count);
        System.out.println("===================================================");
    }
}

