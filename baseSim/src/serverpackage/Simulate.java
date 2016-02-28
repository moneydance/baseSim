package serverpackage;

import java.util.List;
import java.util.PriorityQueue;

interface SimulateInterface
{
    public void run();
}

public class Simulate implements SimulateInterface
{
    private PriorityQueue<Event> calander;
    private double max_time;
    private Server server;
    private Statistics stats;
    public static long SEED;
    public static double CLOCK;
    public static double LAMBDA;
    public static double MONITOR_RATE;
    public static double MU;

    public Simulate(long seed, double lambda, double ts, double max_time, Server server, Statistics stats)
    {
        CLOCK = 0.0;
        MU = 1/ts;
        LAMBDA = lambda;
        MONITOR_RATE = lambda * .2;
        SEED = seed;
        calander = new PriorityQueue<Event>();
        this.server = server;
        this.max_time = max_time;
        this.stats = stats;
    }

    public void run()
    {
        calander.add(new EventBirth());
        Boolean collect_stats = false;
        while(CLOCK<max_time)
        {
            Event current_event = calander.poll();
            CLOCK = current_event.getTimeStamp();
            resolveEvent(current_event, collect_stats);
        }
        collect_stats = true;
        calander.add(new EventMonitor());
        while(CLOCK<2*max_time)
        {
            Event current_event = calander.poll();
            CLOCK = current_event.getTimeStamp();
            resolveEvent(current_event, collect_stats);
        }
        stats.printStats();
    }

    private void resolveEvent(Event current_event, Boolean collect_stats)
    {
        if (current_event instanceof EventBirth)
        {
           List<Event> new_events = server.arrival(current_event);
           addEvents(new_events);
        }
        else if (current_event instanceof EventDeath)
        {   if (collect_stats)
                stats.recordTimes(current_event.task);
            EventDeath new_death_event = server.departure();
            if (new_death_event != null)
                calander.add(new_death_event);
        }
        else if (current_event instanceof EventMonitor)
        {
            int queue_length = server.getQueueLength();
            int system_length = server.getSystemLength();
            stats.recordLengths(queue_length, system_length);
            calander.add(new EventMonitor());
            stats.writeStats();
        }
    }

    private void addEvents(List<Event> new_events)
    {
        for (Event event : new_events)
            calander.add(event);
    }
}
