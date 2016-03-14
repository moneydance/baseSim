package servermdonekpackage;

import serverpackage.*;
import serverpackage.eventpackage.*;
import java.util.List;
import java.util.PriorityQueue;

public class ServerMDOneKSimulate extends Simulate
{
    private PriorityQueue<Event> calander;
    private double max_time;
    private ServerMDOneK server;
    private double clock;

    public ServerMDOneKSimulate(double lambda, double ts, double max_time, int k, boolean record_logs)
    {
        clock = 0.0;
        calander = new PriorityQueue<Event>();
        server = new ServerMDOneK(lambda, ts, k, record_logs);
        calander.add(new EventBirth(lambda, clock));
        calander.add(new EventMonitor(lambda * .02, max_time));
        this.max_time = max_time;
    }

    public void run()
    {
        while(clock<max_time * 2)
        {
            Event current_event = calander.poll();
            clock = current_event.getTimeStamp();
            resolveEvent(current_event);
        }
        server.stats.printStats(clock, max_time);
    }

    private void resolveEvent(Event current_event)
    {
        if (current_event instanceof EventBirth)
        {
           List<Event> new_events = server.arrival(current_event, clock);
           addEvents(new_events);
        }
        else if (current_event instanceof EventDeath)
        {   if (clock > max_time)
                server.stats.recordTimes(current_event.getTask());
            Event new_death_event = server.departure(clock);
            if (new_death_event != null)
                calander.add(new_death_event);
        }
        else if (current_event instanceof EventMonitor)
        {
            Event new_monitor = server.monitor(clock, max_time);
            calander.add(new_monitor);
        }
    }

    private void addEvents(List<Event> new_events)
    {
        for (Event event : new_events)
            calander.add(event);
    }
}
