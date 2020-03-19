<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**이전 학습**: 어댑터 뷰 개념](adapterview.html)
# 리스트뷰(ListView)

## 학습목표
- 리스트뷰의 설정 방법을 이해한다.

## 1. 리스트뷰(ListView) 란?
- [ListView](https://developer.android.com/reference/android/widget/ListView)는 어댑터뷰의 대표 위젯으로서, 복수 개의 항목을 수직으로 표시


	<img src="figure/listview.png" width=300>

<a name="2"></a>
## 2. 간단한 리스트뷰 만들어 보기
- 리스트 뷰 설정 절차
	1. SimpleListViewTest 프로젝트 생성
	2. 메인화면 레이아웃에 ListView 위젯 정의 (XML 코드)
	2. 어댑터 객체 생성 (Java 코드)
	3. ListView 객체에 어댑터 연결 (Java 코드)

<a name="2.1"></a>
### 2.1 메인화면 레이아웃에 ListView 위젯 정의
- 메인화면 레이아웃(예, activity\_main.xml)에 ListView 위젯을 추가하고, XML 레이아웃 파일에 정의된 **ListView 위젯을 Java 코드에서 참조하기 위하여 id 속성을 정의**한다.
	- res/layout/activity\_main.xml

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:orientation="vertical"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">

	    <ListView
	        android:id="@+id/listView"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	       />
	</LinearLayout>
```

### 2.2 어댑터 객체 생성
- 데이터 원본이 배열인 경우에  [ArrayAdapter](https://developer.android.com/reference/android/widget/ArrayAdapter) 객체 사용
- ArrayAdapter 생성자
	- **ArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects)**
		- context: 현재 컨텍스트
		- resource: 항목으로 표시될 텍스트 뷰의 리소스 ID

			리소스 ID | 설명
			--------|-----
			android.R.layout.simple\_list\_item\_1 | 하나의 텍스트 뷰로 구성된 레이아웃
			android.R.layout.simple\_list\_item\_2 | 두 개의 텍스트 뷰로 구성된 레이아웃
			android.R.layout.simple\_list\_item\_checked | 오른쪽에 체크 표시가 나타남
			android.R.layout.simple\_list\_item\_single\_choice | 오른쪽에 라디오 버튼이 나타남
			android.R.layout.simple\_lsit\_item\_multiple\_choice | 오른쪽에 체크 버튼이 나타남
		- objects: 어댑터로 공급될 데이터 원본으로 단순 배열

- String 배열을 이용한 ArrayAdapter 객체 생성 예제

	```java
	public class MainActivity extends AppCompatActivity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        // 데이터 원본 준비
	        String[] items = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8"};

	        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용
	        ArrayAdapter<String> adapt
	                = new ArrayAdapter<String>(
	                this,
	                android.R.layout.simple_list_item_1,
	                items);
	     }
	}
	```  



### 2.3 ListView 객체에 어댑터 연결
1. 현재 화면 레이아웃(activity\_main.xml)에 정의된 뷰 중에서 id가 *listView*인 	ListView 객체를 [findViewById](https://developer.android.com/reference/android/app/Activity.html#findViewById(int))() 메소드를 통해서 얻어온다.
2. 얻어온 ListView 객체에 생성된 어댑터 객체(예, ArrayAdapter 객체-adapt)를 [setAdapter](https://developer.android.com/reference/android/widget/ListView.html#setAdapter(android.widget.ListAdapter))()라는 메소드를 통해서 설정한다.

	```java
	public class MainActivity extends AppCompatActivity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        // 데이터 원본 준비
	        String[] items = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8"};

	        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용
	        ArrayAdapter<String> adapt
	                = new ArrayAdapter<String>(
	                this,
	                android.R.layout.simple_list_item_1,
	                items);

	        //어댑터 연결
	        ListView list = (ListView) findViewById(R.id.listView);
	        list.setAdapter(adapt);
	    }
	}
	```

- 실행 결과

	<img src="figure/listview_example.png">
	
- SimpleListViewTest 프로젝트 Github URL : https://github.com/kwanulee/AndroidProgramming/tree/master/examples/SimpleListViewTest

---
[**다음 학습**: 그리드뷰](gridview.html)

