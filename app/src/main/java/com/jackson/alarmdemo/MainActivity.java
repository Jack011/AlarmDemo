package com.jackson.alarmdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent, mLoggerReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent,
            mLoggerReceiverPendingIntent;
    private static final long INITIAL_ALARM_DELAY = 5 * 1000L;//闹钟延迟时间
    protected static final long JITTER = 60000L;//颤抖,恐慌

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the AlarmManager Service 获得闹钟管理器
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        //创建一个开启用来显示闹铃通知的Intent
        mNotificationReceiverIntent = new Intent(MainActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        //创建一个启动广播的延迟Intent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mNotificationReceiverIntent, 0);

        // Create an Intent to broadcast to the AlarmLoggerReceiver
        //创建一个启动警报服务的Intent
        mLoggerReceiverIntent = new Intent(MainActivity.this,
                AlarmLoggerReceiver.class);

        // Create PendingIntent that holds the mLoggerReceiverPendingIntent
        //创建一个启动警报服务的延时Intent
        mLoggerReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mLoggerReceiverIntent, 0);

        // Set up single alarm Button 设置一个警报按钮
        final Button singleAlarmButton = (Button) findViewById(R.id.single_alarm_button);
        singleAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set single alarm 设置一个定时器
                mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + INITIAL_ALARM_DELAY,
                        mNotificationReceiverPendingIntent);

                // Set single alarm to fire shortly after previous alarm
                //设置一个警报不久后的警报
                mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + INITIAL_ALARM_DELAY
                                + JITTER, mLoggerReceiverPendingIntent);

                // Show Toast message
                Toast.makeText(getApplicationContext(), "Single Alarm Set",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Set up repeating Alarm Button
        //重复警报
        final Button repeatingAlarmButton = (Button) findViewById(R.id.repeating_alarm_button);
        repeatingAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Set repeating alarm
                mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        mNotificationReceiverPendingIntent);

                // Set repeating alarm to fire shortly after previous alarm

                mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY
                                + JITTER,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        mLoggerReceiverPendingIntent);

                // Show Toast message
                Toast.makeText(getApplicationContext(), "Repeating Alarm Set",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Set up inexact repeating alarm Button
        final Button inexactRepeatingAlarmButton = (Button) findViewById(R.id.inexact_repeating_alarm_button);
        inexactRepeatingAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Set inexact repeating alarm
                mAlarmManager.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        mNotificationReceiverPendingIntent);
                // Set inexact repeating alarm to fire shortly after previous alarm
                mAlarmManager.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY
                                + JITTER,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        mLoggerReceiverPendingIntent);

                Toast.makeText(getApplicationContext(),
                        "Inexact Repeating Alarm Set", Toast.LENGTH_LONG)
                        .show();
            }
        });

        // Set up cancel repeating alarm Button
        final Button cancelRepeatingAlarmButton = (Button) findViewById(R.id.cancel_repeating_alarm_button);
        cancelRepeatingAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Cancel all alarms using mNotificationReceiverPendingIntent
                mAlarmManager.cancel(mNotificationReceiverPendingIntent);

                // Cancel all alarms using mLoggerReceiverPendingIntent
                mAlarmManager.cancel(mLoggerReceiverPendingIntent);

                // Show Toast message
                Toast.makeText(getApplicationContext(),
                        "Repeating Alarms Cancelled", Toast.LENGTH_LONG).show();
            }
        });
    }

    //两秒内按返回键两次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
