package game;

import java.awt.*;

public class GotoLevelWindow extends Frame {

    private Button buttonOk;
    private TextField textField;

    public GotoLevelWindow() {
        super("Enter level");
        pack();
        resize(100, 70);
        init();
        show();
    }

    private void init() {
        setLayout(new FlowLayout());
        add(textField = new TextField(2));
        add(buttonOk = new Button("OK"));
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
        case 201: // Event.WINDOW_DESTROY
            hide();
            return true;

        case 1001: // Event.ACTION_EVENT
            if (event.target == buttonOk) {
                core.player.cheater = true;
                core.player.resetPoints();
                core.world.flag[111] = true;
                core.currLevel = Integer.parseInt(textField.getText()) - 1;
            }
            return true;
        }
        return false;
    }
}
