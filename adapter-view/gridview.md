<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**이전 학습**: 리스트뷰](listview.html)
# 그리드뷰 (GridView)

## 학습목표
- 그리드뷰의 설정 방법을 이해한다.

## 1. 그리드뷰 (GridView) 란?
- [GridView](https://developer.android.com/reference/android/widget/GridView)는 2차원 스크롤가능한 그리드에 항목을 표시


	<img src="figure/gridview.png" width=300>


## 2. 간단한 그리드뷰 만들어 보기
- [간단한 리스트뷰 만들어 보기](listview.html#2)와 유사함
- 절차
	1. SimpleGridViewTest 프로젝트 생성
	2. 메인화면 레이아웃에 GridView 위젯 정의 (XML 코드)
	3. ArrayAdapter 객체를 생성하고 GridView 객체에 연결 (Java 코드)

### 2.1 메인화면 레이아웃에 GridView 위젯 정의
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

### 2.2 ArrayAdapter 객체를 생성하고 GridView 객체에 연결
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

1. [간단한 리스트뷰 만들어 보기](listview.html#2) 예제와 마찬가지로, [ArrayAdapter](https://developer.android.com/reference/android/widget/ArrayAdapter) 객체를 생성
2. id를 바탕으로 메인화면 레이아웃(activity\_main.xml)에 정의된 GridView 객체 로딩
3. 생성된 ArrayAdapter 객체를 GridView 객체에 연결

- 실행화면

	<img src="figure/simplegridview.png">
	
- SimpleGridViewTest 프로젝트 Github URL : https://github.com/kwanulee/AndroidProgramming/tree/master/examples/SimpleGridViewTest

---
## 3. 이미지 그리드뷰 만들어 보기
- 이미지 그리드뷰 설정 절차
	1. ImageGridViewTest 프로젝트 생성
	2. 메인화면 레이아웃에 GridView 위젯 정의 (XML 코드)
	3. 어댑터 정의 (Java 코드)
	3. 어댑터를 생성하고 GridView 객체에 연결 (Java 코드)


### 3.1 메인화면 레이아웃에 GridView 위젯 정의
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
	
<a name="3.2"></a>
<a name="imageadapter-code"></a>
### 3.2 어댑터 정의
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
  		- **getCount()**는 항목의 총 개수를 반환하기 위해 *mThubIds* 배열의 크기를 반환
    	- **getItem()**는 특정 위치의 항목을 반환하기 위해 *mThubIds* 배열의 지정된 위치의 항목을 반환
    	- **getItemId()**는 특정위치의 항목 아이디를 반한하는 것인데, 여기서는 배열의 첨자를 항목의 아이디로 간주함
    	- **getView()**는 getView 메소드는 첫번째 파라미터로 주어진 위치의 항목 뷰를 반환하는 것이므로, *mThubIds* 배열의 position 위치에 있는 이미지 리소스를 ImageView의 이미지로 설정하고, 이 설정된 ImageView 객체를 그리드 뷰의 항목뷰로 반환한다.
      		- getView() 메소드의 두번째 파라미터인 **convertView**는 이전에 생성된 항목뷰 (여기서는 ImageView)를 의미한다. 만약 해당 위치의 항목뷰가 처음 만들어지는 경우라면, 새로운 이미지뷰 객체를 만들고 크기와 스케일타입, 패팅을 설정한다. 만약 이전에 이미 만들어진 것이라면, 이를 재사용한다.
      		- 이미지 뷰의 **scaleType**은 원본 이미지를 이미지 뷰에 맞게 확대 및 축소시킬 때, 어떻게 처리할 지를 지정하는 것인데, 여기서 *CENTER\_CROP*은  종횡비를 유지하여 스케일링하며 뷰의 크기 이상으로 채우게 됨을 의미한다. 따라서 이미지 일부가 잘릴 수 있다.

---
<a name="practice1"></a>
### [연습1] - Android Studio에서 ImageAdapter 클래스 정의하기
1. **app>java** 하위에 ImageAdapter 클래스 생성
2. 3.2 절에 포함된 [ImageAdapter 코드](#imageadapter-code)를 복사함
3. **app>res>drawable** 하위에 이름이 *sample\_0*에서 *sample\_7*까지인 8개의 이미지 파일을 추가한다. 이미지 파일의 확장자는 크게 중요하지 않다.

---

### 3.3 어댑터를 생성하고 GridView 객체에 연결
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

### 3.4 항목 클릭 이벤트 처리
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
	
- ImageGridViewTest 프로젝트 Github URL : https://github.com/kwanulee/AndroidProgramming/tree/master/examples/ImageGridViewTest

---
[**다음 학습**: 커스텀 항목뷰](custom-item-view.html)
