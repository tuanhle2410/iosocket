package vn.topica.itlab4.iosocket.ex1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class IOExecute {

    /**
     * input and output file path
     */
    private static final String filepathIN = ".\\resource\\input1.txt";
    private static final String filepathOUT = ".\\resource\\output1.txt";

    public static void main(String[] args) throws IOException, ParseException {
        /**
         * Read file text input
         */
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
        String contentInput = sb.toString();

        /**
         * Exercise 1.1:
         * - read the input file
         * - write the list of Device in ascending order of warrantyYear to the output file
         */
        Utils.convertToObject(contentInput, false, false);
        Collections.sort(Utils.listDevice, Device.Comparators.sortByWarrantyYear);

        for (int i = 0; i < Utils.listDevice.size(); i++) {
            Utils.writeObjectToFile(Utils.listDevice.get(i), filepathOUT);
            System.out
                    .println("The Device whose code is " + Utils.listDevice.get(i).getCode() + " was written to a file");
        }
        Utils.writeHashToFile(filepathOUT);
        System.out.println("---------------------------------------------");

        /**
         * Exercise 1.2:
         * - standardize attribute owner
         * - write the list of Devices whose owner was standardized
         * 	 in descending order of warrantyYear to the output file
         */
        Utils.convertToObject(contentInput, true, false);
        Collections.sort(Utils.listDevice, Device.Comparators.sortByWarrantyYear);

        for (int i = Utils.listDevice.size() - 1; i >= 0; i--) {
            Utils.writeObjectToFile(Utils.listDevice.get(i), filepathOUT);
            System.out
                    .println("The Device whose owner is " + Utils.listDevice.get(i).getOwner() + " was written to a file");
        }
        Utils.writeHashToFile(filepathOUT);
        System.out.println("---------------------------------------------");

        /**
         * Exercise 1.3:
         * - find the Device whose code contains "TOPICA"
         * 	 and inputDate from 31/10/2018 to 31/10/2019
         * - write the Device found above to the output file
         *   from the most recent inputDate
         *   or in ascending order if the Devices have the same inputDate
         */
        Utils.convertToObject(contentInput, true, true);
        Collections.sort(Utils.listDevice, Device.Comparators.sortByInputDate);

        for (int i = 0; i < Utils.listDevice.size(); i++) {
            Utils.writeObjectToFile(Utils.listDevice.get(i), filepathOUT);
            System.out
                    .println("The Device whose code is " + Utils.listDevice.get(i).getCode() + " was written to a file");
        }
        Utils.writeHashToFile(filepathOUT);
        System.out.println("---------------------------------------------");

        /**
         * Exercise 1.4: Write the word that appears the most frequent in the field owner of Device
         */
        String mostFrequentWord = Utils.findTheMostFrequentWord(contentInput);
        Utils.writeStringToFile(mostFrequentWord, filepathOUT);

        System.out
                .println("The word " + mostFrequentWord + " was written to a file");
    }
}
