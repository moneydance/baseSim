package serverpackage.eventpackage;

import serverpackage.Task;

public class EventBirth extends Event
{
    public EventBirth(double time_step, double clock)
    {
        this.time_stamp = clock + time_step;
        this.task = new Task(time_stamp);
    }

    public EventBirth(Task task, double time_step, double clock){
        this.task = task;
        this.time_stamp = clock + time_step;
    }
}
