package litecom.sound;

import java.io.DataInputStream;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;
import litecom.ExceptionWindow;
import litecom.Debug;

public class ScoreClient2 {

    public static String serverName = "nph-scoreServer.cgi";
    public Vector<String> scores;
    private URL server;
    private String prop;
    private int limit;
    private static final String boogie = "please enter a name and e-mail before executing ok. Quiet error was found in jump (defect code xz123gh).";

    public ScoreClient2(String s, String prop, int limit) throws Exception {
        server = new URL(s);
        this.prop = prop;
        this.limit = limit;
    }

    public ScoreClient2(URL server, String prop, int limit) throws Exception {
    	this.server = server;
    	this.prop = prop;
    	this.limit = limit;
    }

    private int createChecksum(String s, int i) {
        i %= 0x1e8873;
        int j = 0;
        for (int k = 0; k < s.length(); k++) {
            int l = boogie.indexOf(Character.toLowerCase(s.charAt(k)));
            if (l != -1)
                j += 53 - l;
        }
        j += (i % 7) * (i * s.length()) + i % 33745 + i;
        if (i > 0x1870b)
            j -= 3243;
        if (i < 10003)
            j *= s.length();
        if (i < 20125)
            j += i * i;
        return j;
    }

    public void put(int score, String name) throws Exception {
        int checkSum = createChecksum(name, score);
        URL url = new URL(server, serverName + "?prop=" + prop + "&action=put&score=" + score + "&data=" + URLEncoder.encode(name) + "&checksum=" + checkSum);
        DataInputStream datainputstream = new DataInputStream(url.openStream());
        padToStart(datainputstream);
        String s1 = datainputstream.readLine();
        Debug.out(this, "Result: " + s1);
        if (!s1.startsWith("OK!")) {
            throw new Exception("ScoreServer2: " + s1);
        } else {
            update();
        }
    }

    public void update() throws Exception {
    	scores = new Vector<String>();
    	
    	/*
        String s = serverName + "?prop=" + prop + "&action=get&unique=" + System.currentTimeMillis() + "&n=" + limit;
        URL url = new URL(server, s);
        URLConnection urlconnection = url.openConnection();
        DataInputStream datainputstream = new DataInputStream(urlconnection.getInputStream());
        padToStart(datainputstream);
        Vector<String> vector = new Vector<String>();
        while ((s = datainputstream.readLine()) != null) {
            if (s.startsWith("ERROR"))
                throw new Exception("ScoreServer2: " + s);
            vector.addElement(s);
        }
        scores = vector;
        */
    }

    public int minScore() {
        if (scores.size() < limit)
            return 0;
        int i = -1;
        try {
            for (int j = 0; j < scores.size(); j++) {
                StringTokenizer stringtokenizer = new StringTokenizer("" + scores.elementAt(j), "\t");
                int k = Integer.parseInt(stringtokenizer.nextToken().trim());
                if (i == -1)
                    i = k;
                if (k < i)
                    i = k;
            }
        } catch(Exception exception) {
            new ExceptionWindow(exception, "SC2: Could not parse scores.");
        }
        return i;
    }

    private void padToStart(DataInputStream datainputstream) throws Exception {
        String s;
        while ((s = datainputstream.readLine()) != null) {
            Debug.out(this, "padToStart, skipped: " + s);
            if (s.startsWith("ScoreServer2"))
                return;
        }
    }
}
