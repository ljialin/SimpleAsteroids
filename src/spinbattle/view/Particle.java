package spinbattle.view;

import math.Vector2d;
import spinbattle.params.SpinBattleParams;
import spinbattle.util.MovableObject;

import java.awt.geom.Path2D;

public class Particle {
    double rotation;
    double rotationRate;
    MovableObject mo = new MovableObject();

    Path2D shape;
    int lifeTicks = 100;
    double speedScale = 2;


    // keep it simple for now though

    Particle setPosition(Vector2d s) {
        mo.s = s.copy();
        return this;
    }

    Particle setRandomVelocity(SpinBattleParams params) {
        mo.v = new Vector2d(params.getRandom().nextGaussian(), params.getRandom().nextGaussian());
        mo.v.mul(speedScale);
        return this;
    }

    Particle setRandomLifeTime(double mean, double sd, SpinBattleParams params) {
        lifeTicks = (int) (mean + sd * params.getRandom().nextGaussian());
        return this;
    }

    public Particle update() {
        lifeTicks--;
        mo.update(null);
        return this;
    }

    public boolean isDead() {
        return lifeTicks < 0;
    }

}
