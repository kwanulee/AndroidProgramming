<style> 
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**이전 학습**: 파일)](sharedpreferences.html)

# 파일 (Files)

## 학습목표

- Android 기기의 파일에 자료를 저장하고 읽는 방법을 이해한다.


## 1. 개요 
- Linux 파일 시스템과 java.io의 입출력 스트림에 대한 이해 필수입니다.- 모든 Android 기기에는 "**내부**" 및 "**외부**" 저장소의 두 가지 파일 저장소 영역이 있습니다.

	내부 저장소                          | 외부 저장소
	--------------------------------- | -------------
	내장 메모리                          | 이동식 저장장치 (SD 카드)
	항상 사용 가능                        | 외부 저장소의 마운트 여부에 따라 사용 가능
	기본적으로 자신의 앱에서만 액세스 할 수 있음 | 모든 사람이 읽을 수 있음
	사용자가 앱을 삭제하면 시스템이 내장 저장소에서 앱의 모든 파일을 제거함 | 사용자가 앱을 삭제하면 getExternalFilesDir()의 디렉터리에 저장한 앱 파일에 한해서 시스템이 제거함	사용자와 다른 앱이 앱의 파일에 직접 액세스하는 것을 원치 않을 때 적합 | 다음 경우에 적합 <ul> <li>액세스 제한이 필요치 않은 파일 <li>다른 앱과 공유하기를 원하는 파일 <li>사용자가 컴퓨터에서 액세스 할 수 있도록 허용하는 파일 </ul>
	
- **FileTest 안드로이드 프로젝트 Github 주소**
	- https://github.com/kwanulee/AndroidProgramming/tree/master/examples/FileTest 
