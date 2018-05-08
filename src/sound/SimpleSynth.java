package sound;

public class SimpleSynth {
    double sampleRate = 48000;
    double mod = 10;
    double modAmp = 10;



    public SimpleSynth setSampleRate(double sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public SimpleSynth setMod(double mod) {
        this.mod = mod;
        return this;
    }

    public SimpleSynth setModAmp(double modAmp) {
        this.modAmp = modAmp;
        return this;
    }

    public byte[] getBytes(double duration, double freq) {
        int len = (int) (duration * sampleRate);
        byte[] bytes = new byte[len];
        for (int i=0; i<len; i++) {
            bytes[i] = sinByte(i, freq);
        }
        return bytes;
    }

    public byte sinByte(int i, double freq) {
        // double freq = 200;

        double x = 127 * Math.sin (freqShift2(i) * freq * Math.PI * 2 / sampleRate);
        // double x = 127 * Math.sin (i * freq * Math.PI * 2 / sampleRate);
        return (byte) x;

    }

    public double freqShift(double x) {
        double freq = 10;
        double v = x * (0.9 + 0.1 * Math.sin(x * freq * Math.PI * 2 / sampleRate));
        return v;
    }



    public double freqShift2(double x) {
        // double freq = 10;
        double v = x + (modAmp *  Math.sin(x * mod * Math.PI * 2 / sampleRate));
        return v;
    }


}
