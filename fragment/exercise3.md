
#### [연습3] - 자바 코드에서 동적으로 프래그먼트 추가하기 
1. [연습1](exercise1.html)에서 생성한 프로젝트를 바탕으로 진행
2. **activity\_main.xml** 파일을 열고, 프래그먼트가 추가된 부모 뷰의 ID를 지정
	- 다음 예에서는 LinearLayout을 프래그먼트를 포함할 뷰로 지정하기 위해서 id 값으로 *fragment\_container*를 지정하였음 

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		android:id="@+id/fragment_container"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="Hello World!" />
	</LinearLayout>

	```
	
3. **MainActivity.java** 파일에서, MainActivity 클래스를 다음과 같이 수정하시오

	```java
	import android.support.v4.app.FragmentManager;
	import android.support.v4.app.FragmentTransaction;
	import android.support.v7.app.AppCompatActivity;
	import android.os.Bundle;
	
	public class MainActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        FragmentManager fragmentManager = getSupportFragmentManager();
	        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	        fragmentTransaction.add(R.id.fragment_container, new FirstFragment());
	        fragmentTransaction.commit();
	    }
	}
	```
	- MainActivity가 AppCompatActivity를 확장하였으므로, FragmentManager  인스턴스를 얻을 때, getSupportFragmentManager()를 사용함
	- add() 메소드의 두번째 파라미터는 추가될 프래그먼트를 포함할 뷰의 아이디로 앞서 정의한 activity\_main.xml 파일의 LinerLayout 위젯의 아이디를 의미함
	
3. 실행결과

<img src="figure/fragment-static-add.png" width=300>