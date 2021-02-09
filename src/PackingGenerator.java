public class PackingGenerator {
    // This is the class file that holds 'main' function, to generate the random packing
    public static void main(String[] args) {
        // Define the container range (rectangular in this case)
        double bDomain = 100.0;
        // Define four boundaries based on domain size
        // First 2 boundaries parallel to yz-plane, next 2 boundaries parallel to xz-plane
        Boundary[] bounds = new Boundary[4];
        bounds[0] = new PlaneBoundary(1.0,0.0,bDomain/2.0,1.0);
        bounds[1] = new PlaneBoundary(1.0,0.0,-bDomain/2.0,-1.0);
        bounds[2] = new PlaneBoundary(0.0,1.0,bDomain/2.0,1.0);
        bounds[3] = new PlaneBoundary(0.0,1.0,-bDomain/2.0,-1.0);
    }
}
