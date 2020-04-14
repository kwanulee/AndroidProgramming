<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
[**이전 학습**: 프래그먼트 개요](fragment-overview.html)
# 프래그먼트 사용하기

## 학습목표
- 프래그먼트를 정적 혹은 동적으로 액티비티에 추가하는 방법을 이해한다.


<a name="1"> </a>
## 1. 프래그먼트 정의하기
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

### [[연습1] - 프래그먼트 정의하기](exercise1.html)  

## 2. 액티비티에 프래그먼트 추가

### 2.1 액티비티의 레이아웃 파일에 정적 추가하기
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
	
### [[연습2] - 프래그먼트를 액티비티의 레이아웃 파일에 정적 추가하기](exercise2.html)

### 2.2 자바 코드에서 동적으로 프래그먼트 추가하기
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
### [[연습3]- 자바 코드에서 동적으로 프래그먼트 추가하기](exercise3.html)

---
[**다음 학습**: 프래그먼트 예제](fragment-example.html)
	