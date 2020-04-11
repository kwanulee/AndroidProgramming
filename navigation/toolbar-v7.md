<style> 
div.polaroid {
  	width: 200px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

# Toolbar 추가하기 (v7 AppCompat 지원라이브러리 사용)
-  **v7 appcompat** 라이브러리는 Android 9.0(API 레벨 28)의 출시와 함께 [Android Jetpack](https://developer.android.com/jetpack) 구성요소인 **AndroidX** 라이브러리로 이전했습니다.
-  따라서, 다음 방식은 

## 1. 액티비티에  [Toolbar](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html) 추가하기



1.  [v7 appcompat](https://developer.android.com/tools/support-library/features#v7-appcompat) 지원 라이브러리를 프로젝트에 추가 ([지원 라이브러리 설정](https://developer.android.com/tools/support-library/setup)의 설명 참조).
	- "com.android.support:appcompat-v7:x.x.x"를 **build.gradle** (Module:app) 파일에 추가

	```
	dependencies {
	    ...
	    implementation 'com.android.support:appcompat-v7:28.0.0'
	    ...
	}
	```
2. 액티비티는 다음과 같이 [AppCompatActivity](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity)를 확장하여 정의

	```java
	public class MyActivity extends AppCompatActivity {
		//...
	}
	```
3. 앱이 기본 [ActionBar](https://developer.android.com/reference/android/app/ActionBar) 클래스를 사용하여 앱바를 제공하지 않도록, 앱 manifest에서 appcompat의 **NoActionBar** 테마 중 하나를 사용하도록 **<application>** 요소를 설정. 
	
	```xml
	<application
				...
				android:theme="@style/Theme.AppCompat.Light.NoActionBar"
       		/>
	```
		
4. [Toolbar](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html) 를 액티비티 레이아웃의 맨 위에 추가
	- 다음 레이아웃 코드는 액티비티 위에 플로팅 방식으로 **Toolbar**를 추가한 것을 예시한 것

		```
		<android.support.v7.widget.Toolbar
		       android:id="@+id/my_toolbar"
		       android:layout_width="match_parent"
		       android:layout_height="?attr/actionBarSize"
		       android:background="?attr/colorPrimary"
		       android:elevation="4dp"
		       android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
		       app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>
		```
5.  **Toolbar**를 액티비티의 **앱바**로 설정
	- 액티비티의 [onCreate](https://developer.android.com/reference/android/app/Activity#onCreate(android.os.Bundle))() 메서드에서 액티비티의 [setSupportActionBar](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity#setSupportActionBar(android.support.v7.widget.Toolbar))() 메서드를 호출하고 액티비티의 **Toolbar**를 전달 	

		```java
		import androidx.appcompat.widget.Toolbar;
		//...
		public class MainActivity extends AppCompatActivity {
			@Override
			protected void onCreate(Bundle savedInstanceState) {
			        super.onCreate(savedInstanceState);
			        setContentView(R.layout.activity_my);
			        
			        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
			        setSupportActionBar(myToolbar);
			}
		}
		```
		