package sound;

import plot.LineChart;
import plot.LineChartAxis;
import utilities.JEasyFrame;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;

// Demonstration of the 'clicking sound' fault in Java 7 & 8
// Repeatedly play a short sample of silence (happens on other samples too).
public class PlaySynth {
    private static int SAMPLE_RATE = 48000;
    private static int FRAME_SIZE = 200;
    private static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            (float) SAMPLE_RATE, 8, 1, FRAME_SIZE, SAMPLE_RATE, false);

    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        // AudioListener listener = new AudioListener();
//        Clip clip = oneSecondSilentClip();
//        System.out.println("Clip is a " + clip);
//        clip.addLineListener(listener);

        double freq = 400;
        double mod = 7;
        double modAmp = 1000;
        double duration = 3;
        SimpleSynth synth = new SimpleSynth().setSampleRate(SAMPLE_RATE).setMod(mod).setModAmp(modAmp);
        byte[] bytes = synth.getBytes(duration, freq);
        plotStart(bytes, 2001);
        System.out.println("Starting");
        for (int i = 0; i < 1; i++) {
            // listener.reset();

            System.out.println("Playing..."); //  + System.currentTimeMillis());
            Clip clip = byteClip(bytes);
            // clip.setFramePosition(0);
            clip.start();
            // clip.drain(); // just to make sure we've finished the clip; the listener is more reliable?

            // On OSX Java 1.6.0_65, you just hear silence here as expected.
            // On OSX Java 1.8.0_25 and 1.7.0_75, at the end of each playback of the clip, there's a
            // very noticeable click.

            // listener.waitUntilDone();

            System.out.println("Started...");
            Thread.sleep(1000);
        }
        System.out.println("Done");
        // clip.removeLineListener(listener);
        // clip.close();
        Thread.sleep(3000);
    }

    static void plotStart(byte[] b, int len) {
        len = Math.min(b.length, len);
        // double[] x = new double[len];
        ArrayList<Double> x = new ArrayList<>();
        for (int i=0; i<len; i++) x.add((double) b[i]);
        System.out.println(x);
        LineChart lineChart = LineChart.easyPlot(x);
        lineChart.yAxis = new LineChartAxis(-128, 128, 5);
        new JEasyFrame(lineChart, "Easy Plot Test");

    }

    public static Clip byteClip(byte[] out) throws LineUnavailableException {
        Clip clip = AudioSystem.getClip();
        clip.open(FORMAT, out, 0, out.length);
        clip.setFramePosition(0);
        return clip;
    }


}