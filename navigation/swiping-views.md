<style> 
div.polaroid {
  	width: 200px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**이전 학습**: 앱바](actionbar.html)

# Swipe Views with Tabs

## 학습목표

- ViewPager2를 사용하여 스와이프 뷰를 구현한다.
- 탭을 표시하는 방법을 살펴본다.

	<img src="figure/tablayout.gif" width=200>

## 1. Swipe Views란?
- **스와이핑 뷰**는 화면을 쓸어 넘기는 동작인 스와이프(Swipe)를 통해 페이지 전환을 할 수 있는 뷰
	- 이 단원에서는 [ViewPager2](https://developer.android.com/reference/kotlin/androidx/viewpager2/widget/ViewPager2)를 통해  스와이핑 뷰 구현을 예시함
	-  [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager)는 ViewPager2의 이전 버전으로서 현재 deprecated된 라이브러리 이므로, 가급적 사용하지 않는 것이 좋음
		- 참고 가이드: [ViewPager로 프래그먼트 간 슬라이드 전환](https://developer.android.com/training/animation/screen-slide?hl=ko)
- [어댑터 뷰 단원](https://kwanulee.github.io/AndroidProgramming/adapter-view/adapterview.html)에서 어댑터 뷰(ListView, GridView)의 항목에 표시될 정보를 [Adapter](https://developer.android.com/reference/android/widget/Adapter) 객체를 통해서 얻었듯이, [ViewPager2](https://developer.android.com/reference/kotlin/androidx/viewpager2/widget/ViewPager2)는 [FragmentStateAdapter](https://developer.android.com/reference/kotlin/androidx/viewpager2/adapter/FragmentStateAdapter?hl=ko) 객체를 통해서 각 페이지에 표시될  정보를 제공 받습니다.

	<img src="figure/viewpager.png">
			

## 2. ViewPager2 사용하기
- [ViewPager2](https://developer.android.com/reference/kotlin/androidx/viewpager2/widget/ViewPager2)를 사용하기 위해서는 다음 의존성을 app/build.gradle 파일에 추가해야 함
- [Create swipe views with tabs using ViewPager2](https://developer.android.com/guide/navigation/navigation-swipe-view-2)


	```
	dependencies {
		 ...
		 implementation 'androidx.viewpager2:viewpager2:1.0.0'
	}
	```

- [FragmentStateAdapter](https://developer.android.com/reference/kotlin/androidx/viewpager2/adapter/FragmentStateAdapter?hl=ko)를 이용한  [ViewPager2](https://developer.android.com/reference/kotlin/androidx/viewpager2/widget/ViewPager2) 사용 예제 구현은 다음과 같은 절차로 진행됩니다.
	1. [XML 레이아웃에 ViewPager2를 추가](#2.1)
	2. [페이지를 나타내는 프레그먼트 정의](#2.2)
	3. [FragementStateAdapter 재정의](#2.3)
	4. [ViewPager 객체에 FragmentPagerAdapter 객체 설정](#2.4)

<a name="2.1"></a>
### 2.1 XML 레이아웃에 ViewPager2를 추가
-  **ViewPagerTest** 안드로이드 프로젝트를 생성하고, **activity\_main.xml** 파일을 아래와 같이 정의합니다.

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	 
	    <androidx.viewpager2.widget.ViewPager2
	        android:id="@+id/vpPager"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content">
	    </androidx.viewpager.widget.ViewPager>
	</LinearLayout>
	```

<a name="2.2"></a>
### 2.2 페이지를 나타내는 프래그먼트 정의
-  3개의 각기 다른 페이지 화면을 프래그먼트(FirstFragment, SencondFragment, ThirdFragment)로 정의합니다.  아래에는 FirstFragment만 예시하였고, 나머지 프래그먼트 코드도 유사하게 작성할 수 있습니다.
-  FirstFragment (Java 코드)

	```java
	public class FirstFragment extends Fragment {
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.fragment_first, container, false);
	    }
	}
	```  
- fragment\_first.xml (XML 레이아웃)

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:background="@android:color/holo_orange_light"
	    tools:context=".FirstFragment">
	
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_gravity="center"
	        android:text="FirstFragment 페이지 입니다."
	        android:textAppearance="?android:attr/textAppearanceLarge" />
	
	</FrameLayout>
	```

<a name="2.3"></a>
### 2.3  FragementStateAdapter 재정의
- [FragmentStateAdapter](https://developer.android.com/reference/kotlin/androidx/viewpager2/adapter/FragmentStateAdapter?hl=ko)를 재정의하여 몇 개의 페이지가 존재하며, 각 페이지를 나타내는 프래그먼트가 무엇인지를 정의합니다.

	```java
	public class PagerAdapter extends FragmentStateAdapter {
	    private static int NUM_ITEMS=3;
	
	    public PagerAdapter(FragmentActivity fa) {
	        super(fa);
	    }
	
	    // 각 페이지를 나타내는 프래그먼트 반환
	    @Override
	    public Fragment createFragment(int position) {
	
	        switch (position) {
	            case 0:
	                FirstFragment first = new FirstFragment();
	                return first;
	            case 1:
	                SecondFragment second = new SecondFragment();
	                return second;
	            case 2:
	                ThirdFragment third = new ThirdFragment();
	                return third;
	            default:
	                return null;
	        }
	    }
	
	    // 전체 페이지 개수 반환
	    @Override
	    public int getItemCount() {
	        return NUM_ITEMS;
	    }
	}
	```

<a name="2.4"></a>
### 2.4  ViewPager2 객체에 FragmentStateAdapter 객체 설정
- 이제, MainActivity 클래스의 onCreate() 메소드에서 ViewPager 객체에 앞서 정의한 PagerAdapter 객체를 설정합니다.

	```java
	public class MainActivity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        ViewPager2 vpPager = findViewById(R.id.vpPager);
	        FragmentStateAdapter adapterViewPager = new PagerAdapter(this);
	        vpPager.setAdapter(adapterViewPager);
	    }
	}
	```
	

	
### 2.5 기타 ViewPager 설정

- [getCurrentItem()](https://developer.android.com/reference/androidx/viewpager/widget/ViewPager.html#getCurrentItem()): ViewPager 객체의 현재 페이지를 반환

	```java
	vpPager.getCurrentItem(); // --> 2
	```

- [setCurrentItem(int item)](https://developer.android.com/reference/androidx/viewpager/widget/ViewPager.html#setCurrentItem(int)): ViewPager 객체의 현재 페이지를 설정

	```java
	vpPager.setCurrentItem(2)
	```
- [ViewPager.OnPageChangeListener](https://developer.android.com/reference/androidx/viewpager/widget/ViewPager.OnPageChangeListener.html): ViewPager 객체의 페이지 변화가 일어날 때, 특정한 일을 처리해 주기 위해서 사용


	```java
	// Attach the page change listener inside the activity
	vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
		@Override
		public void onPageSelected(int position) {
	                Toast.makeText(MainActivity.this,
	                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
		}
	});
	```

### 2.6 실행 결과
- 실행화면

	<img src="figure/screen.gif" width=200>

## 3. Google Play Style 탭 표시하기
-  [**TabLayout**](https://developer.android.com/reference/com/google/android/material/tabs/TabLayout.html)은 Google Play Style의 슬라이딩 탭을 구현한 것으로, 디자인 지원 라이브러리에 포함되어 있습니다.
-  이를 이용하기 위해서는 3장까지 진행된 프로젝트에 다음 두 단계를 추가합니다. (PagerAdapter.getPageTitle(int) 메소드가 재정의 되어 있는 것을 가정)
	1. 디자인 지원 라이브러리 설정
	2.  XML 레이아웃에 TabLayout 추가 
	
### 3.1  디자인 지원 라이브러리 설정
- app/build.gradle 파일에 다음 의존성을 추가

	```
	dependencies {
	    ...
	    implementation 'com.google.android.material:material:1.1.0'
	}
	```

### 3.2 XML 레이아웃에 TabLayout 추가
- XML 레이아웃에서 ViewPager 위젯의 자식뷰로 **com.google.android.material.tabs.TabLayout**을 추가 (PagerTabStrip 위젯은 제거)

	``` xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	    
	    <com.google.android.material.tabs.TabLayout
	        android:id="@+id/sliding_tabs"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        app:tabMode="fixed" />
	    
	    <androidx.viewpager2.widget.ViewPager2
	        android:id="@+id/vpPager"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"/>
	
	</LinearLayout>	
	```

- 다양한 TablLayout의 속성 값을 커스터마이징 할 수 있습니다.

	| Name | Options | Description|
	|----|----|----|
	|tabBackground	|@drawable/image	|Background applied to the tabs|
	|tabGravity|	center, fill|	Gravity of the tabs|
	|tabIndicatorColor|	@color/blue	|Color of the tab indicator line
	|tabIndicatorHeight|	@dimen/tabh	|Height of the tab indicator line
	|tabMaxWidth	|@dimen/tabmaxw	|Maximum width of the tab
	|tabMode|	fixed, scrollable|	Small number of fixed tabs or scrolling list
	|tabTextColor	|@color/blue	|Color of the text on the tab
	
	- [추가 속성 정보](https://developer.android.com/reference/com/google/android/material/tabs/TabLayout.html#lattrs)

### 3.3 TabLayoutMediator

- [TabLayoutMediator](https://developer.android.com/reference/com/google/android/material/tabs/TabLayoutMediator)는 TabLayout을 ViewPager2로 연결시켜 주는 역할을 함
-  탭의 스타일 및 텍스트를 설정하기 위해서는 [TabLayoutMediator.TabConfigurationStrategy](https://developer.android.com/reference/com/google/android/material/tabs/TabLayoutMediator.TabConfigurationStrategy?hl=ko) 인터페이스를 구현하여, TabLayoutMediator 생성자 파라미터로 설정해 주어야 함.

	```java
	    protected void onCreate(Bundle savedInstanceState) {
	        //...	
	        // create a TabLayoutMediator to link the TabLayout to the ViewPager2, and attach it
	        new TabLayoutMediator(tabLayout, vpPager,
	                new TabLayoutMediator.TabConfigurationStrategy() {
	                    @Override
	                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
	                        tab.setText(((PagerAdapter)adapterViewPager).getPageTitle(position));
	                    }
	                }
	        ).attach();
	        //...
		}
	```
	https://github.com/kwanulee/AndroidProgramming/blob/master/examples/ViewPagerTest/app/src/main/java/com/example/viewpagertest/MainActivity.java#L30-L38
	
### 3.4 실행 결과
- 실행 화면

	<img src="figure/tablayout.png" width=200>
	
- 전체 프로젝트 코드 (Github 저장소 위치)
	- https://github.com/kwanulee/AndroidProgramming/tree/master/examples/ViewPagerTest


### 3.5 TabLayout 추가 정보
- [Google Play Style Tabs using TabLayout](https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#add-custom-view-to-tablayout) 자료에는  다음과 같은 작업을 수행하는 방법을 공부할 수 있습니다.
	
	| TabLayout의 탭에 Icon 설정 | TabLayout의 탭에 Icon+Text 설정|
	|---|---|
	|<img src="https://i.imgur.com/dYvY5NKl.jpg" width=200>|<img src="https://i.imgur.com/A8xEpKsl.jpg" width=200>|
	-  TabLayout의 탭에 커스텀 뷰 설정

### 참고링크
- [ViewPager2로 프래그먼트 간 슬라이드 전환](https://developer.android.com/training/animation/screen-slide-2?hl=ko)
- [ViewPager with FragmentPagerAdapter](https://guides.codepath.com/android/ViewPager-with-FragmentPagerAdapter)

---