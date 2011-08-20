package com.game.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class MenuActivity extends Activity implements OnClickListener {

	static final int[] RadioButtonIds = {R.id.circle_point, R.id.cross_point};
	
	private int selectPoint;
	
	private OnCheckedChangeListener radioBtnCheckedChangedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			
			if(isChecked){
				int curId = buttonView.getId();
				selectPoint = curId==R.id.circle_point?Controller.CirclePoint:Controller.CrossPoint;
				for(int id : RadioButtonIds){
					if(id!=curId){
						((RadioButton)findViewById(id)).setChecked(false);
					}
				}
			}
		}
		
	};
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		((RadioButton)this.findViewById(R.id.circle_point)).setOnCheckedChangeListener(radioBtnCheckedChangedListener);
		((RadioButton)this.findViewById(R.id.cross_point)).setOnCheckedChangeListener(radioBtnCheckedChangedListener);
		
		findViewById(R.id.exit_btn).setOnClickListener(this);
		findViewById(R.id.start_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.exit_btn:
			finish();
			break;
			
		case R.id.start_btn:
			Controller.startGameActivity(this, selectPoint);
			break;
		}
	}
	
}
