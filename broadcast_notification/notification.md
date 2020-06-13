# 알림 (Notification)

## 1. 개요
- 토스트 메시지는 잠시만 보였다가 사라짐
- 알림(Notification)은 애플리케이션의 정상 UI 외부에서 사용자에게 표시할 수 있는 메시지
	- 시스템에 알림을 실행하라고 명령하면 처음에 알림 영역(상태 표시줄)에서 아이콘으로 나타납니다. 
	<img src="https://developer.android.com/images/ui/notifications/notification-area_2x.png?hl=ko" width=300/>
	
	- 알림 세부 정보를 보려면, 사용자는 상태 표시줄을 아래로 스와이프하여 알림 창을 열 수 있습니다. 
	<img src="https://developer.android.com/images/ui/notifications/notification-drawer_2x.png?hl=ko" width=300/>
	
- **알림 분석**
	- 알림의 가장 일반적인 부분은 다음 그림과 같습니다.
	
	 	<img src="https://developer.android.com/images/ui/notifications/notification-callouts_2x.png?hl=ko" width=300/>
		1. 소형 아이콘: 이는 필수 항목이며 [setSmallIcon()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setSmallIcon(int))으로 설정합니다.
		2. 앱 이름: 시스템에서 제공합니다.
		3. 타임 스탬프: 시스템에서 제공하지만 [setWhen()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setWhen(long))으로 재정의하거나[setShowWhen(false)](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setShowWhen(boolean))으로 숨길 수 있습니다.
		4. 대형 아이콘: 이는 선택 항목이며(일반적으로 연락처 사진에만 사용되며, 앱 아이콘에는 사용하지 않음) [setLargeIcon()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setLargeIcon(android.graphics.Bitmap))으로 설정합니다.
		5. 제목: 이는 선택 항목이며 [setContentTitle()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setContentTitle(java.lang.CharSequence))로 설정합니다.
		6.	텍스트: 이는 선택 항목이며 [setContentText()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setContentText(java.lang.CharSequence))로 설정합니다.

		
	[참고자료: https://developer.android.com/guide/topics/ui/notifiers/notifications.html?hl=ko]
	
- **알림 채널**
	- **Android 8.0(API 레벨 26)**부터 모든 알림은 채널에 할당되어야 합니다. 그렇지 않으면 알림이 나타나지 않습니다. 
	- 알림을 채널로 분류하면 사용자가 (모든 알림을 비활성화하지 않고도) 앱의 특정 알림 채널을 비활성화할 수 있고 각 채널의 시청각적 옵션을 제어할 수 있습니다.  

	![](https://developer.android.com/images/ui/notifications/channel-settings_2x.png?hl=ko)
	
### 예제 프로젝트
- 예제 프로젝트 Github [링크](https://github.com/kwanulee/AndroidProgramming/tree/master/examples/NotificationTest)  
	
##2.간단한 알림 생성
  
###2.1  알림 내용 설정
- **[NotificationCompat.Builder](https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html)**를 사용하여 알림을 위한 내용과 채널을 지정하여 Builder 객체를 생성
- 최소한 다음 요소를 포함해야 함
	- 아이콘 설정: [setSmallIcon(int)](https://developer.android.com/reference/android/app/Notification.Builder.html#setSmallIcon(int))
	- 제목 설정: [setContentTitle()](https://developer.android.com/reference/android/app/Notification.Builder.html#setContentTitle(java.lang.CharSequence))
	- 상세 텍스트 설정: 	[setContentText()](https://developer.android.com/reference/android/app/Notification.Builder.html#setContentText(java.lang.CharSequence))
- 예제

	```java
	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
	    	.setSmallIcon(R.drawable.notification_icon)
	    	.setContentTitle("My notification")
	    	.setContentText("Detailed description of My notification ");
	```
	
- 기본적으로 알림의 텍스트 콘텐츠는 한 줄에 맞춰 잘립니다. 알림을 더 길게 표시하고 싶은 경우 [setStyle()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setStyle(android.support.v4.app.NotificationCompat.Style))을 사용하여 스타일 템플릿을 추가하여 확장 가능한 알림을 사용 설정할 수 있습니다. 예를 들어 다음 코드를 사용하면 더 큰 텍스트 영역이 생성됩니다

	```java
	NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
	        .setSmallIcon(R.drawable.notification_icon)
	        .setContentTitle("My notification")
	        .setContentText("Much longer text that cannot fit one line...")
	        .setStyle(new NotificationCompat.BigTextStyle()
	                    .bigText("Much longer text that cannot fit one line..."));
	```

###2.2 채널 만들기 및 중요도 설정
- **Android 8.0** 이상에서 알림을 제공하려면 먼저 [NotificationChannel](https://developer.android.com/reference/android/app/NotificationChannel?hl=ko) 인스턴스를 [createNotificationChannel()](https://developer.android.com/reference/android/app/NotificationManager?hl=ko#createNotificationChannel(android.app.NotificationChannel))에 전달하여 앱의 알림 채널 을 시스템에 등록해야 합니다.

- NotificationChannel 생성 예제

	```java
	final String CHANNEL_ID = "my_channel_01";

	private void createNotificationChannel() {
	        if (android.os.Build.VERSION.SDK_INT >=26) {	            
	            // The user-visible name of the channel.
	            CharSequence name = getString(R.string.channel_name);
	
	            // The user-visible description of the channel.
	            String description = getString(R.string.channel_description);
	
	            int importance = NotificationManager.IMPORTANCE_HIGH;
	
	            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
	
	            // Configure the notification channel.
	            channel.setDescription(description);
	            channel.enableLights(true);
	            channel.setLightColor(Color.BLUE);
	            channel.enableVibration(true);
	            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
	
				// Register the channel with the system
	            NotificationManager notificationManager =
	                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	            notificationManager.createNotificationChannel(mChannel);
	        }
	    }
	```
	
https://github.com/kwanulee/AndroidProgramming/blob/master/examples/NotificationTest/app/src/main/java/com/kwanwoo/android/notificationtest/MainActivity.java#L41-L67

	
###2.3 알림 발행
- 알림을 표시하려면 [NotificationManagerCompat.notify()](https://developer.android.com/reference/androidx/core/app/NotificationManagerCompat?hl=ko#notify(int,%20android.app.Notification))를 호출하고 알림의 고유 ID 및 [NotificationCompat.Builder.build()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#build())의 결과를 전달하세요. 

- 예제

	```java
	NotificationManagerCompat nm = NotificationManagerCompat.from(this);
    nm.notify(NOTI_ID, mBuilder.build());
	```

		
##3. 알림 클릭 행동 정의
- 사용자가 알림을 클릭했을 때의 행동을 정의
- **행동**은 [PendingIntent](https://developer.android.com/reference/android/app/PendingIntent?hl=ko)로 정의되며, 이는 액티비티를 시작시킬 인텐트를 포함하고 있음

###3.1 PendingIntent 
- [PendingIntent](https://developer.android.com/reference/android/app/PendingIntent?hl=ko) 클래스는 다른 인텐트를 래핑하며 다른 응용 프로그램으로 전달하여 실행 권한을 준다
	- 일반 인텐트와의 차이점으로는 다른 컴포넌트에게 작업을 요청하는 인텐트를 사전에 생성시키고 만든다는 점과 "특정 시점"에 자신이 아닌 다른 컴포넌트들이 펜딩인텐트를 사용하여 다른 컴포넌트에게 작업을 요청시키는 데 사용된다는 점이 차이점
	
		[출처: http://techlog.gurucat.net/80 ]	
- 다음 세 개의 정적 메서드를 이용하여 생성
	- PendingIntent **getActivity**(Context context, int requestCode, Intent intent, int flag) 
		- Activity를 시작하는 인텐트를 생성함
		- Context.startActivity(Intent) 호출과 유사함
	-  PendingIntent **getBroadcast**(Context context, int requestCode, Intent intent, int flag)
		- BroadcastReceiver를 시작하는 인텐트를 생성함  
		- Context.startBroadcast(Intent) 호출과 유사함

	-  PendingIntent **getService**(Context context, int requestCode, Intent intent, int flag)
		- Service를 시작하는 인텐트를 생성함 
		- Context.startService(Intent) 호출과 유사함

###3.2 PendingIntent를 알림 클릭 동작과 연결
- 	NotificationCompat.Builder의 [setContentIntent()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setContentIntent(android.app.PendingIntent)) 메소드 사용
-  예제 

	```java
        Intent notificationIntent = new Intent(this, NotificationDetail.class);
        notificationIntent.putExtra("notificationId", NOTI_ID); //전달할 값
        PendingIntent contentIntent 
        		= PendingIntent.getActivity(
        					this, 
        					0, 
        					notificationIntent, // 작업 요청할 명시적 인텐트
        					PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "default");
        notification.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
*           .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTI_ID, notification.build());

	```
	
	https://github.com/kwanulee/AndroidProgramming/blob/master/examples/NotificationTest/app/src/main/java/com/kwanwoo/android/notificationtest/MainActivity.java#L73-L90
	

## 4. 알림 삭제	
- 알림 삭제는 다음 중 한 가지가 발생할 때 일어납니다.
	- 알림을 만들 때 [setAutoCancel()](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder?hl=ko#setAutoCancel(boolean))을 호출했으며 사용자가 알림을 클릭합니다.

		```java
	        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID);
	        notification.setContentTitle(getString(R.string.notification_title))
	                .setContentText(getString(R.string.notification_text))
	                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
	                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
	                .setContentIntent(contentIntent)
	        *       .setAutoCancel(true);
	 ```
	 
	- 특정 알림 ID에 [cancel()](https://developer.android.com/reference/android/app/NotificationManager?hl=ko#cancel(int))을 호출합니다. 이 메서드는 진행 중인 알림도 삭제합니다.

		```java
		public class NotificationDetail extends AppCompatActivity {
		
		    @Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_notification_detail);
		        CharSequence s = "전달 받은 값은 ";
		        int id=0;
		
		        Bundle extras = getIntent().getExtras();
		        if (extras == null) {
		            s = "error";
		        }
		        else {
		            id = extras.getInt("notificationId");
		        }
		        TextView t = (TextView) findViewById(R.id.textView);
		        s = s+" "+id;
		        t.setText(s);
		        NotificationManager nm =
		                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		        //노티피케이션 제거
		*       nm.cancel(id);
		    }
		}
		```
	- [cancelAll()](https://developer.android.com/reference/android/app/NotificationManager?hl=ko#cancelAll())을 호출하여 이전에 발행한 모든 알림을 삭제합니다. 

	
