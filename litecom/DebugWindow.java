package litecom;

import java.awt.*;

class DebugWindow extends Frame {

    public TextArea text;

    public DebugWindow() {
        super("Debug");
        setLayout(new BorderLayout());
        text = new TextArea();
        text.setEditable(false);
        add("Center", text);
        resize(500, 300);
        show();
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
        case 201: // Event.WINDOW_DESTROY
            Debug.closeWindow();
            break;

        default:
            return false;
        }
        return true;
    }
}
