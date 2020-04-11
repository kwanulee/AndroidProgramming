<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**이전 학습**: 액티비티](activity.html)
# 인텐트

## 학습목표
- 인텐트의 기본 개념을 이해하고, 이를 활용하여 액티비티 컴포넌트 간에 메시지를 주고 받는 방법을 실습한다.


<a name="1"> </a>
## 1. 인텐트란?
- **인텐트 (Intent)**는 일종의 메시지 객체입니다. 
- 이것을 사용해  앱의 다른 **컴포넌트** (*액티비티* 혹은 *서비스*)를 시작시키거나, 다른 앱들에게 메시지를 전달할 수 있습니다.
	- 액티비티 시작하기
	- 서비스 시작하기
	- 브로드캐스트 전달하기

	<div class="polaroid">
	<img src="figure/intent-msg.png">
	</div>

---
<a name="2"> </a>
## 2. 인텐트 유형

### 2.1 명시적 인텐트 (Explicit Intent)
-  시작할 컴포넌트(예, 액티비티나 서비스)의 이름을 인텐트 객체에 설정하고 이를 **startActivity()** 또는 **startService()**에 넘긴다.
	
	<div class="polaroid">
		<img src="figure/explicit-intent.png">
	</div>

	- 보통 현재 앱 안에 있는 컴폰넌트를 시작시킬 때 사용


<a name="exercise2"> </a>
### [[연습2] - FirstActivity에서 SecondActivity 시작시키기](exercise2.html)


### 2.2 암시적 인텐트 (Implicit Intent)
- 시작할 컴포넌트의 이름을 지정하지 않고, 수행할 **작업**(예, 전화걸기, 지도보기 등)과 데이터를 인텐트 객체에 설정하고 이를 **startActivity()**에 넘긴다.
	- 예를 들어, 114 번호로 다이얼 작업을 수행할 수 있는 액티비티를 실행시키기 위해서는 다음과 같이 인텐트를 생성하고 이를 **startActivity()** 메소드에 전달하면 된다.

	<div class="polaroid">
		<img src="figure/implicit-intent.png">
	</div>
	
	- 보통 다른 앱 안에 있는 컴포넌트(예, 액티비티)를 구동시키는 데 사용된다.

- **Android System**은 등록된 모든 앱을 검색하여 해당 인텐트와 일치하는 **인텐트 필터**를 찾고, 일치된 인텐트 필터를 포함한 앱 컴포넌트를 시작시킴
	- **인텐트 필터**란 해당 컴포넌트가 수신하고자 하는 인텐트의 유형을 나타낸 것
	
	<div class="polaroid">
		<img src="https://developer.android.com/images/components/intent-filters_2x.png">
	</div>

### [[연습3] - FirstActivity에서 암시적 인텐트로 다른 액티비티 시작시키기](exercise3.html) 	



<a name="3"> </a>	 
## 3. 인텐트 객체
- [Intent](https://developer.android.com/reference/android/content/Intent.html) 객체에는 안드로이드 시스템이 어떤 컴포넌트를 시작할지 판별하는 데 필요한 정보가 담겨 있습니다.
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

- **엑스트라(extras)**: 요청한 작업을 수행하기 위해 필요한 추가 정보를 담고 있는 **키-값** 쌍의 데이터



## 4. 암시적 인텐트 수신 (인텐트 필터)
- 본인의 앱이 수신할 수 있는 암시적 인텐트가 어느 것인지 알리려면, 각 앱 컴포넌트에 대한 하나 이상의 인텐트 필터를 **\<intent-filter\>** 요소로 AndroidManifest 파일에 선언합니다.
- 각 인텐트 필터가 인텐트의 **작업**, **데이터** 및 **카테고리**를 근거로 어느 유형의 인텐트를 수신할 지를 결정합니다.
	- 암시적 인텐트를 수신하기 위해서는  **카테고리**를 *android.intent.category.DEFAULT*로 설정해야 합니다.

- 예제
	-  **ShareActivity** 액티비티는 텍스트 데이터를 바탕으로 **android.intent.action.SEND** 인텐트를 처리합니다.
	
		```xml
		<activity android:name="ShareActivity">
		    <intent-filter>
		        <action android:name="android.intent.action.SEND"/>
		        <category android:name="android.intent.category.DEFAULT"/>
		        <data android:mimeType="text/plain"/>
		    </intent-filter>
		</activity>
		```

### [[연습4] - SecondActivity가 ACTION\_DIAL 암시적 인텐트를 수신하도록 만들기](exercise4.html)

---
[**다음 학습**: 액티비티 간의 통신](activity-communication.html)