package Application;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Speech {

    /**
     * chuyen text thanh giong noi.
     */
    public void TextToSpeech(String word) {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
            VoiceManager voiceManager = VoiceManager.getInstance();
            Voice voice = voiceManager.getVoice("kevin");
            voice.allocate();
            voice.speak(word);
            voice.deallocate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openURL(String url) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI(url));
    }

    /**
     * them 1 tu vao lich su tim kiem.
     */
    public void addHistory(String word) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/History.txt"));
        ArrayList<String> history = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            history.add(line);
            line = reader.readLine();
        }
        reader.close();
        if (history.contains(word)) {
            return;
        }
        history.add(0, word);
        if (history.size() > 20) {
            history.remove(20);
        }
        Writer writer = new FileWriter("src/History.txt");
        for (String a : history) {
            if (a == null) {
                continue;
            }
            writer.write(a + "\n");
        }
        writer.close();
    }

}

