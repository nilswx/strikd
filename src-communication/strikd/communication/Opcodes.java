package strikd.communication;

public interface Opcodes
{
	public interface Opcode { }
	
	public enum Incoming implements Opcode
	{
	    NOP,
	    CREATE_USER,
	    LOGIN,
	    FACEBOOK_LINK,
	    FACEBOOK_UNLINK,
	    CHANGE_NAME,
	    REQUEST_MATCH,
	    EXIT_MATCH_QUEUE,
	    PLAYER_READY,
	    STRIKE_LETTERS,
	    ACTIVATE_ITEM;
		
		private static final Opcodes.Incoming[] values = values();
		
		public static final Opcodes.Incoming valueOf(byte opcode)
		{
			return values[opcode];
		}
	}
	
	public enum Outgoing implements Opcode
	{
	    NOP,
	    SESSION_INFO,
	    USER_INFO,
	    CURRENCY_BALANCE,
	    ITEMS,
	    ITEM_ADDED,
	    NAME_CHANGED,
	    NAME_REJECTED,
		QUEUE_ENTERED,
		QUEUE_EXITED,
	    BOARD_UPDATE,
	    ANNOUNCE_MATCH,
	    START_MATCH,
		ALERT,
	    SERVER_SHUTTING_DOWN,
	    SERVER_REDIRECT;
		
		private static final Opcodes.Outgoing[] values = values();
		
		public static final Opcodes.Outgoing valueOf(byte opcode)
		{
			return values[opcode];
		}
	}
}
