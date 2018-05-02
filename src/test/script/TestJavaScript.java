package test.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TestJavaScript {
    public static void main(String[] args) throws Exception {

        callScriptTest();
        System.exit(0);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        // JavaScript code in a String
        String script1 = (String) "function hello(name) {print ('Hello, ' + name);}";
        String script2 = (String) "function getValue(a,b) { if (a===\"Number\") return 1; else return b; }";
        // evaluate script
        engine.eval(script1);
        engine.eval(script2);

        Invocable inv = (Invocable) engine;

        inv.invokeFunction("hello", "Scripting!!");  //This one works.
    }

    public static void callScriptTest() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            System.out.println("okay1");
            FileInputStream fileInputStream = new FileInputStream("JavaScript/samples/SampleFunctions.js");
            System.out.println("okay2");
            if (fileInputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                engine.eval(reader);
                System.out.println("okay3");
                // Invocable javascriptEngine = null;
                System.out.println("okay4");
                Invocable invocableEngine = (Invocable) engine;
                System.out.println("okay5");
                int x = 9;
                System.out.println("invocableEngine is : " + invocableEngine);
                Object object = invocableEngine.invokeFunction("myProduct", x, x);

                System.out.println("Object = " + object);
                invocableEngine.invokeFunction("playSound2");
                System.out.println("okay6");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
