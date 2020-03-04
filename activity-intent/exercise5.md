#### [연습5] - FirstActivity에서 ThirdActivity로 데이터 전달하기
1. [연습4](exercise4.html)까지 수행한 프로젝트를 바탕으로 진행
2. 기존 activity\_first.xml의 "지도보기 작업 시작하기" 버튼 위젯 아래에 다음과 같은 뷰를 추가한다. 
	- 데이터를 입력받을 수 있는 \<EditText\> 위젯과 세번째 액티비티를 호출할 \<Button\> 위젯을 수평으로 배열시키는 \<LinearLayout\>을추가한다.

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout ...>
	
			<TextView
					... />
	
			<Button
					android:text="두번째 액티비티 시작하기"
					... />
			<Button
					android:text="다이얼 작업 시작하기"
					... />		
			<Button
					android:text="지도보기 작업 시작하기"
					... />
	
			<!-- 추가된 뷰(시작) -->
	    	<LinearLayout
	        	android:layout_width="match_parent"
	        	android:layout_height="wrap_content"
	        	android:orientation="horizontal">
	        	<EditText
	            	android:layout_width="wrap_content"
	            	android:layout_height="wrap_content"
	            	android:id="@+id/edit_data"
	            	android:layout_weight="1"
	            	android:hint="데이터 입력"/>
	        	<Button
	            	android:layout_width="wrap_content"
	            	android:layout_height="wrap_content"
	            	android:text="세번째 액티비티에 데이터 전달"
	            	android:id="@+id/buttonThirdActivity"/>
	    	</LinearLayout>
	    	<!-- 추가된 뷰(끝)-->
	</LinearLayout>
	```

3. FirstActivity 클래스에서 다음 코드를 추가하시오
	- "**세번째 액티비티에 데이터 전달**" 버튼이 클릭 되었을 때,
		1. 세번째 액티비티를 시작시키기 위한 명시적 인텐트 객체를 생성 
		2. id가 *edit\_data*인 EditText 객체에 입력된 문자열 값을 가져와서 이를 앞에서 생성한 인텐트 객체의 Extra에 설정 (이름은 "dataFromFirstActivity"로 지정)
		3. startActivity()의 파라미터로 인텐트 객체 전달 

	```java
	public class FirstActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	//...(연습 4까지 진행된 코드)
	    	
	        // 연습 4까지 진행된 코드 이후에 추가된 코드
	        btn = findViewById(R.id.buttonThirdActivity);
	        btn.setOnClickListener(new View.OnClickListener(){
	
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
	
	                EditText edit = findViewById(R.id.edit_data);
	                intent.putExtra("dataFromFirstActivity", edit.getText().toString());
	
	                startActivity(intent);
	            }
	        });
	    }
	```

4. ThirdActivity에서 사용할 레이아웃 파일 activity\_third.xml을 다음과 같이 작성

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="세번째 액티비티입니다."
	        android:id="@+id/textView2" />
	
	    <EditText
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:id="@+id/editText"
	        android:inputType="text"
	        />
	    <Button
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content" 
	        android:text="닫기"
	        android:id="@+id/buttonThirdActivity"/>
	</LinearLayout>
	```


5. ThirdActivity 클래스의 onCreate() 메소드에서 다음 코드를 추가한다.
	- ThirdActivity로 전달된 데이터를 **Extra**에서 가져와서 EditText 뷰에 표시
	- 버튼이 클릭되면 finish()메소드를 호출하여서 현재의 액티비티를 종료하는 버튼 클릭 이벤트 처리기를 추가

	```java
	public class ThirdActivity extends AppCompatActivity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_third);

	        Intent intent = getIntent();
	        String msg = intent.getStringExtra("dataFromFirstActivity");
	        EditText et = (EditText)findViewById(R.id.editText);
	        et.setText(msg);
	        
	        Button btn = findViewById(R.id.buttonThirdActivity);
        	btn.setOnClickListener(new View.OnClickListener() {
            	@Override
            	public void onClick(View view) {
                	finish();
            	}
        });
	    }
	}
	```

6. 실행결과

초기 실행 화면 | 데이터 입력  | 버튼 클릭 후
------------|----------------------------|-------------
<img src="figure/first-activity3.png">|<img src="figure/data-input.png">|<img src="figure/third-activity.png">
