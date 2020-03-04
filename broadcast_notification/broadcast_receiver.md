# 브로드캐스트 리시버

##1. 브로드캐스트 개요

- 안드로이드 앱은 안드로이드 시스템 혹은  다른 안드로이드 앱 간에 메시지를 주고 받을 수 있다.

	- 상황에 따라 안드로이드 시스템은 다양한 브로드캐스트 메시지를 전달
		- 시스템 시작
		- 디바이스 충전 시작
		- 문자 수신
	- 어떠한 앱은 다른 앱이 관심이 있을 만한 정보를 브로드캐스트 메시지로 전달 
		- 예, 새로운 데이터 다운로드됨 
		
- 앱에서 특정 브로드캐스트를 받기 위해서
	- 시스템에 특정 브로드캐스트 수신을 등록(register)
	- 해당 브로드캐스트가 발생하면 시스템은 등록된 앱들에게 전달

### 시스템 브로드캐스트
* 시스템에서 특정 이벤트 발생시 보내는 브로드캐스트 
    - 브로드캐스트는 Intent로 만들어져서 전달됨
        + Intent에 extra 데이터가 있기도 함
        + Intent를 받기 위해 IntentFilter가 필요함
    - 시스템 브로드캐스트 전체 목록
      + Sdk/platforms/android-26/data/broadcast_actions.txt

---
##2. 브로드캐스트 리시버
* 두 가지 방법이 가능
    - Manifest에 리시버 정의하기 (**Android 8.0-API level 26부터 사용불가**)
    - 컨텍스트에 리시버 정의하기

---
###2.1 Manifest에 브로드캐스트 리시버 정의하기
- Manifest에 브로드캐스트 리시버를 선언하면, 브로드캐스트가 보내질때 (앱이 실행중이 아닐때) 시스템은 앱을 시작시킨다. (API level 25 이하에서만 사용 가능) 
* AndroidManifest.xml

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<manifest >
	    <application ... >
	        <!-- 생략 -->
	        <receiver
	           android:name=".SystemBroadcastReceiver" android:enabled="true" android:exported="true">
	            <intent-filter>
	                <action android:name="android.intent.action.AIRPLANE_MODE"/>
	                <action android:name="android.intent.action.BOOT_COMPLETED"/>
	            </intent-filter>
	        </receiver>
	    </application>
	</manifest>
	```

	* exported 속성은 해당 리시버가 앱 외부에서도 호출 가능한지를 나타냄

	https://github.com/kwanulee/Android/blob/master/examples/BroadcastTest/app/src/main/AndroidManifest.xml#L22-L30


* SystemBroadcastReceiver.java

	```java
	public class SystemBroadcastReceiver extends BroadcastReceiver {
	    private static final String TAG = "SystemBroadcastReceiver";
	
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Action: " + intent.getAction() + "\n");
	        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
	        String log = sb.toString();
	        Log.d(TAG, log);
	        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
	    }
	}
	```

* 실행 결과: 에어플래인 모드를 켜고 끌때마다 토스트 메시지와 로그가 출력됨

https://github.com/kwanulee/Android/blob/master/examples/BroadcastTest/app/src/main/java/com/kwanwoo/android/broadcasttest/SystemBroadcastReceiver.java

---
###2.2 컨텍스트에 리시버 정의하기
1. 앞의 경우와 마찬가지로
    - BroadcastReceiver를 상속 받은 SystemBroadcastReceiver 클래스 생성
2. SystemBroadcastReceiver 객체 생성

		BroadcastReceiver br = new SystemBroadcastReceiver();
	
3. IntentFilter생성하고 registerReceiver()로 등록

		IntentFilter filter = new 	IntentFilter();
		intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		this.registerReceiver(br, filter);

4. 등록을 취소하려면 [unregisterReceiver](https://developer.android.com/reference/android/content/Context.html#unregisterReceiver(android.content.BroadcastReceiver))() 호출
    - 보통 onCreate()나 onResume()에서 등록
    - onDestroy()나 onPause()에서 등록 취소



	```java
	public class MainActivity extends AppCompatActivity {
	    BroadcastReceiver mSystemBR;
	    //...
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        mSystemBR = registSystemBR();
	
	       //...
	    }
	
	    private BroadcastReceiver registSystemBR() {
	        SystemBroadcastReceiver my_br = new SystemBroadcastReceiver();
	        IntentFilter intentFilter_br = new IntentFilter();
	        intentFilter_br.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
	        intentFilter_br.addAction(Intent.ACTION_BOOT_COMPLETED);
	        
	        registerReceiver(my_br, intentFilter_br);
	        
	        return my_br;
	    }
	
	    @Override
	    protected void onDestroy() {
	        unregisterReceiver(mSystemBR);
	        super.onDestroy();
	    }
	}
	```

https://github.com/kwanulee/Android/blob/master/examples/BroadcastTest/app/src/main/java/com/kwanwoo/android/broadcasttest/MainActivity.java

---
##3. 배터리 상태 표시
- 배터리를 많이 소모하는 작업은 배터리의 남은 양에 따라 작업의 강도를 조정할 필요 있음
- 안드로이드 시스템은 배터리의 상태를 대신 감시하며 변화가 있을 때마다 방송을 함
- 배터리와 관련된 방송

	|액션						| 설명	|
	|:----------------|:--------------------|
	| ACTION\_BATTERY\_CHANGED | 배터리의 충전 상태 변경 (매니페스트에 등록해서 받을 수 없음) |
	| ACTION\_BATTERY\_LOW | 배터리 상태가 위험 수준으로 낮아졌음 |
	| ACTION\_BATTERY\_OKAY | 배터리 상태가 위험 수준에서 양호한 상태로 전환되었음 |
	| ACTION\_POWER\_CONNECTED | 외부 전원이 연결되었음 |
	| ACTION\_POWER\_DISCONNECTED | 외부 전원이 분리되었음 |
	
###3.1 배터리의 중요한 변화 모니터링
- **ACTION\_BATTERY\_CHANGED**는 모든 배터리의 상태변화시 발생되지만, 이를 지속적으로 모니터링하는 작업은 배터리 전력 소비에 큰 영향을 주므로, 중요한 변화가 있을 때만 모니터링하는 것이 좋음
- 주요한 배터리 상태변화 모터링을 위한 브로드캐스트 리시버 등록 예

	```java
	    mBatteryBR = new BatteryWatchBR(status_output);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mBatteryBR, filter);
	```

	
###3.2 현재 충전상태 확인
- 배터리의 현재상태를 확인하기 위해서는 **BroadcastReceiver를 등록할 필요가 없습니다**. 다음 스니펫에서와 같이 registerReceiver를 호출하여 null을 수신기로 전달하면 현재 배터리 상태 인텐트가 반환됩니다. [https://developer.android.com/training/monitoring-device-state/battery-monitoring.html]

	```java
	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	Intent batteryStatus = context.registerReceiver(null, ifilter);
	```	
		
- 배터리 상태에 대한 상세한 정보는 인텐트의 **Extras**에 실려 전달됨
	- 조사 가능한 값들은 [BatteryManager](https://developer.android.com/reference/android/os/BatteryManager.html) 클래스에 상수로 정의되어 있음 
	
	|상태				| 설명				|
	|:---------------|:--------------|
	|EXTRA\_PLUGGED | 외부 전원에 연결되어 있는지 조사|
	|EXTRA\_STATUS | 배터리의 현재 상태 |
	|EXTRA\_LEVEL | 배터리 현재 충전 레벨|
	|EXTRA\_HEALTH | 배터리의 성능상태 조사|
	
```java
public class BatteryWatchBR extends BroadcastReceiver {
    TextView mStatus;