## 2. 내부 저장소의 파일 입출력
안드로이드에서 자바의 모든 입출력 기능을 다 사용할 수 는 없고, 보안상의 제약으로 인해 [Context](https://developer.android.com/reference/android/content/Context.html) 클래스에서 보안이 적용된 파일 관리 메서드를 별도로 제공하며, 이를 이용하여 파일을 Open한다.

```java
FileOutputStream openFileOutput (String name, int mode)FileInputStream openFileInput (String name)
```	
- name
	- 파일의 이름으로 경로를 표시하는 ‘/’ 문자가 들어가면 에러	- 파일의 위치는 **/data/data/패키지명/files** 디렉토리로 지정- mode
	모드          | 설명
	-------------|---------------------------------------------------
	MODE_RPIVATE | 혼자만 사용하는 배타적인 모드로 파일을 생성. (디폴트)
	MODE_APPEND  | 파일이 이미 존재할 경우 덮어쓰기 모드가 아닌 추가 모드로 Open.---

### 2.1 OpenFileOuput

~~~java
package com.example.kwanwoo.filetest;
... 생략 ...

    private void saveToInternalStorage() {
        String data = input.getText().toString();

        try {
            FileOutputStream fos = openFileOutput
                                        ("myfile.txt", // 파일명 지정
                                        Context.MODE_APPEND);// 저장모드
            PrintWriter out = new PrintWriter(fos);
            out.println(data);
            out.close();

            result.setText("file saved");
        } catch (Exception e) {
            result.setText("Exception: internal file writing");
        }
    }
~~~
[https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L121-L136](https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L121-L136)

- "myfile.txt"는 디바이스의 **data/data/com.example.kwanwoo.filetest/files/** 에 위치
	- **Android Studio**의 좌측 하단에 있는 **Device File Explorer** 탭을 열어 해당 파일이 생성되었는 지 확인 가능합니다.
	
		<img src="figure/device-file-explorer.png">
	




### 2.2 OpenFileInput

```java
    private void loadFromIntenalStorage() {
        try {
            FileInputStream fis = openFileInput("myfile.txt");//파일명
            BufferedReader buffer = new BufferedReader
                    (new InputStreamReader(fis));
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴

            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            StringBuffer data = new StringBuffer();
            while (str != null) {
                data.append(str + "\n");
                str = buffer.readLine();
            }
            buffer.close();
            result.setText(data);
        } catch (FileNotFoundException e) {
            result.setText("File Not Found");
        } catch (Exception e) {
            result.setText("Exception: internal file reading");
        }
    }
```

[https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L138-L158](https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L138-L158)



## 3. res/raw 폴더 파일 이용하기
* 대용량의 읽기 전용 데이터 파일은 리소스에 포함시켜 두는 것이 좋다.
    - ( ex: 게임의 지도 맵 데이터, 우편 번호부, 영한사전 데이터 등 )
* 포함시킬 파일은 **res/raw**에 복사해 둔다
	<div class="polaroid">
		<img src="figure/raw-resource.png">
	</div>
* 리소스의 파일을 읽을 때는 [Resources](https://developer.android.com/reference/android/content/res/Resources.html) 클래스의 [openRawResource](https://developer.android.com/reference/android/content/res/Resources.html#openRawResource(int)) 메서드를 사용하며, id로는 확장자를 뺀 파일명을 부여한다.

	```java
		InputStream openRawResource (int id)
	```
	   
	- 모든 file resource는 접미사(확장자)를 제외하고 유일한 이름을 가져야 한다.
	- res/raw에 file1.txt 와 file1.dat가 동시에 존재하면 안됨

	```java
	    private void loadFromRawResource() {
	        try {
	            InputStream is = getResources().openRawResource(R.raw.description);
	            BufferedReader buffer = new BufferedReader
	                    (new InputStreamReader(is));
	
	            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
	
	            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
	            StringBuffer data = new StringBuffer();
	
	            while (str != null) {
	                data.append(str + "\n");
	                str = buffer.readLine();
	            }
	            buffer.close();
	            result.setText(data);
	        } catch (Exception e) {
	            result.setText("Exception: raw resource file reading");
	        }
	    }
	```
[https://github.com/kwanulee/Android/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L160-L180](https://github.com/kwanulee/Android/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L160-L180)


## 4. 외부 저장소 사용하기
1. 외부 저장소 접근 권한 설정
2. 외부 저장소 상태 확인
3. 앱 실행 시 접근 권한 확인 및 요청
4. 외부 저장소 사용
    - 다른 앱과 공유되는 파일 입출력
    - 앱 전용 파일 입출력


### 4.1 외부 저장소의 접근 권한 설정
* 외부 저장소의 파일을 읽거나 쓰러면 Manifest 파일에서 **READ\_EXTERNAL\_STORAGE** 또는 **WRITE\_EXTERNAL\_STORAGE** 권한을 요청해야 합니다.
	*  파일을 읽고 쓰려면 읽기 액세스 권한도 암시적으로 요청하는 **WRITE\_EXTERNAL\_STORAGE** 권한만 요청하면 됩니다. 

```xml
<manifest ...>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
</manifest>
```
[https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/AndroidManifest.xml#L4](https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/AndroidManifest.xml#L4)



### 4.2 외부 저장소의 상태 확인
* 외부 저장소를 사용하기 전에 *사용 가능성* 검사
    - static String Environment.getExternalStorageState()
        + 반환값
            - MEDIA\_MOUNTED: 미디어가 읽기/쓰기 권한으로 마운트 됨
            - MEDIA\_MOUNTED\_READ\_ONLY: 미디어가 읽기 권한으로 마운트 됨
            - MEDIA\_REMOVED: 미디어가 존재하지 않음
            - MEDIA\_UNMOUNTED: 미디어가 마운트 안됨
	
	```java
	    public boolean isExternalStorageWritable() {
	        String state = Environment.getExternalStorageState();
	        if (Environment.MEDIA_MOUNTED.equals(state)) {
	            result.setText("외부메모리 읽기 쓰기 모두 가능");
	            return true;
	        }
	        return false;
	    }
	```

[https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L182-L191](https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L182-L191)

### 4.3 앱 실행 시 접근 권한 확인 및 요청

- **Android 6.0 (API level 23)** 이상부터는앱 실행 중에 사용하려는 권한(permission)을 반드시 요청 (참고자료: [런타임에 권한 요청](https://developer.android.com/training/permissions/requesting.html))
	1. 여러분의 앱에 위험 권한이 필요한 경우, 여러분은 해당 권한이 요구되는 작업을 실행할 때마다 이 **권한의 보유 여부를 확인**해야 합니다.
		- 권한 보유 여부를 확인하려면 [ContextCompat.checkSelfPermission()](https://developer.android.com/reference/android/support/v4/content/ContextCompat.html#checkSelfPermission(android.content.Context,%20java.lang.String)) 메서드를 호출합니다. 
			
	2. 여러분의 앱에 필요한 권한이 아직 없는 경우, 앱은 [ActivityCompat.requestPermissions()](https://developer.android.com/reference/android/support/v4/app/ActivityCompat.html#requestPermissions(android.app.Activity,%20java.lang.String[],%20int)) 메서드를 호출하여 **적절한 권한을 요청**해야 합니다.	 
   		- static void ActivityCompat.**requestPermissions** (Activity *activity*, 
                String[] *permissions*, 
                int *requestCode*)
            - *activity*: 접근권한을 필요로하는 앱의 액티비티
            - *permissions*: 요청할 권한의 String 배열. null이어서는 안됨
            - *requestCode*: 사용자 정의 int 상수로서, **onRequestPermissionsResult(int *requestCode*, String[] permissions, int[] grantResults)**에 전달될 결과와 매칭하기 위해서 사용됨 .  


				```java
				// 사용자 정의 상수 선언
				final int REQUEST_SAVETO_EXTERNAL_STORAGE = 1;
				... 생략 ...
				
	                String[] PERMISSIONS_STORAGE = {
	                        Manifest.permission.WRITE_EXTERNAL_STORAGE
	                };
	
	                if (ContextCompat.checkSelfPermission(MainActivity.this,
	                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
	                        != PackageManager.PERMISSION_GRANTED) { //권한이 없는 경우
	                    ActivityCompat.requestPermissions(
	                            MainActivity.this,
	                            PERMISSIONS_STORAGE,
	                            REQUEST_SAVETO_EXTERNAL_STORAGE
	                    );
	                } else {  // 권한이 이미 허가된 경우
                        saveToExtenalStorage();
                   }
             ```
             
				[https://github.com/kwanulee/Android/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L78-L93](https://github.com/kwanulee/Android/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L78-L93)
			
		- ActivityCompat.**requestPermissions()** 메서드가 호출되면 다음과 같은 대화상자가 나타나서, 앱 사용자는 권한의 승인/거부를 앱 실행 중에 결정할 수 있습니다.
		
		 <img width= 200 src="figure/permission-accept.png">
		 
	3. 사용자가 응답하면, 시스템은 앱의 [onRequestPermissionsResult()](https://developer.android.com/reference/android/support/v4/app/ActivityCompat.OnRequestPermissionsResultCallback.html#onRequestPermissionsResult(int,%20java.lang.String[],%20int[])) 메서드를 호출하여 사용자 응답에 전달합니다. 
		- 권한이 부여되었는지 여부를 확인하려면 여러분의 앱이 이 메서드를 재정의해야 합니다. 
		- 이 콜백에는 여러분이 **requestPermissions()**에 전달한 것과 동일한 요청 코드가 전달됩니다. 예를 들어, 앱이 **REQUEST\_SAVETO\_EXTERNAL\_STORAGE** 요청 코드가 **requestPermissions()**를 통해 요청된 경우, 다음과 같은 콜백 메서드를 가질 수 있습니다.
		
		```java
	    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
	        if (grantResults.length > 0
	                && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted
	            switch (requestCode) {
	                case REQUEST_SAVETO_EXTERNAL_STORAGE:
	                    saveToExtenalStorage();
	                    break;
	                 ...
	            }
	        } else { // permission was denied
	            Toast.makeText(getApplicationContext(),"접근 권한이 필요합니다",Toast.LENGTH_SHORT).show();
	        }
    }
	
		```
			- 앱의 권한 설정은 **설정>애플리케이션>**[해당 앱]**>권한**에서 언제든지 변경할 수 있음
					
	<img width= 200 src="figure/permission-change.png">
	
	

<img src="images/permissionreq.png" width=200 style="top:150px; right:100px; position:absolute;">


### 4.4 외부 저장소 사용 

---

#### 4.4.1 다른 앱과 공유되는 파일 입출력
* 공유 디렉토리 (Music, Pictures, Ringtones) 접근하기
    - [static File Environment.getExternalStoragePublicDirectory(String type)](https://developer.android.com/reference/android/os/Environment.html#getExternalStoragePublicDirectory(java.lang.String))
        + Type
            - DIRECTORY\_MUSIC, DIRECTORY\_PODCASTS, DIRECTORY\_RINGTONES, DIRECTORY\_ALARMS, DIRECTORY\_NOTIFICATIONS, DIRECTORY\_PICTURES, or DIRECTORY\_MOVIES, DIRECTORY\_DOWNLOADS, DIRECTORY\_DCIM, \DIRECTORY\_DOCUMENTS. 등
        + 반환값: 외부저장소의 루트 디렉토리의 지정된 타입의 서브 디렉토리 (예,sdcard/Download)

```java
    // 공유 디렉토리 (sdcard/Download) 사용할 경우
    File path = Environment.getExternalStoragePublicDirectory
                           (Environment.DIRECTORY_DOWNLOADS);
    File f = new File(path, "external.txt");      // 경로, 파일명
    FileWriter write = new FileWriter(f, true);   // 지정된 파일에 문자 스트림 쓰기

    PrintWriter out = new PrintWriter(write);     // formatted 출력 스트림
    out.println(data);
    out.close();
```

[https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L228-L241](https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L228-L241)

---

#### 4.4.2 앱 전용 파일 입출력
* 외부 저장소의 앱 전용(private) 저장소 디렉토리 접근하기
    - [File getExternalFilesDir(String type)](https://developer.android.com/reference/android/content/Context.html#getExternalFilesDir(java.lang.String))    [[Context](https://developer.android.com/reference/android/content/Context.html) 클래스 메소드]
        + Type
            - DIRECTORY\_MUSIC 
            - DIRECTORY\_PODCASTS
            - DIRECTORY\_RINGTONES
            - DIRECTORY\_ALARMS
            - DIRECTORY\_NOTIFICATIONS
            - DIRECTORY\_PICTURES
            - DIRECTORY\_MOVIES 등
        + 반환값: 외부저장소의 Android/data/패키지명/files 디렉토리 아래의 지정된 타입의 서브 디렉토리
            - (예,sdcard/Android/data/com.example.kwanwoo.filetest/files/Download)

```java
//  앱 전용 저장소 (sdcard/Android/data/com.example.kwanwoo.filetest/files/Download를 사용할 경우
    File path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    
    File f = new File(path, "external.txt");      // 경로, 파일명
    FileWriter write = new FileWriter(f, true);   // 지정된 파일에 문자 스트림 쓰기

    PrintWriter out = new PrintWriter(write);     // formatted 출력 스트림
    out.println(data);
    out.close();
```[https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L228-L241](https://github.com/kwanulee/AndroidProgramming/blob/master/examples/FileTest/app/src/main/java/com/example/kwanwoo/filetest/MainActivity.java#L228-L241)