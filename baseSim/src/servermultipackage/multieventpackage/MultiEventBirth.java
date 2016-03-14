package servermultipackage.multieventpackage;

import serverpackage.eventpackage.EventBirth;
import serverpackage.Task;

public class MultiEventBirth extends EventBirth
{
    private int id;
    public MultiEventBirth(double time_step, double clock, int id)
    {
        super(time_step, clock);
        this.id = id;
    }

    public MultiEventBirth(Task task, double time_step, double clock, int id){
        super(task, time_step, clock);
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
