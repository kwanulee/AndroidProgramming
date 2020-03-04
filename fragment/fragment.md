<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
# 프래그먼트
---
## 학습목표
- 프래그먼트의 기본 개념 및 프래그먼트 수명주기를 이해한다.
- 태블릿용 및 스마트폰용 디바이스에서 프래그먼트를 재사용할 수 있는 방법은 실습한다.

---
<a name="1"> </a>
## 1. 프래그먼트 개요

<a name="1.1"> </a>
### 1.1 프래그먼트(Fragment) 란?

- 액티비티 위에서 동작하는 **모듈화된 사용자 인터페이스**
	- 액티비티와 분리되어 독립적으로 동작할 수 없음 
- 여러 개의 프래그먼트를 하나의 액티비티에 조합하여 창이 여러 개인 UI를 구축할 수 있으며, 하나의 프래그먼트를 여러 액티비티에서 재사용할 수 있슴

	<div class="polaroid">
	<img src="https://developer.android.com/images/fundamentals/fragments.png">
	</div>	

### 1.2 액티비티 vs 프래그먼트 비교 
(출처: https://24getmenot7.tistory.com/105)

<div class="polaroid">
	<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=http%3A%2F%2Fcfile8.uf.tistory.com%2Fimage%2F996C67425A5CE42E1609F2">
</div>	
	
- 시스템의 액티비티 매니저에서 인텐드를 해석해 액티비티 간 데이터를 전달
- 액티비티의 프래그먼트매니저 에서 메소드로 프래그먼트 간 데이터를 전달

### 1.3 프래그먼트 정의하기
- 액티비티를 만들 때와 비슷하게, 하나의 **자바 소스** 파일과 하나의 **XML 레이아웃**로 정의
	- **자바 소스 파일 생성**
		- 프래그먼트를 생성하려면 [Fragment](https://developer.android.com/reference/android/app/Fragment.html)의 서브클래스(또는 이의 기존 서브클래스)를 생성 
			- 프래그먼트에 대해 레이아웃을 제공하려면 반드시 [onCreateView](https://developer.android.com/reference/android/app/Fragment.html#onCreateView(android.view.LayoutInflater,%20android.view.ViewGroup,%20android.os.Bundle))() 콜백 메서드를 구현 
		- 예제

			```java
			public static class FirstFragment extends Fragment {
			    @Override
			    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                             Bundle savedInstanceState) {
			        // Inflate the layout for this fragment
			        return inflater.inflate(R.layout.fragment_first, container, false);
			    }
			}
			```
			
			- [inflate()](https://developer.android.com/reference/android/view/LayoutInflater.html#inflate(int,%20android.view.ViewGroup,%20boolean))함수를 통해서 *fragment\_first.xml* 파일로부터 레이아웃을 로드

	- **XML 레이아웃 생성**
		- 프래그먼트도 부분 화면이므로 화면에 표시될 뷰들을 정의하는 XML 파일을 /res/layout 폴더 안에 생성한다.

#### [[연습1] - 프래그먼트 정의하기](exercise1.html)  

### 1.4 프래그먼트를 액티비티의 레이아웃 파일에 정적 추가하기
- 프래그먼트를 액티비티의 레이아웃 파일 안에서 선언
	- **\<fragment\>** 안의 **android:name** 특성은 레이아웃 안에서 인스턴스화할 **Fragment** 클래스를 지정
	- [**중요**] 각 프래그먼트에는 액티비티가 재시작되는 경우 프래그먼트를 복구하기 위해 시스템이 사용할 수 있는 고유한 식별자가 필요합니다. 프래그먼트에 ID를 제공하는 데에는 다음과 같은 세 가지 방법이 있습니다.
		- 고유한 ID와 함께 **android:id** 속성을 제공합니다.
		- 고유한 문자열과 함께 **android:tag** 속성을 제공합니다.
		- 위의 두 가지 중 어느 것도 제공하지 않으면, 시스템은 컨테이너 뷰의 ID를 사용합니다.
- 예제

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="Hello World!" />
	    <fragment
	            android:name="com.kwanwoo.android.fragmentbasic.FirstFragment"
	            android:layout_width="match_parent"
	            android:layout_height="match_parent"
	            android:id="@+id/fragment"
	            />
	</LinearLayout>
	```
	
#### [[연습2] - 프래그먼트를 액티비티의 레이아웃 파일에 정적 추가하기](exercise2.html)

### 1.5 자바 코드에서 동적으로 프래그먼트 추가하기
- **프래그먼트 매니저(FragmentManager)**는 프래그먼트를 다루는 작업을 해주는 객체로서 다음 두 가지 방법중에 하나로 얻어온다.
	- [getFragmentManager()](https://developer.android.com/reference/android/app/Activity.html#getFragmentManager()): 현재 액티비티가 [Activity](https://developer.android.com/reference/android/app/Activity)를 확장하여 만든 경우에 사용함
	- [getSupportFragmentManager()](https://developer.android.com/reference/android/support/v4/app/FragmentActivity.html#getSupportFragmentManager()):  안드로이드 이전 버전들에서도 프래그먼트를 사용할 수 있도록 만든 appcompat\_v7 라이브러리 프로젝트에서 지원하는 기능으로, 현재 액티비티가 [AppCompatActivity](https://developer.android.com/reference/android/support/v7/app/AppCompatActivity?hl=en)를 확장하여 만든 경우에 사용함

- [**FragmentTransaction**](https://developer.android.com/reference/android/app/FragmentTransaction.html)은 프래그먼트를 추가, 삭제 또는 교체 등의 작업 수행 중에 오류가 발생하면 다시 원래 상태로 되돌릴 수 있도록해주는 기능을 구현한 클래스
	-  [FragmentManager](https://developer.android.com/reference/android/app/FragmentManager.html)의 [beginTransaction()](https://developer.android.com/reference/android/app/FragmentManager.html#beginTransaction()) 메소드 호출을 통해 [FragmentTransaction](https://developer.android.com/reference/android/app/FragmentTransaction.html)의 인스턴스를 얻어온다.
	-  주어진 트랜잭션에 대해 수행하고자 하는 모든 변경 사항을 설정하려면 [add()](https://developer.android.com/reference/android/app/FragmentTransaction.html#add(android.app.Fragment,%20java.lang.String)), [remove()](https://developer.android.com/reference/android/app/FragmentTransaction.html#remove(android.app.Fragment)), 및 [replace()](https://developer.android.com/reference/android/app/FragmentTransaction.html#replace(int,%20android.app.Fragment))와 같은 메서드를 사용함
	-  주어진 트랜잭션에 대해 수행하고자 하는 모든 변경 사항을 적용하려면 [FragmentTransaction](https://developer.android.com/reference/android/app/FragmentTransaction.html)의 [commit()](https://developer.android.com/reference/android/app/FragmentTransaction.html#commit())을 호출해야 함.

- 예제

	```java
	FragmentManager fragmentManager = getSupportFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	fragmentTransaction.add(R.id.fragment_container, new FirstFragment());
	fragmentTransaction.commit();
	
	```
	위 코드를 아래와같이 작성할 수도 있습니다.
	
	```java
	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new FirstFragment()).commit();
	```

<!-- 
- 액티비티가 실행 중인 동안에 [**FragmentTransaction**](https://developer.android.com/reference/android/app/FragmentTransaction.html)의 인스턴스를 통해서, 언제든지 액티비티 레이아웃에 프래그먼트를 추가할 수 있음 
- FragmentTransaction의 인스턴스를 액티비티에서 가져오는 방법
	1. 액티비티와 관련된 **프래그먼트 매니저(FragmentManager)** 객체를 다음 두 가지 방법중에 하나로 얻어온다.
		- [getFragmentManager()](https://developer.android.com/reference/android/app/Activity.html#getFragmentManager())
		- [getSupportFragmentManager()](https://developer.android.com/reference/android/support/v4/app/FragmentActivity.html#getSupportFragmentManager())
	2. [FragmentManager](https://developer.android.com/reference/android/app/FragmentManager.html)의 [beginTransaction()](https://developer.android.com/reference/android/app/FragmentManager.html#beginTransaction()) 메소드 호출을 통해 [FragmentTransaction](https://developer.android.com/reference/android/app/FragmentTransaction.html)의 인스턴스를 얻어온다.

	```java
	FragmentManager fragmentManager = getSupportFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	```
- 그런 다음 [add()](https://developer.android.com/reference/android/app/FragmentTransaction.html#add(int,%20android.app.Fragment)) 메서드를 사용하여 추가할 프래그먼트와 이를 포함할 뷰를 지정
	- [add()](https://developer.android.com/reference/android/app/FragmentTransaction.html#add(int,%20android.app.Fragment))  메소드
		- 첫번째 파라미터: 프래그먼트를 포함할 부모 뷰의 ID  
		- 두번재 파라미터: 추가할 프래그먼트 객체
		
	```java
	FirstFragment fragment = new FirstFragment();
	fragmentTransaction.add(R.id.fragment_container, fragment);
	fragmentTransaction.commit();
	```
-->
#### [[연습3]- 자바 코드에서 동적으로 프래그먼트 추가하기](exercise3.html)

---
<a name="2"> </a>
## 2. 프래그먼트 수명주기	
- 프래그먼트도 액티비티처럼 수명주기 상태에따라서 호출되는 콜백 메소드를 정의하고 있다. 
- 프래그먼트는 액티비티 위에 올라가는 것이므로, 프래그먼트의 수명주기도 액비티티의 수명주기에 종속적입니다. 하지만, 프래그만트만 가질 수 있는 상태 메소드들이 더 추가 되었습니다.
	- **onAttach()** : 프래그먼트가 액티비티에 연결될 때 호출됨
	- **onCreateView()** : 프래그먼트의 레이아웃을 생성
	- **onActivityCreated()** : 연결된 액티비티의 onCreate가 완료된 후 호출됨

	<img src="figure/lifecycle.png">


---
<a name="3"> </a>
## 3. 프래그먼트 예제 - 태블릿과 스마트폰에서 다른 화면 구현하기

1. **TitlesFragment**에서는 제목을 ListView로 표시하고, **DetailsFragment**에서는 ListView에서 선택된 제목의 상세정보를 표시한다.
2. 태블릿에서는 **TitlesFragment**와 **DetailsFragment**가 나오고, 스마트폰의 경우에는 첫화면(MainActivity)에서 **TitlesFragment**만 나오고, ListView에 나열된 항목이 선택되면 해당 항목에 대한 상세정보를 표시하는 **DetailsFragment**를 다른 화면(DetailsActivity)으로 표시한다.

<img src="figure/example-overview.png">

- **TitlesFragment**에서 표시된 ListView의 항목이 선택되었을 때, 해당 항목의 상세정보를 **DetailsFragment**에 어떻게 전달할 것인가?
	
	- **TitlesFragment**가 서로 다른 액티비티에서 재사용될 수 있도록 하기 위해서는 **TitlesFragment**가 **DetailsFragment**를 직접 접근하지 않도록 해야 합니다. 만약 **TitlesFragment**가 목록에서 선택된 내용을 **DetailsFragment**로 전달하기 위해서 **DetailsFragment**를 직접 접근하게 된다면 **TitlesFragment**는 **DetailsFragment**와 항상 같은 액티비티에 포함되어야 할 것입니다.

- 따라서, **TitlesFragment**의 목록에서 선택된 내용을 **DetailsFragment**로 전달할 때, 프래그먼트가 서로 종속적이지 않고 독립적으로 동작하게 하기 위해서는 아래 그림과 같이, 선택된 항목번호를 **MainActivity**로 보내고, **MainActivity**에서 다시 **DetailsFragment**로 보내는 방법을 취해야 합니다. 이에 대한 구체적인 방법은 이어지는 절에서 자세히 설명합니다.

	<img src="figure/communication.png">	 

- **예제 프로젝트 소스**
	https://github.com/kwanulee/Android/tree/master/examples/TabletPhoneFragment

---
### 3.1 TitlesFragment 구현
-  제목 목록을 ListView로 보여주는 TitlesFragment를 구현한다.
-  ListView의 선택된 항목 번호를 액티비티로 전달하는 방법을 이해한다.

	<img src="figure/titlesfragment.png" width=300>

#### [[연습4] - 리스트뷰를 포함한 TitlesFragment 정의하기](exercise4.html) 

---

#### 3.1.1 액티비티와의 통신 방법
- **TitlesFragment**의 ListView 항목이 선택되었을 때, 선택된 항목 번호를 **TitlesFragment**와 연결된 **MainActivity**에 전달하는 방법은?

	- **방법1** : 액티비티에서 직접 구현된 메소드 호출
		- **MainActivity**에 **void onTitleSelected(int i)** 메소드를 추가 정의
		- ListView의 항목이 선택되었을 때 선택된 항목 위치를 **MainActivity**의 **onTitleSelected()**메소드를 호출하면서 파라미터로 전달

		![](figure/activity-communication1.png)
	- **방법2** : 인터페이스를 구현한 액티비티를 인터페이스로 접근
		-  **void onTitleSelected(int i)** 메소드를 포함한 인터페이스 **OnTitleSelectedListener**를 **TitlesFragment** 내부에 정의
		-  **MainActivity**가 이 인터페이스를 구현하도록한 후, ListView의 항목이 선택되었을 때 선택된 항목 번호를 **MainActivity**가 구현한 인터페이스의 **onTitleSelected()**메소드를 호출하면서 파라미터로 전달
		
		![](figure/activity-communication2.png)

- 두 방법의 비교

	 | 장점 | 단점
	----|:----:|:----:
	방법1 | 간단한 구조 | 특정 Activity에 종속
	방법2 | 정의된 인터페이스를 구현하는 모든 Activity와 통신 가능 | 다소 복잡한 구조

	
#### [[연습5] - 프래그먼트-액티비티 통신 방법 (액티비티에서 직접 구현된 메소드 호출)](exercise5.html)

#### [[연습6] - 프래그먼트-액티비티 통신 방법 (인터페이스를 구현한 액티비티를 인터페이스로 접근)](exercise6.html)
		   

### 3.2 태블릿용 화면 레이아웃 추가
- 디바이스의 화면이 태블릿과 같이 큰 경우에 작은 크기의 화면과는 다른 화면 구성을 하고자 한다면, **res/layout\-large** 폴더에 MainActivity의 레이아웃 파일과 동일한 이름의 레이아웃 파일을 정의
	
	![](figure/layout-large.png)

#### [[연습7]- 태블릿용 화면 레이아웃 추가하기](exercise7.html) 

 
### 3.3 DetailsFragment 구현
- ListView에서 선택한 항목 번호의 상세정보를 보여주는 DetailsFragment를 구현한다.
	<img src="figure/communication2.png">	

- TitlesFragment에서 리스트뷰 항목 선택 번호를 MainActivity로 전달하였으므로, MainActivity에서는 전달받은 항목 선택 번호를 다시 DetailsFragment로 넘긴다. 


#### [[연습8] - 선택된 항목의 상세정보를 보여주는 DetailsFragment 추가하기](exercise8.html)


### 3.4 스마트폰용 **TitlesFragment**와 **DetailsFragment**을 스마트폰용에서 재사용
<img src="figure/communication3.png">

- 스마트폰의 경우에는 첫화면(**MainActivity**)에서 **TitlesFragment**만 나오고, ListView에 나열된 항목이 선택되면 해당 항목에 대한 상세정보를 표시하는 **DetailsFragment**를 다른 화면(**DetailsActivity**)으로 표시한다. 


1. **MainActivity**는 **DetailsActivity**에게 리스트뷰의 항목 선택 번호를 인텐트의 **Extra**를 통해 전달한다.
	- **onTitleSelected()** 메소드 수정
		- 파라미터로 전달받은 리스트뷰의 선택된 항목번호를 화면의 크기에 따라서 **DetailsFragment**와 **DetailsActivity** 중에 하나에게 전달될 수 있도록 수정
			- 화면크기를 판별하는 방법
				- [Configuration](https://developer.android.com/reference/android/content/res/Configuration.html) 클래스의 [isLayoutSizeAtLeast()](https://developer.android.com/reference/android/content/res/Configuration.html#isLayoutSizeAtLeast(int)) 메소드 이용

		```java
		public void onTitleSelected(int i) {
		        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
		            DetailsFragment detailsFragment = new DetailsFragment();
		            detailsFragment.setSelection(i);
		            getSupportFragmentManager().beginTransaction().replace(R.id.details, detailsFragment).commit();
		        } else { 	// 화면 크기가 작은 경우
		            Intent intent = new Intent(this, DetailsActivity.class);
		            intent.putExtra("index", i);
		            startActivity(intent);
		        }
		    }
		``` 

2. **DetailsActivity**는 **MainActivity**로부터 전달받은 리스트뷰의 항목 선택 번호를 새로이 생성된 **DetailsFragment**에 전달하고 새로이 생성된 **DetailsFragment** 객체를 기존 것과 교체

	- **activity_details.xml**
		- DetailsFragment를 포함할 컨테이너로 FrameLayout을 정의 (id는 *details*로 설정됨)
			
			```xml
			<?xml version="1.0" encoding="utf-8"?>
			<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:orientation="vertical">
			
			    <FrameLayout
			        android:id="@+id/details"
			        android:layout_width="match_parent"
			        android:layout_height="match_parent" />
			
			</LinearLayout>
			```

	- **DetailsActivity.java**

		```java
		public class DetailsActivity extends AppCompatActivity {
		
		    @Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_details);
		
		        DetailsFragment details = new DetailsFragment();
				// 액티비티로 전달된 인텐트의 Extra에서 이름이 "index"인 int형 값을 뽑아와서 새로이 생성된 DetailsFragment 객체에 전달		        
				details.setSelection(getIntent().getIntExtra("index",-1));
		        
		        // 새로이 생성된 DetailsFragment 객체를 기존 것과 교체
		        getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();
		    }
		}
		```

	