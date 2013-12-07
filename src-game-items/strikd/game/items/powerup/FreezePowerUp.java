package strikd.game.items.powerup;

import strikd.communication.outgoing.AlertMessage;
import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Freezes the opponent's screen with removable ice for x seconds.
 */
public class FreezePowerUp extends PowerUp
{
	public FreezePowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	protected void onActivate(MatchPlayer player, String... args)
	{
		// Freeze opponents screen blabla
		
		player.getOpponent().send(new AlertMessage(String.format("%s hit you with a breezy FREEZE!", player.getInfo().getName())));
	}	
}
