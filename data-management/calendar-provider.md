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
- **Android 6.0 (API level 23) 이상**부터는 **앱 실행 중에 필요한 권한(permission)을 반드시 확인하고 없으면 요청**해야 합니다. 이에 대한 자세한 내용은 다음 링크를 참조합니다.
	- https://kwanulee.github.io/AndroidProgramming/data-management/content-provider.html#2


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
- 다음은 CalendarProvider를 통해서 캘린더 테이블의 캘린더 레코드를 가져오는 방법을 나타낸 예시입니다.
	<img src="figure/calendarprovider-screenshot1.png" width=200>
	<img src="figure/calendarprovider-screenshot2.png" width=200>

	- **ACCOUNT\_Name** 은 캘린더를 디바이스와 동기화할 때 사용하는 계정 이름을 나타내며, 이를 바탕으로 캘린더 테이블의 레코드를 검색합니다.
	-  **캘린더 조회** 버튼을 누르면, 입력된 Account\_Name 값과 매칭되는 캘린더 레코드만 리스트뷰로 출력합니다. Account\_Name 값을 입력하지 않으면, 모든 캘린더 레코드를 출력합니다.

- 코드

	```java
    private void queryCalendars() {
        // 권한 검사
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.READ_CALENDAR},
                    REQUEST_CODE_QUERY_CALENDARS);
            return;
        }

        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        final String[] CALENDAR_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        ContentResolver contentResolver = getContentResolver();

        // 화면의 EditText 창으로부터 사용자가 입력한 문자열을 읽어와 mAccountName 변수에 저장
        mAccountName = ((EditText) findViewById(R.id.account_name)).getText().toString();

        Cursor cursor = null;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        if (mAccountName == null || mAccountName.equals(""))
            cursor = contentResolver.query(uri, CALENDAR_PROJECTION, null, null, null);
        else { // ACCOUNT_NAME 속성 값이 mAccountName의 값과 일치하는 캘린더를 추출함
            String selection = "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?)";
            String[] selectionArgs = new String[]{mAccountName};
            cursor = contentResolver.query(uri, CALENDAR_PROJECTION, selection, selectionArgs, null);
        }

        // SimpleCursorAdapter 설정 및 생성
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.calendar_item,
                cursor,
                CALENDAR_PROJECTION,
                new int[] {R.id._id, R.id.accountName, R.id.displayName},
                0);

        ListView lv = findViewById(R.id.listview);
        lv.setAdapter(adapter);
	//...
    }
	```
	
		
