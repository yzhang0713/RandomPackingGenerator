public interface Boundary {
    // The container boundary interface
    // The boundary should provide several functions
    //      1. To check if particle is violate the boundary condition
    //      2. Compute the penetration depth of a current violation event
    //      2. To compute the position of particle when in contact with boundary and one base particle,
    //         in this case, the rolling of particle does not terminate, so information should be
    //         returned in order to continue rolling
    //      3. To compute the position of particle when in contact with boundary and two base particles,
    //         in this case, the rolling of particle terminates.
    //      4. To compute the position of particle when in contact with boundary, one base particle, and
    //         and a second boundary. The computation will require the type of second boundary. In this
    //         case, the particle also reach final position.
    public boolean isBoundaryViolation(double x, double y, double r);
    public double penetrationDepth(double x, double y, double r);
    public double[] contOneBoundOneBase(double x, double y, Particle first, FirstBaseParticleInfo firstBase);
    public double[] contOneBoundTwoBase();
    public double[] contTwoBoundOneBase();

}
