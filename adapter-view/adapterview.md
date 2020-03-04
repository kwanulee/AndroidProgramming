<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
# 어댑터 뷰
---
## 학습목표
- 어댑터뷰의 기본 개념 및 종류를 이해한다.
- 리스트뷰의 설정 방법을 이해한다.
- 그리드뷰의 설정 방법을 이해한다.
- 커스텀 항목 뷰를 정의하는 방법을 이해한다.

---

## 1. 어댑터뷰 개요

### 1.1 어댑터뷰란?

- **어댑터뷰**는 여러 개의 **항목을 다양한 형식으로 나열하고 선택**할 수 있는 기능을 제공하는 뷰
	- *리스트뷰(ListView)*는 항목을 수직으로 나열시키는 방식이고, *그리드뷰(ListView)*는 항목을 격자 형태로 나열시키는 방식임.
- **어댑터 뷰**는 표시할 **항목 데이터를** 직접 관리하지 않고, **어댑터라는 객체로부터 공급받습니다**.

	<div class="polaroid">
	<img src="figure/adapterview_overview.png">
	</div>

### 1.2 어댑터 (Adapter)
- 데이터를 관리하며 **데이터 원본과 어댑터뷰(ListView, GridView) 사이의 중계 역할**
- **어댑터뷰는 어떻게 데이터 항목을 표시할까요?**
	1. 어댑터뷰가 어댑터를 사용하기 위해서는 먼저 데이터원본이 어댑터에 설정되어야 하고, 어댑터뷰에는 어댑터가 설정되어야 합니다.
	2. 어댑터뷰는 항목을 표시하기 위해서 먼저 표시할 항목의 총 개수를 알 필요가 있을 것입니다. 이 때, 어댑터 뷰는 어댑터의 **getCount()**란 메소드를 통해 현재 어뎁터가 관리하는 데이터 항목의 총 개수를 반환합니다.
	3. 어댑터 뷰는 어댑터의 **getView()**란 메소드를 통해서 화면에 실제로 표시할 **항목뷰**를 얻고, 이를 화면에 표시합니다.
-  사용자가 어댑터뷰의 특정 위치의 항목을 선택하였을 때, 어댑터뷰는 선택된 *항목*, *항목ID*, *항목뷰*를 어댑터의 *getItem()*, *getItemId()*, *getView()* 메소드를 통해 얻어와서 이를 항목선택 이벤트 처리기에 넘겨줍니다.

	<div class="polaroid">
	<img src="figure/adapter.png">
	</div>

- 요약하면, 어댑터뷰는 어댑터에 정의된 인터페이스를 바탕으로 필요한 정보를 요청하여 항목뷰를 화면에 표시하거나 선택된 항목뷰를 처리합니다.

