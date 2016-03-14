package servermultipackage;

import serverpackage.*;
import servermultipackage.multieventpackage.*;
import serverpackage.eventpackage.*;
import java.util.List;
import java.util.PriorityQueue;

public class MultiServerSimulate extends Simulate
{
    private PriorityQueue<Event> calander;
    private double max_time;
    private MultiServer server;
    private double clock;

    public MultiServerSimulate(double lambda, double max_time, boolean record_logs)
    {
        clock = 0.0;
        calander = new PriorityQueue<Event>();
        server = new MultiServer(lambda, record_logs);
        calander.add(new MultiEventBirth(Event.nextExponential(lambda), clock, 0));
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
        server.printSubServerStats(clock, max_time);
    }

    private void resolveEvent(Event current_event)
    {
        if (current_event instanceof EventBirth)
        {
           List<Event> new_events = server.arrival(current_event, clock);
           addEvents(new_events);
        }
        else if (current_event instanceof EventDeath)
        {
            boolean record_stats = (clock > max_time);
            List<Event> new_events = server.departure(current_event, clock, record_stats);
            addEvents(new_events);
        }
        else if (current_event instanceof EventMonitor)
        {
            Event new_monitor = server.monitor(clock, max_time);
            calander.add(new_monitor);
        }
    }

    private void addEvents(List<Event> new_events)
    {
        if (new_events != null)
        {
            for (Event event : new_events)
            {
                if (event != null)
                    calander.add(event);
            }
        }
    }
}
