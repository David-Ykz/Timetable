public class Room {
    private int roomId;
    private String roomNum;
    private String name;
    private String type;
    
    Room(int roomId, String roomNum, String roomName) {
        this.roomId = roomId;
        this.roomNum = roomNum;
        this.name = roomName;
//        this.type = type;
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
