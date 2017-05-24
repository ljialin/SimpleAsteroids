package test;

/**
 * Created by simonmarklucas on 24/05/2017.
 */
public class RandomSequenceTest {

    // this is just a random test string
    static String str = "00010000111100000011111000001111111000000000111111100001110001110000011011000011111111100001111110000111000111";


    // now

    public static void main(String[] args) throws Exception {


        char[] chars = str.toCharArray();
        System.out.println(chars);
        int ix = 0;
        for (int i=0; i<chars.length; i++) {
            System.out.println(chars[i]);
        }




    }

    class RandEntry {
        String personId;
        String sequence;

    }

}
