<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**이전 학습**: 그리드뷰](gridview.html)
# 커스텀 항목뷰

## 학습목표
- 커스텀 항목 뷰를 정의하는 방법을 이해한다.



##1. 커스텀 항목 뷰 란?
- 어댑터 뷰의 항목 하나는 단순한 문자열 이나 이미지 뿐만 아니라, **다수의 문자열이나 이미지를 포함하는 임의의 뷰**가 될 수 있습니다.

  <div class="polaroid">
  <img src="figure/custom-item-view-overview.png">
  </div>
  
##  2. 커스텀 항목 뷰 만들어 보기
- 커스텀 항목뷰 설정 절차
  1. [커스텀 항목을 위한 XML 레이아웃 정의](#2.1)
  2. [항목 관련 데이터 클래스 정의](#2.2)
  2. [어댑터 클래스 정의](#2.3)
  3. [메인화면 레이아웃에 ListView 위젯 정의](#2.4)
  3. [어댑터를 생성하고 어댑터뷰 객체에 연결](#2.5)

<a name="2.1"></a>
### 2.1 커스텀 항목을 위한 XML 레이아웃 정의
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

<a name="2.2"></a>
### 2.2 항목 관련 데이터 클래스 정의
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

<a name="2.3"></a>
### 2.3 어댑터 클래스 정의
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

<a name="2.4"></a>
### 2.4 메인화면 레이아웃에 ListView 위젯 정의
- [리스트뷰의 2.1 절](listview.html#2.1)에서와 설명한 방식과 동일하게 메인화면 레이아웃(activity\_main.xml)에 ListView 위젯을 추가

<a name="2.5"></a>
### 2.5 어댑터 생성 및 연결
- MainActivity.java 파일에서 다음 역할의 코드를 추가한다.
  1. 어댑터 객체에서 관리할 항목 데이터의 ArrayList 객체를 준비한다.
  2. MyAdapter 객체를 생성하고 초기화 한다.
  3. 생성된 MyAdapter 객체를 어댑터뷰인 리스트뷰에 연결한다.

- [**주의**] [[연습- Android Studio에서 ImageAdapter 클래스 정의하기]](gridview.html#practice1)의 세번째 스탭에서처럼 **app>res>drawable** 하위에 이름이 *sample\_0*에서 *sample\_7*까지인 8개의 이미지 파일을 추가되어 있어야 함

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

- CustomItemViewTest 프로젝트 Github URL : https://github.com/kwanulee/AndroidProgramming/tree/master/examples/CustomItemViewTest