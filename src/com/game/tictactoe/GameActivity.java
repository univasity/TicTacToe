package com.game.tictactoe;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity implements OnTouchListener, OnClickListener {

	static final String LOG_TAG = GameActivity.class.getSimpleName();
	
	static final int PlayerTuns = 1;
	static final int ComputerTuns = 2;
	
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
	
	private ImageView[] points;
	private int[] boardMap;
	
	private int curTurns = PlayerTuns;
	private int selectPoint;
	
	private TextView playerTurnsView;
	private TextView computerTurnsView;
	
	private ImageView resultView;
	
	private Random rnd;
	private boolean isGameOver;
	
	private final int MSG_AUTOMOVE = 0x001;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==MSG_AUTOMOVE){
				int rivalId = curTurns==PlayerTuns?Controller.ComputerId:Controller.PlayerId;
				int selfId = curTurns==PlayerTuns?Controller.PlayerId:Controller.ComputerId;
				int restId = Controller.RestId;
				int nextStep = Controller.getNextStep(boardMap, rivalId, selfId, restId);
				Log.i(LOG_TAG, "nextStep..."+nextStep);
				if(nextStep!=-1){
					points[nextStep].setImageResource(curTurns==PlayerTuns?getPlayerPointId():getComputerPointId());
					boardMap[nextStep] = curTurns==PlayerTuns?Controller.PlayerId:Controller.ComputerId;
					checkWin();
					if(!isGameOver){
						notifyForNextTurns();
					}
				}
			}
			super.handleMessage(msg);
		}
	};
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initGame(getIntent());
	}

	private void initGame(Intent intent) {
		selectPoint = intent.getIntExtra("point", Controller.CirclePoint);
		if(selectPoint==Controller.CirclePoint){
			((ImageView)findViewById(R.id.player_point)).setImageResource(R.drawable.circle);
			((ImageView)findViewById(R.id.computer_point)).setImageResource(R.drawable.cross);
		}else{
			((ImageView)findViewById(R.id.player_point)).setImageResource(R.drawable.cross);
			((ImageView)findViewById(R.id.computer_point)).setImageResource(R.drawable.circle);
		}
		
		//
		points = new ImageView[9];
		for(int i=0; i<9; i++){
			points[i] = (ImageView)findViewById(R.id.pos0+i);
			points[i].setClickable(true);
			points[i].setOnTouchListener(this);
		}
		resultView = (ImageView)findViewById(R.id.result_img);
		
		//
		playerTurnsView = (TextView)findViewById(R.id.player_turns);
		computerTurnsView = (TextView)findViewById(R.id.computer_turns);
		findViewById(R.id.menu_btn).setOnClickListener(this);
		findViewById(R.id.restart_btn).setOnClickListener(this);
		
		restartGame();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if(isGameOver){
				return false;
			}
			int id = v.getId();
			if(id>=R.id.pos0 && id<R.id.pos0+9)
			{
				if(curTurns==PlayerTuns 
				&& boardMap[id-R.id.pos0]==0)
				{
					((ImageView)v).setImageResource(curTurns==PlayerTuns?getPlayerPointId():getComputerPointId());
					boardMap[id-R.id.pos0] = curTurns==PlayerTuns?Controller.PlayerId:Controller.ComputerId;
					checkWin();
					if(!isGameOver){
						notifyForNextTurns();
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private void notifyForNextTurns() {
		curTurns = curTurns==PlayerTuns?ComputerTuns:PlayerTuns;
		Animation shinningAnim = AnimationUtils.loadAnimation(this, R.anim.shining);
		if(curTurns==PlayerTuns){
			computerTurnsView.clearAnimation();
			playerTurnsView.startAnimation(shinningAnim);
		}else{
			playerTurnsView.clearAnimation();
			computerTurnsView.startAnimation(shinningAnim);
			
			long delay = 1000+((rnd.nextInt()>>>1)%2000);
			mHandler.sendEmptyMessageDelayed(MSG_AUTOMOVE, delay);
		}
	}

	private void checkWin() {
		int winCode = checkWin(1);
		boolean playerWin = false;
		if(winCode!=NotWin){
			// 玩家胜
			Log.d(LOG_TAG, "玩家胜!");
			playerWin = true;
		}else{
			winCode = checkWin(2);
			if(winCode!=NotWin){
				// 电脑胜
				Log.d(LOG_TAG, "电脑胜!");
				playerWin = false;
			}
		}
		if(winCode==NotWin){
			// 检测是否平局
			winCode = Deuce;
			for(int state:boardMap){
				if(state==0){
					winCode = NotWin;
					break;
				}
			}
		}
		if(winCode==NotWin){
			isGameOver = false;
		}else{
			showWinResult(winCode, playerWin);
			isGameOver = true;
		}
	}
	
	private void showWinResult(int winCode, boolean playerWin) {
		int resId = 0;
		switch(winCode){
		case HorizontalWinRow1:
			resId = R.drawable.line_h1;
			break;
			
		case HorizontalWinRow2:
			resId = R.drawable.line_h2;
			break;
			
		case HorizontalWinRow3:
			resId = R.drawable.line_h3;
			break;
			
		case VerticalWinCol1:
			resId = R.drawable.line_v1;
			break;
			
		case VerticalWinCol2:
			resId = R.drawable.line_v2;
			break;
			
		case VerticalWinCol3:
			resId = R.drawable.line_v3;
			break;
			
		case LeftCrossWin:
			resId = R.drawable.line_lcross;
			break;
			
		case RightCrossWin:
			resId = R.drawable.line_rcross;
			break;
			
		case Deuce:
			resId = -1;
			break;
		}
		
		if(resId!=-1){
			resultView.setBackgroundResource(resId);
			resultView.setVisibility(View.VISIBLE);
		}
		
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
		dialog.setContentView(R.layout.result_dialog);
		ImageView imgView = (ImageView)dialog.findViewById(R.id.img);
		TextView txtView = (TextView)dialog.findViewById(R.id.txt);
		if(winCode==Deuce){
			txtView.setText(R.string.txt_deuce);
		}else{
			if(curTurns==PlayerTuns){
				// win
				txtView.setText(R.string.txt_you_win);
			}else{
				// lose
				txtView.setText(R.string.txt_you_lose);
			}
		}
		(dialog.findViewById(R.id.dialog)).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					dialog.dismiss();
				}
				return false;
			}
		});
		dialog.show();
	}

	private int checkWin(int id){
		boolean isWin = false;
		// 横
		for(int row=0; row<3; row++){
			isWin = true;
			for(int col=0; col<3; col++){
				if(boardMap[row*3+col]!=id){
					isWin = false;
				}
			}
			if(isWin){
				switch(row){
				case 0:
					return HorizontalWinRow1;
				case 1:
					return HorizontalWinRow2;
				case 2:
					return HorizontalWinRow3;
				}
			}
		}
		// 纵
		for(int col=0; col<3; col++){
			isWin = true;
			for(int row=0; row<3; row++){
				if(boardMap[row*3+col]!=id){
					isWin = false;
				}
			}
			if(isWin){
				switch(col){
				case 0:
					return VerticalWinCol1;
				case 1:
					return VerticalWinCol2;
				case 2:
					return VerticalWinCol3;
				}
			}
		}
		// 交叉
		isWin = true;
		for(int i=0; i<3; i++){
			if(boardMap[i*3+i]!=id){
				isWin = false;
			}
		}
		if(isWin){
			return LeftCrossWin;
		}
		isWin = true;
		for(int i=0; i<3; i++){
			if(boardMap[(3-i-1)*3+i]!=id){
				isWin = false;
			}
		}
		if(isWin){
			return RightCrossWin;
		}
		
		return NotWin;
	}

	private int getPlayerPointId(){
		return selectPoint==Controller.CirclePoint?R.drawable.circle:R.drawable.cross;
	}
	
	private int getComputerPointId(){
		return selectPoint==Controller.CirclePoint?R.drawable.cross:R.drawable.circle;
	}
	
	/**
	 * 重新开始游戏
	 */
	private void restartGame(){
		isGameOver = false;
		resultView.setVisibility(View.GONE);
		for(ImageView view:points){
			view.setImageDrawable(null);
		}
		boardMap = new int[9];
		// 随机选取谁先下
		rnd = new Random(System.currentTimeMillis());
		curTurns = (rnd.nextInt()>>>1)%2==0?PlayerTuns:ComputerTuns;
		notifyForNextTurns();
	}
	
	/**
	 * 返回主菜单
	 */
	private void backToMenu(){
		Controller.startMenuActivity(this);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.menu_btn:
			backToMenu();
			break;
			
		case R.id.restart_btn:
			restartGame();
			break;
		}
	}
	
}
