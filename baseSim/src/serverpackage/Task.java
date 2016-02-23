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

    public void updateWaitTime()
    {
        this.wait_time = Simulate.CLOCK - arrival_time;
    }

    public double getServiceTime()
    {
        return service_time;
    }

    public double getWaitTime()
    {
        return wait_time;
    }
}
