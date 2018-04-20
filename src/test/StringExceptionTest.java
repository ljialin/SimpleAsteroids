package test;

public class StringExceptionTest {

    public static void main ( String [] args ){
        System .out. println ( message ());
    }

    private static String message (){
        String s = "";
        try {
            throw new Exception ();
        }
        catch ( Exception e){
            s += " catch ";
        }
        finally {
            s += " finally ";
        }
        return s;
    }

}

