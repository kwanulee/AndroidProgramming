<style> 
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

# 멀티미디어 실습
## 0. MainActivity의 화면 

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/AndroidProgramming"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >
    
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:id="@+id/musicPlayBtn"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"

            android:layout_gravity="center"
            android:text="음악재생"
            />
        <Button
            android:id="@+id/videoPlayBtn"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"

            android:layout_gravity="center"
            android:text="비디오재생"
            />
        <Button
            android:id="@+id/imageCaptureBtn"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"

            android:layout_gravity="center"
            android:text="사진촬영"
            />
        <Button
            android:id="@+id/videoRecBtn"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"

            android:layout_gravity="center"
            android:text="비디오녹음/재생"
            />

    </LinearLayout>
    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        />
    <VideoView
        android:id="@+id/videoView"
        android:layout_gravity="center"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        />


</LinearLayout>


```

초기화면 

<img src="images/multimedia-lab0.png" width="200">

## 1. 음악 파일 재생
* 먼저 아래 링크의 음악 파일을 다운로드 하세요.
    - https://github.com/kwanu70/AndroidExamples/tree/master/musics/gitan.mp3,
* 위 음악파일을 res/raw 폴더에 저장한 후에 이 음악 파일을 재생하는 코드를 작성해 보세요.
* 위 음악파일을 앱전용 외부저장소의 Music 폴더에 저장한 후에 이 음악 파일을 재생하는 코드를 작성해 보세요
* [참조코드]     
	- raw 폴더 리소스 재생
 		+ https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/java/com/example/kwanwoo/multimediatest/MainActivity.java#L165-L166
	- 앱전용 외부저장소 파일 재생
 		+ https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/java/com/example/kwanwoo/multimediatest/MainActivity.java#L172-L173

	    
* 앱전용 외부저장소의 Music 폴더에 파일을 복사하는 방법 
	- Android Studio 화면 우측 하단에 Device File Explorer 클릭하여 폴더 탐색 후 파일 업로드.
	<div class="polaroid">
			<img src="images/device-file-explorer.png">
	</div>	

## 2. 동영상 파일 재생
* 먼저 아래 링크의 mp4 파일을 다운로드 하세요.
    - https://github.com/kwanu70/AndroidExamples/tree/master/musics/twice.mp4
* 위 동영상 파일을  앱 전용 외부저장소의 Movies 폴더에 저장한 후에 이 파일을 재생하는 코드를 작성해 보세요

* [참조코드]  
https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/java/com/example/kwanwoo/multimediatest/MainActivity.java#L182-L194

## 3. 카메라 앱의 사진 저장하기 
* 사진찍기 버튼을 누르면, 카메라 앱을 실행시키고, 카메라 앱에서 찍은 사진을 앱 전용 외부저장소의 Pictures 폴더에 저장하고, 저장된 파일을 다시 읽어서 화면에 표시해 보세요.
* 저장된 이미지가 해당 폴더에 제대로 있는지도 확인해 보세요

	<img src="images/multimedia-lab2.png" width="200">
	
* [참조코드]  
https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/java/com/example/kwanwoo/multimediatest/MainActivity.java#L370-L388
 
	https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/java/com/example/kwanwoo/multimediatest/MainActivity.java#L412-L416

## 4. 카메라 앱의 동영상 저장하기 
* 동영상 촬영 버튼을 누르면, 카메라 앱을 실행시키고, 카메라 앱에서 찍은 동영상을 앱전용 외부저장소의 Movies 폴더에 저장하고, 저장된 파일을 다시 읽어서 화면에 표시해 보세요.
* 저장된 동영상 파일이 해당 폴더에 제대로 있는지도 확인해 보세요
	

* [참조코드]  

	https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/java/com/example/kwanwoo/multimediatest/MainActivity.java#L390-L407  

 https://github.com/kwanulee/AndroidProgramming/blob/master/examples/MultimediaTest/app/src/main/java/com/example/kwanwoo/multimediatest/VideoActivity.java