package servermmonepackage;

import serverpackage.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ServerMMOne extends Server
{
    private LinkedList<Task> task_queue;
    private int queue_length;
    private Task current_task;

    public ServerMMOne()
    {
        task_queue = new LinkedList<Task>();
        queue_length = 0;
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

    public List<Event> arrival(Event event)
    {
        List<Event> new_events = new ArrayList<Event>();
        Task arriving_task = event.getTask();
        if (queue_length==0 && current_task==null)
        {
            arriving_task.updateWaitTime();
            current_task = arriving_task;
            new_events.add(new EventDeath(arriving_task));
        }
        else
            enqueue(arriving_task);
        new_events.add(new EventBirth());
        return new_events;
    }

    public EventDeath departure()
    {
        if (queue_length > 0)
        {
            Task departing_task = dequeue();
            departing_task.updateWaitTime();
            current_task = departing_task;
            return new EventDeath(departing_task);
        }
        current_task = null;
        return null;
    }
}
