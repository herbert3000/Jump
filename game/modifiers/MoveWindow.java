package game.modifiers;

import java.awt.*;

public class MoveWindow extends ModifierWindow {

	private static final long serialVersionUID = 1L;
	
    private Button ok;
    private Button cancel;
    private Checkbox dirX;
    private Checkbox dirY;
    private Checkbox dirZ;
    private TextField speed1;
    private TextField speed2;
    private TextField delay1;
    private TextField delay2;
    private TextField len;
    private TextField phase;
    private MoveModifier m;
    GridBagLayout layout;
    GridBagConstraints gbc;

	public void setModifier(Modifier modifier) {
        m = (MoveModifier)modifier;
        setTitle("MoveModifier");
        pack();
        resize(270, 280);
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
        gbc.anchor = 13;
        Object obj = new Label("Direction: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        CheckboxGroup checkboxgroup = new CheckboxGroup();
        gbc.anchor = 17;
        gbc.weightx = 0.29999999999999999D;
        obj = dirX = new Checkbox("X", checkboxgroup, true);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        obj = dirY = new Checkbox("Y", checkboxgroup, false);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        obj = dirZ = new Checkbox("Z", checkboxgroup, false);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.weightx = 1.0D;
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Phase: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = phase = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Speed 1: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = speed1 = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Delay 1: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = delay1 = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Speed 2: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = speed2 = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Delay 2: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = delay2 = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Length: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = len = new TextField(3);
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
        phase.setText(String.valueOf(m.phase));
        speed1.setText(String.valueOf(m.speed1));
        speed2.setText(String.valueOf(m.speed2));
        delay1.setText(String.valueOf(m.delay1));
        delay2.setText(String.valueOf(m.delay2));
        len.setText(String.valueOf(m.len));
        switch(m.dir) {
        case 1:
            dirX.setState(true);
            return;

        case 2:
            dirY.setState(true);
            return;

        case 3:
            dirZ.setState(true);
            return;
        }
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
        case 201: // Event.WINDOW_DESTROY
            hide();
            return true;

        case 1001: // Event.ACTION_EVENT
            if (event.target == ok) {
                if (dirX.getState())
                    m.dir = 1;
                if (dirY.getState())
                    m.dir = 2;
                if (dirZ.getState())
                    m.dir = 3;
                m.phase = (new Double(phase.getText())).doubleValue();
                m.speed1 = (new Double(speed1.getText())).doubleValue();
                m.speed2 = (new Double(speed2.getText())).doubleValue();
                m.delay1 = (new Double(delay1.getText())).doubleValue();
                m.delay2 = (new Double(delay2.getText())).doubleValue();
                m.len = (new Double(len.getText())).doubleValue();
                hide();
            }
            if (event.target == cancel)
                hide();
            return true;
        }
        return false;
    }

    private void closeWindow() {
        hide();
    }
}
