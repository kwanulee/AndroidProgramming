<style> 
div.polaroid {
  	width: 600px;
  	box-shadow: 0 10px 30px 0 rgba(0, 0, 0, 0.2), 0 16px 30px 0 rgba(0, 0, 0, 0.19);
  	text-align: center;
	margin-bottom: 0.5cm;
}
</style>

# Git/GitHub 실습

## 1. MyAndroidApp  생성 
1. 다음 요구사항을 만족시키는 안드로이드 프로젝트를 생성하고, 이를  API 28	에뮬레이터(AVD)에서 실행하시오.
	- Application 이름: *MyAndroidApp*
	- Package 이름: *com.hansung.android.myandroidapp*
	- Minimum SDK 버전: *API 14*
	- Activity 이름: 	StartActivity
	- StartActivity의 화면을 정의한 XML layout 파일 이름: start\_activity\_view.xml 

	
## 2. MyAndroidApp의 버전관리
1. GitHub 계정을 만드시오. (참고, [링크](create-github-account.html))
2. **GitHub Desktop**에서 다음의 설정을 확인하시오
	- 자신의 GitHub 계정으로  로그인 설정이 되었는가?
	- **Configure git** 에서 커밋시 사용할 이름과 이메일 주소가 자신의 정보인가?
3. **MyAndroidApp**를 위한 새로운 지역 **git** 저장소를 생성 하시오 (참고, [링크](git_github.html#1.1) )
4. **MyAndroidApp**의 일부 파일을 수정 후, 지역 **git** 저장소에 커밋하시오 (참고, [링크](git_github.html#1.2))
5. 지역 **git** 저장소를 **GitHub**의 원격 저장소와 동기화시키고, 결과를 확인하시오. (참고, [링크](git_github.html#1.4))

## 3. MyAndroidApp의 협업하기 
1. 자신이 만든 **GitHub** 저장소에 다른 사람을 Collaborator로 추가하고, 상대방에게도 본인이 만든  **GitHub** 저장소에 자신을 Collaborator로 추가하도록 하시오. (참고, [링크](git_github.html#2.1))
2. 상대방이 만든 **GitHub** 저장소를 복제하시오. (참고, [링크](git_github.html#2.2))
3. 복제된  **GitHub** 저장소 수정하고 동기화하시오. (참고, [링크](git_github.html#2.3))