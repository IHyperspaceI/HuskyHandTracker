import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class HuskyHandTracker {
    private final float SENSITIVITY = 10; // Positional sensitivity of the device

    protected static final double DEFAULT_SS_DEADZONE  = 0.01;
    protected static final double DEFAULT_FB_DEADZONE  = 0.01;
    protected static final double DEFAULT_VERT_DEADZONE = 0.01;

    protected static final double DEFAULT_ROT_DEADZONE = 1.0;

    public String pose;
    private double xPos; // Sideways
    private double yPos; // Height
    private double zPos; // Forward/back

    private double pitch;
    private double roll;
    private double yaw;

    public HuskyHandTracker() throws IOException {
        var address = InetAddress.getLocalHost();
        System.out.println("\n" + address);
        Socket socket = new Socket("127.0.0.1", 8080);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String inputLine;

        // Repeats every tick, getting info from the websocket
        while ((inputLine = in.readLine()) != null) {
            if ("ded".equals(inputLine)) {
                System.out.println("Server closed");
                break;
            }
            parseRaw(inputLine);
        }
    }

    private void parseRaw(String raw) {
        try {
            JSONArray array = new JSONArray("[" + raw + "]"); // The "[" are for formatting the course info
            for(int i=0; i < array.length(); i++) {
                try {
                    JSONObject object = array.getJSONObject(i);

                    this.pose = object.get("pose").toString();

                    this.xPos = object.getDouble("xPos") * SENSITIVITY;
                    this.yPos = object.getDouble("yPos") * SENSITIVITY;
                    this.zPos = object.getDouble("zPos") * SENSITIVITY;

                    this.pitch = object.getDouble("pitch");
                    this.roll = object.getDouble("roll");
                    this.yaw = object.getDouble("yaw");

                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getPose() {
        return pose;
    }

    public double getPitch() {
        return (Math.abs(pitch) > DEFAULT_ROT_DEADZONE) ? pitch : 0.0;
    }

    public double getRoll() {
        return (Math.abs(roll) > DEFAULT_ROT_DEADZONE) ? roll : 0.0;
    }

    public double getYaw() {
        return (Math.abs(yaw) > DEFAULT_ROT_DEADZONE) ? yaw : 0.0;
    }

    public double getxPos() {
        return (Math.abs(xPos) > DEFAULT_SS_DEADZONE) ? xPos : 0.0;
    }

    public double getyPos() {
        return (Math.abs(yPos) > DEFAULT_VERT_DEADZONE) ? yPos : 0.0;
    }

    public double getzPos() {
        return (Math.abs(zPos) > DEFAULT_FB_DEADZONE) ? zPos : 0.0;
    }
}
