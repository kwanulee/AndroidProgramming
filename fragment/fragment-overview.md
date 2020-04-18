<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
[**목차**: 프래그먼트](https://kwanulee.github.io/AndroidProgramming/#6-프래그먼트-fragment)
# 프래그먼트 개요

## 학습목표
- 프래그먼트의 기본 개념 및 프래그먼트 수명주기를 이해한다.

<a name="1"> </a>
## 1. 프래그먼트(Fragment) 란?

- 액티비티 위에서 동작하는 **모듈화된 사용자 인터페이스**
	- 액티비티와 분리되어 독립적으로 동작할 수 없습니다. 
- 여러 개의 프래그먼트를 하나의 액티비티에 조합하여 창이 여러 개인 UI를 구축할 수 있으며, 하나의 프래그먼트를 여러 액티비티에서 재사용할 수 있습니다.

	<div class="polaroid">
		<img src="https://developer.android.com/images/fundamentals/fragments.png">
	</div>	

## 2. 액티비티 vs 프래그먼트 비교 

<div class="polaroid">
	<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=http%3A%2F%2Fcfile8.uf.tistory.com%2Fimage%2F996C67425A5CE42E1609F2">
</div>	
	
- 시스템의 **액티비티 매니저**에서 인텐드를 해석해 액티비티 간 데이터를 전달
- 액티비티의 **프래그먼트 매니저**에서 메소드로 프래그먼트 간 데이터를 전달

<!--
- [Fragement-Oriented Architecture](https://vinsol.com/blog/2014/09/15/advocating-fragment-oriented-applications-in-android/)
	- 액티비티의 역할 
		- 여러 프래그먼트를 관리 (추가/삭제)
		- 프래그먼트 간의 통신 담당
		- 다른 앱간의 통신 (알림 및 방송수신) 담당
		- 세션 (사용자 인증)을 유지하거나 애플리케이션의 전역적인 로직 및 정책을 관리
		- 전역적인 UI 담당 (NavigationDrawer UI) 
	- 프래그먼트의 역할
		-  재사용 가능한 컨텐츠 영역의 뷰와 이벤트 처리 로직을 담당 -->	

<a name="3"> </a>
## 3. 프래그먼트 수명주기	
- 프래그먼트도 액티비티처럼 수명주기 상태에따라서 호출되는 콜백 메소드를 정의하고 있다. 
- 프래그먼트는 액티비티 위에 올라가는 것이므로, 프래그먼트의 수명주기도 액비티티의 수명주기에 종속적입니다. 하지만, 프래그만트만 가질 수 있는 상태 메소드들이 더 추가 되었습니다.
	- **onAttach()** : 프래그먼트가 액티비티에 연결될 때 호출됨
	- **onCreateView()** : 프래그먼트의 레이아웃을 생성
	- **onActivityCreated()** : 연결된 액티비티의 onCreate가 완료된 후 호출됨

	<img src="figure/lifecycle.png">
	
- 다양한 프래그먼트 콜백 메소드의 사용 예제 ([참고자료](https://guides.codepath.com/android/Creating-and-Using-Fragments))

	```java
	public class SomeFragment extends Fragment {
	    ThingsAdapter adapter;
	    FragmentActivity listener;
	        
	    // This event fires 1st, before creation of fragment or any views
	    // The onAttach method is called when the Fragment instance is associated with an Activity. 
	    // This does not mean the Activity is fully initialized.
	    @Override
	    public void onAttach(Context context) {
	        super.onAttach(context);
	        if (context instanceof Activity){
	            this.listener = (FragmentActivity) context;
	        }
	    }
	       
	    // This event fires 2nd, before views are created for the fragment
	    // The onCreate method is called when the Fragment instance is being created, or re-created.
	    // Use onCreate for any standard setup that does not require the activity to be fully created
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        ArrayList<Thing> things = new ArrayList<Thing>();
	        adapter = new ThingsAdapter(getActivity(), things);
	    }
	
	    // The onCreateView method is called when Fragment should create its View object hierarchy,
	    // either dynamically or via XML layout inflation. 
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	        return inflater.inflate(R.layout.fragment_some, parent, false);
	    }
		
	    // This event is triggered soon after onCreateView().
	    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
	    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
	    @Override
	    public void onViewCreated(View view, Bundle savedInstanceState) {
	        super.onViewCreated(view, savedInstanceState);
	        ListView lv = (ListView) view.findViewById(R.id.lvSome);
	        lv.setAdapter(adapter);
	    }
	
	    // This method is called when the fragment is no longer connected to the Activity
	    // Any references saved in onAttach should be nulled out here to prevent memory leaks. 
	    @Override
	    public void onDetach() {
	        super.onDetach();
	        this.listener = null;
	    }
	        
	    // This method is called after the parent Activity's onCreate() method has completed.
	    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
	    // At this point, it is safe to search for activity View objects by their ID, for example.
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	    }
	}
	```
	
--- 
[**다음학습**: 프래그먼트 사용](fragment-usage.html)
