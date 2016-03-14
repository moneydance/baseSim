package servermultipackage;

import servermultipackage.multieventpackage.*;
import servermultipackage.subserverpackage.*;
import serverpackage.*;
import serverpackage.eventpackage.*;
import java.util.List;
import java.util.ArrayList;

public class MultiServer
{
    private double lambda;
    private double monitor_rate;
    private Cpu cpu;
    private Disk disk;
    private Network network;
    public MultiServerStatistics stats;

    public MultiServer(double lambda, boolean record_logs)
    {
        this.lambda = lambda;
        this.monitor_rate = lambda * .02;
        this.cpu = new Cpu(.001, .039, record_logs);
        this.disk = new Disk(.1, .03, record_logs);
        this.network = new Network(.025, record_logs);
        stats = new MultiServerStatistics("MultiServer", record_logs);
    }

    public List<Event> arrival(Event event, double clock)
    {
        List<Event> new_events = new ArrayList<Event>();
        int id = event.getId();
        if (id == 0){
            new_events.add(cpu.arrival(event, clock));
            new_events.add(new MultiEventBirth(Event.nextExponential(lambda), clock, 0));
        }
        else if (id == 1)
        {
            new_events.add(cpu.arrival(event, clock));
        }
        else if(id == 2)
        {
            new_events.add(disk.arrival(event, clock));
        }
        else if(id == 3)
        {
            new_events.add(network.arrival(event, clock));
        }
        return new_events;
    }

    public List<Event> departure(Event event, double clock, boolean record_stats)
    {
        int id = event.getId();
        if (id == 1)
        {
            return cpu.departure(event, clock);
        }
        else if(id == 2)
        {
            return disk.departure(event, clock);
        }
        else if(id == 3)
        {
            return network.departure(event, clock);
        }
        else if(id == 0 && record_stats)
        {
            Task task = event.getTask();
            task.updateWaitTime(clock);
            stats.recordTimes(task);
        }
        return null;
    }

    public Event monitor(double clock, double max_time){
        stats.recordLengths(0,0);
        stats.writeStats(clock, max_time);
        cpu.monitor(clock, max_time);
        disk.monitor(clock, max_time);
        network.monitor(clock, max_time);
        return new EventMonitor(monitor_rate, clock);
    }

    public void printSubServerStats(double clock, double max_time){
        System.out.println("Cpu");
        cpu.stats.printStats(clock, max_time);
        System.out.println("Network");
        network.stats.printStats(clock, max_time);
        System.out.println("Disk");
        disk.stats.printStats(clock, max_time);
    }
}

