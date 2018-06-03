package caveswing.core;

import ggi.core.AbstractGameState;
import math.Vector2d;

public class CaveGameState implements AbstractGameState {

    public Map map;
    CaveSwingParams params;

    Actuator actuator;

    public int nextAnchorIndex;
    public int nTicks;

    public MovableObject avatar;
    boolean gameOver;
    // boolean isAttached;
    public Anchor currentAnchor;

    public CaveGameState setParams(CaveSwingParams params) {
        this.params = params;
        return this;
    }

    public CaveGameState setup() {
        map = new Map().setup(params);
        avatar = new MovableObject();
        avatar.s = new Vector2d(params.width/10, params.height/2);
        avatar.v = new Vector2d();
        return this;
    }

    @Override
    public AbstractGameState copy() {
        CaveGameState cgs = new CaveGameState();
        // shallow copy the map and the current Anchor
        cgs.map = map;
        cgs.currentAnchor = currentAnchor;
        cgs.nextAnchorIndex = nextAnchorIndex;
        cgs.nTicks = nTicks;
        // deep copy the avatar and the params
        cgs.avatar = avatar.copy();
        cgs.params = params.copy();
        cgs.gameOver = gameOver;

        return cgs;
    }

    @Override
    public AbstractGameState next(int[] actions) {
        // the array of actions is to allow for a multi-player game
        // quick return if game over
        if (isTerminal()) return this;

        // otherwise let's calculate the updates
        int action = actions[0];

        Vector2d resultantForce = params.gravity.copy();

        // now will it be to attach or to release?
        if (action == Constants.actionAttach) {
            if (currentAnchor == null) {
                // if already attached, do nothing
                // if not yet attached, attach to the next one if available
                // currentAnchor = map.getAnchor(nextAnchorIndex);
                currentAnchor = map.getClosestAnchor(avatar.s);
                if (currentAnchor != null) {
                    // nextAnchorIndex++;
                }
            }
            // now if there is an anchor, apply the necessary force
            if (currentAnchor != null) {
                Vector2d tension = currentAnchor.getForce(avatar.s);
                resultantForce.add(tension);
            }
        } else if (action == Constants.actionRelease) {
            currentAnchor = null;
        }

        avatar.update(resultantForce, params.lossFactor);

        nTicks++;

        return this;
    }

    @Override
    public int nActions() {
        return 2;
    }

    @Override
    public double getScore() {
        // reward increasing x-position
        // reward a high Y- position
        // punish use of game ticks (i.e. want to get there as quickly as possible
        // it's important that nTicks is not incremented after the game is over
        // add in success bonus
        double score = avatar.s.x * params.pointPerX + avatar.s.y * params.pointPerY
                - nTicks * params.costPerTick;
        if (avatar.s.x >= params.width) score += params.successBonus;
        if (avatar.s.y < 0 || avatar.s.y >= params.height) score-= params.failurePenalty;
        return score;
    }

    @Override
    public boolean isTerminal() {
        // if already over, then return quickly
        if (gameOver) return gameOver;

        // now test for game over
        if (nTicks >= params.maxTicks || !map.bounds.contains(avatar.s.x, avatar.s.y)) {
            gameOver = true;
        }
        return gameOver;
    }
}

