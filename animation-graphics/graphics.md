# 그래픽

---
## 그래픽 개요
* 사용 목적
    - 기존 뷰(버튼)를 수정 및 확장하여 새로운 룩앤필 구현
    - 다양한 그래픽(도형, 비트맵)를 캔버스에 그려서 비디오 게임 개발
* 그래픽 그리기
    - 점, 선, 사각형, 원, 텍스트, 비트맵 등
* 그래픽 애니메이션
    - 뷰(View) 클래스 이용
        + 메인 스레드만이 뷰에 접근 가능
        + 복잡한 그래픽 객체들을 메인스레드가 모두 처리하면 속도의 지연 발생 가능
    - 서피스뷰(SurfaceView) 클래스 이용
        + 뷰의 서브 클래스로, 시스템 뷰 계층을 관리하는 메인 스레드와 독립적으로, 다른 스레드에 의해 서피스 뷰를 자유롭게 이용할 수 있음
        + 3D 객체와 같이 복잡한 그래픽을 효율적으로 처리하기 위해 개발됨. 2D 그래픽도 적용가능

---
##1.  간단한 도형 그리기
1. 뷰(View)를 상속한 클래스 정의

	```java
	public class CustomView extends View {
		// Simple constructor to use when creating a view from code
	    public CustomView(Context context) {
	        super(context);
	    }	
	   
	   	// Constructor that is called when inflating a view from XML
	    public CustomView(Context context, @Nullable AttributeSet attrs) {
        	super(context, attrs);
    	}

	}
	```


2. 페인트 객체 초기화 (색상, 선 스타일, 선 굵기 등 설정)
	
	```java
	public class CustomView extends View {
	    private Paint paint;
	    
	    public CustomView(Context context) {
	        super(context);
	        init();
	    }
	    
	    public CustomView(Context context, @Nullable AttributeSet attrs)) {
	        super(context);
	        init();
	    }
	    	    
	    // 페인트 객체 초기화
	    public void init() {
	        paint = new Paint();
	        paint.setColor(Color.RED);
	    }
	```

3. View.onDraw() 메소드 내에서 도형 그리기

	```java
	public class CustomView extends View {
	    //...생략...
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        canvas.drawRect(100, 100, 200, 200, paint);
	    }
	}
	```

4. 새로 만든 뷰를 액티비티 레이아웃에 추가하기

	```xml
	<com.kwanwoo.android.graphicstest.CustomView 
		android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:id="@+id/customView"
	 />
	```

	[**주의**] XML 레이아웃에 추가하기 위해서는 생성자 중에서  **public CustomView(Context context, @Nullable AttributeSet attrs)**가  반드시 재정의 되어야 함
	

