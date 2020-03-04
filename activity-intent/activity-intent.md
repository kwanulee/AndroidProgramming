<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
# 액티비티와 인텐트
---
## 학습목표
- 액티비티의 기본 개념 및 액티비티 수명주기를 이해한다.
- 인텐트의 기본 개념을 이해하고, 이를 활용하여 액티비티 컴포넌트 간에 메시지를 주고 받는 방법을 실습한다.

---
<a name="1"> </a>
## 1. 액티비티

<a name="1.1"> </a>
### 1.1 액티비티 개요?
- **액티비티(Activity)**는 사용자와 상호작용할 수 있는 화면을 제공하는 애플리케이션의 구성요소
	- 예: 전화 걸기, 사진 찍기, 이메일 보내기 또는 지도 보기 등
- 액티비티 마다 **창(Window)**이 하나씩 주어져 이곳에 **사용자 인터페이스를 끌어올(draw)** 수 있음
	- **사용자 인터페이스**는 **View 객체**들로 구성

	<div class="polaroid">
	<img src="figure/activity-overview.png">
	</div>

<a name="1.2"> </a>
### 1.2 	액티비티와 사용자 인터페이스 연결
- **setContentView()**를 이용하여 액티비티에 사용자인터페이스를 정의한 View를 설정

	```java
	public class FirstActivity extends AppCompatActivity {
		   @Override
		   protected void onCreate(Bundle savedInstanceState) {
		       super.onCreate(savedInstanceState);
		       setContentView(R.layout.activity_first);
		  }
	}
	```

	- [참고] **R.java**파일
		- 애플리케이션이 컴파일 될 때, 자동 생성됨
		- res 디렉토리에 있는 모든 리소스에 대한 리소스 ID 포함
			- 형식: R.[리소스유형].[리소스_이름]
			- 예
				- R.layout.activity\_first
				- R.string.hello
				- R.drawable.myimage

<a name="1.3"> </a>
### 1.3 	액티비티 등록
- 모든 Activity 컴포넌트는 **Android Manifest 파일에 등록**되어야 함

	```xml
	<manifest>
	    <application>
	        <activity android:name=".FirstActivity"
	            android:label="First Activity">
	        </activity>
	    </application>
	</manifest>

	```

- Android Manifest 역할
	- 애플리케이션 패키지 이름 (애플리케이션의 고유한 식별자 역할) 설정
	- **애플리케이션 구성요소들을 설명**
	- 이 애플리케이션과 상호작용하는 다른 애플리케이션이 가져야할 권한 설정
	- 애플리케이션에서 사용하는 라이브러리 설정
	- 애플리케이션이 필요로 하는 Android API의 최소 수준 설정

	- [추가자료] https://developer.android.com/guide/topics/manifest/manifest-intro.html

***
<a name="exercise1"></a>
#### [[연습1] - 세 개의 액티비티로 구성된 앱 만들기](exercise1.html)  



<a name="2"> </a>
## 2. 인텐트
<a name="2.1"> </a>
### 2.1 인텐트란?

- **인텐트 (Intent)**는 일종의 메시지 객체입니다.
- 이것을 사용해 **다른 앱 구성 요소**(액티비티, 서비스, 브로드캐스트 리시버)로
작업을 요청할 수 있습니다.
	- 액티비티 시작하기
	- 서비스 시작하기
	- 브로드캐스트 전달하기


	<div class="polaroid">
	<img src="figure/intent-msg.png">
	</div>

<a name="2.2"> </a>
### 2.2 인텐트 유형

<div class="polaroid">
	<img src="https://developer.android.com/images/components/intent-filters_2x.png">
</div>

- **명시적 인텐트 (Explicit Intent)**
	- 시작할 구성요소의 이름을 인텐트 객체에 설정하고 이를 **startActivity()** 또는 **startService()**에 넘긴다.
	- 보통 현재 앱 안에 있는 구성요소(예, 액티비티나 서비스)를 시작시킬 때 사용

- **암시적 인텐트 (Implicit Intent)**
	- 시작할 구성요소의 이름을 지정하지 않고 일반적인 **작업**(예, 전화걸기, 지도보기 등)을 인텐트 객체에 설정하고 이를 **startActivity()**에 넘긴다.
	- 안드로이드 시스템은 모든 앱을 검색하여 해당 인텐트와 일치하는 **인텐트 필터**를 찾고, 일치된 인텐트 필터를 포함한 앱 구성요소를 시작시킴
		- **인텐트 필터**란 해당 구성요소가 수신하고자 하는 인텐트의 유형을 나타낸 것

	- 보통 다른 앱 안에 있는 구성요소를 구동하는데 사용된다.

