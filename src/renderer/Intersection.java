package renderer;

import obj.Triangle;
import scene.surface.Surface;

public class Intersection {
	
	Surface surf;
	double t;
	double beta,gamma;
	Triangle tri;
	
	public Intersection(Surface surf,double t) {
		this.t= t;
		this.surf = surf;
	}
	
	public Intersection(Surface surf, double t, double beta, double gamma, Triangle tri) {
		this.surf = surf;
		this.t = t;
		this.beta = beta;
		this.gamma = gamma;
		this.tri = tri;
	}

	public Surface getSurf() {
		return surf;
	}

	public double getT() {
		return t;
	}

	public double getBeta() {
		return beta;
	}

	public double getGamma() {
		return gamma;
	}

	public Triangle getTri() {
		return tri;
	}
}
