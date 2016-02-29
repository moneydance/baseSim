package servermmonepackage;

import serverpackage.*;
import serverpackage.eventpackage.*;
import java.util.List;
import java.util.PriorityQueue;

interface SimulateInterface
{
    public void run();
}

public class ServerMMOneSimulate extends Simulate
{
    private PriorityQueue<Event> calander;
    private double max_time;
    private Server server;
    private Statistics stats;
    private double clock;
    public double monitor_rate;

    public ServerMMOneSimulate(double lambda, double ts, double max_time, boolean record_logs)
    {
        clock = 0.0;
        monitor_rate = lambda * .02;
        calander = new PriorityQueue<Event>();
        server = new ServerMMOne(lambda, 1/ts);
        stats = new ServerMMOneStatistics(server.getServerType(), record_logs);
        this.max_time = max_time;
        calander.add(new EventBirth(lambda, clock));
    }

    public void run()
    {
        Boolean collect_stats = false;
        while(clock<max_time)
        {
            Event current_event = calander.poll();
            clock = current_event.getTimeStamp();
            resolveEvent(current_event, collect_stats);
        }
        collect_stats = true;
        calander.add(new EventMonitor(monitor_rate, clock));
        while(clock<2*max_time)
        {
            Event current_event = calander.poll();
            clock = current_event.getTimeStamp();
            resolveEvent(current_event, collect_stats);
        }
        stats.printStats(clock);
    }

    private void resolveEvent(Event current_event, Boolean collect_stats)
    {
        if (current_event instanceof EventBirth)
        {
           List<Event> new_events = server.arrival(current_event, clock);
           addEvents(new_events);
        }
        else if (current_event instanceof EventDeath)
        {   if (collect_stats)
                stats.recordTimes(current_event.getTask());
            Event new_death_event = server.departure(clock);
            if (new_death_event != null)
                calander.add(new_death_event);
        }
        else if (current_event instanceof EventMonitor)
        {
            int queue_length = server.getQueueLength();
            int system_length = server.getSystemLength();
            stats.recordLengths(queue_length, system_length);
            calander.add(new EventMonitor(monitor_rate, clock));
            stats.writeStats(clock);
        }
    }

    private void addEvents(List<Event> new_events)
    {
        for (Event event : new_events)
            calander.add(event);
    }
}
