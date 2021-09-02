package litecom.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Hashtable;

public class SoundManager {

    private Hashtable<String, AudioClip> sounds;
    private Applet a;
    private String path;

    public SoundManager(Applet applet) {
        this(applet, null);
    }

    public SoundManager(Applet applet, String s) {
        sounds = new Hashtable<String, AudioClip>();
        a = applet;
        if (s == null)
            s = "";
        path = s;
    }

    public void add(String s) {
        sounds.put(s, a.getAudioClip(a.getDocumentBase(), path + s + ".au"));
    }

    public void play(String s) {
        AudioClip audioclip = (AudioClip)sounds.get(s);
        audioclip.play();
    }
}
