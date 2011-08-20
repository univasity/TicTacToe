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
	
	public static final int NotWin = 0;
	public static final int HorizontalWinRow1 = 1;
	public static final int HorizontalWinRow2 = 2;
	public static final int HorizontalWinRow3 = 3;
	public static final int VerticalWinCol1 = 4;
	public static final int VerticalWinCol2 = 5;
	public static final int VerticalWinCol3 = 6;
	public static final int LeftCrossWin = 7;
	public static final int RightCrossWin = 8;
	public static final int Deuce = 9;
	
	public Board(int size) {
		this.size = size;
	}

	public synchronized void init(){
		if(dataMap==null){
			dataMap = new int[size*size];
		}else{
			for(int i=dataMap.length-1; i>=0; i--){
				dataMap[i] = EMPTY_DATA;
			}
		}
	}
	
	public synchronized void setTurn(int turn){
		if(turn!=PLAYER1_TURN & turn!=PLAYER2_TURN){
			throw new IllegalArgumentException("not support turn:"+turn);
		}
		curTurn = turn;
	}
	
	public synchronized int getTurn(){
		return curTurn;
	}
	
	public synchronized boolean setDataAt(int index){
//		int index = size*row+col;
		if(index<0 || index>=dataMap.length){
			return false;
		}
		if(dataMap[index]!=EMPTY_DATA){
			return false;
		}
		dataMap[index] = curTurn==PLAYER1_TURN?PLAYER1_ID:PLAYER2_ID;
		return true;
	}
	
	public synchronized void changeTurn(){
		curTurn = curTurn==PLAYER1_TURN?PLAYER2_TURN:PLAYER1_TURN;
	}
	
	public abstract int checkWin();
	
	public abstract int getNextStep();
	
}
