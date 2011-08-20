package com.game.tictactoe;

import java.util.ArrayList;
import java.util.Random;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Controller {

	static final String LOG_TAG = Controller.class.getSimpleName();
	
	public static final int CirclePoint = 1;
	public static final int CrossPoint = 2;
	
	public static final int RestId = 0;
	public static final int PlayerId = 1;
	public static final int ComputerId = 2;
	
	/**
	 * 启动菜单界面
	 * @param context
	 */
	public static void startMenuActivity(Context context){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClass(context, MenuActivity.class);
		try{
			context.startActivity(intent);
		}catch(ActivityNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * 启动游戏界面
	 * @param context
	 */
	public static void startGameActivity(Context context, int selectPoint){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClass(context, GameActivity.class);
		intent.putExtra("point", selectPoint);
		try{
			context.startActivity(intent);
		}catch(ActivityNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	public static int getNextStep(int[] boardMap, final int rivalId, final int selfId, final int restId){
		Log.i(LOG_TAG, "getNextStep...rival:"+rivalId+"..self:"+selfId);
		// 必胜
		for(int at : findMatch(boardMap, selfId, 2)){
			int[] winPos = findRestPosition(boardMap, at, restId);
			Log.i(LOG_TAG, "win...At:"+at+"..len:"+winPos.length);
			if(winPos!=null && winPos.length!=0){
				Log.i(LOG_TAG, "win..."+winPos[0]);
				return winPos[0];
			}
		}
		
		// 防守
		for(int at : findMatch(boardMap, rivalId, 2)){
			int[] warning = findRestPosition(boardMap, at, restId);
			Log.i(LOG_TAG, "lose...At:"+at+"..len:"+warning.length);
			if(warning!=null && warning.length!=0){
				Log.i(LOG_TAG, "lose..."+warning[0]);
				return warning[0];
			}
		}
		
		// 随机进攻
		for(int at : findMatch(boardMap, selfId, 1)){
			int[] restPos = findRestPosition(boardMap, at, restId);
			if(restPos!=null && restPos.length!=0){
				Log.i(LOG_TAG, "rnd_1..."+restPos[0]);
				return restPos[0];
			}
		}
		ArrayList<Integer> restMap = new ArrayList<Integer>();
		for(int i=boardMap.length-1; i>=0; i--){
			if(boardMap[i]==0){
				restMap.add(i);
			}
		}
		Random rnd = new Random(System.currentTimeMillis());
		return restMap.get((rnd.nextInt()>>>1)%restMap.size());
		
//		ArrayList<Integer>[] restMap = new ArrayList[8];
//		int rivalCount = 0;
//		int selfCount = 0;
//		int id = -1;
//		// 横
//		for(int row=0; row<3; row++){
//			rivalCount = 0;
//			selfCount = 0;
//			id++;
//			for(int col=0; col<3; col++){
//				int index = row*3+col;
//				if(boardMap[index]==rivalId){
//					rivalCount++;
//				}else
//				if(boardMap[index]==selfId){
//					selfCount++;
//				}else
//				if(boardMap[index]==restId){
//					if(restMap[id]==null){
//						restMap[id] = new ArrayList<Integer>();
//					}
//					restMap[id].add(index);
//					if(selfCount>1 || rivalCount>1){
//						Log.i(LOG_TAG, "horizontal...s:"+selfCount+" r:"+rivalCount+"..."+index);
//						return index;
//					}
//				}
//			}
//			Log.i(LOG_TAG, "row:"+row+"...s:"+selfCount+" r:"+rivalCount);
//		}
//		// 纵
//		for(int col=0; col<3; col++){
//			rivalCount = 0;
//			selfCount = 0;
//			id++;
//			for(int row=0; row<3; row++){
//				int index = row*3+col;
//				if(boardMap[index]==rivalId){
//					rivalCount++;
//				}else
//				if(boardMap[index]==selfId){
//					selfCount++;
//				}else
//				if(boardMap[index]==restId){
//					if(restMap[id]==null){
//						restMap[id] = new ArrayList<Integer>();
//					}
//					restMap[id].add(index);
//					if(selfCount>1 || rivalCount>1){
//						Log.i(LOG_TAG, "vertical...s:"+selfCount+" r:"+rivalCount+"..."+index);
//						return index;
//					}
//				}
//			}
//			Log.i(LOG_TAG, "col:"+col+"...s:"+selfCount+" r:"+rivalCount);
//		}
//		// 交叉
//		rivalCount = 0;
//		selfCount = 0;
//		id++;
//		for(int i=0; i<3; i++){
//			int index = i*3+i;
//			if(boardMap[index]==rivalId){
//				rivalCount++;
//			}else
//			if(boardMap[index]==selfId){
//				selfCount++;
//			}else
//			if(boardMap[index]==restId){
//				if(restMap[id]==null){
//					restMap[id] = new ArrayList<Integer>();
//				}
//				restMap[id].add(index);
//				if(selfCount>1 || rivalCount>1){
//					Log.i(LOG_TAG, "leftcross...s:"+selfCount+" r:"+rivalCount+"..."+index);
//					return index;
//				}
//			}
//		}
//		Log.i(LOG_TAG, "leftcross...s:"+selfCount+" r:"+rivalCount);
//		
//		rivalCount = 0;
//		selfCount = 0;
//		id++;
//		for(int i=0; i<3; i++){
//			int index = (3-i-1)*3+i;
//			if(boardMap[index]==rivalId){
//				rivalCount++;
//			}else
//			if(boardMap[index]==selfId){
//				selfCount++;
//			}else
//			if(boardMap[index]==restId){
//				if(restMap[id]==null){
//					restMap[id] = new ArrayList<Integer>();
//				}
//				restMap[id].add(index);
//				if(selfCount>1 || rivalCount>1){
//					Log.i(LOG_TAG, "rightcross...s:"+selfCount+" r:"+rivalCount+"..."+index);
//					return index;
//				}
//			}
//		}
//		Log.i(LOG_TAG, "rightcross...s:"+selfCount+" r:"+rivalCount);
//		
//		int maxSize = 0;
//		int maxIndex = -1;
//		for(int i=restMap.length-1; i>=0; i--){
//			if(restMap[i]!=null && restMap[i].size()>maxSize){
//				maxSize = restMap[i].size();
//				maxIndex = i;
//			}
//		}
//		if(maxIndex!=-1){
//			return restMap[maxIndex].get(0);
//		}
//		
//		return -1;
	}
	
	static final int NoMatch = 0;
	static final int Row1 = 1;
	static final int Row2 = 2;
	static final int Row3 = 3;
	static final int Col1 = 4;
	static final int Col2 = 5;
	static final int Col3 = 6;
	static final int LeftCross = 7;
	static final int RightCross = 8;
	
	/**
	 * 找到与给定ID的数量匹配的行、列或交叉
	 * @param boardMap
	 * @param targetId
	 * @param matchNum
	 * @return
	 */
	public static int[] findMatch(int[] boardMap, int targetId, int matchNum){
		ArrayList<Integer> matches = new ArrayList<Integer>();
		int counter = 0;
		// 横
		for(int row=0; row<3; row++){
			counter = 0;
			for(int col=0; col<3; col++){
				if(boardMap[row*3+col]==targetId){
					counter++;
				}
			}
			if(counter==matchNum){
				switch(row){
				case 0:
					matches.add(Row1);
					break;
				case 1:
					matches.add(Row2);
					break;
				case 2:
					matches.add(Row3);
					break;
				}
			}
		}
		// 纵
		for(int col=0; col<3; col++){
			counter = 0;
			for(int row=0; row<3; row++){
				if(boardMap[row*3+col]==targetId){
					counter++;
				}
			}
			if(counter==matchNum){
				switch(col){
				case 0:
					matches.add(Col1);
					break;
				case 1:
					matches.add(Col2);
					break;
				case 2:
					matches.add(Col3);
					break;
				}
			}
		}
		// 交叉
		counter = 0;
		for(int i=0; i<3; i++){
			if(boardMap[i*3+i]==targetId){
				counter++;
			}
		}
		if(counter==matchNum){
			matches.add(LeftCross);
		}
		counter = 0;
		for(int i=0; i<3; i++){
			if(boardMap[(3-i-1)*3+i]==targetId){
				counter++;
			}
		}
		if(counter==matchNum){
			matches.add(RightCross);
		}
		
		int[] result = new int[matches.size()];
		for(int i=result.length-1; i>=0; i--){
			result[i] = matches.get(i);
		}
		return result;
	}
	
	/**
	 * 找到空白的位置
	 * @param boardMap
	 * @param at
	 * @param restId
	 * @return
	 */
	public static int[] findRestPosition(int[] boardMap, int at, int restId){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(at==Row1 || at==Row2 || at==Row3){
			int row = at==Row1?0:(at==Row2?1:2);
			Log.i(LOG_TAG, "findRest...row"+row);
			for(int col=0; col<3; col++){
				int index = row*3+col;
				if(boardMap[index]==restId){
					Log.i(LOG_TAG, "findRest...index"+index+"..."+boardMap[index]);
					result.add(index);
				}
			}
		}else
		if(at==Col1 || at==Col2 || at==Col3){
			int col = at==Col1?0:(at==Col2?1:2);
			for(int row=0; row<3; row++){
				int index = row*3+col;
				if(boardMap[index]==restId){
					result.add(index);
				}
			}
		}else
		if(at==LeftCross){
			for(int i=0; i<3; i++){
				int index = i*3+i;
				if(boardMap[index]==restId){
					result.add(index);
				}
			}
		}else
		if(at==RightCross){
			for(int i=0; i<3; i++){
				int index = (3-i-1)*3+i;
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
