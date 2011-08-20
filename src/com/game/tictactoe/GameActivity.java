package com.game.tictactoe;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
	
	private ImageView[] points;
	
	private int selectPoint;
	
	private TextView playerTurnsView;
	private TextView computerTurnsView;
	
	private TextView menuBtnView;
	private TextView restartBtnView;
	
	private Animation shinningAnim;
	
	private ImageView resultView;
	
	private Random rnd;
	private boolean isGameOver;
	
	private Board board;
	private int PlayerTurn = Board.PLAYER1_TURN;
	private int ComputerTurn = Board.PLAYER2_TURN;
	
	private final int MSG_AUTOMOVE = 0x001;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==MSG_AUTOMOVE){
				int nextStep = board.getNextStep();
				Log.i(LOG_TAG, "nextStep..."+nextStep);
				if(nextStep!=-1){
					int curTurn = board.getTurn();
					if(board.setDataAt(nextStep)){
						points[nextStep].setImageResource(curTurn==PlayerTurn?getPlayerPointId():getComputerPointId());
						int state = board.checkWin();
						if(state==Board.NotWin){
							notifyForNextTurns();
						}else{
							isGameOver = true;
							showWinResult(state);
						}
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
		selectPoint = intent.getIntExtra("point", Define.CirclePoint);
		if(selectPoint==Define.CirclePoint){
			((ImageView)findViewById(R.id.player_point)).setImageResource(R.drawable.circle);
			((ImageView)findViewById(R.id.computer_point)).setImageResource(R.drawable.cross);
		}else{
			((ImageView)findViewById(R.id.player_point)).setImageResource(R.drawable.cross);
			((ImageView)findViewById(R.id.computer_point)).setImageResource(R.drawable.circle);
		}
		
		//
		board = new MyBoard(Define.BoardSize);
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
		menuBtnView = (TextView)findViewById(R.id.menu_btn);
		menuBtnView.setOnClickListener(this);
		restartBtnView = (TextView)findViewById(R.id.restart_btn);
		restartBtnView.setOnClickListener(this);
		
		shinningAnim = AnimationUtils.loadAnimation(this, R.anim.shining);
		
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
				if(board.getTurn()==PlayerTurn)
				{
					((ImageView)v).setImageResource(getPlayerPointId());
					if(board.setDataAt(id-R.id.pos0)){
						points[id-R.id.pos0].setImageResource(getPlayerPointId());
						int state = board.checkWin();
						if(state==Board.NotWin){
							notifyForNextTurns();
						}else{
							isGameOver = true;
							showWinResult(state);
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private void notifyForNextTurns() {
		board.changeTurn();
		if(board.getTurn()==PlayerTurn){
			computerTurnsView.clearAnimation();
			playerTurnsView.startAnimation(shinningAnim);
		}else{
			playerTurnsView.clearAnimation();
			computerTurnsView.startAnimation(shinningAnim);
			
			long delay = 1000+((rnd.nextInt()>>>1)%2000);
			mHandler.sendEmptyMessageDelayed(MSG_AUTOMOVE, delay);
		}
	}
	
	private void showWinResult(int winCode) {
		int resId = 0;
		switch(winCode){
		case Board.HorizontalWinRow1:
			resId = R.drawable.line_h1;
			break;
			
		case Board.HorizontalWinRow2:
			resId = R.drawable.line_h2;
			break;
			
		case Board.HorizontalWinRow3:
			resId = R.drawable.line_h3;
			break;
			
		case Board.VerticalWinCol1:
			resId = R.drawable.line_v1;
			break;
			
		case Board.VerticalWinCol2:
			resId = R.drawable.line_v2;
			break;
			
		case Board.VerticalWinCol3:
			resId = R.drawable.line_v3;
			break;
			
		case Board.LeftCrossWin:
			resId = R.drawable.line_lcross;
			break;
			
		case Board.RightCrossWin:
			resId = R.drawable.line_rcross;
			break;
			
		case Board.Deuce:
			resId = -1;
			break;
		}
		
		if(resId!=-1){
			resultView.setBackgroundResource(resId);
			resultView.setVisibility(View.VISIBLE);
		}
		
		Dialog dialog = null;
		if(winCode==Board.Deuce){
			dialog = Utils.createDialog(this, -1, R.string.txt_deuce);
		}else{
			if(board.getTurn()==PlayerTurn){
				// win
				dialog = Utils.createDialog(this, -1, R.string.txt_you_win);
			}else{
				// lose
				dialog = Utils.createDialog(this, -1, R.string.txt_you_lose);
			}
		}
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				playerTurnsView.clearAnimation();
				computerTurnsView.clearAnimation();
				restartBtnView.startAnimation(shinningAnim);
			}
		});
		dialog.show();
	}

	private int getPlayerPointId(){
		return selectPoint==Define.CirclePoint?R.drawable.circle:R.drawable.cross;
	}
	
	private int getComputerPointId(){
		return selectPoint==Define.CirclePoint?R.drawable.cross:R.drawable.circle;
	}
	
	/**
	 * 重新开始游戏
	 */
	private void restartGame(){
		isGameOver = false;
		restartBtnView.clearAnimation();
		resultView.setVisibility(View.GONE);
		for(ImageView view:points){
			view.setImageDrawable(null);
		}
		board.init();
		// 随机选取谁先下
		rnd = new Random(System.currentTimeMillis());
		board.setTurn((rnd.nextInt()>>>1)%2==0?PlayerTurn:ComputerTurn);
		notifyForNextTurns();
	}
	
	/**
	 * 返回主菜单
	 */
	private void backToMenu(){
		Utils.startMenuActivity(this);
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
