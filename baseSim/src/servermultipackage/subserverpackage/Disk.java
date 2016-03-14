package servermultipackage.subserverpackage;

import serverpackage.*;
import serverpackage.eventpackage.*;
import servermultipackage.multieventpackage.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Disk
{
    private LinkedList<Task> task_queue;
    private int queue_length;
    private int current_task;
    private int total_requests;
    private double mean;
    private double std;
    public SubServerStatistics stats;

    public Disk(double mean, double std, boolean record_logs)
    {
        current_task = 0;
        task_queue = new LinkedList<Task>();
        queue_length = 0;
        this.mean = mean;
        this.std = std;
        stats = new SubServerStatistics("Disk", record_logs);
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

    private Event getDestination(Task departed_task, double clock)
    {
        double rand = Math.random();
        if (rand <.5)
            return new MultiEventBirth(departed_task, 0, clock, 1);
        else
            return new MultiEventBirth(departed_task, 0, clock, 3);
    }

    public int getQueueLength()
    {
        return queue_length;
    }

    public int getSystemLength()
    {
        return queue_length + current_task;
    }

    public int getTotalRequests(){
        return total_requests;
    }

    public Event arrival(Event event, double clock)
    {
        total_requests++;
        Task arriving_task = event.getTask();
        if (queue_length==0 && current_task<1)
        {
            current_task++;
            double ts = Event.nextNormal(mean, std);
            stats.recordTs(ts);
            return new MultiEventDeath(arriving_task, ts, clock, 2);
        }
        else
            enqueue(arriving_task);
        return null;
    }

    public List<Event> departure(Event event, double clock)
    {
        List<Event> new_events = new ArrayList<Event>();
        Task departed_task = event.getTask();
        Event destination = getDestination(departed_task, clock);
        new_events.add(destination);
        if (queue_length > 0)
        {
            Task departing_task = dequeue();
            double ts = Event.nextNormal(mean, std);
            stats.recordTs(ts);
            new_events.add(new MultiEventDeath(departing_task, ts, clock, 2));
        }
        else{
            current_task--;
        }
        return new_events;
    }

    public void monitor(double clock, double max_time){
        stats.recordLengths(getQueueLength(), getSystemLength());
        stats.recordRequests(getTotalRequests());
        stats.writeStats(clock, max_time);
    }
}

