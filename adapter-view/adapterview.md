<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**목차**: 어댑터 뷰](https://kwanulee.github.io/AndroidProgramming/#4-%EC%96%B4%EB%8C%91%ED%84%B0-%EB%B7%B0)
# 어댑터 뷰 개념

## 학습목표
- 어댑터뷰의 기본 개념 및 종류를 이해한다.



## 1. 어댑터뷰란?

- **어댑터뷰**는 여러 개의 **항목을 다양한 형식으로 나열하고 선택**할 수 있는 기능을 제공하는 뷰
	- *리스트뷰(ListView)*는 항목을 수직으로 나열시키는 방식이고, *그리드뷰(ListView)*는 항목을 격자 형태로 나열시키는 방식임.
- **어댑터 뷰**는 표시할 **항목 데이터를** 직접 관리하지 않고, **어댑터라는 객체로부터 공급받습니다**.

	<div class="polaroid">
	<img src="figure/adapterview_overview.png">
	</div>

## 2. 어댑터 (Adapter)
- 데이터를 관리하며 **데이터 원본과 어댑터뷰(ListView, GridView) 사이의 중계 역할**
- **어댑터뷰는 어떻게 데이터 항목을 표시할까요?**
	1. 어댑터뷰가 어댑터를 사용하기 위해서는 먼저 데이터원본이 어댑터에 설정되어야 하고, 어댑터뷰에는 어댑터가 설정되어야 합니다.
	2. 어댑터뷰는 항목을 표시하기 위해서 먼저 표시할 항목의 총 개수를 알 필요가 있을 것입니다. 이 때, 어댑터 뷰는 어댑터의 **getCount()**란 메소드를 통해 현재 어뎁터가 관리하는 데이터 항목의 총 개수를 반환합니다.
	3. 어댑터 뷰는 어댑터의 **getView()**란 메소드를 통해서 화면에 실제로 표시할 **항목뷰**를 얻고, 이를 화면에 표시합니다.
-  사용자가 어댑터뷰의 특정 위치의 항목을 선택하였을 때, 어댑터뷰는 선택된 *항목*, *항목ID*, *항목뷰*를 어댑터의 *getItem()*, *getItemId()*, *getView()* 메소드를 통해 얻어와서 이를 항목선택 이벤트 처리기에 넘겨줍니다.

	<div class="polaroid">
	<img src="figure/adapter.png">
	</div>

- 요약하면, 어댑터뷰는 어댑터에 정의된 인터페이스를 바탕으로 필요한 정보를 요청하여 항목뷰를 화면에 표시하거나 선택된 항목뷰를 처리합니다.

## 3. 어댑터 종류
- [BaseAdapter](https://developer.android.com/reference/android/widget/BaseAdapter)
	- 어댑터 클래스의 공통 구현, 사용자정의 어댑터 구현 시 사용
- [ArrayAdapter<T>](https://developer.android.com/reference/android/widget/ArrayAdapter?hl=en)
	- 객체 배열이나 리소스에 정의된 배열로부터 데이터를 공급받음
- [CursorAdapter](https://developer.android.com/reference/android/widget/CursorAdapter)
	- 데이터베이스로부터 데이터를 공급받음
- [SimpleAdapter](https://developer.android.com/reference/android/widget/SimpleAdapter)
	- 데이터를 Map(키, 값)의 리스트로 관리
	- 데이터를 XML 파일에 정의된 뷰에 대응시키는 어댑터


	<img src="figure/adapter_class.png" width=400>

---
[**다음 학습**: 리스트뷰](listview.html)