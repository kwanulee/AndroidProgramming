<style> 
div.polaroid {
  	width: 200px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

# 앱바와 액션 추가 및 처리

## 1. 앱바 (App Bar)
- **앱바**는 애플리케이션 화면 상단의 UI 요소로서, *타이틀*, *액션 아이템*, *오버 플로 메뉴(옵션 메뉴)* 등으로 구성됨

	<img src="figure/app_bar.png" width=300> 

	- *액션 아이템* 중에서 자주 사용되는 것은 빠르게 실행시킬 수 있도록 아이콘으로 **앱바**에 배치시키고, 나머지는 *오버 플로어 메뉴(옵션 메뉴)*에 포함시킨다.
	- *액션 아이템*은 res/menu 폴더 내의 XML 메뉴 파일 안에 정의된다. ([2. XML로 액션 아이템 정의](#2) 참조)  

	
- Android 3.0(API 레벨 11)부터는, 기본 테마를 사용하는 모든 액티비티에는 [ActionBar](https://developer.android.com/reference/android/app/ActionBar.html)가 **앱바**로 제공 
- **앱바** 기능은 다양한 Android 릴리스를 거쳐 기본 ActionBar에 점차적으로 추가되어 왔습니다. 이 결과, 어떤 버전의 Android 시스템이 사용 중인지에 따라 기본 ActionBar가 다르게 동작합니다.
- [Toolbar](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html) 가장 최신 앱바의 기능이 포함된 지원 라이브러리로서, [Toolbar](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html)를 **앱바**로 사용하면 어떤 버전의 Android 시스템이던지 상관없이 최신 **앱바**의 기능을 사용할 수 있습니다.
	- 보다 자세한 내용은 다음 [링크](https://developer.android.com/training/appbar/setting-up.html)롤 통해서 학습하세요.

<a name="2"></a>
## 2. XML로 액션 아이템 정의
1. res 폴더 위에서 마우스 오른쪽 버튼을 눌러서 [**New**]-[**Directory**] 메뉴를 통해서 menu 폴더를 생성
2. menu 폴더 위해서 마우스 오른쪽 버튼을 눌러서 [**New**]-[**Menu resource file**] 선택
3. 파일이름으로 *main\_menu* 입력 후, **OK** 버튼 선택
4. 아래 XML 코드로 main\_menu.xml 파일 내용을 업데이트

	```xml
	<menu xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto">
	    <item
	        android:id="@+id/quick_action1"
	        android:title="Quick1"
	        android:icon="@drawable/ic_android_black_24dp"
	        app:showAsAction="ifRoom|withText"/>
	    <item
	        android:id="@+id/action_settings"
	        android:title="@string/action_settings"
	        android:icon="@drawable/ic_settings_black_24dp"
	        app:showAsAction="ifRoom"/>
	    <item
	        android:id="@+id/action_subactivity"
	        android:title="Sub Activity"/>
	</menu>
	```
	- **app:showAsAction**에 설정 가능한 값
		- *ifRoom*: 앱바에 버튼을 표시할 공긴이 있다면 표시하고, 없다면, 옵션 메뉴로
		- *never*: 해당 액션은 항상 옵션 메뉴에 포함
		- *withText*: icon과 함께 title도 텍스트로 같이 표시해 주는데, ifRoom이나 always 속성과 함께 설정해 줍니다. 이것도 공간이 있으면 텍스트도 표시하지만 공간이 충분하지 않으면 아이콘만 표시하게 됨
		- *always*: 항상 앱바에 표시하고자 할 때 사용되지만, 공간이 없다면 표시되지 않을 수 있음 
	- **android:icon** 속성에 설정된 *@drawable/ic\_android\_black\_24dp* 이미지 리소스는 아래 방법으로 추가
		1. res/drawable 폴더 위에서 마우스 오른쪽 버튼을 눌러서 [**New**]-[**Vector Asset**] 메뉴를 선택
		2. **Next** 버튼을 클릭하고, 다음 화면에서 **Finish**를 클릭하여 추가
	- **android:icon** 속성에 설정된 *@drawable/ic\_settings\_black\_24dp* 이미지 리소스는 아래 방법으로 추가
		1. res/drawable 폴더 위에서 마우스 오른쪽 버튼을 눌러서 [**New**]-[**Vector Asset**] 메뉴선택
		2. **Clip Art** 옆의 아이콘 클릭
		3. 검색창에 *settings* 입력하여 해당 이미지 리소스를 찾고 이를 선택한 후에 **OK** 버튼 클릭
		4. 	**Next** 버튼을 클릭하고, 다음 화면에서 **Finish**를 클릭하여 추가
	- **android:title** 속성에 설정된 *@string/action_settings*를 정의하기 위해서 res/values 폴더 내의 strings.xml 리소스를 열고 다음 항목을 추가

		```xml
		<string name="action_settings">설정</string>
		```

## 3. 옵션메뉴 생성
- **옵션 메뉴(오버플로 메뉴)**는 현재 액티비티와 관련된 여러 가지 동작이나 선택사항을 설정하는 메뉴이다.
- 액티비티에서 옵션 메뉴를 지정하려면 [onCreateOptionMenu()](https://developer.android.com/reference/android/app/Activity.html?hl=ko#onCreateOptionsMenu(android.view.Menu)) 메소드를 재정의한다.
	- 이 메서드에서 메뉴 리소스(XML에 정의됨)를 콜백에서 제공된 [Menu](https://developer.android.com/reference/android/view/Menu.html?hl=ko)로 팽창할 수 있습니다 
- 예제
	- 	main\_menu.xml에 의하여 정의된 메뉴 리소스가 [Menu](https://developer.android.com/reference/android/view/Menu.html?hl=ko) 객체로 팽장된다.	

	```java
	public class MainActivity extends AppCompatActivity {
		// ... 생략
		
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main_menu, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	}
	```

## 4. 액션 아이템 선택시 이벤트 처리
- 사용자가 옵션 메뉴에서 항목(앱 바의 작업 항목 포함)을 선택하면, 시스템이 액티비티의 [onOptionsItemSelected()](https://developer.android.com/reference/android/app/Activity.html?hl=ko#onOptionsItemSelected(android.view.MenuItem)) 메서드를 호출합니다. 
	- 이 메서드는 선택된 메뉴 항목(액션 아이템)에 대한 [MenuItem](https://developer.android.com/reference/android/view/MenuItem.html?hl=ko) 객체를 전달합니다. 
	- 항목을 식별하려면 [getItemId()](https://developer.android.com/reference/android/view/MenuItem.html?hl=ko#getItemId())를 호출하여 메뉴 항목에 대한 고유 ID를 얻을 수 있습니다.  

- 예제 

	```java
	public class MainActivity extends AppCompatActivity {
		// ... 생략
		
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.quick_action1:
	                Toast.makeText(getApplicationContext(), "action_quick", Toast.LENGTH_SHORT).show();
	                return true;
	            case R.id.action_settings:
	                Toast.makeText(getApplicationContext(), "action_settings", Toast.LENGTH_SHORT).show();
	                return true;
	            case R.id.action_subactivity:
	                Toast.makeText(getApplicationContext(), "action_subactivity", Toast.LENGTH_SHORT).show();
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	}
	```
