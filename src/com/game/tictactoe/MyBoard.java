package com.game.tictactoe;

import java.util.ArrayList;
import java.util.Random;

public class MyBoard extends Board {
	
	int rowStart;
	int rowEnd;
	int colStart;
	int colEnd;
	int leftCross;
	int rightCross;
	int totalNum;
	
	private Random rnd;
	
	public MyBoard(int size) {
		super(size);
		rowStart = 0;
		rowEnd = rowStart+size;
		colStart = rowEnd+1;
		colEnd = colStart+size;
		leftCross = colEnd+1;
		rightCross = leftCross+1;
		totalNum = rightCross+1;
		
		rnd = new Random(System.currentTimeMillis());
	}
	
	public int isFinish(){
		return -1;
	}
	
	public int getNextStep(){
		analyzeDataMap();
		
		int nextStep = -1;
		
		int matchPoint = (size-1);
		
		/**
		 * ÕÒ±ØÊ¤µã
		 */
		nextStep = getGamePointPos(selfStates, rivalStates, matchPoint); 
		if(nextStep!=-1){
			return nextStep;
		}
		
		/**
		 * ·ÀÊØ
		 */
		nextStep = getGamePointPos(rivalStates, selfStates, matchPoint); 
		if(nextStep!=-1){
			return nextStep;
		}
		
		/**
		 * Ëæ»úÑ¡Ôñ
		 */
		int maxSize = 0;
		int maxIndex = -1;
		for(int i=restMap.length-1; i>=0; i--){
			if(restMap[i]!=null && restMap[i].size()>maxSize){
				maxSize = restMap[i].size();
				maxIndex = i;
			}
		}
		if(maxIndex!=-1){
			return restMap[maxIndex].get(0);
		}
		
		return -1;
	}
	
	private int[] selfStates;
	private int[] rivalStates;
	private ArrayList<Integer>[] restMap = new ArrayList[8];
	
	private void analyzeDataMap(){
		
		int selfId = curTurn==PLAYER1_TURN?PLAYER1_ID:PLAYER2_ID;
		int rivalId = selfId==PLAYER1_ID?PLAYER2_ID:PLAYER1_ID;
		
		//
		selfStates = new int[totalNum];
		rivalStates = new int[totalNum];
		
		int stateIndex = -1;
		
		// ºá
		for(int row=0; row<size; row++){
			stateIndex++;
			for(int col=0; col<size; col++){
				int index = size*row+col;
				int data = dataMap[index];
				if(data==selfId){
					selfStates[stateIndex]++;
				}else
				if(data==rivalId){
					rivalStates[stateIndex]++;
				}else
				if(data==EMPTY_DATA){
					if(restMap[stateIndex]==null){
						restMap[stateIndex] = new ArrayList<Integer>();
					}
					restMap[stateIndex].add(index);
				}
			}
		}
		// ×Ý
		for(int col=0; col<size; col++){
			stateIndex++;
			for(int row=0; row<size; row++){
				int index = size*row+col;
				int data = dataMap[index];
				if(data==selfId){
					selfStates[stateIndex]++;
				}else
				if(data==rivalId){
					rivalStates[stateIndex]++;
				}else
				if(data==EMPTY_DATA){
					if(restMap[stateIndex]==null){
						restMap[stateIndex] = new ArrayList<Integer>();
					}
					restMap[stateIndex].add(index);
				}
			}
		}
		// ½»²æ
		stateIndex++;
		for(int i=0; i<size; i++){
			int index = size*i+i;
			int data = dataMap[index];
			if(data==selfId){
				selfStates[stateIndex]++;
			}else
			if(data==rivalId){
				rivalStates[stateIndex]++;
			}else
			if(data==EMPTY_DATA){
				if(restMap[stateIndex]==null){
					restMap[stateIndex] = new ArrayList<Integer>();
				}
				restMap[stateIndex].add(index);
			}
		}
		stateIndex++;
		for(int i=0; i<size; i++){
			int index = size*(size-i-1)+i;
			int data = dataMap[index];
			if(data==selfId){
				selfStates[stateIndex]++;
			}else
			if(data==rivalId){
				rivalStates[stateIndex]++;
			}else
			if(data==EMPTY_DATA){
				if(restMap[stateIndex]==null){
					restMap[stateIndex] = new ArrayList<Integer>();
				}
				restMap[stateIndex].add(index);
			}
		}
	}
	
