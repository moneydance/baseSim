package servermdonekpackage;

import serverpackage.*;
import serverpackage.eventpackage.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ServerMDOneK extends Server
{
    private LinkedList<Task> task_queue;
    private int queue_length;
    private Task current_task;
    private double ts;
    private double lambda;
    private double monitor_rate;
    int k;
    int requests;
    int accepted_requests;
    public ServerMDOneKStatistics stats;

    public ServerMDOneK(double lambda, double ts, int k, boolean record_logs)
    {
        task_queue = new LinkedList<Task>();
        queue_length = 0;
        requests = 0;
        accepted_requests = 0;
        stats = new ServerMDOneKStatistics(getServerType(), record_logs);
        this.ts = ts;
        this.lambda = lambda;
        this.k = k;
        this.monitor_rate = lambda * .02;
    }

    private void enqueue(Task t)
    {
        if (queue_length < k-1)
        {
            task_queue.add(t);
            queue_length++;
            accepted_requests++;
        }
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

    public double getAcceptanceProbability(){
        return accepted_requests/(double)(requests);
    }

    public List<Event> arrival(Event event, double clock)
    {
        requests++;
        List<Event> new_events = new ArrayList<Event>();
        Task arriving_task = event.getTask();
        if (queue_length==0 && current_task==null)
        {
            accepted_requests++;
            arriving_task.updateWaitTime(clock);
            current_task = arriving_task;
            new_events.add(new EventDeath(arriving_task, ts, clock));
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
            return new EventDeath(departing_task, ts, clock);
        }
        current_task = null;
        return null;
    }

    public Event monitor(double clock, double max_time)
    {
        stats.recordLengths(getQueueLength(), getSystemLength());
        stats.recordAcceptance(getAcceptanceProbability());
        stats.writeStats(clock, max_time);
        return new EventMonitor(monitor_rate, clock);
    }
}
