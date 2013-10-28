package strikd.communication;

public interface Opcodes
{
	public interface Opcode { }
	
	public enum Incoming implements Opcode
	{
		CLIENT_CRYPTO,
	    CREATE_USER,
	    LOGIN,
	    FACEBOOK_LINK,
	    FACEBOOK_UNLINK,
	    FACEBOOK_LIKE,
	    CHANGE_NAME,
	    REQUEST_MATCH,
	    EXIT_MATCH,
	    PLAYER_READY,
	    UPDATE_TILE_SELECTION,
	    PURCHASE_ITEM,
	    ACTIVATE_ITEM;
		
		private static final Opcodes.Incoming[] values = values();
		
		public static final Opcodes.Incoming valueOf(byte opcode)
		{
			return values[opcode];
		}
	}
	
	public enum Outgoing implements Opcode
	{
		VERSIONCHECK,
		SERVER_CRYPTO,
		SESSION_INFO,
		CREDENTIALS,
		USER_INFO,
		CURRENCY_BALANCE,
		ITEMS,
		ITEM_ADDED,
		FACEBOOK_STATUS,
		NAME_CHANGED,
		NAME_REJECTED,
		QUEUE_ENTERED,
		QUEUE_EXITED,
		ANNOUNCE_MATCH,
		MATCH_STARTED,
		MATCH_ENDED,
		SQUARE_UPDATES,
		TILE_SELECTION_EXTENDED,
		TILE_SELECTION_CLEARED,
		WORD_FOUND,
		ALERT,
		SERVER_SHUTTING_DOWN,
		SERVER_REDIRECT
	}
}
