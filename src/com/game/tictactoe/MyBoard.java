package com.game.tictactoe;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

public class MyBoard extends Board {
	
	static final String LOG_TAG = MyBoard.class.getSimpleName();
	
	private Random rnd;
	
	public MyBoard(int size) {
		super(size);
		
		rnd = new Random(System.currentTimeMillis());
	}
	
	public int isFinish(){
		return -1;
	}
	
	public int getNextStep(){
		
		int selfId = curTurn==PLAYER1_TURN?PLAYER1_ID:PLAYER2_ID;
		int rivalId = selfId==PLAYER1_ID?PLAYER2_ID:PLAYER1_ID;
		
		int matchPoint = size-1;
		
		// ��ʤ
		for(int at : findMatch(dataMap, selfId, matchPoint)){
			int[] winPos = findRestPosition(dataMap, at, EMPTY_DATA);
			Log.i(LOG_TAG, "win...At:"+at+"..len:"+winPos.length);
			if(winPos!=null && winPos.length!=0){
				Log.i(LOG_TAG, "win..."+winPos[0]);
				return winPos[0];
			}
		}
		
		// ����
		for(int at : findMatch(dataMap, rivalId, matchPoint)){
			int[] warning = findRestPosition(dataMap, at, EMPTY_DATA);
			Log.i(LOG_TAG, "lose...At:"+at+"..len:"+warning.length);
			if(warning!=null && warning.length!=0){
				Log.i(LOG_TAG, "lose..."+warning[0]);
				return warning[0];
			}
		}
		
		// �������
		for(int at : findMatch(dataMap, selfId, 1)){
			int[] restPos = findRestPosition(dataMap, at, EMPTY_DATA);
			if(restPos!=null && restPos.length!=0){
				Log.i(LOG_TAG, "rnd_1..."+restPos[0]);
				return restPos[0];
			}
		}
		ArrayList<Integer> restMap = new ArrayList<Integer>();
		for(int i=dataMap.length-1; i>=0; i--){
			if(dataMap[i]==EMPTY_DATA){
				restMap.add(i);
			}
		}
		return restMap.get((rnd.nextInt()>>>1)%restMap.size());
	}

	@Override
	public int checkWin() {
		int[] result = null;
		// Player1
		result = findMatch(dataMap, PLAYER1_ID, size);
		if(result!=null && result.length>0){
			return result[0];
		}
		// Player2
		result = findMatch(dataMap, PLAYER2_ID, size);
		if(result!=null && result.length>0){
			return result[0];
		}
		// ����Ƿ�����
		for(int data : dataMap){
			if(data==EMPTY_DATA){
				return NotWin;
			}
		}
		return Deuce;
	}
	
	/**
	 * �ҵ������ID������ƥ����С��л򽻲�
	 * @param boardMap
	 * @param targetId
	 * @param matchNum
	 * @return
	 */
	private int[] findMatch(int[] boardMap, int targetId, int matchNum){
		ArrayList<Integer> matches = new ArrayList<Integer>();
		int counter = 0;
		// ��
		for(int row=0; row<size; row++){
			counter = 0;
			for(int col=0; col<size; col++){
				if(boardMap[row*size+col]==targetId){
					counter++;
				}
			}
			if(counter==matchNum){
				switch(row){
				case 0:
					matches.add(HorizontalWinRow1);
					break;
				case 1:
					matches.add(HorizontalWinRow2);
					break;
				case 2:
					matches.add(HorizontalWinRow3);
					break;
				}
			}
		}
		// ��
		for(int col=0; col<size; col++){
			counter = 0;
			for(int row=0; row<size; row++){
				if(boardMap[row*size+col]==targetId){
					counter++;
				}
			}
			if(counter==matchNum){
				switch(col){
				case 0:
					matches.add(VerticalWinCol1);
					break;
				case 1:
					matches.add(VerticalWinCol2);
					break;
				case 2:
					matches.add(VerticalWinCol3);
					break;
				}
			}
		}
		// ����
		counter = 0;
		for(int i=0; i<size; i++){
			if(boardMap[i*size+i]==targetId){
				counter++;
			}
		}
		if(counter==matchNum){
			matches.add(LeftCrossWin);
		}
		counter = 0;
		for(int i=0; i<size; i++){
			if(boardMap[(size-i-1)*size+i]==targetId){
				counter++;
			}
		}
		if(counter==matchNum){
			matches.add(RightCrossWin);
		}
		
		int[] result = new int[matches.size()];
		for(int i=result.length-1; i>=0; i--){
			result[i] = matches.get(i);
		}
		return result;
	}
	
	/**
	 * �ҵ��հ׵�λ��
	 * @param boardMap
	 * @param at
	 * @param restId
	 * @return
	 */
	private int[] findRestPosition(int[] boardMap, int at, int restId){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(at==HorizontalWinRow1 || at==HorizontalWinRow2 || at==HorizontalWinRow3){
			int row = at==HorizontalWinRow1?0:(at==HorizontalWinRow2?1:2);
			Log.i(LOG_TAG, "findRest...row"+row);
			for(int col=0; col<size; col++){
				int index = row*size+col;
				if(boardMap[index]==restId){
					Log.i(LOG_TAG, "findRest...index"+index+"..."+boardMap[index]);
					result.add(index);
				}
			}
		}else
		if(at==VerticalWinCol1 || at==VerticalWinCol2 || at==VerticalWinCol3){
			int col = at==VerticalWinCol1?0:(at==VerticalWinCol2?1:2);
			for(int row=0; row<size; row++){
				int index = row*size+col;
				if(boardMap[index]==restId){
					result.add(index);
				}
			}
		}else
		if(at==LeftCrossWin){
			for(int i=0; i<size; i++){
				int index = i*size+i;
				if(boardMap[index]==restId){
					result.add(index);
				}
			}
		}else
		if(at==RightCrossWin){
			for(int i=0; i<size; i++){
				int index = (size-i-1)*size+i;
				if(boardMap[index]==restId){
					result.add(index);
				}
			}
		}
		
		int[] array = new int[result.size()];
		for(int i=array.length-1; i>=0; i--){
			array[i] = result.get(i);
		}
		return array;
	}
	
}
