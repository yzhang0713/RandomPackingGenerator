import java.util.*;

public class Particle {
    // The class for discrete particle

    // The properties of particle
    public double radius;       // Particle radius
    public double xPos;         // Particle x coordinate
    public double yPos;         // Particle y coordinate
    public double zPos;         // Particle z coordinate
    public int xGrid;           // Particle x grid value
    public int yGrid;           // Particle y grid value
    public double radMax;       // Maximum particle radius, used to compute grid values
    private static final double DELTA = 1.0e-6;      // Delta value for double precision number calculation

    // Constructor of Particle class
    public Particle(double x, double y, double z, double rad, double rmax) {
        xPos = x;
        yPos = y;
        zPos = z;
        radius = rad;
        radMax = rmax;
        updateGrid();
    }

    // Update particle grid information
    public void updateGrid() {
        xGrid = (int) (xPos/2.0/radMax);
        yGrid = (int) (yPos/2.0/radMax);
    }

    // Compute the distance between current particle and other particle
    public double dist(Particle other) {
        return Math.sqrt((other.xPos-xPos)*(other.xPos-xPos)+(other.yPos-yPos)*(other.yPos-yPos)+(other.zPos-zPos)*(other.zPos-zPos));
    }

    // Compute the horizontal distance between current particle and other particle
    public double horDist(Particle other) {
        return Math.sqrt((other.xPos-xPos)*(other.xPos-xPos)+(other.yPos-yPos)*(other.yPos-yPos));
    }

    // Method to build contacted particle list
    // For parameter dz:
    //      At free fall stage, set dz = 0 to exclude particles at higher position
    //      At other stage, use dz = radius + other.radius to exclude particles that are too high
    public Set<Integer> contParList(Map<Key, Set<Integer>> gridMap, Particle[] pars, double dz) {
        // Set containing particles that can be in contact with current particle
        Set<Integer> parCont = new HashSet<>();
        // Loop over the particle grid and neighboring grids
        for (int i = xGrid - 1; i < xGrid + 2; i++) {
            for (int j = yGrid - 1; j < yGrid + 2; j++) {
                Key curKey = new Key(i, j);
                if (gridMap.containsKey(curKey)) {
                    for (int pid : gridMap.get(curKey)) {
                        if (pars[pid].zPos - dz < zPos && horDist(pars[pid]) < (radius+pars[pid].radius)*(1.0-DELTA)) {
                            parCont.add(pid);
                        }
                    }
                }
            }
        }
        return parCont;
    }

    // Method to determine the azimuthal and polar angle of incoming particle
    public double[] sphCoorAng(Particle base) {
        // Return values, the three values hold angle phi, angle theta, and ztem
        double[] result = new double[3];
        // Horizontal distance between two particles
        double dh = horDist(base);
        // Verticle coordinate at first contact
        result[2] = base.zPos + Math.sqrt((radius+base.radius)*(radius+base.radius)-dh*dh);
        // Determine azimuthal angle
        if (dh/(radius+base.radius) < DELTA) {
            Random rand = new Random();
            result[0] = rand.nextDouble() * 2.0 * Math.PI;
        } else {
            result[0] = Math.acos((xPos-base.xPos)/dh);
            if (yPos < base.yPos) {
                result[0] = 2.0 * Math.PI - result[0];
            }
        }
        // Determine polar angle
        result[1] = Math.acos((result[2]-base.zPos)/(radius+base.radius));
        return result;
    }

    // Method to determine the first base particle
    public FirstBaseParticleInfo contOneBase(Particle[] pars, Set<Integer> parCont) {
        zPos = 0.0;
        int pid1 = -1;
        double phi1 = 0.0;
        double theta1 = 0.0;
        for (int pid : parCont) {
            double[] result = sphCoorAng(pars[pid]);
            if (result[2] > zPos) {
                zPos = result[2];
                pid1 = pid;
                phi1 = result[0];
                theta1 = result[1];
            }
        }
        return new FirstBaseParticleInfo(pid1,phi1,theta1);
    }

    // Free fall of particle procedure
    public void freeFall(Map<Key, Set<Integer>> gridMap, Particle[] pars) {
        // Build potential contact particle set
        Set<Integer> parCont = contParList(gridMap, pars, 0.0);
        // Free fall result of particle
        if (parCont.isEmpty()) {
            // No particle in contact with current particle, sink to the bottom of container
            zPos = radius;
        } else {
            // Determine first base particle
            FirstBaseParticleInfo firstBase = contOneBase(pars, parCont);
            rollingOneBase(gridMap, pars, firstBase);
        }
    }

    // Rolling on one particle as base
    public void rollingOneBase(Map<Key, Set<Integer>> gridMap, Particle[] pars, FirstBaseParticleInfo firstBase, Boundary[] bounds) {
        // The first base particle object
        Particle first = pars[firstBase.pid];
        // Compute the position of particle when rolling on first base particle to 90 degrees
        double xRoll = first.xPos + (radius + first.radius) * Math.cos(firstBase.phi);
        double yRoll = first.yPos + (radius + first.radius) * Math.sin(firstBase.phi);
        double zRoll = first.zPos;
        // Check boundary violation
        int bCheck = checkBoundary(bounds, xRoll, yRoll);
        if (bCheck >= 0) {

        }
        return;
    }

    // Check boundary violation
    public int checkBoundary(Boundary[] bounds, double x, double y) {
        // Return integer value to represent if corresponding boundary condition is violated
        // result = -1: no violation
        // 0 <= result < bounds.length: violation to certain boundary
        // In case of multiple violation, return the one with largest penetration depth
        int bCheck = -1;
        double pDepth = 0.0;
        for (int i = 0; i < bounds.length; i++) {
            if (bounds[i].isBoundaryViolation(x, y, radius)) {
                double pDepthCur = bounds[i].penetrationDepth(x, y, radius);
                if (pDepthCur > pDepth) {
                    bCheck = i;
                    pDepth = pDepthCur;
                }
            }
        }
        return bCheck;
    }
}
