<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
[**목차**: 안드로이드 UI](https://kwanulee.github.io/AndroidProgramming/#3-안드로이드-ui)

# 안드로이드 UI 기본

## 학습목표
- 안드로이드 UI 기본 개념 및 설계 방법을 이해한다.

## 1. UI (User Inteface) 설계 개요

- 안드로이드 앱의 UI를 구성하는 기본 단위는 뷰(View)이다.
	- 뷰는 크게 **위젯(Widget)**과 **레이아웃(Layout)**으로 구분된다.
		- **위젯**:  [View](https://developer.android.com/reference/android/view/View)의 서브클래스로서, 앱 화면을 구성하는 시각적인 모양을 지닌 UI 요소 (예, 버튼, 메뉴, 리스트 등)
		- **레이아웃(Layout)** : [ViewGroup](https://developer.android.com/reference/android/view/ViewGroup)의 서브클래스로서, 다른 뷰(위젯 혹은 레이아웃)를 포함하면서 이들을 정렬하는 기능을 지닌 UI 요소

    <div class="polaroid">
	   <img src="figure/ui-overview.png">
	    </div>

## 2. UI 설계 방법
### 2.1 AndroidStudio의 Layout Editor 이용
- 드래그 앤 드롭 방식의 WYSIWYG (what you see is what you get) 에디터
- 다양한 디바이스/안드로이드 버전에 대한 Preview
- XML 코드 자동 변환 및 동기화

	<div class="polaroid">
	<img src="figure/layout-editor.png">
	</div>

### 2.2 XML file을 직접 편집
- 필요한 XML 태그나 속성을 잘 모를 경우 불편
- Copy & paste를 이용한 편집이 효율적인 경우가 많음

	<div class="polaroid">
	<img src="figure/xml-editor.png">
	</div>
	
	- [activity\_main.xml](activity_main.xml.html)

### 2.3 자바 코드로 직접 UI 설계
- Java 코드를  이용하여 레이아웃과 뷰들을 생성하고, 뷰의 속성도 설정할 수 있다.
- AndroidStduio에서 UIBasic2 프로젝트를 생성한 후, MainActivity.java 파일을 아래 MainActivity.java 파일의 내용으로 변경한다.
	- [MainActivity.java](MainActivity.java.html)

---

[**다음 학습**: 위젯 (Widget)](ui-widget.html)