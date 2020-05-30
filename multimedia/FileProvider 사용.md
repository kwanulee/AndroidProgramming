
## FileProvider 사용

- [FileProvider](https://developer.android.com/reference/androidx/core/content/FileProvider.html)는 [ContentProvider](https://developer.android.com/reference/android/content/ContentProvider)의 특별한 서브 클래스로서 특정한 앱에서 생성한 파일을 다른 앱과 안전한 방법으로 공유할 수 있도록, **file:///Uri** 형식 대신에 **content://Uri** 형식의 [Uri](https://developer.android.com/reference/android/net/Uri) 객체를 얻을 수 있는 방법을 제공해 줍니다. 
- 다음은  [FileProvider](https://developer.android.com/reference/androidx/core/content/FileProvider.html)를 이용하여 File 객체에 대한 콘텐츠 Uri (**content://Uri**) 객체를 얻는 방법에 대해 설명합니다.
	 
### 1. FileProvider 설정
- 앱의 매니페스트 파일의 \<application\> 태그 안에 에 FileProvider 설정
	
	```xml
	<application...>
		...
		<provider
			android:name="androidx.core.content.FileProvider"
		   	android:authorities="패키지이름.fileprovider"
		   	android:exported="false"
			android:grantUriPermissions="true" >
		    <meta-data
		          android:name="android.support.FILE_PROVIDER_PATHS"
		          android:resource="@xml/file_paths" />
		 </provider>
	
	```
			
	- **android:authorities** 속성은 '*fileprovider*' 문자열이 추가된 앱의 **android:package**의 속성값으로 설정
- 예제 
	- 현재 앱의 **android:package** 속성 값이 *com.example.kwanwoo.multimediatest*인 경우,  **android:authorities**  속성 값은 *com.example.kwanwoo.multimediatest.fileprovider*로 설정

	```xml
		<provider
		    android:name="androidx.core.content.FileProvider"
		    android:authorities="com.example.kwanwoo.multimediatest.fileprovider"
		    android:exported="false"
		    android:grantUriPermissions="true" >
		    <meta-data
		          android:name="android.support.FILE_PROVIDER_PATHS"
		          android:resource="@xml/file_paths" />
		    </provider>
	```
			
	https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/AndroidManifest.xml#L27-L35
	
### 2. 다른 앱과 공유 가능 디렉토리 설정
- **res/xml/**에 **file_paths.xml**이라는 파일을 생성
	- 앱 매니페스트 파일의 \<**provider**\> 태그 하위의 \<**meta-data**\> 태그의  **android:resource** 속성 값으로 설정한 파일 이름과 일치해야 합니다.
- 공유할 디렉토리에 대한 XML 태그 요소를 \<path\> 태그 하위에 추가
	- **\<external-files-path\>** 
		- 공유할 디렉토리가 앱 전용 외부 저장소 영역 (**Context.getExternalFilesDir(null)**가 반환하는 path)의 서브 디렉토리인 경우
	
		```
		<external-files-path name="name" path="sub-directory-name" />
		```	
	- **\<files-path\>** 
		- 공유할 디렉토리가 앱 내부 저장소 영역(data/data/app-package/files/)의 서브 디렉토리인 경우
				
		```
		<files-path name="name" path="sub-directory-name" />
		``` 

	- 참고자료: https://developer.android.com/reference/android/support/v4/content/FileProvider.html
- 예제 
	- 앱 전용 외부저장소 영역의 하위의 Pictures와 Movies 디렉토리는 다른 앱과 공유 가능함을 설정 
	
		```xml
		<?xml version="1.0" encoding="utf-8"?>
		<paths xmlns:android="http://schemas.android.com/apk/res/android">
			    <external-files-path name="image_capture" path="Pictures/" />
			    <external-files-path name="video_capture" path="Movies/" />
		</paths>
		```

		https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/res/xml/file_paths.xml
	

### 3. 콘텐츠 Uri 획득	
1. 파일 생성
	- 생성된 파일의 위치는 두 번째 단계에서 정의한 **res/xml/file\_paths.xml**에 정의된 디렉토리와 일치해야 합니다. 
2.  **FileProvider,getUriForFile(context, authority, file)** 클래스를 사용하여 생성된 파일에 대한 콘텐츠 Uri 획득
	-  두번째 파라미터인 **authority**는  매니페스트 파일에 선언된 것과 일차해야 합니다.

```java
// getExternalFilesDir() + "/Pictures" should match the declaration in fileprovider.xml paths
String mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
File mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

// wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
Uri imageUri = FileProvider.getUriForFile(this, "com.example.kwanwoo.multimediatest.fileprovider", mPhotoFile);
```