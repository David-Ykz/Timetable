/**
 * [Room.java] This class contains information about the available rooms at the school 
 * 
 * @author Blair Wang
 * @author Brian Zhang
 * @author David Ye
 * @author Anthony Tecsa
 * @author Allen Liu
 * @version Jan 15 2022
 */
public class Room {
    private int roomId;
    private String roomNum;
    private String name;
    
    Room(int roomId, String roomNum, String roomName) {
        this.roomId = roomId;
        this.roomNum = roomNum;
        this.name = roomName;
    }
    
    public int getRoomId() {
        return this.roomId;
    }
    public String getRoomNum() {
        return this.roomNum;
    }
    public String getRoomName() {
        return this.name;
    }
}