	private int findRestPosition(int at){
		int[] restPos = findRestPosition(dataMap, at, EMPTY_DATA);
		if(restPos!=null && restPos.length>0){
			return restPos[(rnd.nextInt()>>>1)%restPos.length];
		}
		return -1;
	}
	
	/**
	 * ÕÒµ½¿Õ°×µÄÎ»ÖÃ
	 * @param boardMap
	 * @param at
	 * @param restId
	 * @return
	 */
	private int[] findRestPosition(int[] boardMap, int at, int restId){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(at>=rowStart && at<=rowEnd){
			int row = at-rowStart;
			for(int col=0; col<size; col++){
				int index = size*row+col;
				if(boardMap[index]==restId){
					result.add(boardMap[index]);
				}
			}
		}else
		if(at>=colStart && at<=colEnd){
			int col = at-colStart;
			for(int row=0; row<size; row++){
				int index = size*row+col;
				if(boardMap[index]==restId){
					result.add(boardMap[index]);
				}
			}
		}else
		if(at==leftCross){
			for(int i=0; i<size; i++){
				int index = size*i+i;
				if(boardMap[index]==restId){
					result.add(boardMap[index]);
				}
			}
		}else
		if(at==rightCross){
			for(int i=0; i<size; i++){
				int index = size*(size-i-1)+i;
				if(boardMap[index]==restId){
					result.add(boardMap[index]);
				}
			}
		}
		
		int[] array = new int[result.size()];
		for(int i=array.length-1; i>=0; i--){
			array[i] = result.get(i);
		}
		return array;
	}
	
	private int getGamePointPos(int[] selfStates, int[] rivalStates, int matchPoint){
		int pos = 0;
		// ºá
		for(int i=rowStart; i<rowEnd; i++){
			if(selfStates[i]==matchPoint && rivalStates[i]==0){
				pos = findRestPosition(i);
				if(pos!=-1){
					return pos;
				}
			}
		}
		// ×Ý
		for(int j=colStart; j<colEnd; j++){
			if(selfStates[j]==matchPoint && rivalStates[j]==0){
				pos = findRestPosition(j);
				if(pos!=-1){
					return pos;
				}
			}
		}
		// ½»²æ
		if(selfStates[leftCross]==matchPoint && rivalStates[leftCross]==0){
			pos = findRestPosition(leftCross);
			if(pos!=-1){
				return pos;
			}
		}
		if(selfStates[rightCross]==matchPoint && rivalStates[rightCross]==0){
			pos = findRestPosition(rightCross);
			if(pos!=-1){
				return pos;
			}
		}
		return -1;
	}

	@Override
	public int checkWin() {
		// ºá
		for(int row=rowStart; row<rowEnd; row++){
			if(selfStates[row]==size || rivalStates[row]==size){
				switch(row-rowStart){
				case 0:
					return HorizontalWinRow1;
				case 1:
					return HorizontalWinRow2;
				case 2:
					return HorizontalWinRow3;
				} 
			}
		}
		// ×Ý
		for(int col=colStart; col<colEnd; col++){
			if(selfStates[col]==size || rivalStates[col]==size){
				switch(col-colStart){
				case 0:
					return VerticalWinCol1;
				case 1:
					return VerticalWinCol2;
				case 2:
					return VerticalWinCol3;
				} 
			}
		}
		// ½»²æ
		if(selfStates[leftCross]==size || rivalStates[leftCross]==size){
			return LeftCrossWin;
		}
		if(selfStates[rightCross]==size || rivalStates[rightCross]==size){
			return RightCrossWin;
		}
		
		// ¼ì²âÊÇ·ñÂúÁË
		if(restMap.length!=0){
			for(ArrayList sub : restMap){
				if(sub.size()>0){
					return NotWin;
				}
			}
		}
		
		return Deuce;
	}
	
}
