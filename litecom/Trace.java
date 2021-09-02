package litecom;

import java.io.OutputStream;
//import java.io.PrintStream;

public class Trace extends OutputStream {

    public static Trace currentInstance;
    private static final String traceID = "[TRACE]";
    public static boolean killOutput;
    public static int identFilter = -1;
    public static final boolean t = false;
    private static int currIndent;
    private String outBuffer;

    static {
        new Trace();
    }

    public Trace() {
        outBuffer = "";
        currentInstance = this;
    }

    public static void out(Object obj, String s) {
        print(makeClassName(obj), s);
    }

    public static void out(String s) {
        print("static", s);
    }

    private static void print(String s, String s1) {
        if (killOutput)
            return;
        if (identFilter != -1 && currIndent >= identFilter) {
            return;
        } else {
            System.out.print(traceID + makeIdent() + s1 + " (" + s + ")\r\n");
            return;
        }
    }

    public static void indent() {
        currIndent++;
    }

    public static void outdent() {
        currIndent--;
    }

    public static void _mth0109(Object obj, String s)
    {
        print(makeClassName(obj), s);
        currIndent++;
    }

    public static void FF(String s)
    {
        print("static", s);
        currIndent++;
    }

    public static void _mth0103(Object obj, String s)
    {
        currIndent--;
        print(makeClassName(obj), s);
    }

    public static void _mth0107(String s)
    {
        currIndent--;
        print("static", s);
    }

    private static String makeClassName(Object obj) {
        String s = obj.getClass().getName();
        return s.substring(s.lastIndexOf('.') + 1);
    }

    private static String makeIdent() {
        String s = " ";
        for (int i = 0; i < currIndent; i++)
            s = s + "   ";

        return s;
    }

    public void write(int i) {
        String s;
        if (i == 10)
            s = "\r\n" + traceID + makeIdent();
        else
            s = String.valueOf((char)i);
        outBuffer += s;
        if (i == 10) {
            System.out.print(outBuffer);
            outBuffer = "";
        }
    }
}
