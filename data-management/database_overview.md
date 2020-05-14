<style> 
div.polaroid {
  	width: 640px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

[**목차**: 데이터베이스](https://kwanulee.github.io/AndroidProgramming/#9-데이터베이스)

# 데이터베이스, SQL 기본 

## 1. 데이터베이스 개요
- **데이터베이스(DB: database)**는 통합하여 관리되는 **데이터의 집합체**를 의미합니다.
- **데이터베이스 종류**
	- 계층형 데이터베이스

		<img src="https://miro.medium.com/max/1400/1*Q09HnjOfqh4FvxdtU-pCOQ.png" width=400>
	- 네트워크 데이터베이스

		<img src="https://miro.medium.com/max/1400/1*ZuDEekCV_ScCSLSS8rT17g.png" width=400>
	- **관계형 데이터베이스**

		<img src="https://miro.medium.com/max/1400/1*Bg3cD5fuAW3aqhgglNAM5A.png" width=400>
	- NoSQL 데이터베이스 	

		<img src="https://d1x2i8adp1v94i.cloudfront.net/wp-content/uploads/2019/08/NoSQL.png" width=400>

	
	- 참고자료: https://medium.com/@rpolding/databases-evolution-and-change-29b8abe9df3e
- **데이터베이스 관리 시스템(DBMS: Database Management System)**은 데이터베이스를 관리하는 미들웨어를 의미합니다.

## 2. 관계형 데이터베이스
- *관계형 데이터베이스*는 현재 가장 많이 사용되고 있는 데이터베이스의 한 종류입니다.
-  *관계형 데이터베이스*는 하나 이상의 **테이블(table)**로 구성됩니다.
	-  **테이블**은 이름을 가지고 있으며, **행(row)**과 **열(column)** 그리고 거기에 대응하는 값

![](http://tcpschool.com/lectures/img_mysql_table.png)

-   *관계형 데이터베이스*는 위와 같이 구성된 테이블이 **다른 테이블들과 관계를 맺고 모여있는 집합체**로 이해

- **관계형 데이터베이스의 특징**
	1. 데이터의 분류, 정렬, 탐색 속도가 빠릅니다.
	2. 오랫동안 사용된 만큼 신뢰성이 높고, 어떤 상황에서도 데이터의 무결성을 보장해 줍니다.
	3. 기존에 작성된 스키마를 수정하기가 어렵습니다.

- **관계형 DBMS 예**
	- Oracle 
	- MS SQL
	- MySQL 
	- MariaDB
	- SQLite

- 참고자료: http://tcpschool.com/mysql/DB


## 3. SQLite 개요
* **SQLite** 
    - **SQL (Structured Query Language)** 문을 이용해 데이터를 조회하는 **관계형 데이터베이스 관리시스템**
    - 안정적 이며, 소규모 데이터베이스에 적합
    - 단순한 파일로 데이터를 저장 (별도의 서버 연결 및 권한 설정 불필요)
    - 복수 사용자는 지원되지 않음
    - 안드로이드의 일부로 포함됨

		<img src="figure/sqlite.png" width=400>

* SQLite DB Browser
	* SQLite과 호환가능한 데이터베이스 파일을 생성,  변경, 삭제할 수 있고, 데이터를 추가,삭제,변경, 조회할 수 있는 기능을 제공해 주는 비주얼 도구.  
	* https://sqlitebrowser.org/dl/ 접속하여 자신의 OS에 맞는 버전을 설치

	![](https://sqlitebrowser.org/images/screenshot.png)


<a name="2"></a>
## 4. SQL (Structured Query Language)
- **SQL**은 데이터베이스에서 데이터를 정의, 조작, 제어하기 위해 사용하는 언어입니다.

* 데이터 정의 언어 (Data Definition Language)
	- 데이터베이스나 테이블 등을 생성, 삭제하거나 그 구조를 변경하기 위한 명령어
    - <a href="https://ko.wikipedia.org/wiki/CREATE_(SQL)">CREATE</a>: 테이블 생성
    - <a href="https://ko.wikipedia.org/wiki/CREATE_(SQL)">DROP</a>: 테이블 삭제
    - <a href="https://ko.wikipedia.org/wiki/ALTER_(SQL)">ALTER</a>: 테이블 수정
* 데이터 조작 언어 (Data Manipulation Language)
	- 데이터베이스에 저장된 데이터를 처리하거나 조회, 검색하기 위한 명령어
    - <a href="https://ko.wikipedia.org/wiki/Insert_(SQL)">INSERT INTO</a>: 레코드(행) 추가
    - <a href="https://ko.wikipedia.org/wiki/Update_(SQL)">UPDATE ~ SET</a>: 레코드(행) 변경
    - <a href="https://ko.wikipedia.org/wiki/Delete_(SQL)">DELETE FROM</a>: 레코드(행) 삭제
    - <a href="https://ko.wikipedia.org/wiki/Select_(SQL)">SELECT ~ FROM ~ WHERE</a>: 레코드(행) 검색
* 데이터 제어 언어 (Data Control Language) 
	- 데이터베이스에 저장된 데이터를 관리하기 위하여 데이터의 보안성 및 무결성 등을 제어하기 위한 명령어

### 4.1 테이블 생성
- 문법

	```
	CREATE TABLE 테이블이름
	(
	    필드이름1 필드타입1,
	    필드이름2 필드타입2,
	    ...
	)
	```
	
- 예제

  ```
  CREATE TABLE "Users" (
      "Id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
      "Name"	TEXT NOT NULL,
      "Phone"	TEXT
  );
  ```

- **SQLite DB Browser 실습**
	1. 데이터베이스 생성
		- [**파일**]-[**새 데이터베이스**] 메뉴 선택 혹은 툴바의 [**새 데이터베이스**] 버튼 클릭
		- 파일이름(예, *sampleDB*)을 입력 후에 **저장** 버튼 클릭
	2. 데이블 정의
		- [**테이블 정의 변경**] 다이얼로그의 **테이블** 입력창에 *Users* 입력
		- **필드 추가** 버튼을 클릭하여 다음과 같이 테이블을 정의한 수에 **확인** 버튼 클릭

    
			![](figure/sqlite-table-definition.png)
	  

### 4.2 데이터 조작
#### 4.2.1 레코드 추가 
- 문법

    ```
    1. INSERT INTO 테이블이름(필드이름1, 필드이름2, 필드이름3, ...)
       VALUES (데이터값1, 데이터값2, 데이터값3, ...)

    2. INSERT INTO 테이블이름
       VALUES (데이터값1, 데이터값2, 데이터값3, ...)
    ```
    - 두 번째 문법처럼 필드의 이름을 생략할 수 있으며, 이 경우에는 데이터베이스의 스키마와 같은 순서대로 필드의 값이 자동 대입됩니다. 
      
- 예제

    ```
    INSERT INTO Users (Id, Name, Phone)
    VALUES (1, '홍길동', '010-1234-5678');
    ```
    ```
    INSERT INTO Users 
    VALUES (1, '홍길동', '010-1234-5678');
    ```
    
- **SQLite DB Browser 실습**
	1. **SQL 실행** 탭을 선택
	2. 다음 SQL문을 입력한 후에, **Execute SQL**  버튼을 클릭 

      ```
      INSERT INTO Users (Id, Name, Phone) VALUES (1, '홍길동', '010-1234-5678');
      INSERT INTO Users VALUES (2, '홍길동', '114');
      INSERT INTO Users VALUES (3,'이순신', '011-2772-8282');
      ```
	
    3. **데이터 보기** 탭을 선택하여 결과 확인

		![](figure/sqlite-data-result.png)

#### 4.2.2 레코드 변경
- 문법

    ```
    UPDATE 테이블이름
    SET 필드이름1=데이터값1, 필드이름2=데이터값2, ...
    WHERE 필드이름=데이터값
    ```
    
- 예제

    ```
    UPDATE Users
    SET Phone = '1234'
    WHERE Name = '홍길동';
    ```
 
- **SQLite DB Browser 실습**
	1. **SQL 실행** 탭을 선택
	2. **탭 열기** 아이콘을 클릭하여 새로운 탭을 열고, 다음 SQL문을 입력한 후에, **Execute SQL**  버튼을 클릭 

      ```
      UPDATE Users SET Phone = '1234' WHERE Name = '이순신';
      ```
	
    3. **데이터 보기** 탭을 선택하여 결과 확인

		![](figure/sqlite-data-result2.png)

 
#### 4.2.3 레코드 조회
- 문법

    ```
    SELECT 필드이름
    FROM 테이블이름
    [WHERE 조건]
    ```
    
- 예제

    ```
    SELECT *
    FROM Users;
    ```

    ```
    SELECT Name, Phone
    FROM Users;
    ```
    
    ```
    SELECT *
    FROM Users;
    WHERE Name='홍길동';
    ```

- **SQLite DB Browser 실습**
	1. **SQL 실행** 탭을 선택
	2. **탭 열기** 아이콘을 클릭하여 새로운 탭을 열고, 다음 SQL문을 입력한 후에, **Execute SQL**  버튼을 클릭하여 실행 결과를 확인해 본다. 

      ```
      SELECT * FROM Users;
      ```
	
  		![](figure/sqlite-data-result3.png)
  	3. 앞의 다른 SELECT 예제에 대해서도 실행해 본다.

#### 4.2.4 레코드 삭제
- 문법

    ```
    DELETE FROM 테이블이름
    WHERE 필드이름=데이터값
    ```
- 예제

    ```
    DELETE FROM Users
    WHERE Name = '홍길동';
    ```

    ```
    DELETE FROM Users;
    ```
- **SQLite DB Browser 실습**
	1. **SQL 실행** 탭을 선택
	2. **탭 열기** 아이콘을 클릭하여 새로운 탭을 열고, 다음 SQL문을 입력한 후에, **Execute SQL**  버튼을 클릭하여 실행 결과를 확인해 본다. 

      ```
      DELETE FROM Users WHERE Phone = '114';
      ```
      
	3. **데이터 보기** 탭을 선택하여 결과 확인
  	
		![](figure/sqlite-data-result4.png)

### 4.3 테이블 수정
-  문법
  
    ```
    ALTER TABLE 테이블이름 ADD 필드이름 필드타입
    ```
    ```
    ALTER TABLE 테이블이름 MODIFY COLUMN 필드이름 필드타입
    ```
    ```
    ALTER TABLE 테이블이름 DROP 필드이름
    ```

-  예제

    ```
    ALTER TABLE Users ADD Height INTEGER;
    ```
    
    ```
    ALTER TABLE Users MODIFY COLUMN Height REAL;
    ```
    
    ```
    ALTER TABLE Users DROP Height;
    ```

- **SQLite DB Browser 실습**
	1. **SQL 실행** 탭을 선택
	2. **탭 열기** 아이콘을 클릭하여 새로운 탭을 열고, 다음 SQL문을 입력한 후에, **Execute SQL**  버튼을 클릭하여 실행 결과를 확인해 본다. 

      ```
      ALTER TABLE Users ADD Height INTEGER;
      ```
      
	3. **데이터 보기** 탭을 선택하여 결과 확인
  	
		![](figure/sqlite-data-result5.png)


### 4.2 테이블 삭제
-  문법
  
    ```
    DROP DATABASE 데이터베이스이름
    ```
-  예제

    ```
    DROP TABLE Users;
    ```

## 참고자료
- SQLite로 가볍게 배우는 데이터베이스, https://wikidocs.net/book/1530

---

[**다음 학습**: SQLite 라이브러리 사용하기](sqlite.html)