## 4. 이벤트 테이블
- [CalendarContract.Events](https://developer.android.com/reference/android/provider/CalendarContract.Events?hl=ko) 테이블에는 각각의 이벤트에 대한 세부 정보가 포함되어 있습니다. 

| 상수 | 설명|
|----|------|
|CALENDAR_ID|	이벤트가 속한 캘린더의 _ID입니다.|
|ORGANIZER|	이벤트 조직자(소유자)의 이메일입니다.|
|TITLE|	이벤트 제목입니다.|
|EVENT_LOCATION|	이벤트가 일어나는 장소입니다.|
|DESCRIPTION|	이벤트 설명입니다.|
|DTSTART|	이벤트가 시작되는 시간을 Epoch 이후 UTC 밀리초 단위로 나타낸 것입니다.|
|DTEND|	이벤트가 종료되는 시간을 Epoch 이후 UTC 밀리초 단위로 나타낸 것입니다.|
|EVENT_TIMEZONE|	이벤트의 표준 시간대입니다.|
- 지원되는 필드의 전체 목록은 [CalendarContract.Events](https://developer.android.com/reference/android/provider/CalendarContract.Events?hl=ko) 참조 

- 이벤트를 추가, 업데이트 또는 삭제하려면 애플리케이션의 매니페스트 파일에 **[WRITE_CALENDAR](https://developer.android.com/reference/android/Manifest.permission?hl=ko#WRITE_CALENDAR)** 권한이 포함되어야 합니다.

### 4.1 CALENDAR\_ID로 이벤트 조회
- 이벤트 테이블에는 모든 캘린더의 이벤트 레코드를 포함하고 있습니다. 
- 다음은 특정 캘린더와 관련된 이벤트만 CalendarProvider를 통해서 가져오는 방법을 나타낸 예시입니다. 

- 코드


	```java
    private void queryEventByCalendar_ID() {
        // 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    REQUEST_CODE_QUERY_EVENT);
            return;
        }

        // 이벤트 테이블의 프로젝션
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
        };

        ContentResolver cr = getContentResolver();

		//  selection 문자열 설정.  "(calendar_id = ?)"
        String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?) ";
        // selection 문자열의 ?에 대응될 값을 포함하는 문자열 배열
        String[] selectionArgs = new String[]{mCalendarId}; // mCalendarId는 이벤트가 속한 캘린더 ID

		// 이벤트 테이블에서 calendar_id가 id와 일치하는 레코드의 결과셋을 가리키는 커서를 반환
        Cursor cursor = cr.query(
                CalendarContract.Events.CONTENT_URI,
                EVENT_PROJECTION,
                selection,
                selectionArgs,
                null);

        // ConvertSimpleCursorAdapter 설정 및 생성
        // ConvertSimpleCusrorAdapter - epoch 시간 값을 YYYY-MM-DD HH:mm 형식으로 변환
        ConvertSimpleCursorAdapter adapter = new ConvertSimpleCursorAdapter(
                getApplicationContext(),
                R.layout.event_item,
                cursor,
                new String[]{
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND},
                new int[]{
                        R.id.tv_title,
                        R.id.tv_dtstart,
                        R.id.tv_dtend},
                0);

        ListView lv = findViewById(R.id.listview);
        lv.setAdapter(adapter);
        ```

### 4.2 이벤트 추가	
- 다음은 새 이벤트를 삽입할 때 지켜야 하는 규칙입니다.
	- CALENDAR_ID 및 DTSTART를 포함해야 합니다.
	- EVENT_TIMEZONE을 포함해야 합니다.  
	- 반복되지 않는 이벤트의 경우, DTEND를 포함해야 합니다.
	- 반복적인 이벤트의 경우 RRULE 또는 RDATE와 함께 DURATION을 포함해야 합니다. 

- 코드

	```java
    private final static int REQUEST_CODE_ADD_EVENT = 1;
    
    private void addEvent() {
        // WRITE_CALENDAR 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_ADD_EVENT);
            return;
        }

        // ...

        ContentResolver cr = getContentResolver();
        
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, mCalendarId);
        values.put(CalendarContract.Events.TITLE, title);	// tile은 이벤트 제목을 나타내는 문자열
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Seoul");
        values.put(CalendarContract.Events.DTSTART, startMillis); // startMills 는 epoch 시간으로 설정된 long 형 변수
        values.put(CalendarContract.Events.DTEND, endMillis); 	// endMills 는 epoch 시간으로 설정된 long 형 변수

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);		
```

### 4.3 이벤트 수정
- 애플리케이션이 사용자에게 이벤트 편집을 허용할 경우, 인텐트를 사용하여  EDIT 인텐트를 사용하는 것이 좋습니다. 그러나 필요한 경우 직접 이벤트를 편집해도 됩니다. 
- 이벤트 업데이트를 직접 수행하려면, 다음 메소드를 이용합니다.

	- int [update](https://developer.android.com/reference/android/content/ContentResolver.html#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])) (Uri uri, ContentValues values, String where, String[] selectionArgs);
		1. 수정할 대상(예, 이벤트 테이블 혹은 이벤트 레코드)을 uri로 설정
		2.  [ContentValues](https://developer.android.com/reference/android/content/ContentValues)
 객체에 변경이 필요한 이벤트 항목과 값을 설정

	-  두 가지 사용 방식
		1. [**방식1**] 수정할 이벤트 레코드의 **_ID**를 URI에 추가된 ID로 제공 ([withAppendedId()](https://developer.android.com/reference/android/content/ContentUris?hl=ko#withAppendedId(android.net.Uri,%20long)) 메소드 이용)
		2. [**방식2**] 수정할 이벤트 레코드를 선택하기 위해서 where 문자열과 selectionArgs 문자열 배열 설정

#### 4.3.1 **방식1** 코드

```java
    private void updateEvent() {
    	// 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_UPDATE_EVENT);
            return;
        }

		// 화면상의 EditText 객체에  입력된 값을 읽어옴
        String idString = ((EditText) findViewById(R.id._id)).getText().toString();
        String title = ((EditText) findViewById(R.id.edit_title)).getText().toString();
        String dtstart = ((EditText) findViewById(R.id.edit_dtstart)).getText().toString();
        String dtend = ((EditText) findViewById(R.id.edit_dtend)).getText().toString();

        ContentResolver cr = getContentResolver();
        
        //  수정할 항목과 값을 ContentValues 객체에 설정
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DTSTART, convertDateToTime(dtstart));
        values.put(CalendarContract.Events.DTEND, convertDateToTime(dtend));

		// Events 테이블 URI 마지막 부분에 id를 덧붙여 새로운 URI를 얻음
        Uri updateUri  = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(idString));
        
        // 
        int rows = cr.update(updateUri, values, null, null);
   }
```

#### 4.3.2 **방식2** 코드

```java
    private void updateEvent() {
    	// 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_UPDATE_EVENT);
            return;
        }

		// 화면상의 EditText 객체에  입력된 값을 읽어옴
        String idString = ((EditText) findViewById(R.id._id)).getText().toString();
        String title = ((EditText) findViewById(R.id.edit_title)).getText().toString();
        String dtstart = ((EditText) findViewById(R.id.edit_dtstart)).getText().toString();
        String dtend = ((EditText) findViewById(R.id.edit_dtend)).getText().toString();

        ContentResolver cr = getContentResolver();
        
        //  수정할 항목과 값을 ContentValues 객체에 설정
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DTSTART, convertDateToTime(dtstart));
        values.put(CalendarContract.Events.DTEND, convertDateToTime(dtend));

	// 수정할 이벤트 레코드를 선택하기 위해서 where 문자열과 selectionArgs 문자열 배열 설정
        String where = "(" + CalendarContract.Events._ID + " = ?) ";
        String[] selectionArgs = new String[]{idString};
        int rows = cr.update(CalendarContract.Events.CONTENT_URI, values, where, selectionArgs);
   }
```
		
### 4.4 이벤트 삭제
- 이벤트 삭제도 이벤트 수정과 마찬가지로 두 가지 방식을 사용할 수 있습니다.
	1.  삭제할 이벤트의 _ID가 추가된 URI를 사용
	2. 삭제할 이벤트 레코드를 선택하기 위ㅐ where 문자열과 selectionArgs 문자열 배열을 사용.

- 다음 코드는 "삭제할 이벤트의 \_ID가 추가된 URI를 사용한 예제 코드입니다.

	```java
    private void deleteEvent() {
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_DELETE_EVENT);
            return;
        }

        String idString = ((EditText) findViewById(R.id._id)).getText().toString();


        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(idString));
        
        ContentResolver cr = getContentResolver();
        int rows = cr.delete(deleteUri, null, null);
    }
```

## 5. 기타 다른 테이블
- 참석자 테이블 
	-  [CalendarContract.Attendees](https://developer.android.com/reference/android/provider/CalendarContract.Attendees?hl=ko) 테이블의 각 행은 이벤트의 참석자 또는 게스트 하나를 나타냅니다. 
	- 추가 자료:  https://developer.android.com/guide/topics/providers/calendar-provider?hl=ko#attendees
- 알림 테이블
	- [CalendarContract.Reminders](https://developer.android.com/reference/android/provider/CalendarContract.Reminders?hl=ko) 테이블의 각 행은 이벤트의 알림 하나를 나타냅니다. 
	- 추가 자료:  https://developer.android.com/guide/topics/providers/calendar-provider?hl=ko#reminders
- 인스턴스 테이블
	- [CalendarContract.Instances](https://developer.android.com/reference/android/provider/CalendarContract.Instances?hl=ko) 테이블에는 이벤트 발생의 시작 및 종료 시간이 포함됩니다. 
	- 이 테이블의 각 행이 하나의 이벤트 발생을 나타냅니다. 
	- 이 인스턴스 테이블은 쓰기가 허용되지 않으며 이벤트 발생을 쿼리하는 수단을 제공할 뿐입니다.
	- 추가 자료: https://developer.android.com/guide/topics/providers/calendar-provider?hl=ko#instances 

## 6. 캘린더 인텐트
- 애플리케이션에 권한이 없어도 캘린더 데이터를 읽고 쓸 수 있습니다. 대신 Android의 캘린더 애플리케이션이 지원하는 인텐트를 사용하여 해당 애플리케이션에 읽기 및 쓰기 작업을 분배하면 됩니다. 다음 표는 캘린더 제공자가 지원하는 인텐트를 나열한 것입니다.

| 작업 | URI | 설명 |
|-------|------|-----|
|VIEW| content://com.android.calendar/time/\<ms\_since\_epoch\> | 캘린더를 \<ms\_since\_epoch\>에서 지정한 시간으로 엽니다.|
|View| content://com.android.calendar/events/\<event\_id\>|  \<event\_id\>에서 지정한 이벤트를 봅니다.|
|EDIT| content://com.android.calendar/events/\<event\_id\> | \<event\_id\>에서 지정한 이벤트를 편집합니다.|
|INSERT| content://com.android.calendar/events | 이벤트를 생성합니다.|


### 6.1 인텐트를 사용하여 이벤트 삽입
- INSERT 인텐트를 사용하면 애플리케이션이 이벤트 삽입 작업을 캘린더 자체에 넘길 수 있습니다. 이 방법을 사용하면 애플리케이션이 WRITE_CALENDAR 권한을 매니페스트 파일에 포함할 필요도 없습니다.
- 다음은 2012년 1월 19일 오전 7:30~8:30까지 한 시간 동안 실시되는 이벤트 일정을 예약하는 스니펫입니다. 이 스니펫에서 다음 사항에 주목하세요.
	- Events.CONTENT_URI를 URI로 지정합니다.
	- CalendarContract.EXTRA\_EVENT\_BEGIN\_TIME 및 CalendarContract.EXTRA\_EVENT\_END\_TIME 추가 필드를 사용하여 이벤트 시간으로 양식을 미리 채웁니다. 이 시간에 해당하는 값은 Epoch로부터 UTC 밀리초 단위로 표시해야 합니다.
	- Intent.EXTRA\_EMAIL 추가 필드를 사용하여 쉼표로 구분된 초청인 목록을 제공하며, 이는 이메일 주소로 나타냅니다.

	```java
	Calendar beginTime = Calendar.getInstance();
	beginTime.set(2012, 0, 19, 7, 30);
	Calendar endTime = Calendar.getInstance();
	endTime.set(2012, 0, 19, 8, 30);
	Intent intent = new Intent(Intent.ACTION_INSERT)
	        .setData(Events.CONTENT_URI)
	        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
	        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
	        .putExtra(Events.TITLE, "Yoga")
	        .putExtra(Events.DESCRIPTION, "Group class")
	        .putExtra(Events.EVENT_LOCATION, "The gym")
	        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
	        .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
	startActivity(intent);
	```
	
### 6.2 인텐트를 사용하여 이벤트 편집
- 이벤트 업데이트에서 설명한 바와 같이 이벤트를 직접 업데이트할 수 있습니다. 그러나 EDIT 인텐트를 사용하면 권한이 없는 애플리케이션이 캘린더 애플리케이션에 이벤트 편집을 넘길 수 있습니다. 사용자가 캘린더에서 이벤트 편집을 마치면 원래 애플리케이션으로 돌아오게 됩니다.

- 다음은 지정된 이벤트에 새 제목을 설정하여 사용자에게 캘린더에서 이벤트를 편집할 수 있도록 해주는 인텐트의 예입니다.

	```java
long eventID = 208;
Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
Intent intent = new Intent(Intent.ACTION_EDIT)
    .setData(uri)
    .putExtra(Events.TITLE, "My New Title");
startActivity(intent);
```

### 6.3 인텐트를 사용하여 캘린더 데이터 보기
- 캘린더 제공자는 VIEW 인텐트를 사용하는 방식을 두 가지 제공합니다.

	1. 캘린더를 특정 날짜에 여는 방식
	2. 이벤트를 보는 방식


- 다음은 캘린더를 특정 날짜에 여는 방법을 보여주는 예입니다.

	```java
// A date-time specified in milliseconds since the epoch.
long startMillis;
...
Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
builder.appendPath("time");
ContentUris.appendId(builder, startMillis);
Intent intent = new Intent(Intent.ACTION_VIEW)
    .setData(builder.build());
startActivity(intent);
```

- 다음은 이벤트를 보기 위해 여는 방법을 나타낸 예입니다.

	```java
long eventID = 208;
...
Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
Intent intent = new Intent(Intent.ACTION_VIEW)
   .setData(uri);
startActivity(intent);
```