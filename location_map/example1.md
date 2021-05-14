## 연습 1 - 예제 프로젝트 시작하기 

1. 새로운 Android 프로젝트 생성
	- *LocationService* 라는 프로젝트를 생성한다.
2. 프로젝트에 **Google Play Services** 라이브러리 추가
    1. build.gradle (Module:app) 파일 오픈
    2. 새로운 빌드 규칙 추가
    
		```java
		dependencies {
		      ...
		      implementation 'com.google.android.gms:play-services-location:18.0.0'
		}
		```
    3. 툴바에서 "Sync Project with Graddle File" 또는 "Sync Now" 클릭

3. activity\_main.xml 파일을 열고, 다음 레이아웃 코드로 대체
	- LinearLayout : Vertical
		- Button 위젯 : **GET LAST LOCATION** 버튼
		- TextView 위젯 : Latitiude 표시
		- TextView 위젯 : Longitude 표시
		- TextView 위젯 : Precision 표시
		- LinearLayout: Horizontal
			- Button 위젯 : **START UPDATES** 버튼
			- Button 위쳊 : **STOP UPDATES** 버튼
		- Button 위젯: **주소 변환** 버튼
		- TextView 위젯: 주소 표시

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	
	    <Button
	        android:id="@+id/get_last_location_button"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="Get Last Location" />
	
	    <TextView
	        android:id="@+id/latitude_text"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_marginLeft="10dp"
	        android:layout_marginStart="10dp"
	        android:textSize="16sp" />
	
	    <TextView
	        android:id="@+id/longitude_text"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_marginLeft="10dp"
	        android:layout_marginStart="10dp"
	        android:textSize="16sp" />
	
	    <TextView
	        android:id="@+id/precision_text"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_marginLeft="10dp"
	        android:layout_marginStart="10dp"
	        android:textSize="16sp" />
	
	    <LinearLayout
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:baselineAligned="false"
	        android:orientation="horizontal">
	
	        <Button
	            android:id="@+id/start_updates_button"
	            android:layout_width="0dp"
	            android:layout_weight="1"
	            android:layout_height="wrap_content"
	            android:layout_marginEnd="10dp"
	            android:layout_marginRight="10dp"
	            android:gravity="center"
	            android:text="Start updates" />
	
	        <Button
	            android:id="@+id/stop_updates_button"
	            android:layout_width="0dp"
	            android:layout_weight="1"
	            android:layout_height="wrap_content"
	            android:layout_marginLeft="10dp"
	            android:layout_marginStart="10dp"
	            android:enabled="false"
	            android:gravity="center"
	            android:text="Stop updates" />
	    </LinearLayout>
	
	    <Button
	        android:id="@+id/address_button"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:layout_marginEnd="10dp"
	        android:layout_marginRight="10dp"
	        android:gravity="center"
	        android:text="Get address" />
	    <TextView
	        android:id="@+id/address_text"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_marginLeft="10dp"
	        android:layout_marginStart="10dp"
	        android:textSize="16sp" />
	</LinearLayout>
	```