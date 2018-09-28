package sound;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundSandbox {
    /**
     * Number of samples per second 44100 is standard, this is configurable
     */
    public static final int SAMPLE_RATE = 44100;

    /**
     * This isn't configurable, for now. Do not change.
     */
    public static final int BITS_PER_SAMPLE = 16;

    /**
     * File length in seconds. This is configurable.
     */
    public static final int FILE_LENGTH = 5;

    // For real time sample playback
    private static AudioFormat mAudioFormat;
    private static SourceDataLine mDataLine;
    private static int mPlayPos;

    public static void main(String[] args) {
        double[][] samples = new double[3][SAMPLE_RATE * FILE_LENGTH]; //Will be casted to short but used double to keep preserve quality until the endS

        // ===== Generate Samples Here =====
        double a = 440.0;
        double d = 293.7;
        double f_sharp = 370.0;

        for (int i = 0; i < samples[0].length; i++) {
            samples[0][i] = (getSampleAtFrequency(a, i, 0xfff)); //A tone
        }
        for (int i = 0; i < samples[0].length; i++) {
            samples[1][i] = (getSampleAtFrequency(d, i, 0xfff)); //D tone
        }
        for (int i = 0; i < samples[0].length; i++) {
            samples[2][i] = (getSampleAtFrequency(f_sharp, i, 0xfff)); //F# tone
        }

        playSamples(samples);
        saveSamples(samples, "sounds/multi.wav", false);
        saveSamples(samples, "sounds/mono.wav", true);

        /*Real-time sound playing example.
        stopRealTime() included in case you don't want it to play infinetely,
        in which case you should clean up resources with it when you're done playing*/

        /*initRealTime();
        while (true) {
            short[] sample = new short[1];
            sample[0] = getSampleAtFrequency(violin_a, mPlayPos, 0xFFF);
            playSampleInRealTime(sample);
        }
        stopRealTime();*/
    }

    /**
     * Returns an amplitude value between -1 and 1 based on the parameters
     *
     * @param frequency
     *            frequency in hz
     * @param sampleNum
     *            number of samples into the wave
     * @return sample value
     */
    public static double getSampleAtFrequency(double frequency, int sampleNum) {
        return Math.sin(frequency * (2 * Math.PI) * sampleNum / SAMPLE_RATE);
    }

    /**
     * Returns an amplitude value between `-amplitude` and `amplitude` based on the parameters
     *
     * @param frequency
     *            frequency in hz
     * @param sampleNum
     *            number of samples into the wave
     * @param amplitude
     *            number to scale the amplitude by
     * @return sample value
     */
    public static double getSampleAtFrequency(double frequency, int sampleNum, double amplitude) {
        return (amplitude * getSampleAtFrequency(frequency, sampleNum));
    }

    /**
     * Play samples through the speakers
     *
     * @param samples
     *            samples to play
     */
    public static void playSamples(double[][] samples) {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
        Info info = new Info(SourceDataLine.class, format);
        SourceDataLine dataLine = null;
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        if (dataLine != null) {
            dataLine.start();

            byte[] sampleBytes = convertSamplesToBytes(samples, true);

            int pos = 0;
            while (pos < sampleBytes.length) {
                pos += dataLine.write(sampleBytes, pos, Math.min(dataLine.getBufferSize(), sampleBytes.length - pos));
            }

            dataLine.drain();
            dataLine.close();
        }
    }

    /**
     * Initialize the sound streams before playing in real time
     */
    public static void initRealTime() {
        mAudioFormat = new AudioFormat(SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
        Info info = new Info(SourceDataLine.class, mAudioFormat);
        mDataLine = null;
        try {
            mDataLine = (SourceDataLine) AudioSystem.getLine(info);
            mDataLine.open(mAudioFormat);
            mDataLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Garbage collect the sound streams after playing in real time
     */
    public static void stopRealTime() {
        mDataLine.drain();
        mDataLine.close();
    }

    /**
     * Plays sound sample by sample for an unlimited* amount of time Make sure not to spend too much time generating each sample because the audio will skip if you don't keep up with your sample rate.
     *
     * *actually plays for about 13.5 hours before overflowing the counting integer when playing at 44.1KHz
     *
     * @param sample sample to play (multichannel)
     */

    public static void playSampleInRealTime(short[] sample) {
        if (mDataLine != null) {
            byte[] sampleByte = { 0, 0 };
            for (int i = 0; i < sample.length; i++) {
                byte[] temp = shortToByteArray(sample[i]);
                sampleByte[0] += temp[0];
                sampleByte[1] += temp[1];
            }

            mDataLine.write(sampleByte, 0, sampleByte.length);
            mPlayPos++;
        }
    }

    /**
     * Same samples to a wav file
     *
     * @param samples
     *            samples to write to the wav file
     * @param outfile
     *            file to save to
     * @return whether saving was successful or not
     */
    public static boolean saveSamples(double[][] samples, String outfile, boolean mono) {
        byte[] byteSamples = convertSamplesToBytes(samples, mono);
        short numChannels;

        if (mono) {
            numChannels = 1;
        } else {
            numChannels = (short) samples.length;
        }

        try {
            DataOutputStream outStream = new DataOutputStream(new FileOutputStream(new File(outfile)));

            // write the wav file per the wav file format
            outStream.writeBytes("RIFF"); // 00 - RIFF
            outStream.write(intToByteArray(36 + byteSamples.length), 0, 4); // 04 - how big is the rest of this file?
            outStream.writeBytes("WAVE"); // 08 - WAVE
            outStream.writeBytes("fmt "); // 12 - fmt
            outStream.write(intToByteArray(16), 0, 4); // 16 - size of this chunk
            outStream.write(shortToByteArray((short) 1), 0, 2); // 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
            outStream.write(shortToByteArray(numChannels), 0, 2); // 22 - mono or stereo? 1 or 2? (or 5 or ???)
            outStream.write(intToByteArray(SAMPLE_RATE), 0, 4); // 24 - samples per second (numbers per second)
            outStream.write(intToByteArray((BITS_PER_SAMPLE / 8) * SAMPLE_RATE * numChannels), 0, 4); // 28 - bytes per second
            outStream.write(shortToByteArray((short) ((BITS_PER_SAMPLE / 8) * numChannels)), 0, 2); // 32 - # of bytes in one sample, for all channels
            outStream.write(shortToByteArray((short) BITS_PER_SAMPLE), 0, 2); // 34 - how many bits in a sample(number)? usually 16 or 24
            outStream.writeBytes("data"); // 36 - data
            outStream.write(intToByteArray(byteSamples.length), 0, 4); // 40 - how big is this data chunk
            outStream.write(byteSamples); // 44 - the actual data itself - just a long string of numbers

            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static byte[] convertSamplesToBytes(double[][] samples, boolean mono) {
        byte[] sampleBytes;
        if (mono) {
            sampleBytes = new byte[samples[0].length * 2];

            // splits up the shorts into separate bytes for the audio stream
            // Adds the channels together in this step as well so they both play at the same time
            for (int i = 0; i < sampleBytes.length; i++) {
                int it = i / 2;

                double sample = 0;
                for (int j = 0; j < samples.length; j++) {
                    sample += samples[j][it];
                }

                byte[] sampleS = shortToByteArray((short)sample);
                sampleBytes[i] = sampleS[0];
                i++;
                sampleBytes[i] = sampleS[1];
            }

        } else {
            sampleBytes = new byte[samples.length * samples[0].length * 2];

            for (int i = 0; i < sampleBytes.length;) {
                int it = i / (2 * samples.length);
                for (int j = 0; j < samples.length; j++) {
                    byte[] sample = shortToByteArray((short)samples[j][it]);
                    sampleBytes[i] = sample[0];
                    i++;
                    sampleBytes[i] = sample[1];
                    i++;
                }
            }
        }
        return sampleBytes;
    }

    /**
     * @param i
     *            data to convert
     * @return unsigned little endian bytes that make up i
     */
    private static byte[] intToByteArray(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i & 0x00FF);
        b[1] = (byte) ((i >> 8) & 0x000000FF);
        b[2] = (byte) ((i >> 16) & 0x000000FF);
        b[3] = (byte) ((i >> 24) & 0x000000FF);
        return b;
    }

    /**
     * @param data
     *            short to convert
     * @return unsigned little endian bytes that make up data
     */
    public static byte[] shortToByteArray(short data) {
        return new byte[] { (byte) (data & 0xff), (byte) ((data >>> 8) & 0xff) };
    }
}

