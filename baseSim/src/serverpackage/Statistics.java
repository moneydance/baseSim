package serverpackage;

import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;

public abstract class Statistics
{
    protected int monitor_event_count;
    private PrintStream print_out_stream;
    private PrintStream log_out_stream;
    private boolean collect_logs;

    public Statistics(String server_type, boolean collect_logs){
        monitor_event_count = 0;
        this.collect_logs = collect_logs;
        String file_path = "logs/" + server_type + "Log.txt";
        File old_file = new File(file_path);
        if (old_file.exists())
            old_file.delete();
        File new_file =new File(file_path);;
        print_out_stream = System.out;
        try
        {
            new_file.createNewFile();
            log_out_stream = new PrintStream(new FileOutputStream(file_path, true));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    abstract public void recordTimes(Task task);
    abstract public void recordLengths(int queue_length, int system_length);
    abstract public void printStats(double clock);

    public void writeStats(double clock)
    {
        if (collect_logs)
        {
        System.setOut(log_out_stream);
        System.out.println("Monitor Event Number: " + monitor_event_count);
        printStats(clock);
        System.setOut(print_out_stream);
        }
    }

    protected double computeStdev(double values, double values_pow2, int count)
    {
        double mean = values/count;
        double mean_pow2 = values_pow2/(double)count;
        double var = mean_pow2 - Math.pow(mean, 2);
        return Math.sqrt(var);
    }

    protected double confidenceInterval(double std, double z_score, int samples)
    {
        return z_score * (std/Math.sqrt(samples));
    }
}
