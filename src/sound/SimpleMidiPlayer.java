package sound;
import javax.sound.midi.*;

public class SimpleMidiPlayer {

    static int maxNote = 127;

    public static void main(String[] args) throws Exception {
        // demonstrate how to use the sound player
        SimpleMidiPlayer player = new SimpleMidiPlayer(0, 117);

        // run through some notes
        for (int i = 55; i < 75; i+=1) {

            // player.synth.getChannels()[player.channel].programChange(1, i);

            int note = i;
            System.out.println("Playing: " + i);
            player.play(note);
            Thread.sleep(500);
            player.stop(note);
        }
        // allow notes to finish playing
        Thread.sleep(200);
        player.close();
        System.exit(0);
    }

    static long timeStamp = -1;
    Synthesizer synth;
    Receiver rcvr;
    ShortMessage myMsg;
    int volume = 127;      // default of max volume
    int channel = 1; // channel selects the virtual instrument to play on

    int bank, program;

    public SimpleMidiPlayer(int bank, int program) {
        this.bank = bank;
        this.program = program;
        try {
            synth = MidiSystem.getSynthesizer();
            System.out.println("Default Synth: " + synth);
            // MidiChannel ch = null;
            // ch.controlChange(0, 0);

            synth.open();

            synth.loadAllInstruments(synth.getDefaultSoundbank());
            // synth.remapInstrument(103, 1);
            rcvr = synth.getReceiver();

            System.out.println("rcvr = " + rcvr);
            System.out.println("Opened it...");
            myMsg = new ShortMessage();
            synth.getChannels()[channel].programChange(bank, program);
            showInstruments();
            // myMsg.
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public SimpleMidiPlayer() {
        // default bank and program
        this(0, 0);
    }

    public void close() {
        synth.close();
    }

    public void play(int pitch) {
        if (pitch <= maxNote) {
            play(pitch, volume);
        }
    }

    public void stop(int pitch) {
        if (pitch <= maxNote) {
            try {
                myMsg.setMessage(ShortMessage.NOTE_OFF, channel, pitch, volume);
                rcvr.send(myMsg, timeStamp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void play(int pitch, int volume) {
        try {
            myMsg.setMessage(ShortMessage.NOTE_ON, channel, pitch, volume);

            rcvr.send(myMsg, timeStamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInstruments() {
        for (Instrument ins : synth.getLoadedInstruments()) {
            System.out.println(ins);
        }

//        System.out.println("Channels");
//        for (MidiChannel ch : synth.getChannels()) {
//            System.out.println(ch);
//            ch.programChange(2, 0);
//        }
    }
}

