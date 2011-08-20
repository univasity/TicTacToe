package com.game.tictactoe;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Utils {
	
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
	
	public static Dialog createDialog(Context context, int imgResId, int txtResId){
		final Dialog dialog = new Dialog(context, android.R.style.Theme_Dialog);
		dialog.setContentView(R.layout.result_dialog);
		ImageView imgView = (ImageView)dialog.findViewById(R.id.img);
		TextView txtView = (TextView)dialog.findViewById(R.id.txt);
		imgView.setImageResource(imgResId);
		txtView.setText(txtResId);
		(dialog.findViewById(R.id.dialog)).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					dialog.dismiss();
				}
				return false;
			}
		});
		return dialog;
	}
	
}
