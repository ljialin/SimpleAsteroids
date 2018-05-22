package spinbattle.view;

import math.Vector2d;
import spinbattle.params.SpinBattleParams;
import utilities.SideStack;

public class ParticleEffect {

    // Enum types = {ShipExplosion};

    int nParticles = 20;
    Vector2d s;

    public ParticleEffect setPosition(Vector2d s) {
        this.s = s.copy();
        return this;
    }

    public ParticleEffect setNParticle(int nParticles) {
        this.nParticles = nParticles;
        return this;
    }

    public ParticleEffect trigger(SideStack<Particle> sideStack, SpinBattleParams params) {
        shipExplosion(sideStack, s, nParticles, params);
        return this;
    }

    public ParticleEffect shipExplosion(SideStack<Particle> sideStack, Vector2d s, int nParticles, SpinBattleParams params) {

        // add in the required number of particles

        for (int i=0; i<nParticles; i++) {
            Particle particle = new Particle().setPosition(s).setRandomVelocity(params);
            particle.setRandomLifeTime(50, 10, params);
            sideStack.push(particle);
        }
        return this;


    }
}
