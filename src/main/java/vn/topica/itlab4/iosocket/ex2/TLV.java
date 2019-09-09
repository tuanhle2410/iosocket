package vn.topica.itlab4.iosocket.ex2;

/**
 * This class represents an object TLV.
 * It provides attributes as information of TLVs
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class TLV{
    private short tag;
    private short length;
    private String value;

    //constructor
    public TLV(short tag, String value){
        this.length = (short)value.getBytes().length;
        this.tag = tag;
        this.value = value;
    }

    /**
     * These methods (line 24-40) are to get and set value to the attributes of InfoPacks
     */
    public short getTag() {return tag;}

    public void setTag(short tag) {this.tag = tag;}

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * This method change tag from a short number into a string
     *
     * @param tag a short number represents tag
     * @return the specific string that shows what the tag means
     */
    public static String tagToString(short tag){
        String stringTag;
        switch(tag){
            case 0:
                stringTag = "Key";
                break;
            case 1:
                stringTag = "PhoneNumber";
                break;
            case 2:
                stringTag = "Name";
                break;
            case 3:
                stringTag = "ResultCode";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tag);
        }
        return stringTag;
    }
}
