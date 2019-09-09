package vn.topica.itlab4.iosocket.ex1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains methods
 * - to convert information from an input file into attributes of Devices
 * - to find the most common word in a string
 * - to standardize words in a string
 * - to write something to an output file
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class Utils {
    static List<Device> listDevice;

    /**
     * This method convert information from an input file into attributes of Devices
     *
     * @param s The string containing all characters the input file
     * @param isStandardize Whether you want to standardize owner or not
     * @param isTOPICA	Whether you want to find Devices whose code contains "TOPICA" or not
     *
     * @return a list of Devices
     *
     * @exception  ParseException Signals that an error has been reached unexpectedly while parsing
     */
    public static List<Device> convertToObject(String s, boolean isStandardize, boolean isTOPICA)
            throws ParseException {
        listDevice = new ArrayList<Device>();
        Device device;
        Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2018");
        Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2019");
        String[] arr = s.split("[\r\n]+");
        for (int i = 0; i < arr.length; i++) {
            String[] deviceAttr = arr[i].split(",");
            device = new Device();
            device.setCode(deviceAttr[0]);
            device.setName(deviceAttr[1]);
            if (isStandardize) {
                device.setOwner(standardizeWord(deviceAttr[2]));
            } else {
                device.setOwner(deviceAttr[2]);
            }
            device.setInputDate(deviceAttr[3]);
            device.setWarrantyYear(deviceAttr[4]);

            if (isTOPICA) {
                if (device.getCode().contains("TOPICA")
                        && device.getInputDate().compareTo(dateStart) > 0
                        && device.getInputDate().compareTo(dateEnd) < 0)
                {
                    listDevice.add(device);
                }
            } else {
                listDevice.add(device);
            }
        }
        return listDevice;
    }

    /**
     * This method find the most common word in the input string using a Map.
     * The key is the words in the string.
     * The value is the number of times that word appears in the string.
     * Sorting the Map by value and get the result
     *
     * @param s The input string
     *
     * @return the most common word in the input string
     */
    public static String findTheMostFrequentWord(String s) {
        StringBuffer sb = new StringBuffer();
        String[] arr = s.split("[\r\n]+");
        for (int i = 0; i < arr.length; i++) {
            String[] deviceAttr = arr[i].split(",");
            sb.append(deviceAttr[2]+" ");
        }

        String allOwnerName = sb.toString();
        String allOwnerNameStandardized = standardizeWord(allOwnerName);
        String[] words = allOwnerNameStandardized.split("\\s+");
        HashMap<String, Integer> sortingMap = new HashMap<String, Integer>();
        for (String word: words) {
            int value = 0;
            if  (sortingMap.containsKey(word)) {
                value = sortingMap.get(word);
            }
            sortingMap.put(word, value + 1);
        }

        Map.Entry<String,Integer> tempResult = sortingMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .findFirst().get();
        return tempResult.getKey();
    }

    /**
     * This method standardize the words from the input string.
     * It replace multi-space with one space
     * and set the first character of each word to uppercase,
     * the other character(s) of that word is in lowercase
     *
     * @param s The input string
     *
     * @return the standardized string
     */
    @SuppressWarnings("static-access")
    public static String standardizeWord(String s) {
        StringBuffer sb = new StringBuffer();
        String str = s.replaceAll("\\s+", " ");
        String[] arr = str.toLowerCase().trim().split(" ");
        for (int i = 0; i < arr.length; i++) {
            String strArr = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
            sb.append(strArr + " ");
        }
        String standardizedWord = sb.toString().trim();
        return standardizedWord;
    }

    /**
     * This method write Devices to a file using FileOutputStream
     *
     * @param device The Device you want to write to file
     * @param filepathOUT The path of the output file
     *
     * @exception IOException Signals that an I/O exception of some sort has occurred
     */
    public static void writeObjectToFile(Device device, String filepathOUT) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepathOUT, true);
            fileOut.write(device.toString().getBytes());
            fileOut.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method write hashes (#) to a file
     *
     * @param filepathOUT The path of the output file
     *
     * @exception IOException Signals that an I/O exception of some sort has occurred
     */
    public static void writeHashToFile(String filepathOUT) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepathOUT, true);
            fileOut.write("###\n".getBytes());
            fileOut.close();

        } catch (IOException ex) {
            ex.printStackTrace();
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
            FileOutputStream fileOut = new FileOutputStream(filepathOUT, true);
            fileOut.write(strToWrite.getBytes());
            fileOut.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
