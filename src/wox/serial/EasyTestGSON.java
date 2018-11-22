package wox.serial;

import com.google.gson.Gson;

import java.io.PrintWriter;

public class EasyTestGSON {
    public static void main(String[] args) throws Exception {
        TestObject ob = new TestObject(5);
        ob.to = null;
        Gson gson = new Gson();
        System.out.println(ob.inc());
        String out = gson.toJson(ob);
        System.out.println("JSON: " + out);
        PrintWriter pw = new PrintWriter("test.gson");

        pw.println(out);
        pw.close();

        Object back = gson.fromJson(out, TestObject.class);
        System.out.println("Loaded object back in");
        System.out.println(((TestObject) back).inc());
    }
}
