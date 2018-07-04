package caveswing.test;

import caveswing.core.CaveSwingParams;

public class CoolTestParams {

    // separate class to enable easy sharing between different
    // tests

    public static CaveSwingParams getParams() {
        CaveSwingParams params = new CaveSwingParams();
        params.maxTicks = 2000;
        params.width = 1200;
        params.nAnchors = 10;

        // todo: how does changing the parameter settings affect AI agent performance?
        // todo: Can you settings that make it really tough for the AI?
        params.gravity.y = 0.4;
        params.gravity.x = -0.0;
        params.hooke = 0.015;

        return params;
    }
}
