package vn.topica.itlab4.iosocket.ex1;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * This class represents an object Device.
 * It provides attributes as information of Devices
 * and some method to compare and sort a list of Devices
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class Device implements Serializable, Comparable<Device> {
    private static final long serialVersionUID = 1L;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String code;
    private String name;
    private String owner;
    private Date inputDate;
    private int warrantyYear;

    // constructor
    public Device(String code, String name, String owner, Date inputDate, int warrantyYear) {
        this.code = code;
        this.name = name;
        this.owner = owner;
        this.inputDate = inputDate;
        this.warrantyYear = warrantyYear;
    }

    public Device() {

    }

    /**
     * These methods (line 42-88) are to get and set value to the attributes of Devices
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public void setInputDate(String s) throws ParseException {
        this.inputDate = new SimpleDateFormat("dd/MM/yyyy").parse(s);
    }

    public int getWarrantyYear() {
        return warrantyYear;
    }

    public void setWarrantyYear(int warrantyYear) {
        this.warrantyYear = warrantyYear;
    }

    public void setWarrantyYear(String s) {
        this.warrantyYear = Integer.parseInt(s);
    }

    /**
     * This method overrides method toString() to print out the attributes of Devices in a specific form
     *
     * @return the specific string
     */
    @Override
    public String toString() {
        return new StringBuffer(this.code).append(",").append(this.name).append(",").append(this.owner).append(",")
                .append(sdf.format(this.inputDate)).append(",").append(this.warrantyYear).append("\n").toString();
    }

    public int compareTo(Device o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * This class contains methods for compare and sort the list of Devices
     */
    public static class Comparators {
        /**
         * This method is to sort Devices ascending by attribute warrantyYear
         *
         * @return a positive number if d1>d2
         * a negative number if d1<d2
         * 0 if d1==d2
         */
        public static Comparator<Device> sortByWarrantyYear = new Comparator<Device>() {
            public int compare(Device d1, Device d2) {
                return Integer.compare(d1.warrantyYear, d2.warrantyYear);
            }
        };

        /**
         * This method is to sort Devices ascending by attribute inputDate for the first priority,
         * warrantyYear for the second priority
         *
         * @return Comparator of inputDate if d1 != d2
         * Comparator of warrantyYear if d1 == d2
         */
        public static Comparator<Device> sortByInputDate = new Comparator<Device>() {
            public int compare(Device d1, Device d2) {
                int com = d1.inputDate.compareTo(d2.inputDate);
                if (com != 0)
                    return com;
                else
                    return Integer.compare(d1.warrantyYear, d2.warrantyYear);
            }
        };
    }
}
