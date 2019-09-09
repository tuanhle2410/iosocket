package vn.topica.itlab4.iosocket.ex2;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods
 * - to read a file into a string
 * - to convert information from a string into attributes of InfoPacks
 * - to convert an int or a short number into a byte array
 * - to concatenate some byte arrays
 * - to convert an InfoPack into a byte array
 * - to convert a byte array into an InfoPack
 * - to checkAUTHEN, checkINSERT, checkCOMMIT, checkSELECT
 * - to write something to an output file
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class Utils {
    static List<InfoPack> listInfoPack;
    static List<TLV> listTLV;
    static StringBuilder sbWholeInfoPack;
    static String wholeInfoPack;
    static int lengthOfMessage;

    /**
     * This method reads a file as a string
     *
     * @param filepathIN The path of the input file
     * @return a specific string
     */
    public static String fileReader(String filepathIN) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepathIN));
        StringBuilder sb = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(ls);
        }
        // delete the last new line separator
        sb.deleteCharAt(sb.length() - 1);
        reader.close();
        String wholeFileToString = sb.toString();
        return wholeFileToString;
    }

    /**
     * This method converts a string into a list of InfoPacks
     *
     * @param s The input string
     * @return a list of InfoPacks
     */
    public static List<InfoPack> stringToListInfoPack(String s)
            throws ParseException {
        listInfoPack = new ArrayList<InfoPack>();
        String[] arr = s.split("[\r\n]+");
        for (int i = 0; i < arr.length; i++) {
            String[] infoPackAttr = arr[i].split(" ");
            sbWholeInfoPack = new StringBuilder();
            short cmdCode;
            switch(infoPackAttr[0]){
                case "AUTHEN":
                    cmdCode = 0;
                    break;
                case "INSERT":
                    cmdCode = 1;
                    break;
                case "COMMIT":
                    cmdCode = 2;
                    break;
                case "SELECT":
                    cmdCode = 3;
                    break;
                case "ERROR":
                    cmdCode = 4;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + infoPackAttr[0]);
            }
            sbWholeInfoPack.append(infoPackAttr[0]+" ");
            listTLV = new ArrayList<TLV>();
            for(int j=1; j<infoPackAttr.length; j+=2){
                short tag;
                String value;
                switch (infoPackAttr[j]){
                    case "Key":
                        tag = 0;
                        break;
                    case "PhoneNumber":
                        tag = 1;
                        break;
                    case "Name":
                        tag = 2;
                        break;
                    case "ResultCode":
                        tag = 3;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + infoPackAttr[j]);
                }
                value = infoPackAttr[j+1];
                listTLV.add(new TLV(tag,value));
                sbWholeInfoPack.append(infoPackAttr[j]+" "+infoPackAttr[j+1]+" ");
            }
            wholeInfoPack = sbWholeInfoPack.toString().trim();
            lengthOfMessage = wholeInfoPack.getBytes().length;
            listInfoPack.add(new InfoPack(lengthOfMessage,cmdCode,listTLV));
        }
        return listInfoPack;
    }

    /**
     * This method converts an int number into a byte array
     *
     * @param value The value of the input number
     * @return an output byte array
     */
    public static byte[] intToByteArray(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        byte[] bytes = buffer.putInt(value).array();
        buffer.flip();
        return bytes;
    }

    /**
     * This method converts a short number into a byte array
     *
     * @param value The value of the input number
     * @return an output byte array
     */
    public static byte[] shortToByteArray(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        byte[] bytes = buffer.putShort(value).array();
        buffer.flip();
        return bytes;
    }

    /**
     * This method concatenate some byte arrays
     *
     * @param arrays The input byte arrays, separated by comma
     * @return the compounded byte array
     */
    public static byte[] concatByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++) {
            totalLength += arrays[i].length;
        }

        byte[] totalArray = new byte[totalLength];

        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, totalArray, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }

        return totalArray;
    }

    /**
     * This method converts an InfoPack into a byte array
     *
     * @param infoPack The input InfoPack
     * @return an output byte array
     */
    public static byte[] infoPackToByteArray(InfoPack infoPack) {
        byte[] cmdCode = shortToByteArray(infoPack.getCmdCode());
        byte[] version = shortToByteArray((short) 0);

        byte[] tempArray = new byte[0];
        for (TLV tlv : infoPack.getListTLV()) {
            tempArray = concatByteArrays(tempArray, shortToByteArray(tlv.getTag()));
            tempArray = concatByteArrays(tempArray, shortToByteArray(tlv.getLength()));
            tempArray = concatByteArrays(tempArray, tlv.getValue().getBytes());
        }

        byte[] infoPackByteArray = concatByteArrays(cmdCode, version, tempArray);
        byte[] lengthOfMessage = intToByteArray(infoPackByteArray.length + 4);
        infoPackByteArray = concatByteArrays(lengthOfMessage, infoPackByteArray);
        return infoPackByteArray;
    }

    /**
     * This method converts a byte array into an object InfoPack
     *
     * @param byteArray The input byte array
     * @return a list of InfoPacks
     * @throws ParseException Signals that an error has been reached unexpectedly while parsing
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public static List<InfoPack> byteArrayToInfoPack(byte[] byteArray)
            throws ParseException, IOException {
        listInfoPack = new ArrayList<InfoPack>();
        DataInputStream wholeRequestDIS = new DataInputStream(new ByteArrayInputStream(byteArray));
        while (wholeRequestDIS.available() > 0) {
            lengthOfMessage = wholeRequestDIS.readInt();
            int lengthOfTLV = lengthOfMessage - 4;
            byte[] tlvArray = new byte[lengthOfTLV];
            wholeRequestDIS.read(tlvArray, 0, lengthOfTLV);
            DataInputStream tlvDIS = new DataInputStream(new ByteArrayInputStream(tlvArray));
            short cmdCode = tlvDIS.readShort();
            short version = tlvDIS.readShort();
            listTLV = new ArrayList<TLV>();
            while (tlvDIS.available() > 0) {
                try {
                    short tag = tlvDIS.readShort();
                    String value = tlvDIS.readUTF();
                    listTLV.add(new TLV(tag,value));
                } catch (EOFException e) {
                    e.printStackTrace();
                }
            }
            tlvDIS.close();
            listInfoPack.add(new InfoPack(lengthOfMessage,cmdCode,listTLV));
        }
        wholeRequestDIS.close();
        return listInfoPack;
    }

    /**
     * This method is to check AUTHEN package, like this:
     * - if phone number matches the given regular expression, check tag Key
     *      + if Key == "topica", change the status of Client to READY,
     *        and create a specific package to return to the Client
     *      + if key != "topica", create a specific package to return to the Client
     * - if phone number does not match the given regular expression,
     *   create a specific package to return to the Client
     *
     * @param infoPack The input InfoPack
     * @return the output InfoPack
     */
    public static InfoPack checkAUTHEN(InfoPack infoPack) {
        String phoneNumber = infoPack.getListTLV().get(0).getValue();
        String key = infoPack.getListTLV().get(1).getValue();
        String phoneNumberRegex = "098[^01][0-9]{6}";
        if (phoneNumber.matches(phoneNumberRegex)) {
            if (key.equalsIgnoreCase("topica")) {
                Client.status = Status.READY;
                wholeInfoPack = "AUTHEN PhoneNumber " + phoneNumber + " ResultCode OK";
                lengthOfMessage = wholeInfoPack.length();
                listTLV = new ArrayList<TLV>();
                listTLV.add(new TLV((short) 1, phoneNumber));
                listTLV.add(new TLV((short) 3, "OK"));
                return new InfoPack(lengthOfMessage, (short) 0, listTLV);
            } else {
                wholeInfoPack = "AUTHEN PhoneNumber " + phoneNumber + " ResultCode NOK";
                lengthOfMessage = wholeInfoPack.length();
                listTLV = new ArrayList<TLV>();
                listTLV.add(new TLV((short) 1, phoneNumber));
                listTLV.add(new TLV((short) 3, "NOK"));
                return new InfoPack(lengthOfMessage, (short) 0, listTLV);
            }
        } else {
            wholeInfoPack = "ERROR ResultCode NA";
            lengthOfMessage = wholeInfoPack.length();
            listTLV = new ArrayList<TLV>();
            listTLV.add(new TLV((short) 3, "NA"));
            return new InfoPack(lengthOfMessage, (short) 4, listTLV);
        }
    }

    /**
     * This method is to check INSERT package, like this:
     * - if Client's status == READY, create a specific package to return to the Client
     * - if Client's status != READY, create a specific package to return to the Client
     *
     * @param infoPack The input InfoPack
     * @return the output InfoPack
     */
    public static InfoPack checkINSERT(InfoPack infoPack) {
        String phoneNumber = infoPack.getListTLV().get(0).getValue();
        String name = infoPack.getListTLV().get(1).getValue();
        if (Client.status != Status.READY) {
            wholeInfoPack = "INSERT PhoneNumber " + phoneNumber + " ResultCode NOK";
            lengthOfMessage = wholeInfoPack.length();
            listTLV = new ArrayList<TLV>();
            listTLV.add(new TLV((short) 1, phoneNumber));
            listTLV.add(new TLV((short) 3, "NOK"));
            return new InfoPack(lengthOfMessage, (short) 1, listTLV);
        } else {
            wholeInfoPack = "INSERT PhoneNumber " + phoneNumber + " ResultCode OK";
            lengthOfMessage = wholeInfoPack.length();
            listTLV = new ArrayList<TLV>();
            listTLV.add(new TLV((short) 1, phoneNumber));
            listTLV.add(new TLV((short) 3, "OK"));
            return new InfoPack(lengthOfMessage, (short) 1, listTLV);
        }
    }

    /**
     * This method is to check COMMIT package, like this:
     * - if Client's status == READY, change Client's status to SELECT,
     *   create a specific package to return to the Client
     * - if Client's status != READY, create a specific package to return to the Client
     *
     * @param infoPack The input InfoPack
     * @return the output InfoPack
     */
    public static InfoPack checkCOMMIT(InfoPack infoPack) {
        String phoneNumber = infoPack.getListTLV().get(0).getValue();
        if (Client.status != Status.READY) {
            wholeInfoPack = "COMMIT PhoneNumber " + phoneNumber + " ResultCode NOK";
            lengthOfMessage = wholeInfoPack.length();
            listTLV = new ArrayList<TLV>();
            listTLV.add(new TLV((short) 1, phoneNumber));
            listTLV.add(new TLV((short) 3, "NOK"));
            return new InfoPack(lengthOfMessage, (short) 2, listTLV);
        } else {
            Client.status = Status.SELECT;
            wholeInfoPack = "COMMIT PhoneNumber " + phoneNumber + " ResultCode OK";
            lengthOfMessage = wholeInfoPack.length();
            listTLV = new ArrayList<TLV>();
            listTLV.add(new TLV((short) 1, phoneNumber));
            listTLV.add(new TLV((short) 3, "OK"));
            return new InfoPack(lengthOfMessage, (short) 2, listTLV);
        }
    }

    /**
     * This method is to check SELECT package, like this:
     * - if Client's status == SELECT, create a specific package to return to the Client
     * - if Client's status != SELECT, create a specific package to return to the Client
     *
     * @param infoPack The input InfoPack
     * @return the output InfoPack
     */
    public static InfoPack checkSELECT(InfoPack infoPack) {
        String phoneNumber = infoPack.getListTLV().get(0).getValue();
        String name = infoPack.getListTLV().get(1).getValue();
        if (Client.status != Status.SELECT) {
            wholeInfoPack = "SELECT PhoneNumber " + phoneNumber + " ResultCode NOK";
            lengthOfMessage = wholeInfoPack.length();
            listTLV = new ArrayList<TLV>();
            listTLV.add(new TLV((short) 1, phoneNumber));
            listTLV.add(new TLV((short) 3, "NOK"));
            return new InfoPack(lengthOfMessage, (short) 3, listTLV);
        } else {
            wholeInfoPack = "SELECT PhoneNumber " + phoneNumber + " ResultCode OK Name" + name;
            lengthOfMessage = wholeInfoPack.length();
            listTLV = new ArrayList<TLV>();
            listTLV.add(new TLV((short) 1, phoneNumber));
            listTLV.add(new TLV((short) 3, "OK"));
            listTLV.add(new TLV((short) 2, name));
            return new InfoPack(lengthOfMessage, (short) 3, listTLV);
        }
    }

    /**
     * This method write a string to a file
     *
     * @param strToWrite The string you want to write to file
     * @param filepathOUT The path of the output file
     *
     * @exception IOException Signals that an I/O exception of some sort has occurred
     */
    public static void writeStringToFile(String strToWrite, String filepathOUT) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepathOUT,true);
            fileOut.write(strToWrite.getBytes());
            fileOut.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
