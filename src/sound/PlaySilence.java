
package sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

// Demonstration of the 'clicking sound' fault in Java 7 & 8
// Repeatedly play a short sample of silence (happens on other samples too).
public class PlaySilence {
    private static int SAMPLE_RATE = 48000;
    private static int FRAME_SIZE = 2;
    private static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            (float) SAMPLE_RATE, 8, 1, FRAME_SIZE, SAMPLE_RATE, false);

    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        AudioListener listener = new AudioListener();
        Clip clip = oneSecondSilentClip();
        System.out.println("Clip is a " + clip);
        clip.addLineListener(listener);

        System.out.println("Starting");
        for (int i = 0; i < 10; i++) {
            listener.reset();

            System.out.println("Playing..." + System.currentTimeMillis());
            clip.setFramePosition(0);
            clip.start();
            clip.drain(); // just to make sure we've finished the clip; the listener is more reliable?

            // On OSX Java 1.6.0_65, you just hear silence here as expected.
            // On OSX Java 1.8.0_25 and 1.7.0_75, at the end of each playback of the clip, there's a
            // very noticeable click.

            listener.waitUntilDone();
        }
        System.out.println("Done");
        clip.removeLineListener(listener);
        clip.close();
    }

    private static Clip oneSecondSilentClip() throws LineUnavailableException {
        int samples = (int) (SAMPLE_RATE * (double) 1);
        byte[] out = new byte[samples];
        Clip clip = AudioSystem.getClip();
        clip.open(FORMAT, out, 0, out.length);
        clip.setFramePosition(0);
        return clip;
    }

    // Adapted from http://stackoverflow.com/questions/577724/trouble-playing-wav-in-java/577926#577926
    // Thanks, McDowell.
    static class AudioListener implements LineListener {
        private boolean done = false;

        public synchronized void reset() {
            done = false;
        }

        public synchronized void update(LineEvent event) {
            Type eventType = event.getType();
            System.out.println("event " + eventType);
            if (eventType == Type.STOP || eventType == Type.CLOSE) {
                done = true;
                notifyAll();
            }
        }

        public synchronized void waitUntilDone() throws InterruptedException {
            while (!done) {
                wait();
            }
        }
    }

}