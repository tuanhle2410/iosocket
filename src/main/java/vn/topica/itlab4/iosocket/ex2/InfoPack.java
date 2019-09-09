package vn.topica.itlab4.iosocket.ex2;

import java.util.List;

/**
 * This class represents an object InfoPack.
 * It provides attributes as information of InfoPacks
 *
 * @author AnhLT14 (anhlt14@topica.edu.vn)
 */
public class InfoPack {
    private int lengthOfMessage;
    private short cmdCode;
    private short version =0;
    private List<TLV> listTLV;

    //constructor
    public InfoPack(int lengthOfMessage, short cmdCode, List<TLV> listTLV) {
        this.lengthOfMessage = lengthOfMessage;
        this.cmdCode = cmdCode;
        this.listTLV = listTLV;
    }

    /**
     * These methods (line 26-52) are to get and set value to the attributes of InfoPacks
     */
    public int getLengthOfMessage() {
        return lengthOfMessage;
    }

    public void setLengthOfMessage(int lengthOfMessage) {
        this.lengthOfMessage = lengthOfMessage;
    }

    public short getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(short cmdCode) {
        this.cmdCode = cmdCode;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public List<TLV> getListTLV() {
        return listTLV;
    }

    /**
     * This method change cmdCode from a short number into a string
     *
     * @param cmdCode a short number represents cmdCode
     * @return the specific string that shows what the cmdCode means
     */
    public String cmdCodeToString(short cmdCode){
        String stringCmdCode;
        switch(cmdCode){
            case 0:
                stringCmdCode = "AUTHEN";
                break;
            case 1:
                stringCmdCode = "INSERT";
                break;
            case 2:
                stringCmdCode = "COMMIT";
                break;
            case 3:
                stringCmdCode = "SELECT";
                break;
            case 4:
                stringCmdCode = "ERROR";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cmdCode);
        }
        return stringCmdCode;
    }

    /**
     * This method overrides method toString() to print out the attributes of InfoPacks in a specific form
     *
     * @return the specific string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(cmdCodeToString(cmdCode)+" ");
        for(int i=0; i<listTLV.size(); i++){
            sb.append(TLV.tagToString(listTLV.get(i).getTag())+" ").append(listTLV.get(i).getValue()+" ");
        }
        return sb.toString();
    }
}
