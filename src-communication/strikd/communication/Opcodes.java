package strikd.communication;

public interface Opcodes
{
	public interface Opcode { }
	
	public enum Incoming implements Opcode
	{
		NOP,
		CREATE_PLAYER,
		LOGIN,
		FACEBOOK_LINK,
		FACEBOOK_UNLINK,
		CHANGE_NAME,
		REQUEST_MATCH,
		CANCEL_REQUEST_MATCH,
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
		LOGIN_OK,
		ALERT,
		PLAYERINFO,
		AVATAR,
		ITEMS,
		CURRENCY_BALANCE,
		NAME_CHANGED;
		
		private static final Opcodes.Outgoing[] values = values();
		
		public static final Opcodes.Outgoing valueOf(byte opcode)
		{
			return values[opcode];
		}
	}
}
