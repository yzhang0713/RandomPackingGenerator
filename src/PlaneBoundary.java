public class PlaneBoundary implements Boundary{
    // This is the plane boundary class
    // Only consider planes that are perpendicular to horizontal plane
    // The plane can be expressed in expression: ax + by + c = 0;
    private double a;
    private double b;
    private double c;
    // The inside of boundary can be defined by the dir parameter
    // dir should be defined as 1.0 or -1.0
    // For any point (xp, yp), (a * xp + b * yp + c) * dir > 0 stands for point inside plane
    private double dir;

    public PlaneBoundary(double a, double b, double c, double dir) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.dir = dir;
    }

    @Override
    public boolean isBoundaryViolation(double x, double y, double r) {
        // Check the c value after the original plane shifted by radius
        if (a != 0) {
            double cShift = c - r * a * Math.sqrt(a * a + b * b) / dir / Math.abs(a);
            return (a * x + b * y + cShift) * dir < 0;
        } else {
            return (b * y + c - b * dir * r) < 0;
        }
    }

    @Override
    public double penetrationDepth(double x, double y, double r) {
        return r - Math.abs(a*x+b*y+c)/Math.sqrt(a*a+b*b);
    }

    @Override
    public double[] contOneBoundOneBase(double x, double y, Particle first, FirstBaseParticleInfo firstBase) {
        return null;
    }

    @Override
    public double[] contOneBoundTwoBase() {
        return null;
    }

    @Override
    public double[] contTwoBoundOneBase() {
        return null;
    }
}
