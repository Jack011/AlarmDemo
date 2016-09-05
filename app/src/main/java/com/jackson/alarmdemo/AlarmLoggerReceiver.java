package com.jackson.alarmdemo;

import java.text.DateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**警报记录接收器**/
public class AlarmLoggerReceiver extends BroadcastReceiver {

	private static final String TAG = "AlarmLoggerReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {

		// Log receipt of the Intent with timestamp
		Log.i(TAG,"Logging alarm at:" + DateFormat.getDateTimeInstance().format(new Date()));
		Toast.makeText(context, "警报接收器收到一条警报:有色狼", Toast.LENGTH_SHORT).show();
	}
}
