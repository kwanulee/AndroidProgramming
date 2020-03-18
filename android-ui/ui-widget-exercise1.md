## 연습: 뷰의 크기 조절


1. **WidgetTest** 이라는 프로젝트를 생성한다.
2. **res/layout** 폴더 하위에 **ui\_component\_size.xml** 파일을 생성한다. 구체적인 생성 방법은 아래와 같습니다.
	1. **res/layout** 폴더를 선택후, 오른쪽 마우스 클릭하여 [New]>[Layout resource file] 메뉴 선택
	- **File name** 입력창에 **ui\_component\_size** 입력
	- **Root element** 입력창에 **LinearLayout** 입력
	- [**OK**] 버튼 클릭
3. 생성된 **ui\_component\_size.xml** 파일의 내용을 아래와 같이 수정해 보자.

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:orientation="vertical"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	
	    <TextView
        	android:layout_width="match_parent"
        	android:layout_height="wrap_content"
        	android:text="Width = Match Parent"/>
    	 <TextView
        	android:layout_width="wrap_content"
        	android:layout_height="wrap_content"
        	android:text="Width = wrap content"/>
    	 <TextView
        	android:layout_width="100dp"
        	android:layout_height="wrap_content"
        	android:text="Width = 100dp"/> 
	</LinearLayout>
	```
4. XML 코드에서 **TextView** 위젯의 **layout\_width**, **layout\_height**의 값을 아래와 같이 다양하게 변경하여 보고, [**Design**] 탭을 클릭하여 변화를 살펴본다.
	- match\_parent 혹은 wrap\_content
	- 100px, 1in, 10mm 등 
5. [참고] **ui\_component\_size.xml** 파일의 내용을 실제 혹은 가상 디바이스에 표시하기 위해서는 **MainActivity** 클래스의 코드에서 setContentView() 함수를 다음과 같이 수정하여야 함

	```java
	public class MainActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ui_component_size); // 이 부분이 변경됨 
	    }
	}
	```