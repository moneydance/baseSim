package serverpackage;

public class Task {
    private double arrival_time;
    private double service_time;
    private double wait_time;

    public Task(double arrival_time)
    {
        this.arrival_time = arrival_time;
    }

    public void updateServiceTime(double service_time)
    {
        this.service_time = service_time;
    }

    public void updateWaitTime(double clock)
    {
        this.wait_time = clock - arrival_time;
    }

    public double getServiceTime()
    {
        return service_time;
    }

    public double getWaitTime()
    {
        return wait_time;
    }
    public double getArrivalTime()
    {
        return arrival_time;
    }
}
