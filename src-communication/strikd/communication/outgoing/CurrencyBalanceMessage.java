package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class CurrencyBalanceMessage extends OutgoingMessage
{
	public CurrencyBalanceMessage(int balance)
	{
		super(Opcodes.Outgoing.CURRENCY_BALANCE);
		super.writeInt(balance);
	}
}
