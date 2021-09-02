package litecom;

import java.awt.*;
import java.io.PrintStream;

public class ExceptionWindow extends Frame {

	private static final long serialVersionUID = 1L;
	
	public static boolean opened;

    public ExceptionWindow(Throwable throwable, String s) {
        super("Error - " + s);
        while(opened) 
            return;
        setLayout(new GridLayout(5, 1));
        add(new Label("An error has occured!"));
        add(new Label("Error message: " + s));
        add(new Label("Exception: " + throwable));
        add(new Label("Exception message: " + throwable.getMessage()));
        add(new Label("Please report this to maciek@litecom.se."));
        throwable.printStackTrace(new PrintStream(Debug.currentInstance, true));
        resize(300, 115);
        show();
        opened = true;
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
        case 201: // Event.WINDOW_DESTROY
            hide();
            opened = false;
            break;

        default:
            return false;
        }
        return true;
    }
}
