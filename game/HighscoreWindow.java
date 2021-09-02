package game;

//import java.applet.Applet;
import java.awt.*;
import litecom.ExceptionWindow;
import litecom.Debug;
//import litecom.sound.ScoreClient2;

public class HighscoreWindow extends Frame {

    private Button buttonOk;
    private Button buttonCancel;
    private TextField textFieldName;
    private TextField textFieldEmail;
    private Object ref;
    private int points;
    private int level;
    private boolean _fld01EC;
    private boolean flag;
    private String badWords[] = {
        "COCK", "COCKS", "IN ASS", "YOUR ASS", "NIGGA", "NIGGER", "HOE", "SUCKS DICK", "SUCK DICK", "SUCK MY DICK", 
        "SUCKS MY DICK", "WHORE", "SLUT", "FUCK", "FUCKS", "FUCKER", "FUCKERS", "FUCKING", "FUCKINGS", "BITCH", 
        "PUSSY", "CUNT", "PENIS", "PENISES"
    };
    GridBagLayout layout;
    GridBagConstraints contraints;
    private char chars[] = {
        '!', '?', '.', '-', ',', '_', '@', '\243', '\244', '%', 
        '&', '/', '(', ')', '=', '?', '+', '$', '"'
    };

    public HighscoreWindow(Object ref, int points, int level, boolean flag) {
        super("Congratulations!!! A high score!");
        this._fld01EC = false;
        this.ref = ref;
        this.points = points;
        this.level = level;
        this.flag = flag;
        init();
        resize(300, 150);
        show();
    }

    private void _mth017B(Component component) {
        layout.setConstraints(component, contraints);
        add(component);
    }

    private void init() {
        layout = new GridBagLayout();
        setLayout(layout);
        contraints = new GridBagConstraints();
        contraints.insets = new Insets(2, 0, 2, 0);
        contraints.weightx = 1.0D;
        contraints.gridwidth = 1;
        contraints.weightx = 1.0D;
        contraints.gridwidth = 1;
        contraints.anchor = 13;
        Object obj = new Label("Name: ");
        layout.setConstraints(((Component) (obj)), contraints);
        add(((Component) (obj)));
        contraints.gridwidth = 0;
        contraints.anchor = 17;
        obj = textFieldName = new TextField(30);
        layout.setConstraints(((Component) (obj)), contraints);
        add(((Component) (obj)));
        if (flag) {
            contraints.weightx = 1.0D;
            contraints.gridwidth = 1;
            contraints.anchor = 13;
            obj = new Label("Email: ");
            layout.setConstraints(((Component) (obj)), contraints);
            add(((Component) (obj)));
            contraints.gridwidth = 0;
            contraints.anchor = 17;
            obj = textFieldEmail = new TextField(30);
            layout.setConstraints(((Component) (obj)), contraints);
            add(((Component) (obj)));
        }
        contraints.insets = new Insets(25, 3, 2, 3);
        contraints.anchor = 13;
        contraints.gridwidth = 1;
        obj = buttonOk = new Button("OK");
        layout.setConstraints(((Component) (obj)), contraints);
        add(((Component) (obj)));
        contraints.anchor = 17;
        contraints.gridwidth = 0;
        obj = buttonCancel = new Button("CANCEL");
        layout.setConstraints(((Component) (obj)), contraints);
        add(((Component) (obj)));
    }

    public boolean handleEvent(Event event) {
        switch(event.id) {
        case 201: // Event.WINDOW_DESTROY
            hide();
            return true;

        case 1001: // Event.ACTION_EVENT
            if (event.target == buttonOk && !_fld01EC) {
                _fld01EC = true;
                if (textFieldName.getText().trim().equals("")) {
                    hide();
                } else {
                    if (!_mth0179(textFieldName.getText())) {
                        Debug.out("Pressed ok, updating highscore.");
                        core.main.showStatus("Saving highscore...");
                        try {
                            if (flag)
                                core.scoreClient2.put(points, textFieldName.getText() + "\t" + level + "\t" + textFieldEmail.getText());
                            else
                                core.scoreClient2.put(points, textFieldName.getText() + "\t" + level);
                        } catch(Exception exception) {
                            new ExceptionWindow(exception, "Could not save score.");
                        }
                        core.main.showStatus("Done.");
                    }
                    hide();
                }
            }
            if (event.target == buttonCancel) {
                Debug.out("Pressed cancel, closing.");
                hide();
            }
            return true;
        }
        return false;
    }

    private boolean _mth0179(String s) {
        s = s.toUpperCase();
        for (int j = 0; j < chars.length; j++)
            s = s.replace(chars[j], ' ');

        for (int k = 0; k < badWords.length; k++) {
            String s1 = badWords[k];
            for (int l = 0; l < s1.length(); l++)
                if (s.indexOf(" " + s1 + " ") != -1)
                    return true;

            if (s.indexOf(s1 + " ") == 0)
                return true;
            if (s.length() == s1.length() && s.equals(s1))
                return true;
            if (s.length() != s1.length() && s.indexOf(" " + s1) == s.length() - (s1.length() + 1))
                return true;
        }
        return false;
    }
}
