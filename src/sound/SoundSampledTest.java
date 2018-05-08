package sound;

import java.io.*;
import javax.sound.sampled.*;

public class SoundSampledTest {
    /**
     * Use SourceDataLine to read line-by-line from the external sound file.
     * For computer game, use Clip to pre-load short-duration sound files.
     */
    public static void main(String[] args) {
        SourceDataLine soundLine = null;
        int BUFFER_SIZE = 64 * 1024;  // 64 KB

        // Set up an audio input stream piped from the sound file.
        try {
            File soundFile = new File("sounds/fire.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            soundLine = (SourceDataLine) AudioSystem.getLine(info);
            soundLine.open(audioFormat);
            soundLine.start();
            int nBytesRead = 0;
            byte[] sampledData = new byte[BUFFER_SIZE];
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
                if (nBytesRead >= 0) {
                    // Writes audio data to the mixer via this source data line.
                    soundLine.write(sampledData, 0, nBytesRead);
                }
                System.out.println("Read/wrote: " + nBytesRead);
            }
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } finally {
            soundLine.drain();
            soundLine.close();
        }
    }
}
