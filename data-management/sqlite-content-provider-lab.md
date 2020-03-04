# SQLite 데이터베이스와 연락처 제공자 실습

## 0. 실습 준비

- [SQLiteDBTest](https://github.com/kwanulee/Android/tree/master/examples/SQLiteDBTest) 프로젝트 다운로드 [**[click here]**](https://www.dropbox.com/sh/z3rjaspswqt5iyt/AAB3e5tV-b-UgwK3EwgGYFlwa?dl=0)
- [ContentResolverTest](https://github.com/kwanulee/Android/tree/master/examples/ContentResolverTest) 프로젝트 다운로드 [**[click here]**](https://www.dropbox.com/sh/z3rjaspswqt5iyt/AAB3e5tV-b-UgwK3EwgGYFlwa?dl=0)
- 연락처 (Contact) 앱을 통해 에뮬레이터나 디바이스에 연락처 3개 이상 추가

## 1. UserContract 계약 클래스를 정의
- 이름과 전화번호를 저장하기 위한 데이터베이스를 위한 데이터베이스, 테이블 및 컬럼의 이름을 String 상수로 정의
- 테이블을 생성 및 삭제하는 SQL 문을 String 상수로 정의

참고 소스: https://github.com/kwanulee/Android/blob/master/examples/SQLiteDBTest/app/src/main/java/com/example/kwanwoo/sqlitedbtest/UserContract.java

## 2. SQLiteOpenHelper 클래스의 서브 클래스를 정의한다.
- 데이터베이스 생성 및 업그래드를 위한 onCreate(), onUpgrade() 함수 재정의
- 생성된 데이터베이스의 테이블에 값을 저장,삭제,수정,조회 할 수 있는 메소드 정의

참고 소스: https://github.com/kwanulee/Android/blob/master/examples/SQLiteDBTest/app/src/main/java/com/example/kwanwoo/sqlitedbtest/DBHelper.java

## 3. MainActivity 클래스 주요 기능

### 3.1. 동기화 기능
- **연락처 제공자**에 저장된 이름과 전화번호를 읽어와서 이를 SQLite 데이터베이스에 저장한다. 
- 단, 동일한 레코드가 데이터베이스에 이미 존재하면 저장하지 않는다.

참고 소스: https://github.com/kwanulee/Android/tree/master/examples/ContentResolverTest


### 3.2. 레코드 추가 기능
- 화면 상단에 있는 입력창으로부터 추가할 레코드의 이름과 전화번호를 입력받아, 이를 데이터베이스에 추가한다.
- 추가 후에 데이터베이스에 저장된 레코드를 읽어와 리스뷰에 표시한다.

참고 소스: https://github.com/kwanulee/Android/blob/master/examples/SQLiteDBTest/app/src/main/java/com/example/kwanwoo/sqlitedbtest/MainActivity.java
	
### 3.3. 레코드 삭제 기능
- 리스트뷰에 표시된 레코드를 선택하면, 선택된 레코드의 ID, Name, Phone 정보를 화면 상단의 입력창에 표시한다.
- 삭제 버튼을 누러면 해당 레코드를 삭제하고 데이터베이스 내용을 리스뷰에 표시한다. 

참고 소스: https://github.com/kwanulee/Android/blob/master/examples/SQLiteDBTest/app/src/main/java/com/example/kwanwoo/sqlitedbtest/MainActivity.java
	
### 3.4. 레코드 수정 기능
- 리스트뷰에 표시된 레코드를 선택하면, 선택된 레코드의 ID, Name, Phone 정보를 화면 상단의 입력창에 표시한다.
- 입력창에 내용을 수정후에 수정 버튼을 누러면 해당 레코드를 수정하고 데이터베이스 내용을 리스뷰에 표시한다. 

참고 소스: https://github.com/kwanulee/Android/blob/master/examples/SQLiteDBTest/app/src/main/java/com/example/kwanwoo/sqlitedbtest/MainActivity.java

