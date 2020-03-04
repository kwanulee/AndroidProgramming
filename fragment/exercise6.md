
#### [연습6] - 프래그먼트-액티비티 통신 방법 (인터페이스를 구현한 액티비티를 인터페이스로 접근) 
1. [연습4](exercise4.html)에서 수행한 프로젝트를 바탕으로 진행
2. **activity\_main.xml** 파일을 열고, 앞서 정의한 **TitlesFragment**를 정적으로 추가	 

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	
	    <fragment
	        android:name="com.android.tabletphonefragment.TitlesFragment"
	        android:id="@+id/titles"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent" />
	
	</LinearLayout>	
	```
	
3. **TitlesFragment** 내부에 **void onTitleSelected(int i)** 메소드를 포함한 인터페이스 **OnTitleSelectedListener**를 정의

	```java
	public class TitlesFragment extends Fragment {
		// ... 기존 코드와 동일
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
	        // ... 기존 코드와 동일
	    }
	
		// 인터페이스 추가 정의
	    public interface OnTitleSelectedListener {
	        public void onTitleSelected(int i);
	    }
	
	}
	
	```
	
4. **MainActivity.java** 파일에서, **OnTitleSelectedListener** 인터페이스 구현하기

	```java
	public class MainActivity extends AppCompatActivity 
									implements TitlesFragment.OnTitleSelectedListener{
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	    }
	
	    public void onTitleSelected(int i) {
	        Toast.makeText(getApplicationContext(),"position="+i,Toast.LENGTH_SHORT).show();
	    }
	}
	```
4. **TitlesFragment**의 ListView의 항목이 선택되었을 때 선택된 항목 위치를 **MainActivity**의 **onTitleSelected()**메소드를 호출하면서 파라미터로 전달
	- **onItemClick()** 메소드 재정의

	```java
	public class TitlesFragment extends Fragment {
		// ... 기존 코드와 동일
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
	        
	        // ... 기존 코드와 동일
	        
	        // 리스트뷰 항목이 선택되었을 때, 항목 클릭 이벤트 처리
	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
	                // 현재 프래그먼트와 연결된 액티비티를 반환
	                Activity activity = getActivity();
	
	                // 선택된 항목 위치(position)을 OnTitleSelectedListener 인터페이스를 구현한 액티비티로 전달
	                if (activity instanceof OnTitleSelectedListener)
	                    ((OnTitleSelectedListener)activity).onTitleSelected(position);
	            }
	        });
	        return rootView;
	    }
		// ... 기존 코드와 동일

	}
	```
	
4. 실행결과

<img src="figure/activity-communication1-result.png" width=300>