<style> 
div.polaroid {
  	width: 200px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

# Toolbar 사용하기 (AndroidX 라이브러리 사용)

-  **v7 appcompat** 라이브러리는 Android 9.0(API 레벨 28)의 출시와 함께 [Android Jetpack](https://developer.android.com/jetpack) 구성요소인 **AndroidX** 라이브러리로 이전했습니다.
-  **v7 appcompat** 라이브러리를 사용했던 기존 프로젝트는 AndroidX로 [이전](https://developer.android.com/jetpack/androidx/migrate?hl=ko)하는 것 또한 고려해 보세요.

## 1. 액티비티에  [Toolbar](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html) 추가하기
- **사전 절차**
	1. 프로젝트가 **AndroidX**라이브러리를 사용하도록 설정되어 있어야 합니다.
		- 최신 Android Studio (3.6.1)를 통해 프로젝트가 생성되었다면, **build.gradle** 파일에 아래의 설정이 추가되어 있는지 확인하면 됩니다.

		```
		dependencies {
			implementation 'androidx.appcompat:appcompat:1.1.0'
			...
		}
		```
		
	2. 액티비티는 다음과 같이 [androidx.appcompat.app.AppCompatActivity](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity)를 확장하여 정의되어 있어야 합니다.

		```java
		import androidx.appcompat.app.AppCompatActivity;
		...
		
		public class MyActivity extends AppCompatActivity {
			//...
		}
		```
- **추가 절차**
	1. 앱이 기본 [ActionBar](https://developer.android.com/reference/android/app/ActionBar) 클래스를 사용하여 앱바를 제공하지 않도록, 앱 manifest에서 appcompat의 **NoActionBar** 테마 중 하나를 사용하도록 **<application>** 요소를 설정.
		-  (권장) **res/values/styles.xml**에서 style 태그의 **parent** 속성 값을 수정 

			```xml
			<resources>
			     <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
			       ...
			    </style>
			</resources>
						
			```
		- 또는, **AndroidManifest.xml**에서 **android:theme** 속성 값을 직접수정
		
			```xml
			<application
	        		android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
			```

		
	2. [Toolbar](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html) 를 액티비티 레이아웃(예, activity\_main.xml)의 맨 위에 추가
		- 다음 레이아웃 코드는 액티비티 위에 플로팅 방식으로 **Toolbar**를 추가한 것을 예시한 것

			```
			<androidx.appcompat.widget.Toolbar
			       android:id="@+id/my_toolbar"
			       android:layout_width="match_parent"
			       android:layout_height="?attr/actionBarSize"
			       android:background="?attr/colorPrimary"
			       android:elevation="4dp"
			       android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
			       app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>
			```
			
			- *?attr/colorPrimary*: 앱의 테마에 정의된 colorPrimary 값을 의미함
			- **android:theme** 속성에서 Toolbar의 테마를 Dark ActionBar 테마로 설정하여, 짙은 배경에 밝은 색의 글씨가 표시되도록 함. 
			- **app:popupTheme** 속성에서 팝업 메뉴를 밝게 설정함 
	3.  [Toolbar](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html)를 액티비티의 **앱바**로 설정
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
		