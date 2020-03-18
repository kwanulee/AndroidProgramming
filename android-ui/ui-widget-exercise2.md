## 연습: Textview/EditText 테스트

1. [뷰의 크기 조절 연습](ui-widget-exercise1.html)에서 생성 및 수정한 **UIBasic** 이라는 프로젝트의 **res/layout** 폴더 하위에 **text\_views.xml** 파일을 생성한다.
2. 생성된 **text\_views.xml** 파일의 내용을 아래와 같이 수정해 보자. 

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:orientation="vertical"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="EditText Test"
	        android:textColor="#ff0000"
	        android:textSize="10pt"
	        android:typeface="serif"
	        android:textStyle="bold"
	        />
	
	    <EditText
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:hint="ID"
	        android:inputType="text"
	        android:textSize="10pt"
	        android:textStyle="italic" />
	    <EditText
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:hint="Password"
	        android:inputType="textPassword"
	        android:textSize="10pt"
	        android:textStyle="italic" />
	    <EditText
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:hint="Telephone"
	        android:inputType="phone"
	        android:textSize="10pt"
	        android:textStyle="italic" />
	    <EditText
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:hint="Email"
	        android:inputType="textEmailAddress"
	        android:textSize="10pt"
	        android:textStyle="italic" />
	    <EditText
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:hint="Description"
	        android:inputType="text|textMultiLine|textAutoCorrect"
	        android:textSize="10pt"
	        android:textStyle="italic" />
	
	</LinearLayout>    
	```
        
3. 위 파일의 내용을 실제 혹은 가상 디바이스에 표시하기 위해서는 **MainActivity** 클래스의 **setContentView()** 함수를 다음과 같이 수정하여야 함

	```java
	public class MainActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.text_views); // 이 부분이 변경됨 
	    }
	}
	```

4. 디바이스 실행화면에 표시된 5개의 **EditText** 입력창을 각각 선택하여 텍스트를 입력하였을 때, 키보드의 변화 및 입력 행위의 변화를 살펴보시오.