### 1.3 어댑터 종류
- [BaseAdapter](https://developer.android.com/reference/android/widget/BaseAdapter)
	- 어댑터 클래스의 공통 구현, 사용자정의 어댑터 구현 시 사용
- [ArrayAdapter<T>](https://developer.android.com/reference/android/widget/ArrayAdapter?hl=en)
	- 객체 배열이나 리소스에 정의된 배열로부터 데이터를 공급받음
- [CursorAdapter](https://developer.android.com/reference/android/widget/CursorAdapter)
	- 데이터베이스로부터 데이터를 공급받음
- [SimpleAdapter](https://developer.android.com/reference/android/widget/SimpleAdapter)
	- 데이터를 Map(키, 값)의 리스트로 관리
	- 데이터를 XML 파일에 정의된 뷰에 대응시키는 어댑터


	<img src="figure/adapter_class.png" width=400>

---
## 2. 리스트뷰(ListView)
### 2.1 리스트뷰(ListView) 란?
- [ListView](https://developer.android.com/reference/android/widget/ListView)는 어댑터뷰의 대표 위젯으로서, 복수 개의 항목을 수직으로 표시


	<img src="figure/listview.png" width=300>

<a name="2.2"></a>
---
### 2.2 간단한 리스트뷰 만들어 보기
- 리스트 뷰 설정 절차
	1. SimpleListViewTest 프로젝트 생성
	2. 메인화면 레이아웃에 ListView 위젯 정의 (XML 코드)
	2. 어댑터 객체 생성 (Java 코드)
	3. ListView 객체에 어댑터 연결 (Java 코드)

<a name="2.2.1"></a>
#### 2.2.1 메인화면 레이아웃에 ListView 위젯 정의
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

#### 2.2.2 어댑터 객체 생성
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



#### 2.2.3 ListView 객체에 어댑터 연결
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
	
- SimpleListViewTest 프로젝트 Github URL : https://github.com/kwanulee/Android/tree/master/examples/SimpleListViewTest

---
## 3. 그리드뷰 (GridView)
### 3.1 그리드뷰 (GridView) 란?
- [GridView](https://developer.android.com/reference/android/widget/GridView)는 2차원 스크롤가능한 그리드에 항목을 표시


	<img src="figure/gridview.png" width=300>

---
### 3.2 간단한 그리드뷰 만들어 보기
- [2.2 간단한 리스트뷰 만들어 보기](#2.2)와 유사함
- 절차
	1. SimpleGridViewTest 프로젝트 생성
	2. 메인화면 레이아웃에 GridView 위젯 정의 (XML 코드)
	3. ArrayAdapter 객체를 생성하고 GridView 객체에 연결 (Java 코드)

#### 3.2.1 메인화면 레이아웃에 GridView 위젯 정의
- 메인화면 레이아웃(예, activity\_main.xml)에 GridView 위젯을 추가하고, XML 레이아웃 파일에 정의된 **GridView 위젯을 Java 코드에서 참조하기 위하여 id 속성을 정의**한다.
- res/layout/activity\_main.xml

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<GridView xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@+id/gridview"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:columnWidth="100dp"
	    android:numColumns="auto_fit"
	    android:verticalSpacing="10dp"
	    android:horizontalSpacing="10dp"
	    android:stretchMode="columnWidth"
	    android:gravity="center"
	    />
	```


	- **android:columnWidth="100dp"** : 그리드 항목 하나의 폭을 100dp로 설정
	- **android:numColumns="auto\_fit"**: 열의 폭과 화면 폭을 바탕으로 자동 계산
	- **android:verticalSpacing**: 항목 간의 간격 설정
	- **android:stretchMode="columnWidth"**: 열 내부의 여백을 폭에 맞게 채움

#### 3.2.2 ArrayAdapter 객체를 생성하고 GridView 객체에 연결
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

        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview = (GridView) findViewById(R.id.gridview);
        // 어댑터를 GridView 객체에 연결
        gridview.setAdapter(adapt);

    }
}
```

1. [2.2 간단한 리스트뷰 만들어 보기](#2.2) 예제와 마찬가지로, [ArrayAdapter](https://developer.android.com/reference/android/widget/ArrayAdapter) 객체를 생성
2. id를 바탕으로 메인화면 레이아웃(activity\_main.xml)에 정의된 GridView 객체 로딩
3. 생성된 ArrayAdapter 객체를 GridView 객체에 연결

- 실행화면

	<img src="figure/simplegridview.png">
	
- SimpleGridViewTest 프로젝트 Github URL : https://github.com/kwanulee/Android/tree/master/examples/SimpleGridViewTest

---
### 3.3 이미지 그리드뷰 만들어 보기
- 이미지 그리드뷰 설정 절차
	1. ImageGridViewTest 프로젝트 생성
	2. 메인화면 레이아웃에 GridView 위젯 정의 (XML 코드)
	3. 어댑터 정의 (Java 코드)
	3. 어댑터를 생성하고 GridView 객체에 연결 (Java 코드)


#### 3.3.1 메인화면 레이아웃에 GridView 위젯 정의
- 메인화면 레이아웃(예, activity\_main.xml)에 GridView 위젯을 추가하고, XML 레이아웃 파일에 정의된 GridView 위젯을 Java 코드에서 참조하기 위하여 id 속성을 정의한다.
- activity\_main.xml

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<GridView xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@+id/gridview"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:columnWidth="100dp"
	    android:numColumns="auto_fit"
	    android:verticalSpacing="10dp"
	    android:horizontalSpacing="10dp"
	    android:stretchMode="columnWidth"
	    android:gravity="center"
	    />
	```
	
<a name="imageadapter-code"></a>
#### 3.3.2 어댑터 정의
- 그리드뷰의 항목으로 간단한 텍스트가 아닌 이미지를 사용하고자 하는 경우에는 그리드뷰의 항목으로 이미지를 공급하는 ImageAdapter를 [BaseAdapter](https://developer.android.com/reference/android/widget/BaseAdapter)로부터 파생하여 정의한다.

	```java
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	
	    public ImageAdapter(Context c) {
	        mContext = c;
	    }
	
	    public int getCount() {
	        return mThumbIds.length;
	    }
	
	    public Object getItem(int position) {
	        return mThumbIds[position];
	    }
	
	    public long getItemId(int position) {
	        return position;
	    }
	
	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {
	            // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	
	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    }
	
	    // references to our images
	    private Integer[] mThumbIds = {
	            R.drawable.sample_2, R.drawable.sample_3,
	            R.drawable.sample_4, R.drawable.sample_5,
	            R.drawable.sample_6, R.drawable.sample_7,
	            R.drawable.sample_0, R.drawable.sample_1,
	            R.drawable.sample_2, R.drawable.sample_3,
	            R.drawable.sample_4, R.drawable.sample_5,
	            R.drawable.sample_6, R.drawable.sample_7,
	            R.drawable.sample_0, R.drawable.sample_1,
	            R.drawable.sample_2, R.drawable.sample_3,
	            R.drawable.sample_4, R.drawable.sample_5,
	            R.drawable.sample_6, R.drawable.sample_7
	    };
	}
	```

  - ImageAdapter가 관리하는 데이터는 편의상 직접 ImageAdapter 내부에 Image 리소스 ID의 배열로 설정
  - BaseAdapter의 **getCount()**, **getItem()**, **getItemId()**, **getView()** 메소드를 재정의함
  		- getCount()는 항목의 총 개수를 반환하기 위해 *mThubIds* 배열의 크기를 반환
    	- getItem()는 특정 위치의 항목을 반환하기 위해 *mThubIds* 배열의 지정된 위치의 항목을 반환
    	- getItemId()는 특정위치의 항목 아이디를 반한하는 것인데, 여기서는 배열의 첨자를 항목의 아이디로 간주함
    	- getView()는 getView 메소드는 첫번째 파라미터로 주어진 위치의 항목 뷰를 반환하는 것이므로, *mThubIds* 배열의 position 위치에 있는 이미지 리소스를 ImageView의 이미지로 설정하고, 이 설정된 ImageView 객체를 그리드 뷰의 항목뷰로 반환한다.
      		- getView() 메소드의 두번째 파라미터인 convertView는 이전에 생성된 항목뷰 (여기서는 ImageView)를 의미한다. 만약 해당 위치의 항목뷰가 처음 만들어지는 경우라면, 새로운 이미지뷰 객체를 만들고 크기와 스케일타입, 패팅을 설정한다. 만약 이전에 이미 만들어진 것이라면, 이를 재사용한다.
      		- 이미지 뷰의 scaleType은 원본 이미지를 이미지 뷰에 맞게 확대 및 축소시킬 때, 어떻게 처리할 지를 지정하는 것인데, 여기서 CENTER\_CROP은  종횡비를 유지하여 스케일링하며 뷰의 크기 이상으로 채우게 됨을 의미한다. 따라서 이미지 일부가 잘릴 수 있다.

<a name="practice1"></a>
#### [연습1] - Android Studio에서 ImageAdapter 클래스 정의하기
1. **app>java** 하위에 ImageAdapter 클래스 생성
2. 3.2.2절에 포함된 [ImageAdapter 코드](#imageadapter-code)를 복사함
3. **app>res>drawable** 하위에 이름이 *sample\_0*에서 *sample\_7*까지인 8개의 이미지 파일을 추가한다. 이미지 파일의 확장자는 크게 중요하지 않다.

#### 3.3.3 어댑터를 생성하고 GridView 객체에 연결
- 그리드뷰 설정의 마지막 단계는 ImageAdapter 객체를 생성하고 이를 GridView 객체에 연결하는 것

	```java
	public class MainActivity extends AppCompatActivity {

	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
	        GridView gridview = (GridView) findViewById(R.id.gridview);
	        // ImageAdapter 객체를 생성하고 GridView 객체에 연결
	        gridview.setAdapter(new ImageAdapter(this));
	    }
	}
	```

#### 3.3.4 항목 클릭 이벤트 처리
- AdapterView의 항목이 클릭될 때, 호출되는 callback method의 인터페이스

	```java
	public static interface AdapterView.OnItemClickListener {
		abstract void onItemClick(AdapterView<?> parent,
	                                   View view,
	                                   int position,
	                                   long id);
	}
	```

	파라미터 | 설명
	-------|----
	parent | 클릭 이벤트가 발생된 AdapterView
	view | 실제 클릭 된 AdapterView안의 View
	position| 어댑터 내에서 클릭 된 항목/뷰의 위치
	id | 클릭 된 항목의 id

- 앞의 ImageGridViewTest 프로젝트 예제에서 항목 클릭 이벤트 처리 코드 추가

	```java
	public class MainActivity extends AppCompatActivity {

	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
	        GridView gridview = (GridView) findViewById(R.id.gridview);
	        // ImageAdapter 객체를 생성하고 GridView 객체에 연결
	        gridview.setAdapter(new ImageAdapter(this));

	        // 항목 클릭 이벤트 처리
	        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v,
	                                    int position, long id) {
	                Toast.makeText(MainActivity.this,
	                        "" + (position+1)+ "번째 선택",
	                        Toast.LENGTH_SHORT).show();
	            }
	        });
	    }
	}
	```
- 실행화면

	<img src="figure/image_gridview_example.png">
	
- ImageGridViewTest 프로젝트 Github URL : https://github.com/kwanulee/Android/tree/master/examples/ImageGridViewTest


## 4. 커스텀 항목뷰 정의 하기
- 어댑터 뷰의 항목 하나는 단순한 문자열 이나 이미지 뿐만 아니라, **다수의 문자열이나 이미지를 포함하는 임의의 뷰**가 될 수 있습니다.

  <div class="polaroid">
  <img src="figure/custom-item-view-overview.png">
  </div>
- 커스텀 항목뷰 설정 절차
  1. [커스텀 항목을 위한 XML 레이아웃 정의](#4.1)
  2. [항목 관련 데이터 클래스 정의](#4.2)
  2. [어댑터 클래스 정의](#4.3)
  3. [메인화면 레이아웃에 ListView 위젯 정의](#4.4)
  3. [어댑터를 생성하고 어댑터뷰 객체에 연결](#4.5)

<a name="4.1"></a>
### 4.1 커스텀 항목을 위한 XML 레이아웃 정의
- 다음과 같은 모양의 커스텀 항목 뷰를 정의

  <img src="figure/custom-item-view.png" width=400>

- XML 코드
  - res/layout 하위에 item.xml 이라는 새로운 xml 파일 생성

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:orientation="horizontal"
      >
      <ImageView
          android:id="@+id/iconItem"
          android:layout_width="@dimen/icon_size"
          android:layout_height="@dimen/icon_size"
          android:scaleType="centerInside"
          android:padding="@dimen/icon_padding"
          android:layout_gravity="center_vertical"
          android:layout_weight="1"
          android:src="@drawable/sample_0"
          />
      <LinearLayout
          android:orientation="vertical"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_weight="2">
          <TextView
              android:id="@+id/textItem1"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:textColor="@color/colorAccent"
              android:textSize="@dimen/list_item_text_size1"
              android:padding="@dimen/list_item_padding"
              android:hint="Name"
              />
          <TextView
              android:id="@+id/textItem2"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:textColor="@color/colorPrimary"
              android:textSize="@dimen/list_item_text_size2"
              android:padding="@dimen/list_item_padding"
              android:hint="Age"
              />
      </LinearLayout>

  </LinearLayout>
  ```

- 위의 item.xml 코드 내에서 ImageView 크기 및 TextView의 textSize 및 padding 속성 값은 dimension 리소스에 정의되어 있음
  - res/values 폴더 내에 dimens.xml 파일을 생성하고 아래 코드를 추가함

  ```xml
  <resources>
    <!-- Default screen margins, per the Android Design guidelines. -->
    <dimen name="activity_horizontal_margin">16dp</dimen>
    <dimen name="activity_vertical_margin">16dp</dimen>
    <dimen name="list_item_text_size1">20dp</dimen>
    <dimen name="list_item_text_size2">16dp</dimen>
    <dimen name="list_item_padding">4dp</dimen>
    <dimen name="icon_size">60dp</dimen>
    <dimen name="icon_padding">8dp</dimen>
  </resources>
  ```

<a name="4.2"></a>
### 4.2 항목 관련 데이터 클래스 정의
- 항목뷰에 표시할 데이터를 정의한 MyItem 클래스 정의

	```java
	public class MyItem {
	    int mIcon; // image resource
	    String nName; // text
	    String nAge;  // text
	
	    MyItem(int aIcon, String aName, String aAge) {
	        mIcon = aIcon;
	        nName = aName;
	        nAge = aAge;
	    }
	}
	```

<a name="4.3"></a>
### 4.3 어댑터 클래스 정의
- 앞서 정의한 MyItem 타입의 객체들을 ArrayList로 관리하는 MyAdapter 클래스를 BaseAdapter를 파생하여 정의
- MyAdapter 클래스는 앞서 예시한 그리드 뷰의 예제에서 처럼, getCount(), getItem(), getItemID(), getView() method를 재정의해야 합니다.
- MyAdapter 클래스 코드

	```java
	public class MyAdapter extends BaseAdapter {
	    private Context mContext;
	    private int mResource;
	    private ArrayList<MyItem> mItems = new ArrayList<MyItem>();
	
	    public MyAdapter(Context context, int resource, ArrayList<MyItem> items) {
	        mContext = context;
	        mItems = items;
	        mResource = resource;
	    }
	
	    // MyAdapter 클래스가 관리하는 항목의 총 개수를 반환
	    @Override
	    public int getCount() {
	        return mItems.size();
	    }
	
	    // MyAdapter 클래스가 관리하는 항목의 중에서 position 위치의 항목을 반환
	    @Override
	    public Object getItem(int position) {
	        return mItems.get(position);
	    }
	
	    // 항목 id를 항목의 위치로 간주함
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	
	    // position 위치의 항목에 해당되는 항목뷰를 반환하는 것이 목적임
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	
	        if (convertView == null) { // 해당 항목 뷰가 이전에 생성된 적이 없는 경우
	            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            // 항목 뷰를 정의한 xml 리소스(여기서는 mResource 값)으로부터 항목 뷰 객체를 메모리로 로드
	            convertView = inflater.inflate(mResource, parent,false);
	        }
	
	        // convertView 변수로 참조되는 항목 뷰 객체내에 포함된 이미지뷰 객체를 id를 통해 얻어옴
	        ImageView icon = (ImageView) convertView.findViewById(R.id.iconItem);
	        // 어댑터가 관리하는 항목 데이터 중에서 position 위치의 항목의 이미지 리소스를 이미지뷰 객체에 설정
	        icon.setImageResource(mItems.get(position).mIcon);
	
	        // convertView 변수로 참조되는 항목 뷰 객체내에 포함된 텍스트뷰 객체를 id를 통해 얻어옴
	        TextView name = (TextView) convertView.findViewById(R.id.textItem1);
	        // 어댑터가 관리하는 항목 데이터 중에서 position 위치의 항목의 문자열을 설정 텍스트뷰 객체에 설정
	        name.setText(mItems.get(position).nName);
	
	        // Set Text 02
	        TextView age = (TextView) convertView.findViewById(R.id.textItem2);
	        age.setText(mItems.get(position).nAge);
	
	        return convertView;
	    }
	}
	```

<a name="4.4"></a>
### 4.4 메인화면 레이아웃에 ListView 위젯 정의
- [2.2.1 절](#2.2.1)에서와 설명한 방식과 동일하게 메인화면 레이아웃(activity\_main.xml)에 ListView 위젯을 추가

<a name="4.5"></a>
### 4.5 어댑터 생성 및 연결
- MainActivity.java 파일에서 다음 역할의 코드를 추가한다.
  1. 어댑터 객체에서 관리할 항목 데이터의 ArrayList 객체를 준비한다.
  2. MyAdapter 객체를 생성하고 초기화 한다.
  3. 생성된 MyAdapter 객체를 어댑터뷰인 리스트뷰에 연결한다.

- [**주의**] [연습1](#practice1)의 세번째 스탭에서 처럼 app>res>drawable 하위에 이름이 sample\_0에서 sample\_7까지인 8개의 이미지 파일을 추가되어 있어야  함

	```java
	public class MainActivity extends AppCompatActivity {
	    static MyAdapter adapter;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        // 데이터 원본 준비
	        ArrayList<MyItem> data = new ArrayList<MyItem>();
	        data.add(new MyItem(R.drawable.sample_0, "Bella", "1"));
	        data.add(new MyItem(R.drawable.sample_1, "Charlie", "2"));
	        data.add(new MyItem(R.drawable.sample_2, "Daisy", "1.5"));
	        data.add(new MyItem(R.drawable.sample_3, "Duke", "1"));
	        data.add(new MyItem(R.drawable.sample_4, "Max", "2"));
	        data.add(new MyItem(R.drawable.sample_5, "Happy", "4"));
	        data.add(new MyItem(R.drawable.sample_6, "Luna", "3"));
	        data.add(new MyItem(R.drawable.sample_7, "Bob", "2"));
	
	        //어댑터 생성
	        adapter = new MyAdapter(this, R.layout.item, data);
	
	        //어댑터 연결
	        ListView listView = (ListView)findViewById(R.id.listView);
	        listView.setAdapter(adapter);
	
	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View vClicked,
	                                    int position, long id) {
	                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
	                String name = ((MyItem)adapter.getItem(position)).nName;
	                Toast.makeText(MainActivity.this, name + " selected",
	                        Toast.LENGTH_SHORT).show();
	            }
	        });
	    }
	}
	```

- 실행 결과

  <img src="figure/custom-item-view-result.png" width=300>

- CustomItemViewTest 프로젝트 Github URL : https://github.com/kwanulee/Android/tree/master/examples/CustomItemViewTest