package servermmonepackage;

import serverpackage.*;
import serverpackage.eventpackage.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ServerMMOne extends Server
{
    private LinkedList<Task> task_queue;
    private int queue_length;
    private Task current_task;
    private double mu;
    private double lambda;
    private double monitor_rate;
    public ServerMMOneStatistics stats;

    public ServerMMOne(double lambda, double mu, boolean record_logs)
    {
        task_queue = new LinkedList<Task>();
        queue_length = 0;
        this.mu = mu;
        this.lambda = lambda;
        this.monitor_rate = lambda * .02;
        stats = new ServerMMOneStatistics(getServerType(), record_logs);
    }

    private void enqueue(Task t)
    {
        task_queue.add(t);
        queue_length++;
    }

    private Task dequeue()
    {
        queue_length--;
        return task_queue.remove();
    }

    public int getQueueLength()
    {
        return queue_length;
    }

    public int getSystemLength()
    {
        if (current_task != null)
        {
            return queue_length + 1;
        }
        else
            return queue_length;
    }

    public List<Event> arrival(Event event, double clock)
    {
        List<Event> new_events = new ArrayList<Event>();
        Task arriving_task = event.getTask();
        if (queue_length==0 && current_task==null)
        {
            arriving_task.updateWaitTime(clock);
            current_task = arriving_task;
            new_events.add(new EventDeath(arriving_task, Event.nextExponential(mu), clock));
        }
        else
            enqueue(arriving_task);
        new_events.add(new EventBirth(Event.nextExponential(lambda), clock));
        return new_events;
    }

    public Event departure(double clock)
    {
        if (queue_length > 0)
        {
            Task departing_task = dequeue();
            departing_task.updateWaitTime(clock);
            current_task = departing_task;
            return new EventDeath(departing_task, Event.nextExponential(mu), clock);
        }
        current_task = null;
        return null;
    }

    public Event monitor(double clock, double max_time){
        stats.recordLengths(getQueueLength(), getSystemLength());
        stats.writeStats(clock, max_time);
        return new EventMonitor(monitor_rate, clock);
    }
}
