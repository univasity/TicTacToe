package com.game.tictactoe;


public abstract class Board {

	static final int PLAYER1_TURN = 0x001;
	static final int PLAYER2_TURN = 0x002;
	
	protected int curTurn;
	
	protected int size;
	protected int[] dataMap;
	
	static final int EMPTY_DATA = 0;
	static final int PLAYER1_ID = 1;
	static final int PLAYER2_ID = 2;
	
	static final int NotWin = 0;
	static final int HorizontalWinRow1 = 1;
	static final int HorizontalWinRow2 = 2;
	static final int HorizontalWinRow3 = 3;
	static final int VerticalWinCol1 = 4;
	static final int VerticalWinCol2 = 5;
	static final int VerticalWinCol3 = 6;
	static final int LeftCrossWin = 7;
	static final int RightCrossWin = 8;
	static final int Deuce = 9;
	
	public Board(int size) {
		this.size = size;
	}

	public void init(){
		if(dataMap==null){
			dataMap = new int[size*size];
		}else{
			for(int i=dataMap.length-1; i>=0; i--){
				dataMap[i] = EMPTY_DATA;
			}
		}
	}
	
	public void setTurn(int turn){
		if(turn!=PLAYER1_TURN & turn!=PLAYER2_TURN){
			throw new IllegalArgumentException("not support turn:"+turn);
		}
		curTurn = turn;
	}
	
	public int getTurn(){
		return curTurn;
	}
	
	public boolean setDataAt(int row, int col){
		int index = size*row+col;
		if(index<0 || index>=dataMap.length){
			return false;
		}
		if(dataMap[index]!=EMPTY_DATA){
			return false;
		}
		dataMap[index] = curTurn==PLAYER1_TURN?PLAYER1_ID:PLAYER2_ID;
		changeTurn();
		return true;
	}
	
	private void changeTurn(){
		curTurn = curTurn==PLAYER1_TURN?PLAYER2_TURN:PLAYER1_TURN;
	}
	
	public abstract int checkWin();
	
	public abstract int getNextStep();
	
}
