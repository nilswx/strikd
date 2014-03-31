package strikd.communication;

public interface Opcodes
{
	public interface Opcode { }
	
	public enum Incoming implements Opcode
	{
		CLIENT_CRYPTO,
		CREATE_PLAYER,
		LOGIN,
		CHANGE_NAME,
		CHANGE_AVATAR,
		CHANGE_LOCALE,
		DELETE_PLAYER,
		REQUEST_MATCH,
		CHALLENGE_PLAYER,
		ACCEPT_CHALLENGE,
		DECLINE_CHALLENGE,
		REVOKE_CHALLENGE,
		EXIT_MATCH,
		PLAYER_READY,
		SELECT_TILES,
		PURCHASE_OFFER,
		ACTIVATE_ITEM,
		FACEBOOK_LINK,
		FACEBOOK_UNLINK,
		FACEBOOK_INIT_FRIENDLIST,
		FACEBOOK_REFRESH_FRIENDS,
		FACEBOOK_CLAIM_LIKE,
		FACEBOOK_REGISTER_INVITES,
		GET_ACTIVITY_STREAM,
		GET_SHOP_PAGE,
		GET_IN_APP_PURCHASE_PRODUCTS,
		REDEEM_IN_APP_PURCHASE,
		GET_NEWS;
		
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
		PLAYER_INFO,
		PLAYER_UNKNOWN,
		LEVELS,
		CURRENCY_BALANCE,
		ITEM_TYPES,
		ITEMS,
		ITEMS_ADDED,
		FACEBOOK_STATUS,
		FACEBOOK_FRIEND_PLAYERS,
		FACEBOOK_FRIENDS_UPDATE,
		NAME_CHANGED,
		NAME_REJECTED,
		AVATAR_CHANGED,
		QUEUE_ENTERED,
		QUEUE_EXITED,
		CHALLENGE,
		CHALLENGE_OK,
		CHALLENGE_FAILED,
		CHALLENGE_REDIRECT,
		CHALLENGE_LOCALE_MISMATCH,
		CHALLENGE_DECLINED,
		CHALLENGE_REVOKED,
		ANNOUNCE_MATCH,
		MATCH_STARTED,
		MATCH_ENDED,
		BOARD_INIT,
		BOARD_UPDATE,
		WORD_FOUND,
		WORD_NOT_FOUND,
		EXPERIENCE_ADDED,
		ALERT,
		SERVER_SHUTTING_DOWN,
		SERVER_REDIRECT,
		ACTIVITY_STREAM,
		NEWS,
		SHOP_PAGE,
		IN_APP_PURCHASE_PRODUCTS,
		IN_APP_PURCHASE_DELIVERED;
	}
}
