package spinbattle.test;

import com.google.gson.Gson;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import spinbattle.core.SpinGameState;
import spinbattle.params.SpinBattleParams;
import wox.serial.ObjectWriter;
import wox.serial.SimpleWriter;
import wox.serial.TestObject;

import java.io.PrintWriter;

public class WoxTest {
    public static void main(String[] args) throws Exception {
        SpinBattleParams params = new SpinBattleParams();
        params.maxTicks = 100;
        params.useVectorField = false;
        params.useProximityMap = false;
        SpinGameState gameState1 = new SpinGameState().setParams(params).setPlanets();


        SpinBattleParams params2 = params.copy();
        params2.gravitationalFieldConstant = 0;

        SpinGameState gameState2 = ((SpinGameState) gameState1.copy()).setParams(params2);


        SpinGameState[] states = {gameState1, gameState2};

        ObjectWriter writer = new SimpleWriter();
        Element el = writer.write( states );
        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
        PrintWriter pw = new PrintWriter("data/res/SpinBattleState.xml");
        out.output( el, System.out );
        out.output(el, pw);
        pw.close();
        System.out.println("");



    }
}
