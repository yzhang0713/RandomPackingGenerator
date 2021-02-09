public class FirstBaseParticleInfo {
    // Class to encapsulate information of first base particle
    public int pid;
    public double phi;
    public double theta;

    public FirstBaseParticleInfo(int pid, double phi, double theta) {
        this.pid = pid;
        this.phi = phi;
        this.theta = theta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FirstBaseParticleInfo)) {
            return false;
        }
        FirstBaseParticleInfo base = (FirstBaseParticleInfo) o;
        return pid == base.pid && Double.compare(phi, base.phi) == 0 && Double.compare(theta, base.theta) == 0;
    }
}
