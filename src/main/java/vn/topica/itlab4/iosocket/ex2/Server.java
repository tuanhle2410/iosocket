package vn.topica.itlab4.iosocket.ex2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents a Server that accept the request from Client.
 * Execute the data and send response back to Client
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(9669)) {
            System.out.println("The server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new MyServer(listener.accept()));
            }
        }
    }

    private static class MyServer implements Runnable {
        private Socket socket;

        MyServer(Socket socket) {
            this.socket = socket;
        }

        /**
         * In this main method, I receive the byte array from Client,
         * convert it into a list of InfoPacks.
         * Check AUTHEN, INSERT, COMMIT, SELECT via methods in Utils.
         * Then send response back to Client.
         */
        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                int requestLength = dis.readInt();
                byte [] requestArray = new byte[requestLength];
                dis.readFully(requestArray);
                Utils.byteArrayToInfoPack(requestArray);

                List<InfoPack> listToResponse = new ArrayList<InfoPack>();
                InfoPack infoPack;
                for (int i = 0; i < Utils.listInfoPack.size(); i++) {
                    switch (Utils.listInfoPack.get(i).getCmdCode()) {
                        case 0:
                            infoPack = Utils.checkAUTHEN(Utils.listInfoPack.get(i));
                            listToResponse.add(infoPack);
                            break;
                        case 1:
                            infoPack = Utils.checkINSERT(Utils.listInfoPack.get(i));
                            listToResponse.add(infoPack);
                            break;
                        case 2:
                            infoPack = Utils.checkCOMMIT(Utils.listInfoPack.get(i));
                            listToResponse.add(infoPack);
                            break;
                        case 3:
                            infoPack = Utils.checkSELECT(Utils.listInfoPack.get(i));
                            listToResponse.add(infoPack);
                            break;
                    }
                }
                StringBuilder sbResponse = new StringBuilder();
                for (int i = 0; i < listToResponse.size(); i++) {
                    sbResponse.append(listToResponse.get(i).toString() + "\n");
                }
                String strResponse = sbResponse.toString();
                System.out.println("string response: \n"+strResponse);

                dos.writeUTF(strResponse);
                dos.flush();
            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }
    }
}