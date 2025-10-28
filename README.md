# 🌿 Leafy - 당신의 반려 식물 관리 앱

Leafy는 식물 관리의 어려움을 덜어주고, 식물과 더 즐겁게 교감할 수 있도록 돕는 웹 애플리케이션입니다. Plant.id API를 활용한 식물 식별, 카카오 소셜 로그인, 식물 관리 일정 추천 등의 기능을 제공합니다.

## ✨ 주요 기능

* 카카오 소셜 로그인을 통한 간편한 회원가입 및 로그인
* 사진 업로드를 통한 식물 정보 식별 (Plant.id API 연동)
* 나만의 식물 목록 관리 (등록, 수정, 삭제)
* 식물별 관리 일정 (물주기, 분갈이 등) 추천 및 알림
* 식물 건강 상태 기록 및 조회 (일지 작성)
* (추가 예정 기능)

## 🛠️ 기술 스택

**Backend:**
* Java 17
* Spring Boot 3.2.x (현재 버전 확인)
* Spring Security (JWT 예정)
* Spring Data JPA
* PostgreSQL
* Gradle
* Springdoc OpenAPI (Swagger UI)

**Frontend:**
* HTML, CSS, JavaScript (Vanilla JS)
* (추후 라이브러리/프레임워크 도입 가능)

**External APIs:**
* Plant.id API
* Kakao Login API

**Deployment:**
* AWS EC2 (예정)
* AWS RDS (예정)
* Nginx (예정)

## 🚀 로컬 개발 환경 설정 방법

### Backend

1.  **JDK 17 설치:** [Adoptium](https://adoptium.net/) 등에서 JDK 17을 설치합니다.
2.  **PostgreSQL 설치:** [Homebrew](https://brew.sh/) (`brew install postgresql`) 또는 [Postgres.app](https://postgresapp.com/)을 통해 설치하고 서버를 실행합니다.
3.  **데이터베이스 및 사용자 생성:**
    ```sql
    psql -U postgres
    CREATE DATABASE leafy_db;
    CREATE USER leafy_user WITH PASSWORD 'your_password'; -- 실제 비밀번호로 변경
    GRANT ALL PRIVILEGES ON DATABASE leafy_db TO leafy_user;
    \q
    ```
4.  **`pg_hba.conf` 설정:** `localhost` 접속 시 `md5` 인증을 사용하도록 설정하고 PostgreSQL 서버를 재시작합니다.
    * 설정 파일 위치: `psql -U postgres -c 'SHOW hba_file;'` 로 확인
    * `host all all 127.0.0.1/32 md5`
    * `host all all ::1/128 md5`
    * 재시작: `brew services restart postgresql`
5.  **`.env` 파일 생성:** 프로젝트 루트(`LEAFY/`)에 `.env` 파일을 만들고 아래 내용을 참고하여 실제 DB 연결 정보를 입력합니다. (이 파일은 `.gitignore`에 포함되어야 합니다!)
    ```bash
    # .env 예시
    DB_URL=jdbc:postgresql://localhost:5432/leafy_db
    DB_USERNAME=leafy_user
    DB_PASSWORD=your_actual_password
    DB_DRIVER=org.postgresql.Driver
    ```
6.  **백엔드 실행:** `LEAFY` 루트 폴더에서 터미널을 열고 다음 명령어들을 실행합니다.
    ```bash
    # 1. .env 파일 읽어서 환경 변수 설정 (Mac/Linux)
    export $(grep -v '^#' .env | xargs)
    # 2. backend 폴더로 이동
    cd backend
    # 3. Spring Boot 앱 실행
    ./gradlew bootRun
    ```

### Frontend

1.  **VSCode Live Server 설치:** VSCode 확장 프로그램 마켓플레이스에서 "Live Server"를 검색하여 설치합니다.
2.  **프론트엔드 실행:** `frontend` 폴더를 VSCode 새 창으로 열고, `index.html` 파일을 우클릭하여 "Open with Live Server"를 실행합니다.

## 📄 API 문서

* **Swagger UI:** 백엔드 서버 실행 후 `http://localhost:8081/swagger-ui/index.html` (또는 설정한 경로) 로 접속하여 확인할 수 있습니다.

## 👥 팀원 및 역할 (선택 사항)

* **[이름]:** [담당 역할]
* **[이름]:** [담당 역할]

---