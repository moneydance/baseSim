package servermultipackage.subserverpackage;

import serverpackage.*;
import serverpackage.eventpackage.*;
import servermultipackage.multieventpackage.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Cpu
{
    private LinkedList<Task> task_queue;
    private int queue_length;
    private int current_tasks;
    private int total_requests;
    private double ts_min;
    private double ts_max;
    public SubServerStatistics stats;

    public Cpu(double ts_min, double ts_max, boolean record_logs)
    {
        task_queue = new LinkedList<Task>();
        queue_length = 0;
        current_tasks = 0;
        this.ts_min = ts_min;
        this.ts_max = ts_max;
        stats = new SubServerStatistics("Cpu", record_logs);
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

    private Event getDestination(Task departed_task, double clock){
        double rand = Math.random();
        if (rand < .1)
            return new MultiEventBirth(departed_task, 0, clock, 2);
        else if (rand < .5)
            return new MultiEventBirth(departed_task, 0, clock, 3);
        else
            return new MultiEventDeath(departed_task,0, clock, 0);
    }

    public int getQueueLength()
    {
        return queue_length;
    }

    public int getSystemLength()
    {
        return queue_length + current_tasks;
    }

    public int getTotalRequests(){
        return total_requests;
    }

    public Event arrival(Event event, double clock)
    {
        total_requests++;
        Task arriving_task = event.getTask();
        if (queue_length==0 && current_tasks<2)
        {
            current_tasks++;
            double ts = Event.nextUniform(ts_min, ts_max);
            stats.recordTs(ts/2);
            return new MultiEventDeath(arriving_task, ts, clock, 1);
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
            double ts = Event.nextUniform(ts_min, ts_max);
            stats.recordTs(ts/2);
            new_events.add(new MultiEventDeath(departing_task, ts, clock, 1));
        }
        else
        {
            current_tasks--;
        }
        return new_events;
    }

    public void monitor(double clock, double max_time)
    {
        stats.recordLengths(getQueueLength(), getSystemLength());
        stats.recordRequests(total_requests);
        stats.writeStats(clock, max_time);
    }
}

