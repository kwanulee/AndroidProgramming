<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
# 안드로이드 UI
---
## 학습목표
- 안드로이드 UI 기본 개념 및 설계 방법을 이해한다.
- 안드로이드 UI 기본 요소인 다양한 위젯 (Widget)의 설정방 법을 이해한다.
- 안드로이드 UI 요소를 배치하는 레이아웃 (Layout)의 기능 및 설정 방법을 이해한다.
- Margins/Padding/Gravity의 설정 방법을 이해하여, UI의 미세한 설정을 제어할 수 있다.

---

## 1. 안드로이드 UI 기본

### 1.1 UI(User Inteface) 설계 개요

- 안드로이드 앱의 UI를 구성하는 기본 단위는 뷰(View)이다.
	- 뷰는 크게 **위젯(Widget)**과 **레이아웃(Layout)**으로 구분된다.
		- **위젯**:  [View](https://developer.android.com/reference/android/view/View)의 서브클래스로서, 앱 화면을 구성하는 시각적인 모양을 지닌 UI 요소 (예, 버튼, 메뉴, 리스트 등)
		- **레이아웃(Layout)** : [ViewGroup](https://developer.android.com/reference/android/view/ViewGroup)의 서브클래스로서, 다른 뷰(위젯 혹은 레이아웃)를 포함하면서 이들을 정렬하는 기능을 지닌 UI 요소

    <div class="polaroid">
	   <img src="figure/ui-overview.png">
	    </div>

### 1.2 UI 설계 방법
#### 1.2.1 XML을 사용하여 UI 설계
- AndroidStudio의 Layout Editor 이용
	- 드래그 앤 드롭 방식의 WYSIWYG (what you see is what you get) 에디터
	- 다양한 디바이스/안드로이드 버전에 대한 Preview
	- XML 코드 자동 변환 및 동기화

	<div class="polaroid">
	<img src="figure/layout-editor.png">
	</div>

- XML file을 직접 편집
	- 필요한 XML 태그나 속성을 잘 모를 경우 불편
	- Copy & paste를 이용한 편집이 효율적인 경우가 많음

	<div class="polaroid">
	<img src="figure/xml-editor.png">
	</div>

#### 1.2.2 자바 코드로 직접 UI 설계

---
## 2. 위젯(Widget)
### 2.1 위젯 이란?
- **위젯(Wdiget)**은 [**View**](https://developer.android.com/reference/android/view/View)의 서브 클래스 중에서 화면에 보이는 것들을 말함
  - 대표적인 위젯은 *TextView*, *EditText*, *Button* 등이 있습니다.

  <div class="polaroid">
	<img src="figure/widget_hierarchy.png">
	</div>

---
### 2.2 [View](https://developer.android.com/reference/android/view/View)
View 클래스는 모든 UI 컴포넌트들의 부모 클래스이므로, View 클래스의 속성은 모든 UI 컴포넌트들에서 공통적으로 사용할 수 있다.

- **id**: UI 컴포넌트를 고유하게 식별하는 식별자
	- 식별자 지정 형식
		
		```
		android:id="@+id/my_button"
		```
		
		- [예시](https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L21) 
	- 식별자 참조 형식
	
		```
		android:id="@id/my_button"
		```
		
		- [예시](https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L30) 
		   
- **layout\_width**,  **layout\_height**: UI 컴포넌트의 크기를 결정
  - *match\_parent* (혹은 *fill\_parent*) : 부모 UI 컴포넌트의 크기에 맞춤
      - SDK2.2(프로요)부터는 *match\_parent*로 변경. 둘 다 사용 가능
  - *wrap\_content*:  UI 컴포넌트의 내용물 크기에 맞춤
	
		```
		<Button
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:text="Start"/>
	    ```
	    <div class="polaroid">
		   <img src="figure/layout_width_height.png">
		  </div>

  - layout\_width,  layout\_height의 값을 특정한 단위로 지정할 수도 있음
  
  		```
		<Button
	            android:layout_width="100px"
	            android:layout_height="100px"
	            android:text="Start"/>
	    ```

    	- *px* (pixels), *in* (inches), *mm* (millimeters)
      		- Pixel 방식으로 view의 크기를 설정하면, 디스플레이의 해상도에 따라 view의 크기가 달라 보일 수 있습니다.
        		- 가령, 가로세로 100 pixel 크기의 UI요소는 저해상도 디스플레이에서 보이는 것이  고해상도 디스플레이어에서 보이는 것보다 크게 보입니다.

			        <div class="polaroid">
			  	     <img src="figure/explicit_value.png">
			  	      </div>
    	- *dp* (density\-independent pixels): 밀도에 독립적인 단위
      		- 1 dp는 밀도가 160dpi의 화면 일때 1 픽셀을 나타냄

		        <div class="polaroid">
		       <img src="figure/dip.png">
		        </div>
- **background**
  - 뷰의 배경을 지정하며, 색상 및 이미지 등의 여러 가지 객체로 지정 가능하다.
  - 색상 지정 시 네 가지 형식이 적용된다.
    - #RGB
    - #ARGB
    - #RRGGBB
    - #AARRGGBB
    - ex) #ff0000 (#f00): 빨간색,  #0000ff : 파란색

---
#### [연습1] 뷰의 크기 조절
1. UIBasic 이라는 프로젝트를 생성한다.
2. res/layout 폴더 하위에 ui\_component\_size.xml 파일을 생성한다.
	- res/layout 폴더를 선택후, 오른쪽 마우스 클릭하여 [New]>[Layout resource file] 메뉴 선택
	- File name 입력창에 ui\_component\_size 입력
	- Root element 입력창에 LinearLayout 입력
	- [OK] 버튼 클릭
3. 생성된 ui\_component\_size.xml 파일의 내용을 아래와 같이 수정해 보자.

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
5. [참고] ui\_component\_size.xml 파일의 내용을 실제 혹은 가상 디바이스에 표시하기 위해서는 **MainActivity** 클래스의 코드에서 setContentView() 함수를 다음과 같이 수정하여야 함

	```java
	public class MainActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ui_component_size); // 이 부분이 변경됨 
	    }
	}
	```

---

### 2.3 [TextView](https://developer.android.com/reference/android/widget/TextView)  
- 화면에 text를 표시하는 용도
- **주요 속성**
  - View 속성 상속: **id**, **layout_width**, **layout_height**, **background**, etc.
  - **text**: 출력할 문자열 지정
  - **textSize**: 폰트 크기
  - **textStyle**: 텍스트 스타일 (normal, bold, italic)
  - **typeface**: 텍스트 폰트(normal, sans, serif, monspace)
  - **textColor**:  문자열 색상
  - **singleLine**: 속성값이 "true"이면, 텍스트가 위젯의 폭보다 길 때 강제로 한 줄에 출력

### 2.4 [EditText](https://developer.android.com/reference/android/widget/EditText)
- 입력이 가능한 Text 창
- **주요 속성**
  - **TextView**의 모든 속성 상속 (EditText는 TextView의 서브클래스임)
  - **inputType**: 입력시 허용되는 키보드 타입 설정 및 키보드 행위를 설정
    	- 키보드 타입 설정 값
      		- "text": 일반적인 텍스트 키보드
      		- "phone": 전화번호 입력 키보드
      		- "textEmailAddress": @ 문자를 가진 텍스트 키보드
    	- 키보드 행위 설정 값
      		- "textCapWords":  문장의 시작을 대문자로 변환
      		- "textAutoCorrect": 입력된 단어와 유사한 단어를 제시하고 제시된 단어 선택시, 입력된 단어를 대치
      		- "textMultiLine": 여러 줄을 입력 받을 수 있음

---	
#### [연습2] Textview/EditText 테스트
1. [연습1]에서 생성 및 수정한 WidgetTest 이라는 프로젝트의 res/layout 폴더 하위에 text\_views.xml 파일을 생성한다.
2. 생성된 text\_views.xml 파일의 내용을 아래와 같이 수정해 보자. 

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
        
3. 위 파일의 내용을 실제 혹은 가상 디바이스에 표시하기 위해서는 **MainActivity** 클래스의 setContentView() 함수를 다음과 같이 수정하여야 함

	```java
	public class MainActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.text_views); // 이 부분이 변경됨 
	    }
	}
	```

4. 디바이스 실행화면에 표시된 5개의 EditText 입력창을 각각 선택하여 텍스트를 입력하였을 때, 키보드의 변화 및 입력 행위의 변화를 살펴보시오.

---

### 2.5 [Button](https://developer.android.com/reference/android/widget/Button?hl=en)
- 일반적으로 많이 사용되는 푸시 버튼으로 사용자가 버튼을 클릭하였을 때, 어떤 행동을 수행하고자 할 때 사용된다.

	![](https://developer.android.com/images/ui/button-types.png)
- Button 클래스는 TextView의  서브클래스이므로, TextView의 모든 속성을 사용할 수 있다.
	- singleLine: 텍스트가 위젯의 폭보다 길 때 강제로 한 줄에 출력 
- 버튼 내에 텍스트, 아이콘을 표시할 수 있음
  - 버튼 전체를 이미지로 그리기 위해서는 ImageButton 사용

- 예제 (연습 2의 text\_views.xml 파일에 버튼 위젯 추가) 

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout ...>
		...
		<EditText ... />
	    <Button
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="Submit"
	        />
	</LinearLayout>


#### 2.5.1 버튼 클릭이벤트 처리
- 사용자가 버튼 위젯을 클릭할 때, 지정된 행동을 수행하기 위해서는 다음 두 가지 방법 중 하나를 사용할 수 있다.

##### 2.5.1.1 버튼 위젯의 onClick 속성 활용 방법
1. 버튼 위젯을 정의한 화면을 contentView로 설정한 액티비티 클래스에 새로운 메소드(예, doAction())를 추가한다.

	```java
	public class MainActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.text_views);
	    }
	    
	    public void doAction(View v) {
	        // Shows a Toast message in response to button
	        Toast.makeText(getApplicationContext(), "Submitted Successfully",
                Toast.LENGTH_SHORT).show();
	    }
	    
	}
	```

2. 버튼 위젯을 정의한 xml 레이아웃 파일(예, text\_views.xml)에서, 버튼 위젯의 **onClick** 속성에 앞 단계에서 추가한 메소드(예, doAction())를 설정한다.

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout ...>
		...
	    <Button
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="Submit"
	        android:onClick="doAction"/>
	
	</LinearLayout>
	```

##### 2.4.1.2 이벤트 처리 객체를 이용하는 방법
이 방법에서는 이벤트를 처리하는 객체를 생성하여 해당 이벤트를 발생시키는 위젯에 등록한다. 위젯에서 이벤트가 발생하면 등록된 이벤트 처리 객체가 정의된 일을 수행한다.

- 절차   
	1. 버튼이 클릭되었을 때 발생되는 클릭 이벤트를 처리하기 위해서는 [View.OnClickListener](https://developer.android.com/reference/android/view/View.OnClickListener) 인터페이스를 구현하는 클	래스 정의한다.
	2. 구현한 클래스의 객체를 생성하여 클릭 이벤트를 발생시키는 버튼 위젯에 등록한다.

- 예제
	- 버튼 위젯을 정의한 xml 레이아웃 파일(예, text\_views.xml)에서, Button 객체를 Java 코드에서 참조하기 위해서 버튼 위젯에 id 속성 추가
	
		```xml
		<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout ...>
			...
		    <Button
		    	android:id="@+id/submit_button"
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:text="Submit"
		        android:onClick="doAction"/>
		
		</LinearLayout>
		```
	
	- 이벤트 처리 객체 생성 및 등록

		```java
		public class MainActivity extends AppCompatActivity {
		
		    @Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.text_views);
		
				 // text_views.xml에 정의된 View 객체 중에서 id가 submit_button인 것을 찾아 반환함 
		        Button btn = findViewById(R.id.submit_button);
		        //2. 구현한 클래스의 객체를 생성하여 클릭 이벤트를 발생시키는 버튼 위젯에 등록
		        btn.setOnClickListener(new ClickListener());
		    }
		
		    ...
		
			//1. 버튼이 클릭되었을 때 발생되는 클릭 이벤트를 처리하기 위해서는 View.OnClickListener 인터페이스를 구현하는 클래스 정의
		    class ClickListener implements View.OnClickListener {
		        @Override
		        public void onClick(View v) {
		            Toast.makeText(getApplicationContext(), R.string.button_clicked_msg,
		                    Toast.LENGTH_SHORT).show();
		        }
		    }
		}
		
		```  	
		
- 참고 (**findViewByID**() 함수)
	- Activity 클래스에 정의된 메소드로 Activity 하위 클래스(예, AppCompatActivity)에서 사용 가능 
	- 해당 액티비티와 연결된 XML layout 리소스 요소(위젯) 중에서 id 속성을 바탕으로 해당 Java 객체를 가져옴 
		- onCreate() 메소드 내의 setContentView()를 통해서 연결된 XML 리소스 요소 중에서만 검색이 가능함. 따라서 다른 액티비티와 연결된 XML layout 리소스에 정의된 위젯을 findViewByID() 메소드로 가져올 수는 없음.


---
## 3. 레이아웃(Layout)
### 3.1 레이아웃 이란?
- [ViewGroup](https://developer.android.com/reference/android/view/ViewGroup)의 파생 클래스로서, 포함된 [View](https://developer.android.com/reference/android/view/View.html)를 정렬하는 기능
- 종류
	- [LinearLayout](https://developer.android.com/reference/android/widget/LinearLayout)
		- 컨테이너에 포함된 뷰들을 수평 또는 수직으로 일렬 배치하는 레이아웃
	- [RelativeLayout](https://developer.android.com/reference/android/widget/RelativeLayout)
		- 뷰를 서로간의 위치 관계나 컨테이너와의 위치관계를 지정하여 배치하는 레이아웃
	- [TableLayout](https://developer.android.com/reference/android/widget/TableLayout)
		- 표 형식으로 차일드를 배치하는 레이아웃
	- [FrameLayout](https://developer.android.com/reference/android/widget/FrameLayout)
		- 컨테이너에 포함된 뷰들을 전부 좌상단에 배치하는 레이아웃

---
### 3.2 LinerLayout
- 자식 뷰를 수평, 수직으로 일렬 배치하는 레이아웃으로, 가장 단순하고 직관적이며 사용빈도가 높다.
- **주요 속성**
	- **orientation**
		- vertical : 차일드를 위에서 아래로 수직으로 배열
		- horizontal : 차일드를 왼쪽에서 오른쪽으로 수평 배열

		<div class="polaroid">
	   <img src="figure/linearlayout.png">
	   </div>
	- **layout\_weight**
		- 자식뷰 들을 배치하고 남은 공간을 layout_weight 값을 기준으로 공간을 할당
- 예제: https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/linear_layout.xml

	[linear_layout.xml](https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/linear_layout.xml) | 실행화면
	--- | ---	
	<img src="figure/linearlayout_example_code.png"> | <img src="figure/linearlayout_example.png" width=200>
---

### 3.3 RelativeLayout
- 위젯끼리의 관계를 지정하거나 위젯과 parent(container)와의 관계 지정하여 자식 뷰를 배치
- **주요 속성**
	- 위젯끼리의 관계를 지정하는 속성: anchor view의 id를 지정 

	속성 | 설명
	----|----
	layout\_alignBaseline | anchor view와 baseline을 맞춘다
	layout\_alignBottom | anchor view와 아래쪽 가장자리를 맞춘다
	layout\_alignTop | anchor view와 위쪽 가장자리를 맞춘다
	layout\_alignLeft | anchor view와 왼쪽 가장자리를 맞춘다
	layout\_alignRight | anchor view와 오른쪽 가장자리를 맞춘다
	layout\_Above | anchor view의 위쪽에 배치
	layout\_Below | anchor view의 아래쪽에 배치
	layout\_toLeft | anchor view의 왼쪽에 배치
	layout\_toRight | anchor view의 오른쪽에 배치

	- Parent와의 관계 지정하는 속성
	
	속성 | 설명
	----|----
	layout\_centerHorizontal | 수평 방향으로 컨테이너의 가운데 배치
	llayout\_centerVertical | 수직 방향으로 컨테이너의 가운데 배치	layout\_centerInParent | 컨테이너의 가운데 배치
	layout_alignParentLeft | 컨테이너와 왼쪽 가장자리를 맞춘다.
	layout_alignParentRight | 컨테이너와 오른쪽 가장자리를 맞춘다.
	layout_alignParentBottom | 컨테이너와 아래쪽 가장자리를 맞춘다.
	layout_alignParentTop | 컨테이너와 위쪽 가장자리를 맞춘다.
	

- 예제: https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L10-L33 

	[other_layout.xml](https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L10-L33) | 실행화면
	--- | ---	
	<img src="figure/relativelayout_example_code.png"> | <img src="figure/relativelayout_example.png" width=200>


---
### 3.4 TableLayout
- 표 형식으로 차일드를 배치하는 레이아웃

- 표를 구성하는 행의 개수만큼 TableRow를 포함하고, TableRow는 각 행에 포함된 셀(View)을 포함한다.
- **주요 속성**
	- **stretchColumns**: 늘릴 열을 지정 (인텍스는 0부터 시작됨)
		- "*" : 모든 열을 늘여서 배치한다.
		- "1, 2" : 1열(왼쪽에서 2번째)과 2열 (왼쪽에서 3번째) 을 늘여서 배치한다

- **TableRow**
	- 정해진 규칙에 따라 크기가 결정되므로,  layout\_width/height를 지정할 필요가 없다.
		- layout\_height는 항상 wrap\_content
		- layout\_width 는 항상 match\_parent

- 예제: https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L35-L57

	[other_layout.xml](https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L35-L57) | 실행화면
	--- | ---	
	<img src="figure/tablelayout_example_code.png"> | <img src="figure/tablelayout_example.png" width=200>


### 3.5 FrameLayout 
- 모든 자식 View는 좌상단에 배치되며, 여러 개의 자식View를 포함하는 경우 나열된 순서대로 겹쳐져서 표시됨
- 예제: https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L58-L78

	[other_layout.xml](https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/other_layout.xml#L58-L78) | 실행화면
	--- | ---	
	<img src="figure/framelayout_example_code.png"> | <img src="figure/framelayout_example.png" width=200>

## 4. Margin/Padding/Gravity
### 4.1 Margins, Padding
- Margins: 뷰와 다른 뷰(컨테이너) 간의 간격
	- **layout\_margin**: 상하좌우로 동일한 마진 설정 시에 사용되는 속성
	- **layout\_marginLeft**,  **layout\_marginRight**, **layout\_marginTop**, **layout\_marginBottom**: 4 방향의 마진을 각기 다르게 설정할 때 사용되는 속성
- Padding: 뷰와 내용물 간의 간격
	- **padding**: 상하좌우로 동일한 패딩 설정 시에 사용되는 속성
	- **paddingLeft**, **paddingRight**, **paddingTop**, **paddingBottom**: 4방향의 마진을 각기 다르게 설정할 때 사용되는 속성
 	
	<div class="polaroid">
	   <img src="figure/margins_padding.png">
	   </div>
	   
- 예제: https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/margin_padding_gravity.xml#L14-L15

	[margin\_padding\_gravity.xml](https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/margin_padding_gravity.xml#L14-L15) | 실행화면
	--- | ---	
	<img src="figure/margin_padding_example_code.png"> | <img src="figure/margin_padding_example.png" width=200>
	 
	   
### 4.2 Gravity
- **gravity** 속성
	- 해당 뷰안의 내용물 위치에 대한 정렬 방식을 지정
- **layout_gravity** 속성
	- 부모 뷰안에서 해당 뷰의 정렬 방식 지정
- 가능한 값들
	- 부모 뷰안에서 해당 뷰의 정렬 방식 지정
		- BOTTOM – 부모 뷰에서 아래쪽에 위치시킴
		- CENTER – 부모 뷰의 중앙에 위치시킴
		- CENTER_HORIZONTAL – 부모 뷰의 수평 기준으로 중앙에 위치시킴
		- CENTER_VERTICAL – 부모 뷰의 수직 기준으로 중앙에 위치시킴
		- END – 부모 뷰에서 텍스트 방향의 끝(한글이나 영어의 경우는 오른쪽)에 위치시킴
		- LEFT – 부모 뷰에서 왼쪽에 위치시킴
		- RIGHT – 부모 뷰에서 오른쪽에 위치시킴
		- TOP – 부모 뷰에서 위쪽에 위치시킴
- 예제: https://github.com/kwanulee/Android/blob/master/examples/UIBasic/app/src/main/res/layout/margin_padding_gravity.xml#L24-L86

	<img src="figure/gravity_example.png" width=200>

```xml
   <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#ff0000"
        android:layout_weight="1">

        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="L | T"
            android:gravity="left|top"
            android:layout_gravity="left"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="C_H | T"
            android:gravity="center_horizontal|top"
            android:layout_gravity="center_horizontal"/>
        <Button 
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="R | T"
            android:gravity="right|top"
            android:layout_gravity="right"/>

        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="L | C_V"
            android:gravity="left|center_vertical"
            android:layout_gravity="center_vertical"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="Center"
            android:layout_gravity="center"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="R | C_V"
            android:gravity="right|center_vertical"
            android:layout_gravity="center_vertical|right"/>


        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="L | B"
            android:gravity="left|bottom"
            android:layout_gravity="bottom"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="C_H | B"
            android:gravity="center_horizontal|bottom"
            android:layout_gravity="center_horizontal|bottom"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="R | B"
            android:gravity="right|bottom"
            android:layout_gravity="right|bottom"/>
    </FrameLayout>
```
