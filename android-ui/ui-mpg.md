<style>
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>
# 안드로이드 UI

[**이전**: 3. 레이아웃(Layout)](ui-layout.html)

## 4. Margin/Padding/Gravity
- 이번 장에서는 **Margins, Padding, Gravity**의 개념을 이해하고, 설정 방법을 통해서 View가 화면에 보이는 모양을 미세하게 제어할 수 있습니다.

### 4.1 Margins, Padding
- Margins: 뷰와 다른 뷰(컨테이너) 간의 간격
	- **layout\_margin**: 상하좌우로 동일한 마진 설정 시에 사용되는 속성
	- **layout\_marginLeft**,  **layout\_marginRight**, **layout\_marginTop**, **layout\_marginBottom**: 4 방향의 마진을 각기 다르게 설정할 때 사용되는 속성
- Padding: 뷰와 내용물 간의 간격
	- **padding**: 상하좌우로 동일한 패딩 설정 시에 사용되는 속성
	- **paddingLeft**, **paddingRight**, **paddingTop**, **paddingBottom**: 4방향의 마진을 각기 다르게 설정할 때 사용되는 속성
 	
	<div class="polaroid">
	   <img src="figure/margins_padding.png">
	   </div>
	   
- 예제: https://github.com/kwanulee/AndroidProgramming/blob/master/examples/UIBasic/app/src/main/res/layout/margin_padding_gravity.xml#L14-L15

	[margin\_padding\_gravity.xml](https://github.com/kwanulee/AndroidProgramming/blob/master/examples/UIBasic/app/src/main/res/layout/margin_padding_gravity.xml#L14-L15) | 실행화면
	--- | ---	
	<img src="figure/margin_padding_example_code.png"> | <img src="figure/margin_padding_example.png" width=200>
	 
	   
### 4.2 Gravity
- **gravity** 속성
	- 해당 뷰안의 내용물 위치에 대한 정렬 방식을 지정
- **layout_gravity** 속성
	- 부모 뷰안에서 해당 뷰의 정렬 방식 지정
- 가능한 값들
	- 부모 뷰안에서 해당 뷰의 정렬 방식 지정
		- BOTTOM – 부모 뷰에서 아래쪽에 위치시킴
		- CENTER – 부모 뷰의 중앙에 위치시킴
		- CENTER_HORIZONTAL – 부모 뷰의 수평 기준으로 중앙에 위치시킴
		- CENTER_VERTICAL – 부모 뷰의 수직 기준으로 중앙에 위치시킴
		- END – 부모 뷰에서 텍스트 방향의 끝(한글이나 영어의 경우는 오른쪽)에 위치시킴
		- LEFT – 부모 뷰에서 왼쪽에 위치시킴
		- RIGHT – 부모 뷰에서 오른쪽에 위치시킴
		- TOP – 부모 뷰에서 위쪽에 위치시킴
- 예제: https://github.com/kwanulee/AndroidProgramming/blob/master/examples/UIBasic/app/src/main/res/layout/margin_padding_gravity.xml#L24-L86

	<img src="figure/gravity_example.png" width=200>

```xml
   <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#ff0000"
        android:layout_weight="1">

        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="L | T"
            android:gravity="left|top"
            android:layout_gravity="left"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="C_H | T"
            android:gravity="center_horizontal|top"
            android:layout_gravity="center_horizontal"/>
        <Button 
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="R | T"
            android:gravity="right|top"
            android:layout_gravity="right"/>

        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="L | C_V"
            android:gravity="left|center_vertical"
            android:layout_gravity="center_vertical"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="Center"
            android:layout_gravity="center"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="R | C_V"
            android:gravity="right|center_vertical"
            android:layout_gravity="center_vertical|right"/>


        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="L | B"
            android:gravity="left|bottom"
            android:layout_gravity="bottom"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="C_H | B"
            android:gravity="center_horizontal|bottom"
            android:layout_gravity="center_horizontal|bottom"/>
        <Button
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:text="R | B"
            android:gravity="right|bottom"
            android:layout_gravity="right|bottom"/>
    </FrameLayout>
```