    public BatteryWatchBR(TextView status) {
        mStatus = status;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
*       printBatteryStatus(batteryStatus);
    }

    private void printBatteryStatus(Intent intent) {
*       int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
*       int plug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
*       int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
*       int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);

        String sPlug, sStatus, sHealth;

        switch(plug) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                sPlug = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                sPlug = "USB";
                break;
            default:
                sPlug = "Battery";
                break;
        }

		 // ....
        mStatus.setText(String.format("Remain: %d \nConnection: %s\n Health: %s \n Status: %s\n ", 
        			level, sPlug, sHealth, sStatus));
    }
}
```
	
https://github.com/kwanulee/Android/blob/master/examples/BroadcastTest/app/src/main/java/com/kwanwoo/android/broadcasttest/BatteryWatchBR.java



---
## SMS 수신
* 인텐트 필터
    - IntentFilter intentFilter = new IntentFilter(  
      **Telephony.Sms.Intents.SMS\_RECEIVED\_ACTION**);
* 권한 부여 필요
    - AndroidManifest.xml
   
    	```xml
    	<uses-permission android:name="android.permission.RECEIVE_SMS">
    	```
    - 안드로이드 6.0부터는 권한 확인/요청 코드 추가 필요
        + 이전 강의 자료 참고

* 예제

	```java
	public class SMSBroadcastReceiver extends BroadcastReceiver {
	    private static final String TAG = "SMSBroadcastReceiver";
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
	            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
	            if (messages != null) {
	                if (messages.length == 0)
	                    return;
	                StringBuilder sb = new StringBuilder();
	                for (int i = 0; i < messages.length; i++) {
	                    sb.append(messages[i].getMessageBody());
	                }
	                String sender = messages[0].getOriginatingAddress();
	                String message = sb.toString();
	
	                Log.i("SMSBroadcastReceiver","From " + sender + " : "+message);
	                Toast.makeText(context, "From" + sender + " : "+message, Toast.LENGTH_SHORT).show();
	
	
	            }
	        }
	    }
	}
	```

  https://github.com/kwanulee/Android/blob/master/examples/BroadcastTest/app/src/main/java/com/kwanwoo/android/broadcasttest/SMSBroadcastReceiver.java