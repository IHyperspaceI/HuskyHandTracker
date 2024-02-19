import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws IOException {
        var m_tracker = new HuskyHandTracker();

        /*System.out.printf("""
                    Pitch: %f
                    Roll: %f
                    Yaw: %f
                    """, m_tracker.getPitch(), m_tracker.getRoll(), m_tracker.getYaw());*/
        System.out.println(m_tracker.getPose());
    }
}
