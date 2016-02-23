package serverpackage;

import java.util.List;
import java.util.PriorityQueue;

public class Simulate
{
    private long seed;
    private PriorityQueue<Event> calander;
    private double max_time;
    private Server server;
    private double last_event_time;
    private Statistics stats;
    public static double CLOCK;
    public static double LAMBDA;
    public static double MU;
   // private final int HEAP_SIZE = 1000;

    public Simulate(long seed, double lambda, double mu, double max_time)
    {
        calander = new PriorityQueue<Event>();
        server = new Server();
        CLOCK = 0.0;
        this.max_time = max_time;
        this.seed = seed;
        last_event_time = 0;
        stats = new Statistics();
        MU = mu;
        LAMBDA = lambda;
        Drand48.set(this.seed);
    }

    public void run()
    {
        calander.add(new EventBirth());
        while(CLOCK<max_time)
        {
            Event current_event = calander.poll();
            CLOCK = current_event.getTimeStamp();
            resolveEvent(current_event);
            last_event_time = CLOCK;
        }
        stats.printStats();
    }

    public void resolveEvent(Event current_event)
    {
        double diff = CLOCK - last_event_time;
        int queue_length = server.getQueueLength();
        int system_length = server.getSystemLength();
        stats.recordLengths(queue_length, system_length, diff);
        if (current_event instanceof EventBirth)
        {
           List<Event> new_events = server.arrival(current_event);
           addEvents(new_events);
        }
        else if (current_event instanceof EventDeath)
        {
            stats.recordTimes(current_event.task);
            EventDeath new_death_event = server.departure();
            if (new_death_event != null)
                calander.add(new_death_event);
        }
    }

    private void addEvents(List<Event> new_events)
    {
        for (Event event : new_events)
            calander.add(event);
    }

    public static void main (String[] args) {
        Simulate sim1 = new Simulate(1234, 60, 66.66, 300.0);
        sim1.run();
    }
}
