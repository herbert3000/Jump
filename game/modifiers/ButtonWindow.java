package game.modifiers;

import java.awt.*;
import litecom.Debug;

public class ButtonWindow extends ModifierWindow {

	private static final long serialVersionUID = 1L;
	
	private Button ok;
    private Button cancel;
    private TextField len;
    private TextField flag;
    private ButtonModifier m;
    GridBagLayout layout;
    GridBagConstraints gbc;

    public void setModifier(Modifier modifier) {
        m = (ButtonModifier)modifier;
        setTitle("Properties for ButtonModifier");
        pack();
        resize(170, 180);
        layItOut();
        show();
    }

    private void addC(Component component) {
        layout.setConstraints(component, gbc);
        add(component);
    }

    private void layItOut() {
        layout = new GridBagLayout();
        setLayout(layout);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.weightx = 1.0D;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0D;
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        Object obj = new Label("Displacement len: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = len = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.weightx = 1.0D;
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Flag: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = flag = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.insets = new Insets(25, 3, 2, 3);
        gbc.anchor = 13;
        gbc.gridwidth = 1;
        obj = ok = new Button("OK");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.anchor = 17;
        gbc.gridwidth = 0;
        obj = cancel = new Button("CANCEL");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        len.setText(String.valueOf(m.len));
        flag.setText(String.valueOf(m.flag));
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
        case 201: // Event.WINDOW_DESTROY
            hide();
            return true;

        case 1001: // Event.ACTION_EVENT
            if (event.target == ok) {
                Debug.out("Pressed ok, updating Displacement modifier.");
                m.len = (new Double(len.getText())).doubleValue();
                m.flag = Integer.parseInt(flag.getText());
                hide();
            }
            if (event.target == cancel) {
                Debug.out("Pressed cancel, closing.");
                hide();
            }
            return true;
        }
        return false;
    }

    private void closeWindow() {
        hide();
    }
}
