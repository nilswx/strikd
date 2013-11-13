package strikd.game.board.impl;

import org.apache.log4j.Logger;

import strikd.game.board.Board;
import strikd.words.WordDictionary;

public class HanzeBoard extends Board
{
	private static final Logger logger = Logger.getLogger(HanzeBoard.class);
	
	public HanzeBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}

	@Override
	public void update()
	{
		logger.debug("Hanzeboard is gehandicapt!");
	}
}
