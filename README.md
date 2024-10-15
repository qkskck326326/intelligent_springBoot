
***본 문서는 작성 중인 문서입니다.***

# Intelliclass - Spring Boot (BackEnd)

Intelliclass는 머신러닝과 클라우드 기술을 활용한 웹 애플리케이션으로,프론트엔드와 DB와의 통신을 통해 데이터를 처리하고 제공하는 역할을 합니다.

## 요구 사항
- Java 17 이상
- Gradle 7 이상

## 설치 및 실행 방법
1. **레포지토리 클론**:
   ```bash
   git clone https://github.com/qkskck326326/intelligent_springBoot.git
   ```

2. **프로젝트 디렉토리 이동**:
   ```bash
   cd intelligent_springBoot
   ```

3. **빌드 및 종속성 설치**:
   ```bash
   ./gradlew build
   ```

4. **애플리케이션 실행**:
   ```bash
   ./gradlew bootRun
   ```

## 프로젝트 기능 및 개요

### 1. 백엔드 아키텍처
- **RESTful API**: 프론트엔드에서 요청이 오면, 그걸 받아서 필요한 데이터를 조회 및 가공해서 다시 프론트엔드로 응답을 보냅니다. 각 기능은 RESTful API로 구현되어 있습니다.
- **MVC 패턴**: Spring Boot의 MVC 패턴을 채택하여, 전체 구조를 깔끔하게 유지하고, 유지보수가 쉽게 만들었습니다.

### 2. 보안
- **Spring Security**: 사용자들의 특정 기능에 대한 접근에 대해, 인증과 권한 관리를 통해 보안을 유지합니다.
- **JJWT**: JWT(JSON Web Token)를 사용해서 로그인한 사용자의 상태를 관리하고, 필요한 권한을 부여합니다.

### 3. 인증 및 권한
- **사용자 인증**: 사용자가 로그인 시도를 하면, Spring Security를 통한 인증을 통해 올바른 접근이라면 JWT를 발급하여 지급합니다.
- **역할 기반 접근 제어**: 관리자와 일반 사용자로 역할을 구분하여, 각 역할에 맞는 기능만 사용할 수 있도록 제어합니다.

### 4. 데이터베이스
- **JPA와 Hibernate**: 데이터베이스와의 상호작용을 JPA와 Hibernate로 처리합니다. 이를 통해 복잡한 SQL문을 직접 작성하지 않아도 되고, 데이터 작업을 훨씬 쉽게 할 수 있습니다.
- **OracleDB**: OracleDB를 사용해서 데이터를 저장합니다.

### 5. 프론트앤드 와의 통신
- **회원 관리**: 회원 가입, 탈퇴, 정보 수정, 색인 기능을 제공합니다.
- **강의 관리**: 강의 등록, 수정, 삭제, 색인 기능을 제공합니다.
- **게시글 관리**: 게시물 등록, 수정, 삭제, 색인 기능을 제공합니다.
- **사용자 통계**: 일일 활성 사용자 수, 월별 사용자 증가량과 같은 통계 정보를 제공합니다.


