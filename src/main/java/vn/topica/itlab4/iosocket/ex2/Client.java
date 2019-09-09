package vn.topica.itlab4.iosocket.ex2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * This class represents a Client.
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class Client {
    public static Status status;
    private static final String filepathIN = ".\\src\\main\\resources\\request.txt";
    private static final String filepathOUT = ".\\src\\main\\resources\\response.txt";
    public static boolean run = true;

    /**
     * In this main method, I define:
     * - a static variable status with default value is INIT
     * - an object DataInputStream to read the input stream
     * - an object DataOutputStream to write the input stream
     * Convert the input file to a string (message), convert it to a list of InfoPacks,
     * then convert them into a byte array to send to Server
     * Finally, receive the response from Server
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("localhost", 9669)) {

            status = Status.INIT;
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            String message;
            message = Utils.fileReader(filepathIN);
            System.out.println("Request: ");
            System.out.println(message);

            List<InfoPack> listInfoPack;
            listInfoPack = Utils.stringToListInfoPack(message);

            byte[] byteArrayRequest = new byte[0];
            for (InfoPack infoPack : listInfoPack) {
                byteArrayRequest = Utils.concatByteArrays(byteArrayRequest, Utils.infoPackToByteArray(infoPack));
            }

            while (run) {
                dos.writeInt(byteArrayRequest.length);
                dos.write(byteArrayRequest);
                String strResponse = (String) dis.readUTF();

                Utils.writeStringToFile(strResponse, filepathOUT);
                System.out.println("Received response from Server!");
                run = false;
            }

            System.out.println("Client closed!");
        }
    }
}
