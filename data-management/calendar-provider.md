<style> 
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**이전 학습**: 콘텐츠 제공자](content-provider.html)

# 캘린더 제공자 (Calendar Provider)
- 본 자료는 다음 내용을 바탕으로 요약 및 각색된 것입니다. 
	- 참고문서: https://developer.android.com/guide/topics/providers/calendar-provider?hl=ko

## 1. 개요

- **캘린더 제공자**는 사용자의 캘린더 이벤트에 대한 리포지토리입니다. 
- **Calendar Provider API**를 사용하면 캘린더, 이벤트, 참석자, 알림 등에서 쿼리, 삽입, 업데이트 및 삭제 등의 작업을 수행할 수 있습니다.
- **캘린더 제공자 데이터 모델**

	![](https://developer.android.com/images/providers/datamodel.png?hl=ko)
	
	- [CalendarContract](https://developer.android.com/reference/android/provider/CalendarContract?hl=ko)가 캘린더의 데이터 모델과 이벤트 관련 정보를 정의합니다.


	| 테이블(클래스) | 설명 |
	|---------|---------|
	| CalendarContract.Calendars |  이 테이블의 행마다 한 캘린더의 세부 정보(예: 이름, 색상, 동기화 정보 등)가 포함됩니다.|
	| CalendarContract.Events	| 이 테이블의 행마다 한 이벤트의 세부 정보(예: 이벤트 제목, 위치, 시작 시간, 종료 시간 정보 등)가 포함됩니다. |
	| CalendarContract.Instances	|이 테이블에는 각 이벤트 발생의 시작 시간과 종료 시간이 담겨 있습니다. 이 테이블의 각 행이 하나의 이벤트 발생을 나타냅니다|
	|CalendarContract.Attendees|	이 테이블에는 이벤트 참석자(게스트) 정보가 담겨 있습니다. 각 행은 주어진 이벤트의 게스트 한 사람을 나타냅니다. 이것은 게스트의 유형과, 이벤트에 대한 해당 게스트의 참석 여부 응답을 나타냅니다.|
	|CalendarContract.Reminders|	이 테이블에는 경고/알림 데이터가 담겨 있습니다. 각 행이 주어진 이벤트에 대한 경고 하나를 나타냅니다. 이벤트 하나에 여러 개의 알림이 있을 수 있습니다. 이벤트당 최대 알림 개수는 MAX_REMINDERS에서 지정되고, 이는 주어진 캘린더를 소유한 동기화 어댑터에 의해 설정됩니다. |

## 2. 캘린더 제공자의 접근 권한

- 캘린더 데이터를 읽으려면 애플리케이션의 매니페스트 파일에 **[READ_CALENDAR](https://developer.android.com/reference/android/Manifest.permission?hl=ko#READ_CALENDAR)** 권한이 포함되어 있어야 합니다. 
- 캘린더 데이터를 삭제, 삽입 또는 업데이트하려면 **[WRITE_CALENDAR](https://developer.android.com/reference/android/Manifest.permission?hl=ko#WRITE_CALENDAR)** 권한이 포함되어 있어야 합니다.

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<manifest xmlns:android="http://schemas.android.com/apk/res/android"...>
	    <uses-sdk android:minSdkVersion="14" />
	    <uses-permission android:name="android.permission.READ_CALENDAR" />
	    <uses-permission android:name="android.permission.WRITE_CALENDAR" />
	    ...
	</manifest>
	```

 

### 2.1 데이터나 장치에 접근하기 전에 **권한**을 확인
- **Android 6.0 (API level 23) 이상**부터는 **앱 실행 중에 필요한 권한(permission)을 반드시 확인하고 없으면 요청**해야 합니다.

```java
public class MainActivity extends AppCompatActivity {

    final int REQUEST_CODE_ACCESS_CALENDARS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 확인
        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR) &&
        	(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR)) {
                != PackageManager.PERMISSION_GRANTED) { // 권한이 없으므로, 사용자에게 권한 요청 다이얼로그 표시
            	ActivityCompat.requestPermissions(
            			MainActivity.this,
   						new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 
       					REQUEST_CODE_ACCESS_CALENDARS);
        } else // 권한 있음! 해당 데이터나 장치에 접근!
            readCalendars();
    }
```

https://github.com/kwanulee/AndroidProgramming/blob/master/examples/ContentResolverTest/app/src/main/java/com/kwanwoo/android/contentresolvertest/MainActivity.java#L20-L36

### 2.2 요청 다이얼로그 결과(Allow 또는 Deny)에 따라

```java
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACCESS_CALENDARS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readCalendars();
            } else {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
```

https://github.com/kwanulee/AndroidProgramming/blob/master/examples/ContentResolverTest/app/src/main/java/com/kwanwoo/android/contentresolvertest/MainActivity.java#L78-L88


## 3. 캘린더 테이블

- [CalendarContract.Calendars](https://developer.android.com/reference/android/provider/CalendarContract.Calendars?hl=ko) 테이블에는 각각의 캘린더에 대한 세부 정보가 포함되어 있습니다. 

| 상수 | 설명|
|----|------|
|NAME|	캘린더 이름입니다.|
|CALENDAR\_DISPLAY_NAME|	사용자에게 표시되는 이 캘린더의 이름입니다.|
|VISIBLE|	캘린더를 표시하기로 선택했는지를 나타내는 부울입니다. 값이 0인 경우 이 캘린더와 관련된 이벤트를 표시하면 안 된다는 뜻입니다. 값이 1인 경우 이 캘린더와 관련된 이벤트를 표시해야 한다는 뜻입니다. 이 값은 CalendarContract.Instances 테이블의 행을 생성하는 데 영향을 미칩니다.|
|SYNC_EVENTS|	캘린더를 동기화하고 이 캘린더의 이벤트를 기기에 저장해야 할지를 나타내는 부울입니다. 값이 0인 경우 이 캘린더를 동기화하거나 이에 속한 이벤트를 기기에 저장하면 안 된다는 뜻입니다. 값이 1인 경우 이 캘린더에 대한 이벤트를 동기화하고 이에 속한 이벤트를 기기에 저장하라는 뜻입니다.|
- 지원되는 필드의 전체 목록은 [CalendarContract.Calendars](https://developer.android.com/reference/android/provider/CalendarContract.Calendars?hl=ko) 참조 

### 3.1 캘린더 쿼리
- 다음은 특정한 사용자가 소유한 캘린더를 가져오는 방법을 나타낸 예시입니다.
- 
- [query](https://developer.android.com/reference/android/content/ContentResolver.html#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)) 메소드 ([ContentResolver](https://developer.android.com/reference/android/content/ContentResolver.html) 클래스 내)


	```java
	Cursor query (Uri uri, 
	                String[] projection, 
	                String selectionClause, 
	                String[] selectionArgs, 
	                String sortOrder)
	```
	인수            | 설명
	--------------|-------------------
	uri           | 콘텐츠 제공자의 URI. content:// 형식
	projection    | 반환해야 할 열들
	selectionClause     | 선택될 행들에 대한 조건
	selectionArgs | 조건에 필요한 인수
	sortOrder     | 선택된 행들의 정렬 방법
	
- 콘텐츠 URI
	+ 콘텐츠 URI는 제공자에서 데이터를 식별하는 URI
	+ 콘텐츠 URI에는 전체 제공자의 상징적인 이름(제공자의 권한)과 테이블을 가리키는 이름(경로)이 포함
	+ 형식
	
		```
		content://AUTHORITY/PATH/ID
		```

- 예제
	+ **연락처 정보**를 제공하는 제공자에 액세스하는 데 필요한 변수를 **ContactsContract** 계약 클래스에 정의된 상수를 이용하여 정의 

		```java
	        String [] projection = {
	                ContactsContract.CommonDataKinds.Phone._ID,
	                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
	                ContactsContract.CommonDataKinds.Phone.NUMBER
	        };
	
			 // 연락처 전화번호 타입에 따른 행 선택을 위한 선택 절
	        String selectionClause = ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ";
	
	        // 전화번호 타입이 'MOBILE'인 것을 지정
	        String[] selectionArgs = {""+ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE};
		```
		
		https://github.com/kwanulee/AndroidProgramming/blob/master/examples/ContentResolverTest/app/src/main/java/com/kwanwoo/android/contentresolvertest/MainActivity.java#L40-L50
		
	+ 설정된 파라미터를 바탕으로 query() 메소드를 실행
	
		```java
	        Cursor c = getContentResolver().query(
	                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  // 조회할 데이터 URI
	                projection, 		// 조회할 컬럼 들
	                selectionClause, 	// 선택될 행들에 대한 조건절
	                selectionArgs, 		// 조건절에 필요한 파라미터
	                null);				// 정렬 안함
		```

		https://github.com/kwanulee/AndroidProgramming/blob/master/examples/ContentResolverTest/app/src/main/java/com/kwanwoo/android/contentresolvertest/MainActivity.java#L49-L54
		
### 3.2 쿼리 결과 표시

- 쿼리 결과인 Cursor 객체가 들어 있는 [SimpleCursorAdapter](https://developer.android.com/reference/android/widget/SimpleCursorAdapter.html) 객체를 생성하며, 이 객체를 [ListView](https://developer.android.com/reference/android/widget/ListView.html)에 대한 어댑터로 설정

	1. ListView의 항목 뷰 정의 (item.xml)
	
		```java
		<LinearLayout
		    xmlns:android="http://schemas.android.com/apk/res/android"
		    android:orientation="horizontal" android:layout_width="match_parent"
		    android:layout_height="match_parent">
		
		    <TextView
 	     		android:id="@+id/name"
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:textAppearance="?android:attr/textAppearanceMedium"
		        android:textColor="@color/colorPrimaryDark"
		        android:layout_weight="1"/>
		    <TextView
		    	android:id="@+id/phone"
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:textColor="@color/colorPrimaryDark"
		        android:textAppearance="?android:attr/textAppearanceMedium"
		        android:layout_weight="1"/>
		
		</LinearLayout>
		```
	https://github.com/kwanulee/AndroidProgramming/blob/master/examples/ContentResolverTest/app/src/main/res/layout-port/item.xml
	
	2.  SimpleCursorAdapter 설정 

		```java
	        String[] contactsColumns = { // 쿼리결과인 Cursor 객체로부터 출력할 열들
	                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
	                ContactsContract.CommonDataKinds.Phone.NUMBER
	        };
	
	        int[] contactsListItems = { // 열의 값을 출력할 뷰 ID (layout/item.xml 내) 
	                R.id.name,
	                R.id.phone
	        };
	
	        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
	                R.layout.item,
	                c,
	                contactsColumns,
	                contactsListItems,
	                0);
	
	        ListView lv = (ListView) findViewById(R.id.listview);
	        lv.setAdapter(adapter);
		```
		
		https://github.com/kwanulee/AndroidProgramming/blob/master/examples/ContentResolverTest/app/src/main/java/com/kwanwoo/android/contentresolvertest/MainActivity.java#L49-L75
		
	
		
## 4. 콘텐츠 제공자에 데이터 삽입, 업데이트 및 삭제
- SQL과 유사하게 query뿐 아니라 insert, update, delete를 지원
- [ContentResolver](https://developer.android.com/reference/android/content/ContentResolver.html)의 아래 메소드를 사용
	- Uri [insert](https://developer.android.com/reference/android/content/ContentResolver.html#insert(android.net.Uri, android.content.ContentValues)) (Uri url, ContentValues values);
	- int [update](https://developer.android.com/reference/android/content/ContentResolver.html#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])) (Uri uri, ContentValues values, String where, String[] selectionArgs);
	- int [delete](https://developer.android.com/reference/android/content/ContentResolver.html#delete(android.net.Uri, java.lang.String, java.lang.String[])) (Uri url, String where, String[] selectionArgs);

- [**주의**] 모든 콘텐츠 제공자가 데이터 삽입,업데이트,삭제 기능을 제공하지는 않습니다. 필요에 따라서 이 기능들을 제공할 수도 있고, 안할 수도 있습니다.
	- 가령, 연락처 정보를 제공하는 콘텐츠 제공자는 연락처 정보를 직접 삽입, 업데이트, 삭제하는 기능을 제공하고 있지 않습니다.

	
---

[**다음 학습**: 캘린더 제공자](https://developer.android.com/guide/topics/providers/calendar-provider?hl=ko)