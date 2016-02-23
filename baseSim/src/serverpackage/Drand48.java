package serverpackage;
import java.nio.*;

//Note: Found Online http://www.exp.univie.ac.at/sc/sim/Drand48.java

public class Drand48
{
    static final long a=25214903917L,c=11L;
    static final long mask=0xffffffffffffL,one=0x3ff0000000000000L;
    static long seed;

    static void set(long newseed)
    {
        seed=newseed&mask;
        return;
    }

    static long get()
    {
        return seed;
    }

    static double nextDouble()
    {
        ByteBuffer buf=ByteBuffer.allocate(8);
        seed=(a*seed+c)&mask;
        buf.putLong(0,one|(seed<<4));
        return buf.getDouble(0)-1.0;
    }
}

