package litecom;

import java.applet.Applet;
//import java.awt.Component;
//import java.awt.TextArea;
import java.io.OutputStream;
//import java.io.PrintStream;

public class Debug extends OutputStream {

    public static boolean showDebug;
    public static boolean v;
    private static boolean windowMode;
    public static DebugWindow dw;
    public static Debug currentInstance;
    private String outBuffer;

    static  {
        new Debug();
    }

    public Debug() {
        outBuffer = "";
        currentInstance = this;
    }

    public static void tPrint(String s) {
        out(s);
    }

    public static void closeWindow() {
        if (dw != null)
            dw.hide();
        windowMode = false;
    }

    public static void openWindow() {
        if(dw == null && !windowMode)
            dw = new DebugWindow();
        windowMode = true;
    }

    public static void out(String s) {
        if(showDebug) {
            if(windowMode) {
                dw.text.appendText(s + "\n");
                return;
            }
            System.out.print("[DEBUG] " + s + "\r\n");
        }
    }

    public static void out(Object obj, String s) {
        if (showDebug)
            out(obj.getClass().getName() + ": " + s);
    }

    public static void start(Applet applet) {
        if (applet.getParameter("debug") != null && applet.getParameter("debug").equals("true"))
            showDebug = true;
        else
            showDebug = false;
        if (showDebug && String.valueOf(applet.getDocumentBase()).startsWith("http") && System.getProperty("browser") != null && System.getProperty("browser").startsWith("ActiveX"))
            openWindow();
    }

    public void write(int i) {
        if (showDebug) {
            String s;
            if (i == 10 && !windowMode)
                s = "\r\n";
            else
                s = "" + (char)i;
            outBuffer += s;
            if (i == 10) {
                if(windowMode)
                    dw.text.appendText(outBuffer);
                else
                    System.out.print(outBuffer);
                outBuffer = "";
            }
        }
    }
}