---
##2. 비트맵 이미지 그리기
* [BitmapFactory](https://developer.android.com/reference/android/graphics/BitmapFactory.html) 클래스
    - 비트맵 이미지를 만들기 위한 클래스 메소드 제공

	클래스 메소드   | 설명
	-------------|----------------------------------------------------------------
	public static Bitmap [decodeFile](https://developer.android.com/reference/android/graphics/BitmapFactory.html#decodeFile(java.lang.String))(String pathName)                          | pathName 위치의 이미지 파일 읽기
	public static Bitmap [decodeResource](https://developer.android.com/reference/android/graphics/BitmapFactory.html#decodeResource(android.content.res.Resources, int))(Resources res, int id)                | 리소스에 저장한 이미지 파일을 id를 통해 읽기
	public static Bitmap [decodeByteArray](https://developer.android.com/reference/android/graphics/BitmapFactory.html#decodeByteArray(byte[], int, int))(byte[] data, int offset, int length) | 바이트 배열로 되어 있는 비트맵 이미지를 읽기

* 비트맵 그리기 예제

	```java
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        //...
	        Bitmap bitmap= BitmapFactory.decodeResource( getResources(), R.drawable.ball );
	        canvas.drawBitmap(bitmap, 100,300,null);
	    }
	```    
	
	- [ball.png 다운로드](https://raw.githubusercontent.com/kwanu70/AndroidExamples/master/chap9/GraphicsTest/app/src/main/res/drawable/ball.png)
	- https://github.com/kwanulee/Android/blob/master/examples/GraphicsTest/app/src/main/java/com/kwanwoo/android/graphicstest/CustomView.java#L35-#36

---
##3. 그래픽 애니메이션
1. 화면에 터치 시에 볼 생성
2. 볼은 임의의 방향과 속도로 움직이다가 벽에 부딪히면 반사

	<img src="figures/graphic-animation.png" width=500>

<a name="3.1"></a>
###3.1 볼 클래스 정의
- Ball 클래스
	- 멤버 변수
		- **mX**, **mY**: 기준좌표
		- **mWidth**, **mHeight**: 넓이와 높이
		- **dx**, **dy**: 볼의 x축 혹은 y축  이동 방향 및 속도 값으로 **랜덤으로 설정**
		- **color**: 볼의 색상 값으로 **랜덤으로 설정**
	- 메소드
		- draw(Canvas canvas): canvas에 볼 도형 그리기
		- move(int width, int height): width와 height 범위 내에서 임의의 방향과 속도로 움직이다가 벽에 부딪히면 반사
	
	<img src="figures/ball.png" width=400/> 
		
	```java
	public class Ball {
	    final int RAD = 24;     // 볼의 반지름
	    int mX, mY;             // 볼의 기준좌표
	    int mWidth, mHeight;    // 볼의 넓이와 높이
	
	    int dx, dy;             // 볼의 x축 혹은 y축  이동 방향, 이동 속도 값
	    int color;
	
	    public Ball(int x, int y) {
	        mX = x;
	        mY = y;
	        
	        mWidth = mHeight = RAD * 2;           // 원의 반지름 (RAD)의 2배가 Ball의 폭과 높이
	
	        Random Rnd = new Random();
	        do {
	            dx = Rnd.nextInt(11) - 5;     // -5 ~ 5 중 난수로 x방향 속도 설정
	            dy = Rnd.nextInt(11) - 5;     // -5 ~ 5 중 난수로 y방향 속도 설정
	        } while (dx == 0 || dy == 0);        //  0은 제외
	
	        // 임의의 색상 설정
	        color = Color.rgb(Rnd.nextInt(256), Rnd.nextInt(256), Rnd.nextInt(256));
	
	    }
	
	    public void draw(Canvas canvas) {
	        Paint paint = new Paint();
	
	        // 바깥쪽은 흐릿하게 안쪽은 진하게 그려지는 원
	        for (int r = RAD, alpha = 1; r > 4; r--, alpha += 5) {
	            paint.setColor(Color.argb(alpha,
	                        Color.red(color),
	                        Color.green(color),
	                        Color.blue(color)));
	            canvas.drawCircle(mX + RAD, mY + RAD, r, paint);
	        }
	    }
	
	    void move(int width, int height) {
	        mX += dx;       // x 좌표값을 dx 만큼 증가
	        mY += dy;       // y 좌표값을 dy 만큼 증가
	
	        if (mX < 0 || mX > width - mWidth) {   // 화면 좌우 경계에 닿은 경우
	            dx *= -1;                       // 좌우 방향 반전
	        }
	        if (mY < 0 || mY > height - mHeight) {    // 화면 상하 경계에 닿은 경우
	            dy *= -1;                           // 상하 방향 반전
	
	        }
	    }
	}
	```
https://github.com/kwanulee/Android/blob/master/examples/GraphicsTest/app/src/main/java/com/kwanwoo/android/graphicstest/Ball.java

---
###3.2 View 터치시 볼 생성 및 그리기


1. View 터치 시
2. 터치 위치에 볼 생성하여 arBall 리스트에 추가
3. onDraw() 간접 호출
4. arBall 리스트에서 볼을 추출
5. 추출된 볼을 캔버스에 그림

```java
public class AnimatedView extends View {
    private ArrayList<Ball> arBall = new ArrayList<Ball>();
    ...
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int idx=0; idx<arBall.size(); idx++) {
            Ball B = arBall.get(idx);  // 4. arBall 리스트에서 볼을 추출
            B.draw(canvas);            // 5. 추출된 볼을 캔버스에 그림
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        // 1. View 터치 시
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 2. 터치 위치에 볼 생성하여 arBall 리스트에 추가
            arBall.add(new Ball((int)event.getX(), (int)event.getY()));
            invalidate();  // 3. onDraw() 간접 호출
            return true;
        }
        return false;
    }
}
```


https://github.com/kwanulee/Android/blob/master/examples/GraphicsTest/app/src/main/java/com/kwanwoo/android/graphicstest/AnimatedView.java#L34-L53

---
###3.3 볼 애니메이션

<img src="figures/animation-loop.png" width=400>

```java
public class AnimatedView extends View {
    private ArrayList<Ball> arBall = new ArrayList<Ball>();
    ...
    public void updateAnimation() {
        for (int idx=0; idx<arBall.size(); idx++) {
            Ball B = arBall.get(idx);    // 3. arBall 리스트에서 볼을 추출
            B.move(getWidth(),getHeight());  // 4. 추출된 볼을 애니메이트
        }
        invalidate();  // 5. 다시 onDraw() 간접 호출 (무한 반복)
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1. arBall 리스트에 있는 모든 Ball 객체 그리기
        for (int idx=0; idx<arBall.size(); idx++) {
            Ball B = arBall.get(idx);
            B.draw(canvas);
        }
        updateAnimation(); // 2. updateAnimation 메소드 호출
    }
```

https://github.com/kwanulee/Android/blob/master/examples/GraphicsTest/app/src/main/java/com/kwanwoo/android/graphicstest/AnimatedView.java#L55-L61


---
##4. **SurfaceView** 이용한 그래픽 애니메이션
* **SurfaceView**는 복잡하고 빠른 그래픽이 필요한 화면에서 주로 사용됨

	<img src="figures/surfaceview.png" width=200/>
	* **SurfaceView**는 쓰레드와 별도의 쓰레드에서 그림을 그릴 수 있는 전용 **Surface**(그래픽 버퍼)를 제공, 이를 안드로이드 UI 화면에 복사 (더블 버퍼링)
	* **SurfaceHolder**는 **Surface**의 캔버스 락(Lock) 제어나 **Surface**의 상태변화를 모니터링하기 위한 인터페이스를 제공

* **SurfaceView** 사용 패턴
	1. **SurfaceView**  클래스 선언  
	2. **SurfaceView** 사용 설정
	4. **SurfaceView**에 그래픽 그리기

###4.1 **SurfaceView** 클래스 정의	

* **SurfaceView**를 상속한 클래스 정의
* **Surface**의 상태변화를 모니터링하기 위한 **SurfaceHolder.Callback** 인터페이스 구현

```java
public class AnimatedSurfaceView extends SurfaceView  
								implements SurfaceHolder.Callback
    
    // Constructor that is called when inflating a view from XML
    public AnimatedSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    // SurfaceHolder.Callback 인터페이스 구현
    public void surfaceCreated(SurfaceHolder surfaceHolder) { /*...*/ }
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) { /*...*/ }
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) { /*...*/ }


}
```

###4.2 **SurfaceView** 사용 설정

* **SurfaceView** 제어를 위한 **SurfaceHolder** 객체를 [getHolder](https://developer.android.com/reference/android/view/SurfaceView.html#getHolder())() 메소드를 통해 얻어, 멤버 변수에 저장
* **SurfaceHolder** 객체에 **SurfaceHolder.Callback** 인터페이스의 구현을 등록

```java
public class AnimatedSurfaceView extends SurfaceView  
                                implements SurfaceHolder.Callback
                                
    SurfaceHolder holder;                            
                                           
    public AnimatedSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();  		// SurfaceHolder 객체 획득
        holder.addCallback(this);  	// SurfaceHolder 객체에 SurfaceHolder.Callback 인터페이스 구현을 등록
        //...
    }
    // ...
```



###4.3 **SurfaceView**에 그래픽 그리기
- 애플리케이션에서 **SurfaceView**에 그래픽을 그릴 때, 시스템이나 다른 애플리케이션에서 접근하는 것을 막기 위해 **SurfaceHolder** 객체이 다음 잠금기능을 사용
	- void [lockCanvas](https://developer.android.com/reference/android/view/SurfaceHolder.html#lockCanvas(android.graphics.Rect))(Rect)
	- void [unlockCanvasAndPost](https://developer.android.com/reference/android/view/SurfaceHolder.html#unlockCanvasAndPost(android.graphics.Canvas))(Canvas) 	

``` java
    private void draw() {
        while (true) {
            //1. 볼을 그릴 캔버스를 lockCanvas() 메소드를 통해 참조하고 캔버스에 락을 걸어 둠
            Canvas canvas  = holder.lockCanvas(null);

            //2. 앞에서 얻은 캔버스에 모든 볼을 이동시키고 그림
            canvas.drawColor(Color.WHITE); // cavas 지우기- 흰색으로 채우기
            synchronized (holder) {
                for (int idx=0; idx<arBall.size(); idx++) { 
                    Ball B = arBall.get(idx);
                    B.move(getWidth(),getHeight());
                    B.draw(canvas);
                }
            }

            // 3. 캔버스 객체에 락을 풀어줌
            holder.unlockCanvasAndPost(canvas);
        }
    }	
```
- Surface가 생성되었을 때, 별도의 쓰레도로 실행
	
```java
public class AnimatedSurfaceView  extends SurfaceView // SurfaceView 상속
                    implements SurfaceHolder.Callback {

    private Thread thread;
    //...

    public AnimatedSurfaceView(Context context, AttributeSet attrs) {
        //...

        thread = new Thread() {
            public void run() {
                draw();  // 볼 애니메이션 
            }
        };
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread.start();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            thread.join();
        } catch (InterruptedException e) {}
    }
```

**주의** 메인 스레드와 별도의 스레드에서 볼 애니메이션을 수행

https://github.com/kwanulee/Android/blob/master/examples/GraphicsTest/app/src/main/java/com/kwanwoo/android/graphicstest/AnimatedSurfaceView.java
