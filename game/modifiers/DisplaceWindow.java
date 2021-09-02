package game.modifiers;

import java.awt.*;
import litecom.Debug;
//import litecom.inca.Point3d;

public class DisplaceWindow extends ModifierWindow {

	private static final long serialVersionUID = 1L;
	
	private Button ok;
    private Button cancel;
    private TextField disX;
    private TextField disY;
    private TextField disZ;
    private DisplaceModifier m;
    GridBagLayout layout;
    GridBagConstraints gbc;

    public void setModifier(Modifier modifier) {
        m = (DisplaceModifier)modifier;
        setTitle("Properties for DisplaceModifier");
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
        Object obj = new Label("X displacement: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = disX = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Y displacement: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = disY = new TextField(3);
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        obj = new Label("Z displacement: ");
        layout.setConstraints(((Component) (obj)), gbc);
        add(((Component) (obj)));
        gbc.gridwidth = 0;
        gbc.anchor = 17;
        obj = disZ = new TextField(3);
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
        disX.setText(String.valueOf(m.displace.x));
        disY.setText(String.valueOf(m.displace.y));
        disZ.setText(String.valueOf(m.displace.z));
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
        case 201: // Event.WINDOW_DESTROY
            hide();
            return true;

        case 1001: // Event.ACTION_EVENT
            if (event.target == ok) {
                Debug.out("Pressed ok, updating Displacement modifier.");
                m.displace.x = (new Double(disX.getText())).doubleValue();
                m.displace.y = (new Double(disY.getText())).doubleValue();
                m.displace.z = (new Double(disZ.getText())).doubleValue();
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
