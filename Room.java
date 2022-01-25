/**
 * [Room.Java]
 * Empty container for a room that classes occur in
 * @author Brian Zhang, Blair Wang
 * ICS4UE
 * @version 1.0, January 25 2022
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
