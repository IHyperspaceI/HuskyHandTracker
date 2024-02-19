import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class HuskyHandTracker {
    private final float SENSITIVITY = 10; // Positional sensitivity of the device

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
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public double getYaw() {
        return yaw;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getzPos() {
        return zPos;
    }
}
