# 위치 기반 서비스

---
## Location Service 개요
* Location APIs
    - Android framework location APIs ([android.location](https://developer.android.com/reference/android/location/package-summary.html))
    - [Google Play Services location APIs](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary) (**추천**)
* 이번 시간에 다룰 내용 (Google Play Service 이용)
    - [Google Play Services 설정](#1)
    - [마지막으로 알려진 위치 얻기](#3)
    - [주기적인 위치 업데이트 시작](#4)/[중단](#5) 하기
    - [주소 찾기](#6)

[출처: https://developer.android.com/training/location/index.html]

- **Location Service 예제 프로젝트 다운로드** [링크](https://minhaskamal.github.io/DownGit/#/home?url=https://github.com/kwanulee/Android/tree/master/examples/LocationService)

<a name="1"> </a>
---
##1. Google Play Services 설정
- **Google Play Services SDK** 다운로드 및 설치
    - **Android Studio**에서 **Tools>SDK Manager** 이용
        + **SDK Tools** 탭에서
        + **Google Play services** 선택 후 OK
     <img src="figures/google-play-service.png">
	     
- 프로젝트에 **Google Play Services** 라이브러리 추가
    1. build.gradle (Module:app) 파일 오픈
    2. 새로운 빌드 규칙 추가
    
		```java
		dependencies {
		      ...
		      implementation 'com.google.android.gms:play-services-location:16.0.0'
		}
		```
    3. 툴바에서 "Sync Project with Graddle File" 또는 "Sync Now" 클릭


[출처: https://developers.google.com/android/guides/setup]

### [연습1 - 예제 프로젝트 시작하기](example1.html)
	

<a name="3"> </a>
---
##2. 마지막으로 알려진 위치 얻기
* **Fused Location Provider**
    - **Google Play Services** 중에 location API
    - 디바이스의 배터리 사용을 최적화
    - 간단한 API 제공
        + 명시적인 위치 제공자 지정 없이, 상위 수준 요구사항 (높은 정확도, 저전력 등) 명세
        
###2.1 **Fused Location Provider** 클라이언트 객체 얻기
- 액티비티 onCreate()메소드에서 다음 코드를 통해 **Fused Location Provider** 클라이언트 객체를 얻어온다.

	```java
	private FusedLocationProviderClient mFusedLocationClient;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	        //...
	        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
	        //...
	}
	```

### 2.2 버튼 클릭시 마지막으로 알려진 위치 가져오기

```java
public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button get_last_location_button = (Button) findViewById(R.id.get_last_location_button);
        get_last_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();

            }
        });
    }

    private void getLastLocation() { 
    	// to be defined
    }
}
```

###2.3 마지막으로 알려진 위치 가져오기 구현하기 - getLastLocation()
- **Fused Location Provider**의 [getLastLocation()](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient#getLastLocation()) 메소드 이용
	1. 위치 접근에 필요한 권한 설정
	    
		* Android Manifest 파일에서 권한 설정
		
			```xml
			<manifest ...>
			     ...
			     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
			     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
			     ...
			</manifest>
			```

			- ACCESS\_COARSE\_LOCATION : 대략적 위치 접근 허용 (도시의 블럭 단위)
	    	- ACCESS\_FINE\_LOCATION : 정밀한 위치 접근 허용

	    * Android 6.0 (API level 23) 이상부터는 앱 실행 중에 사용하려는 권한(예, ACCESS\_FINE\_LOCATION)을 검사하고, 권한이 허용되지 않았으면 반드시 요청해야 함
    		
    		- 앱이 권한을 요청하면 시스템은 사용자에게 대화상자를 표시합니다. 사용자가 이에 응답하면 시스템은 앱의 [**onRequestPermissionsResult**](https://developer.android.com/reference/android/support/v4/app/ActivityCompat.OnRequestPermissionsResultCallback.html#onRequestPermissionsResult(int,%20java.lang.String[],%20int[]))() 메서드를 호출하여 사용자 응답에 전달합니다.
			
				```java	                      	
				    @Override
				    public void onRequestPermissionsResult(
				    										int requestCode, 
				    										String[] permissions, 
				    										int[] grantResults) {
				    										
				        switch (requestCode) {
				            case REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION: {
				                if (grantResults.length > 0 
				                	&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				                     /* 권한 획득 후 수행할 일: 
				                     	 getLastLocation(); 
				                     */
				                } else {
				                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
				                }
				            }
				        }
				    }
				```
	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L100-L112
	
			[참고자료: https://developer.android.com/training/permissions/requesting.html]
			
				
    2. [getLastLocation()](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient#getLastLocation()) 메소드를 호출하면 [Task](https://developers.google.com/android/reference/com/google/android/gms/tasks/Task) 객체를 반환 
    3. Task가 성공적으로 완료 후 호출되는 [OnSuccessListener](https://developers.google.com/android/reference/com/google/android/gms/tasks/OnSuccessListener) 등록 
    4. onSuccess() 메소드를 통해 디바이스에 마지막으로 알려진 위치를 [Location](https://developer.android.com/reference/android/location/Location.html) 객체 (위도, 경도, 정확도, 고도 값 등을 얻을 수 있음)로 받음
    	- 위치가 가용하지 않으면 null값이 반환될 수 있음

	```java
	    private void getLastLocation() {
	    	// 1. 위치 접근에 필요한 권한 검사 및 요청
	        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
	                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
	            ActivityCompat.requestPermissions(
	                    MainActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
	                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
	                    REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
	            );
	            return;
	        }
	        
	        // 2. Task<Location> 객체 반환
	        Task task = mFusedLocationClient.getLastLocation();        
	        
	        // 3. Task가 성공적으로 완료 후 호출되는 OnSuccessListener 등록
	        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
	            @Override
	            public void onSuccess(Location location) {
	                // 4. 마지막으로 알려진 위치(location 객체)를 얻음.
	                if (location != null) {
	                    mLastLocation = location;
	                    updateUI();
	                } else
	                    Toast.makeText(getApplicationContext(),
	                            "No location detected",
	                            Toast.LENGTH_SHORT)
	                            .show();
	            }
	        });
	    }
	
	
	    private void updateUI() {
	        double latitude = 0.0;
	        double longitude = 0.0;
	        float precision = 0.0f;
	
	
	        TextView latitudeTextView = (TextView) findViewById(R.id.latitude_text);
	        TextView longitudeTextView = (TextView) findViewById(R.id.longitude_text);
	        TextView precisionTextView = (TextView) findViewById(R.id.precision_text);
	
	
	        if (mLastLocation != null) {
	            latitude = mLastLocation.getLatitude();
	            longitude = mLastLocation.getLongitude();
	            precision = mLastLocation.getAccuracy();
	        }
	        latitudeTextView.setText("Latitude: " + latitude);
	        longitudeTextView.setText("Longitude: " + longitude);
	        precisionTextView.setText("Precision: " + precision);
	    }
	
	```

	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L123-L173


<a name="4"> </a>
---
##4. 주기적인 위치 업데이트 
### 4.1. 업데이트 **시작 버튼**/**중지 버튼** 동작 설정
- 업데이트 **시작 버튼** 클릭시, 
	- **위치 업데이트 시작**, **시작 버튼** 비활성화, **중지 버튼** 활성화
- 업데이트 **중지 버튼** 클릭시, 
	- **위치 업데이트 중지**, **중지 버튼** 비활성화, **시작 버튼** 활성화

```java
    private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
    private boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // ...
       
        mStartUpdatesButton = findViewById(R.id.start_updates_button);
        mStartUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mRequestingLocationUpdates) {
                        startLocationUpdates();
                        mRequestingLocationUpdates = true;
                        mStartUpdatesButton.setEnabled(false);
                        mStopUpdatesButton.setEnabled(true);

                }
            }
        });

        mStopUpdatesButton = findViewById(R.id.stop_updates_button);
        mStopUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRequestingLocationUpdates) {
                    stopLocationUpdates();
                    mRequestingLocationUpdates = false;
                    mStartUpdatesButton.setEnabled(true);
                    mStopUpdatesButton.setEnabled(false);
                }
            }
        });
    }

```

### 4.2 주기적인 위치 업데이트 시작
1. [위치 요청 설정](#4.1.1)
2. [위치 업데이트 콜백 정의](#4.1.2)
3. [위치 업데이트 요청](#4.1.3)

```java
    private LocationCallback mLocationCallback;
    final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;

    private void startLocationUpdates() {
        // 1. 위치 요청 (Location Request) 설정
        LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(10000);
        locRequest.setFastestInterval(5000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // 2. 위치 업데이트 콜백 정의
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLastLocation = locationResult.getLastLocation();
                updateUI();
            }
        };

        // 3. 위치 접근에 필요한 권한 검사
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                    REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return;
        }

        // 4. 위치 업데이트 요청
        mFusedLocationClient.requestLocationUpdates(locRequest,
                mLocationCallback,
                null /* Looper */);
    }

```

https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L175-L208

---
<a name="4.1.1"> </a>
#### 4.1.1 위치 요청 설정
* Fused Location Provider에 위치 요청을 위한 파라미터를 설정
    - setInterval(): 요구되는 위치 업데이트 간격 설정 (더 빠를 수도, 더 느릴 수도 있음)
    - setFastestInterval(): 위치 업데이트를 처리하는 가장 빠른 주기
    - setPriority(): 어떤 위치 소스를 사용할 지에 대한 힌트
        + PRIORITY\_BALANCED\_POWER\_ACCURACY: 대략적인 정밀도 (100m, 도시 블럭), 적은 전력 소비, Wifi와 기지국 위치 사용.
        + PRIORITY\_HIGH_ACCURACY: 가능한 가장 정밀한 위치 요청, GPS 사용
        + PRIORITY\_LOW_POWER: 도시 수준의 정밀도 (10 km), 훨씬 낮은 전원 소비
        + PRIORITY\_NO\_POWER : 전원 소비가 무시될 정도, 해당 앱이 위치 업데이트를 요청하지 않고, 다른 앱에 의해 요청된 위치를 수신.

	```java
	LocationRequest locRequest = new LocationRequest();
	
	locRequest.setInterval(10000); 		// 10초 보다 빠를 수도 느릴 수도 있음
	locRequest.setFastestInterval(5000);		// 5초 보다 더 빠를 순 없음
	locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	```

---
<a name="4.1.2"> </a>
#### 4.1.2 위치 업데이트 콜백 정의
* Fused Location Provider는 위치 정보가 가용할 때, LocationCallback의 [onLocationResult](https://developers.google.com/android/reference/com/google/android/gms/location/LocationCallback.html#onLocationResult(com.google.android.gms.location.LocationResult))([LocationResult](https://developers.google.com/android/reference/com/google/android/gms/location/LocationResult) result) 콜백 메소드를 호출
	* LocationResult 객체로부터 가장 최근 위치 및 [Location](https://developer.android.com/reference/android/location/Location.html) 객체 리스트를 얻을 수 있음. 	

	```java
	    mLocationCallback = new LocationCallback() {
	        @Override
	        public void onLocationResult(LocationResult locationResult) {
	            for (Location location : locationResult.getLocations()) {
	                // Update UI with location data
	                // ...
	            }
	        };
	    };
	```

---
<a name="4.1.3"> </a>
#### 4.1.3 위치 업데이트 요청
* Fused Location Provider의 **[requestLocationUpdates()](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient#requestLocationUpdates(com.google.android.gms.location.LocationRequest, com.google.android.gms.location.LocationCallback, android.os.Looper))** 메소드 호출
* [사전 조건] 
	* LocationRequest 객체 준비
	* LocationCallback 객체 준비
	* 위치 접근에 필요한 권한 검사

	```java
	    mFusedLocationClient.requestLocationUpdates(
	    			mLocationRequest,     // LocationRequest 객체
	            	mLocationCallback,	  // LocationCallback 객체
	            	null                  // looper
	            	);
	```

---
### 4.2 주기적인 위치 업데이트 중단하기
* 위치 업데이트 중단

	```java
	public void stopLocationUpdates() {
	   mFusedLocationClient.removeLocationUpdates(mLocationCallback);
	}
	```

<a name="6"> </a>
---
##6. 주소 찾기
###6.1 Geocoding

- 위치좌표를 주소로 변경하거나 주소를 위치좌표로 변경하는 것
* 특별한 Permission은 필요치 않음
* Geocoding 방법
    - Geocoder 객체 생성
        + Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        + 두번째 생성자 파라미터는 로케일 값으로, 특정 지역 및 언어 영역을 나타냄
    - [Geocoder](https://developer.android.com/reference/android/location/Geocoder.html) class의 아래 함수들을 이용
        + List &lt;[Address](https://developer.android.com/reference/android/location/Address.html)&gt; [**getFromLocation**](https://developer.android.com/reference/android/location/Geocoder.html#getFromLocation(double, double, int))(double latitude, double longitude, int maxResults);
        + List &lt;[Address](https://developer.android.com/reference/android/location/Address.html)&gt; [**getFromLocationName**](https://developer.android.com/reference/android/location/Geocoder.html#getFromLocationName(java.lang.String, int, double, double, double, double))(String locationName, int maxResults);


* [Address](https://developer.android.com/reference/android/location/Address.html) class:
    - getLatitude() : double type의 위도
    - getLongitude(): double type의 경도
    - getMaxAddressLineIndex() : 주소가 표시줄 수
    - getAddressLine(int index): 행별 주소 문자열
    - getLocality():  locality(서울특별시)
    - getFeatureName():  장소명(395-3)
    - getThoroughfare():  길이름(삼선동2가)
    - getCountryName(): 국가명(대한민국)
    - getPostalCode(): 우편번호 (136-792)

---
###6.2 위치로부터 주소 얻기 예제 (코드 발췌)

```java
    protected void onCreate(Bundle savedInstanceState) {
    	// ...
    	
        Button getAddressButton = (Button) findViewById(R.id.address_button);
        getAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });

    }
    
          
    private void getAddress() {
        TextView addressTextView = (TextView) findViewById(R.id.address_text);
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(),1);
            if (addresses.size() >0) {
                Address address = addresses.get(0);
                addressTextView.setText(String.format("\n[%s]\n[%s]\n[%s]\n[%s]",
                        address.getFeatureName(),
                        address.getThoroughfare(),
                        address.getLocality(),
                        address.getCountryName()
                ));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed in using Geocoder",e);
        }

    }
```
https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L91-L97
https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L215-L233

---
### 6.3 주소 이름으로부터 위치 얻기 예제

```java
try {
    Geocoder geocoder = new Geocoder(this, Locale.KOREA);
*   List<Address> addresses = geocoder.getFromLocationName(input,1);
    if (addresses.size() >0) {
        Address bestResult = (Address) addresses.get(0);

        mResultText.setText(String.format("[ %s , %s ]",
            bestResult.getLatitude(),
            bestResult.getLongitude()));
     }
} catch (IOException e) {
    Log.e(getClass().toString(),"Failed in using Geocoder.", e);
    return;
}
```