<a name="2.3"> </a>	 
### 2.3 인텐트 객체
- [Intent](https://developer.android.com/reference/android/content/Intent.html) 객체에는 안드로이드 시스템이 어떤 구성요소를 시작할지 판별하는 데 필요한 정보가 담겨 있습니다.
- **컴포넌트 이름(Component Name)**: 인텐트를 처리하는 타켓 컴포넌트 이름
	- 선택항목이므로, 컴포넌트 이름이 지정되지 않으면 암시적 인텐트를 의미함  
- **작업(Action)**: 수행되어야할 작업을 나타내는 문자열

	문자열 상수 | 작업 내용
	---------|---------
	[ACTION\_VIEW](https://developer.android.com/reference/android/content/Intent.html#ACTION_VIEW) | 사용자에게 데이터를 표시 (예, 사진 보기, 지도 보기)
	[ACTION\_SEND](https://developer.android.com/reference/android/content/Intent.html#ACTION_SEND) | 사용자가 다른 앱을 통해 공유할 수 있는 데이터를 보내기 (예, 메시지 전송, 이메일 전송)
	[ACTION\_MAIN](https://developer.android.com/reference/android/content/Intent.html#ACTION_MAIN) | 메인 진입점으로 시작
	[ACTION\_CALL](https://developer.android.com/reference/android/content/Intent.html#ACTION_CALL) | 전화통화 수행
	[ACTION\_DIAL](https://developer.android.com/reference/android/content/Intent.html#ACTION_DIAL) | 전화번호 누르는 화면을 표시
- **데이터(Data)**: 작업에 필요한 데이터
	- 데이터는 [**URI**](https://ko.wikipedia.org/wiki/%ED%86%B5%ED%95%A9_%EC%9E%90%EC%9B%90_%EC%8B%9D%EB%B3%84%EC%9E%90) 형식으로 설정
		- [참고] URI는 정보의 고유한 명칭으로 웹 주소를 나타내는 URL보다 더 상위의 개념
	- 예 (작업, 데이터)

		작업 | 데이터 | 설명
		---|----|---
		ACTION\_VIEW|http://www.google.com | http://www.google.com 웹페이지를 표시
		ACTION\_CALL|tel:114| 114번 전화번호로 전화연결 시작
		ACTION\_DIAL|tel:114| 114번 전화번호로 전화걸기 화면을 표시

- **카테고리(Category)**: 작업에 대한 추가적인 정보 제공

	카테코리 | 설명
	----|----
	CATEGORY\_BROWSABLE | 대상 액티비티가 웹브라우저에 의해 시작되어서 이미지와 같은 데이터를 표시할 수 있다.
	CATEGORY\_LAUNCHER | 이 액티비티가 작업의 최초 액티비티이며, 이것이 시스템의 애플리케이션 시작 관리자에 목록으로 게재되어 있다.

- **엑스트라(extra)**: 요청한 작업을 수행하기 위해 필요한 추가 정보를 담고 있는 **키-값** 쌍의 데이터

<a name="2.4"> </a>	 
### 2.4 명시적 인텐트로 다른 액티비티 시작하기
- 명시적 인텐트로 다른 액티비티를 시작시키기 위해서는 **startActivity()** 메소드를 호출하고, 시작하고자 하는 액티비티를 설명하는 **Intent** 객체를 전달하면 됩니다.

	<div class="polaroid">
		<img src="figure/explicit-intent.png">
	</div>


***
<a name="exercise2"> </a>
#### [[연습2] - FirstActivity에서 SecondActivity 시작시키기](exercise2.html)


<a name="2.4"> </a>	 
### 2.5 암시적 인텐트로 다른 액티비티 시작하기
- 암시적 인텐트로 다른 액티비티를 시작시키기 위해서는 인텐트 안에 작업과 데이터를 지정해야 한다. 예를 들어 114 번호로 다이얼 작업을 수행할 수 있는 액티비티를 실행시키기 위해서는 다음과 같이 인텐트를 생성하고 이를 **startActivity()** 메소드에 전달하면 된다.

	<div class="polaroid">
		<img src="figure/implicit-intent.png">
	</div>

#### [[연습3] - FirstActivity에서 암시적 인텐트로 다른 액티비티 시작시키기](exercise3.html) 	

### 2.6 암시적 인텐트 수신 (인텐트 필터)
- 본인의 앱이 수신할 수 있는 암시적 인텐트가 어느 것인지 알리려면, 각 앱 구성 요소에 대한 하나 이상의 인텐트 필터를 **\<intent-filter\>** 요소로 매니페스트 파일에 선언합니다.
- 각 인텐트 필터가 인텐트의 **작업**, **데이터** 및 **카테고리**를 근거로 어느 유형의 인텐트를 수신할 지를 결정한다.

#### [[연습4] - SecondActivity가 ACTION\_DIAL 암시적 인텐트를 수신하도록 만들기](exercise4.html)

---
<a name="3"> </a>
## 3 액티비티 간의 통신
- 인텐트는 액티비티 간에 데이터를 전달하는 도구로도 사용
- **Extras**를 활용하여 이름과 값의 쌍으로된 정보를 전달
  - Extras에 값을 저장하는 메소드
    	- Intent **putExtra**(String name, int value)
    	- Intent **putExtra**(String name, String value)
    	- Intent **putExtra**(String name, boolean value)
  - Extras에 저장된 값을 읽는 메소드
    	- int **getIntExtra**(String name, int defaultValue)
    	- String **getStringExtra**(String name)
    	- boolean **getBooleanExtra**(String name, boolean defaultValue)
- 액티비티로 인수를 전달하고 계산된 결과를 돌려받기 위해서는 다음 메소드를 사용
  - public void **startActivityForResult**(Intent *intent*, int *requestCode*)
    	- 위 메소드를 이용하여 리턴값을 돌려 주는 액티비티 호출
      		- *intent*: 시작 시킬 액티비티나 작업을 기술한 인텐트
      		- *requestCode*: 호출한 대상을 나타내는 식별자
  - protected void **onActivityResult**(int *requestCode*, int *resultCode*, Intent *data*)
    	- 호출된 액티비티가 종료되면 호출되는 메소드
      		- *requestCode*:  액티비티를 호출할 때 전달한 요청코드
      		- *resultCode*: 액티비티의 실행결과
     		- *data*: 호출된 액티비티의 수행결과가 Extras를 통해 전달된 인텐트 객체.
  - public void **setResult**(int *requestCode*, Intent *data*)
    	- 현재 액티비티를 띄운 액티비티로 응답을 보낼 때 사용
     		 - *resultCode*: 호출한 액티비티로 전달될 실행결과코드
      		- *data*: 액티비티의 수행결과를 Extras를 통해 전달할 인텐트 객체.

  <div class="polaroid">
    		<img src="figure/activity-communication.png">
  </div>

#### [[연습5] - FirstActivity에서 ThirdActivity로 데이터 전송 및 수신](exercise5.html)
#### [[연습6] - FirstActivity에서 시작시킨 ThirdActivity의 수행결과를 수신하기](exercise6.html)

---
<a name="4"> </a>
## 4 액티비티 수명주기

### 4.1 개요
<div class="polaroid">
    <img src="figure/activity-lifecycle.png">
</div>

- 한 액티비티의 수명은 **onCreate**() 호출과 **onDestroy**() 호출 사이에 있습니다. 
	- onCreate()에서 액티비티가 생성되어 레이아웃  설정 등을 수행한 다음에, onDestroy()가 호출되는 시점에 사용하고 있는 리소스를 모두 해제하고 생을 마감합니다.
- 액티비티의 화면이 눈에 보이게 되는  Visibility는 **onStart**()에서 **onStop**() 호출 사이에 있습니다.
	- 이 기간 중에는 사용자가 액티비티를 화면에서 보고 이와 상호작용할 수 있습니다. 예컨대 onStop()이 호출되어 새 액티비티가 시작되면 이 액티비티는 더 이상 표시되지 않게 됩니다. 시스템은 액티비티의 전체 수명 내내 onStart() 및 onStop()을 여러 번 호출할 수 있으며, 이때 액티비티는 사용자에게 표시되었다 숨겨지는 상태를 오가게 됩니다.

- 액티비티가 foreground에서 동작하는 구간은 **onResume**()에서 **onPause**() 호출 사이를 말합니다.
	- 이 기간 중에는 이 액티비티가 화면에서 다른 모든 액티비티 앞에 표시되며 사용자 입력도 여기에 집중됩니다. 액티비티는 전경에 나타났다 숨겨지는 전환을 자주 반복할 수 있습니다. 예를 들어 , 기기가 절전 모드에 들어가거나 대화 상자가 나타나면 onPause()가 호출됩니다.

### 4.2 수명주기 콜백 메소드
- 액티비티가 생성되면서 해제될 때까지 액비티티의 상태에 따라서 불려지는 메소드를 **라이프사이클 콜백 메소드**라 부르고, 애플리케이션 개발자는 필요한 경우에 해당 콜백 메소드를 재정의하여 필요한 일을 수행하게 할 수 있습니다.
- 주요 콜백 메소드
	- onCreate(): 반드시 구현해야 하는 메소드로서 액티비티가 생성되면서 호출됨
		- 가장 중요한 작업은 화면을 setContentView()를 호출하여 설정하는 것
	- onPause(): 사용자가 액티비티를 떠나고 있을 때, 호출됨
		- 액티비티가 완전히 소멸되는 것은 아니지만 사용자가 돌아오지 않을 수 있기 때문에 그 동안 이루어졌던 변경사항을 저장함

#### 4.2.1 액티비티 전환시 수명주기 콜백 메소드 호출 순서
1. FirstActivity에서 SecondActivity 시작 
	1. FirstActivity의 onPause()
	2. SecondActivity의 onCreate(), 	3. onStart(), onResume()
	4. FirstActivity의 onStop()
2. 단말기의 뒤로가기 버튼 누름
   1. SecondActivity의 onPause()
	2. FirstActivity의 onRestart(), onStart(), onResume()
	3. SecondActivity의 onStop(), onDestroy()

#### [[연습7] - 수명주기 콜백 메소드 호출 순서 살펴보기](exercise7.html)
