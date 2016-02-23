package serverpackage;

public class Statistics {
    private int checks;
    private double mean_queue_length;
    private double mean_queue_time;
    private double mean_system_time;
    private double mean_system_length;
    private double total_service_time;

    public Statistics()
    {
        checks = 0;
        mean_queue_length = 0.0;
        mean_queue_time = 0.0;
        mean_system_time = 0.0;
        mean_system_length = 0.0;
        total_service_time = 0.0;
    }

    public void recordTimes(Task task)
    {
        double wait_time = task.getWaitTime();
        double service_time = task.getServiceTime();
        double total_time = service_time + wait_time;
        mean_system_time +=  total_time;
        mean_queue_time += wait_time;
        total_service_time += service_time;
        checks++;
    }

    public void recordLengths(int queue_length, int system_length, double time)
    {
        mean_queue_length += queue_length * time;
        mean_system_length += system_length * time;
    }

    public void printStats()
    {
        System.out.println("W: " + mean_queue_length/Simulate.CLOCK);
        System.out.println("Q: " + mean_system_length/Simulate.CLOCK);
        System.out.println("Wt: " + mean_queue_time/checks);
        System.out.println("Qt: " + mean_system_time/checks);
        System.out.println("Rho: " + total_service_time/Simulate.CLOCK);
    }
}
