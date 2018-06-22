package renderer;

import java.util.ArrayList;

import obj.OBJParser;
import obj.Triangle;
import scene.surface.*;
import util.Vec3;

public class Ray {

	Vec3 origin, direction;

	public Ray(Vec3 origin, Vec3 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Ray addEpsilon() {
		return new Ray(origin.addWith(direction.multiplyWithScalar(0.001)), direction);
	}

	public Vec3 getIP(double t) {
		return origin.addWith(direction.multiplyWithScalar(t));
	}

	public Ray getTransformedRay(Surface s) {
		Vec3 newOrg = s.getTransformationMatrix().multiplyPoint(origin);
		Vec3 newDir = s.getTransformationMatrix().multiplyVector(direction);
		return new Ray(newOrg, newDir);
	}

	public Vec3 calculateTransmittanceDirection(Vec3 normal, double iof) {
		Vec3 Tnormal = normal.normalize();
		double nt = iof;
		double n = 1;

		Vec3 dir = direction.normalize();

		double rdotn = dir.dotproductWith(Tnormal);
		if (rdotn >= 0) {
			Tnormal = Tnormal.negate();
			n = iof;
			nt = 1;
		} else {
			rdotn = -rdotn;
		}

		double disc = 1 - ((Math.pow(n, 2) * (1 - Math.pow(rdotn, 2))) / Math.pow(nt, 2));
		Vec3 T = new Vec3(0, 0, 0);
		if (disc > 0) {
			T = dir.addWith(Tnormal.multiplyWithScalar(rdotn));
			T = T.multiplyWithScalar(n);
			T = T.dividedByScalar(nt);
			T = T.subtractedWith(Tnormal.multiplyWithScalar(Math.sqrt(disc)));
		} else {
			//return calculateReflectanceDirection(normal);
			return null;
		}
		return T.normalize();
	}

	public Vec3 calculateReflectanceDirection(Vec3 normal) {
		normal = normal.normalize();
		Vec3 dir = direction.normalize();
		double ndotr = dir.dotproductWith(normal);
		Vec3 normalmultipscal = normal.multiplyWithScalar(2 * ndotr);
		return dir.subtractedWith(normalmultipscal).normalize();
	}
	
	public Vec3 calculateInternalReflectionDirection(Vec3 normal){
		Vec3 Tnormal = normal.normalize();
		Vec3 dir = direction.normalize();
		double rdotn = dir.dotproductWith(Tnormal);
		if (rdotn >= 0) {
			Tnormal = Tnormal.negate();
		}
		return calculateReflectanceDirection(Tnormal);
	}

	/**
	 * Calculates if a sphere is intersecting the ray If so, returns the
	 * distance
	 * 
	 * @param c
	 *            the origin of the sphere
	 * @param r
	 *            the radius of the sphere
	 * @param ray
	 *            Calculates if a sphere is intersecting the ray If so, returns
	 *            the distance
	 * @return the distance t to the sphere or 0 if ray doesn't intersect
	 */
	public Intersection intersectionTestSphere(Sphere s) {
		Vec3 e = origin;
		Vec3 d = direction;
		Vec3 c = s.getPosition();
		double r = s.getRadius();
		Vec3 eminc = e.subtractedWith(c);
		double discriminant = Math.pow(d.dotproductWith(eminc), 2)
				- d.dotproductWith(d) * ((eminc.dotproductWith(eminc)) - Math.pow(r, 2));
		if (discriminant == 0) {
			double t = -d.dotproductWith(e.subtractedWith(c)) / (d.dotproductWith(d));
			return new Intersection(s, t);
		} else if (discriminant > 0) {
			discriminant = Math.sqrt(discriminant);
			double plus = (-d.dotproductWith(e.subtractedWith(c)) + discriminant) / (d.dotproductWith(d));
			double minus = (-d.dotproductWith(e.subtractedWith(c)) - discriminant) / (d.dotproductWith(d));
			if (plus < 0 && minus > 0) {
				return new Intersection(s, minus);
			} else if (minus < 0 && plus > 0) {
				return new Intersection(s, plus);
			} else if (plus > minus && minus > 0) {
				return new Intersection(s, minus);
			} else if (minus > plus && plus > 0) {
				return new Intersection(s, plus);
			}
		}
		return new Intersection(s, 0);
	}

	/**
	 * Calculates if a Mesh is intersecting the ray If so, returns the distance
	 * 
	 * @param m
	 *            Mesh to test to
	 * @param obj
	 *            the OBJParser Object of the Mesh
	 * @param ray
	 *            Calculates if a sphere is intersecting the ray
	 * @param set
	 *            Determines if the beta,gamma, and the three vertices needs to
	 *            be set in Nearest
	 * @return the distance t to the Mesh or 0 if the ray doesn't intersect
	 */
	public Intersection intersectionTestMesh(Mesh m) {
		Vec3 ev = origin;
		Vec3 dv = direction;
		OBJParser obj = m.getObj();
		ArrayList<Triangle> triangles = obj.getTriangles();
		ArrayList<Vec3> vertices = obj.getVertices();

		double t = Double.MAX_VALUE;
		Triangle nearest = null;
		double nearestBeta = 0, nearestGamma = 0;

		for (Triangle tri : triangles) {
			Vec3 av = vertices.get((int) (tri.getVertex1().getX() - 1));
			Vec3 bv = vertices.get((int) (tri.getVertex2().getX() - 1));
			Vec3 cv = vertices.get((int) (tri.getVertex3().getX() - 1));

			double a = av.getX() - bv.getX();
			double b = av.getY() - bv.getY();
			double c = av.getZ() - bv.getZ();
			double d = av.getX() - cv.getX();
			double e = av.getY() - cv.getY();
			double f = av.getZ() - cv.getZ();
			double g = dv.getX();
			double h = dv.getY();
			double i = dv.getZ();
			double j = av.getX() - ev.getX();
			double k = av.getY() - ev.getY();
			double l = av.getZ() - ev.getZ();

			double M = a * (e * i - h * f) + b * (g * f - d * i) + c * (d * h - e * g);
			double beta = (j * (e * i - h * f) + k * (g * f - d * i) + l * (d * h - e * g)) / M;
			if (beta >= 0) {
				double gamma = (i * (a * k - j * b) + h * (j * c - a * l) + g * (b * l - k * c)) / M;
				if (gamma >= 0 && gamma + beta <= 1) {
					double currentT = -(f * (a * k - j * b) + e * (j * c - a * l) + d * (b * l - k * c)) / M;
					if (currentT > 0 && currentT < t) {
						t = currentT;
						nearest = tri;
						nearestBeta = beta;
						nearestGamma = gamma;
					}
				}
			}
		}
		if (nearest != null) {
			return new Intersection(m, t, nearestBeta, nearestGamma, nearest);
		} else {
			return new Intersection(m, 0, 0, 0, null);
		}
	}

	public Ray normalize() {
		return new Ray(origin, direction.normalize());
	}

	public Vec3 getOrigin() {
		return origin;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public void print() {
		System.out.print("org: ");
		origin.print();
		System.out.print("direction: ");
		direction.print();
		System.out.println();
	}

}
