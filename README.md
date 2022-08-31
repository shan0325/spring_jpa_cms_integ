## 프로젝트 구성
* Back End
  * SpringBoot
  * SpringDataJPA
  * SpringSecurity
  * SpringRestDocs
  * RestAPI
  * JWT
  * Gradle
  * H2
* Front End
  * NuxtJS
  * Vuetify
  * VuexStore

## NuxtJS + Spring + JWT 토큰 발급 과정

* Spring(BackEnd)에 JWT 토큰(accessToken, refreshToken)을 발급 받을 수 있는 로그인 및 accessToken 만료 시 재발급 가능 한 API가 개발되어 있습니다.


* JWT 토큰(accessToken, refreshToken)은 어디에 저장해야 안전한가?
  * 보안적인 이슈인 XSS, CSRF를 어떻게 막을지 생각해봐야함
  * localStorage 저장 
    * localStorage는 javascript로 접근이 쉽기 때문에 XSS 공격 및 보안에 취약함
  * cookie 저장 방식
    * cookie도 localStorage와 마찬가지로 javascript로 접근이 가능함. 하지만 secure, httpOnly를 설정하면 막을 수 있음
    * secure설정은 https접속에서만 가능하게 함
    * httpOnly를 설정하면 javascript 접근이 불가능하여 XSS취약점 공격에 대응할 수 있음
  * 결론적으로 refreshToken만 cookie에 저장하고 accessToken은 NuxtJS 내 로컬변수에 저장하여 CSRF 취약점을 방어함(완벽한 보안은 아님)<br>accessToken은 로컬변수에서 API 요청 시 Authorization 헤더에 넣어 API 요청<br>추가적으로 스프링에서는 html/javascript escape처리 및 외부에서 오는 URL 막는 조치 등 해야함


* 프론트(NuxtJS) JWT 토큰 발급 처리
  * 로그인
    * 아이디 및 비밀번호를 입력 후 로그인 시도 시 Spring에서 JWT(accessToken, refreshToken)을 내려줍니다.<br>Spring에서 응답 시 refreshToken을 secure, httpOnly 설정하여 쿠키를 생성 응답해 줍니다.
    * 프론트에서는 accessToken을 로컬변수에 저장하여 API 요청 시 Authorization 헤더에 넣어 사용합니다.
  * 페이지 새로고침 및 페이지 이동 시(토큰 재발급)
    * 페이지 새로고침을 하게 되면 로컬 변수에 있는 accessToken은 사라지므로 쿠키에 있는 refreshToken으로 재발급을 받아야 합니다.
    * NuxtJS middleware auth.js를 만들고 새로고침 혹은 페이지이동 시 체크를 합니다.
      * 새로고침은 무조건 재발급 대상으로 하였습니다.
      * 페이지 이동은 로컬 변수에 있는 accessToken을 확인하여 만료 시에 재발급 대상으로 합니다.
    * Spring으로 재발급 API 요청을 하면 Spring에서는 새로 생성된 accessToken, refreshToken을 응답해 줍니다.<br>프론트에서는 로그인과 동일하게 accessToken은 로컬 변수 refreshToken은 쿠키에 설정합니다.
  * axios API 요청 시(토큰 재발급)
    * 페이지 새로고침 및 페이지 이동 시 외에도 axios로 api 요청할 때 accessToken 만료를 체크해야 합니다.
    * 만약 만료된 accessToken을 Authorization 헤더에 넣고 api 요청을 하면 spring은 http상태코드 401, errorCode(EXPIRED_JWT)를 응답해 줍니다.
    * 프론트에서는 $axios.onError interceptor를 만들어 errorCode가 EXPIRED_JWT인 경우 Spring으로 토큰 재발급 요청을 합니다.
    * 새로 응답 받은 토큰으로 실패한 요청을 재 요청하여 다음을 진행합니다.