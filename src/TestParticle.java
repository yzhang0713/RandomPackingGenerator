import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestParticle {
    @Test
    public void TestJunit() {
        Particle par1 = new Particle(0.0,0.0,0.0,2.0,2.0);
        Particle par2 = new Particle(0.5,0.5,5.0,2.0,2.0);
        // Test Particle.dist() method
        double expected = Math.sqrt(0.5*0.5*2+5.0*5.0);
        assertEquals(expected, par2.dist(par1), 0.001);
        // Test Particle.horDist() method
        expected = Math.sqrt(0.5*0.5*2);
        assertEquals(expected, par2.horDist(par1), 0.001);
        // Particle grid map
        Map<Key, Set<Integer>> gridMap = new HashMap<>();
        Set<Integer> parSet = new HashSet<>();
        Particle[] parArray = new Particle[10];
        parArray[0] = par1;
        parSet.add(0);
        gridMap.put(new Key(par1.xGrid, par1.yGrid), parSet);
        // Test Particle.contParList() method
        Set<Integer> contPars = par2.contParList(gridMap, parArray, 0.0);
        assertEquals(parSet, contPars);
        // Test Particle.sphCoorAng() method
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            double phi_expected = 2.0 * Math.PI * rand.nextDouble();
            double theta_expected = 0.5 * Math.PI * rand.nextDouble();
            double rb = 2.0;
            double zb = par2.zPos - (rb+par2.radius)*Math.cos(theta_expected);
            double dh = Math.abs((rb+par2.radius)*Math.sin(theta_expected));
            double xb = par2.xPos - dh * Math.cos(phi_expected);
            double yb = par2.yPos - dh * Math.sin(phi_expected);
            Particle parb = new Particle(xb,yb,zb,rb,2.0);
            double[] result = par2.sphCoorAng(parb);
            double[] res_expected = new double[]{phi_expected, theta_expected, par2.zPos};
            assertArrayEquals(res_expected, result, 0.001);
        }
        // Test Particle.contOneBase() method
        FirstBaseParticleInfo test = par2.contOneBase(parArray, contPars);
        assertEquals(0, test.pid);
        assertEquals(Math.PI * 0.25, test.phi, 0.001);
        // Test boundary violation function
        Boundary[] bounds = new Boundary[4];
        bounds[0] = new PlaneBoundary(1.0,-1.0,0.5,1.0);
        bounds[1] = new PlaneBoundary(1.0,0.0,-2.4,-1.0);
        bounds[2] = new PlaneBoundary(0.0,1.0,1.6,1.0);
        bounds[3] = new PlaneBoundary(1.0,1.0,-5.0,-1.0);
        assertTrue(bounds[0].isBoundaryViolation(0.5,0.5,2.0));
        assertTrue(bounds[1].isBoundaryViolation(0.5,0.5,2.0));
        assertFalse(bounds[2].isBoundaryViolation(0.5,0.5,2.0));
        assertFalse(bounds[3].isBoundaryViolation(0.5,0.5,2.0));
        // Test Particle.checkBoundary()
        int bCheck = par2.checkBoundary(bounds, 0.5, 0.5);
        assertEquals(0,bCheck);
    }
}
