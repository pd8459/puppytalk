# 🐶 PuppyTalk (퍼피톡)
> **AI 기술을 접목한 반려견 정보 공유 커뮤니티 & 쇼핑 플랫폼& 딥러닝(Deep Learning) 기반의 견종 분석/챗봇 기능과 **안정적인 커머스(결제/재고 관리) 시스템**을 결합한 올인원 서비스입니다.

<br>

## 📅 프로젝트 개요 (Overview)
* **진행 기간:** 2025.07.28 ~ 2026.02.01
* **개발 인원:** 개인 프로젝트 (Full Stack)
* **기획 의도:**
    1.  **소통:** 반려인 간의 정보 교류를 위한 커뮤니티 제공
    2.  **AI 기술:** PyTorch 모델을 활용한 견종 분석 및 양육 상담 챗봇
    3.  **커머스:** 반려동물 양육에 필요한 용품 판매 (결제/주문/재고 관리 시스템 구현)

<br>

## 🛠 기술 스택 (Tech Stack)

### Backend (Main & Commerce)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### AI Server (Microservice)
![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=for-the-badge&logo=fastapi&logoColor=white)
![PyTorch](https://img.shields.io/badge/PyTorch-EE4C2C?style=for-the-badge&logo=pytorch&logoColor=white)

### Frontend
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

### Payment & Tools
![PortOne](https://img.shields.io/badge/PortOne-FC5230?style=for-the-badge)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

<br>

## 🚀 핵심 기술 및 문제 해결 (Key Features & Challenges)

### 1️⃣ 보안 강화 (Security Enhancement) - ⭐ Critical
* **JWT 저장소 변경 (XSS 방지):**
    * 기존에는 구현의 편의성을 위해 JWT를 브라우저 `LocalStorage`에 저장했으나, 자바스크립트를 통한 **XSS(교차 사이트 스크립팅) 공격에 취약함**을 인지했습니다.
    * 이를 해결하기 위해 **로컬 스토리지 저장을 제거**하고, 보안 로직을 개선하여 토큰 탈취 위험을 최소화했습니다.
* **Spring Security:** URL별 접근 권한(User/Admin) 제어 및 BCrypt 비밀번호 암호화 적용.

### 2️⃣ 커머스 기능 및 동시성 제어 (E-Commerce)
* **결제 검증 (Payment Validation):** 프론트엔드에서의 결제 조작을 방지하기 위해, PortOne API를 통해 서버단에서 실제 결제 금액과 주문 금액을 대조하는 **2차 검증 로직**을 구현했습니다.
* **재고 관리 (Concurrency Control):** 주문이 몰릴 때 재고가 음수가 되는 **Race Condition(경쟁 상태)**을 방지하기 위해 JPA **`@Version` 기반의 낙관적 락(Optimistic Lock)**을 적용했습니다.
* **N+1 문제 해결:** 장바구니 조회 시 상품 정보 조회를 위해 발생하는 다량의 쿼리를 `JOIN FETCH`로 최적화했습니다.

### 3️⃣ AI 모델 서빙 (AI Integration)
* **FastAPI 연동:** Spring Boot(Main)와 FastAPI(AI) 간의 통신 아키텍처를 구축했습니다.
* **견종 분석:** PyTorch로 직접 훈련시킨 CNN 모델을 통해 업로드된 반려견 사진의 품종을 분석합니다.
* **지능형 챗봇:** SentenceTransformers를 활용하여 사용자의 질문 의도를 파악하고, 반려견 양육 관련 정보를 답변합니다.

## 🚀 대규모 트래픽 대비 성능 최적화 및 병목 분석

### 1️⃣ 실험 환경 및 조건
* **Framework:** Spring Boot 3.x
* **Web Server:** Embedded Tomcat
* **Database:** MySQL 8.0
* **Cache:** Redis 7.0 (Docker)
* **Load Test Tool:** nGrinder (Single Agent)
* **Infra:** MacBook M4 Pro (12-core CPU / 16GB RAM, Local 환경)
* **Test Scenario:** 댓글 10,000개 이상이 존재하는 단일 게시글 상세 조회 집중 부하 테스트
* **VUser Range:** 500 ~ 1,000
> ※ 본 테스트는 단일 로컬 머신 환경에서 수행되었으며, 네트워크 지연이 없는 Loopback 구조입니다.

---

### 2️⃣ 문제 정의 및 기준 지표 (Baseline)
게시글 상세 조회 시, 대량의 댓글 데이터를 매 요청마다 DB에서 조회하면서 심각한 I/O 병목이 발생했습니다. VUser 500 부하 테스트 결과, TPS는 200대에서 정체되었고 응답 시간은 2초를 초과했습니다.

| VUser | Peak TPS | Avg Response Time | Error Rate | 비고 |
| :--- | :--- | :--- | :--- | :--- |
| 500 | 200 | 2,100ms | 0% | DB I/O 병목 발생 |

---

### 3️⃣ 가설 설정 및 실험 설계
* **가설 1:** 병목의 근본 원인은 매 요청마다 발생하는 DB Full Scan일 것이다. 
* **가설 2:** 자주 조회되는 데이터(1페이지 댓글)를 In-Memory 캐시에 적재(Cache-Aside)하면 DB 부하를 차단할 수 있을 것이다.
* **가설 3:** 캐시 적용 후에도 트래픽이 증가하면 WAS(Tomcat)의 Thread Pool 설정이 새로운 병목 지점이 될 것이다.

---

### 4️⃣ 실험 ① Redis Cache-Aside 전략 도입
1페이지 댓글 데이터에 대해 Redis 캐시를 적용하여 DB 접근을 최소화했습니다.

| VUser | Peak TPS | Avg Response Time | Error Rate | 비고 |
| :--- | :--- | :--- | :--- | :--- |
| 500 | 938 | 793ms | 0% | TPS 약 4.6배 향상 |

**🎯 검증 결과**
* **캐시 적중(Cache Hit) 시 DB 쿼리 실행이 발생하지 않음을 Hibernate 로그를 통해 확인**했습니다. (Warm-up 이후 기준)
* 응답 시간이 2,100ms ➡️ 793ms로 약 62% 감소했습니다.

**⚠ 추가 트러블슈팅**
* `@Cacheable(sync=true)` 옵션 사용 시 동일 키에 대한 Lock 경합이 발생하여 이를 제거했습니다.
* `LocalDateTime` 및 `PageImpl` 객체 캐싱 시 발생한 역직렬화 에러를 커스텀 DTO와 `JavaTimeModule` 설정으로 해결했습니다.

---

### 5️⃣ 실험 ② Thread Pool 확장과 리소스 한계 검증
처리량을 더욱 극대화하기 위해 Tomcat Thread를 늘려(max-threads=500) 부하 테스트를 진행했습니다.

| VUser | Peak TPS | Avg Response Time | Error Rate | 비고 |
| :--- | :--- | :--- | :--- | :--- |
| 500 | 1,554 | 341ms | **52% (24.6K)** | 한계 초과 구간 |

**🔎 결과 분석**
* Thread 확장은 일시적으로 처리량(Peak TPS 1,554)을 크게 증가시켰으나, **OS 레벨의 네트워크 소켓 한계를 초과하고 Context Switching 비용이 급증하면서 Error Rate가 52%까지 치솟았습니다.**
* 즉, 하드웨어 스펙을 고려하지 않은 무작정 확장은 오히려 시스템 안정성을 무너뜨림을 데이터로 확인했습니다.

---

### 6️⃣ 최종 검증: 단일 노드 Sweet Spot 도출
안정성과 처리량의 최적 균형을 찾기 위해 Tomcat Thread(`max-threads=200`) 및 Redis Connection Pool(`max-active=200`)을 재조정하고, 부하를 더 높여 최종 검증을 수행했습니다.

> **📊 성능 개선 및 안정성 확보 시각화 지표**

<img width="1000" height="600" alt="Image" src="https://github.com/user-attachments/assets/cbc6cf75-69f6-4c0a-bf9a-cc837194b251" />
*(VUser 증가에 따른 TPS 처리량 추세 및 안정화 구간)*

<img width="1000" height="600" alt="Image" src="https://github.com/user-attachments/assets/bb115f5f-c56c-4da4-b1e1-c25c8e76a874" />
*(DB 직접 조회 vs Redis 캐싱 vs 최종 튜닝 후 응답속도 비교)*
<img width="1000" height="600" alt="Image" src="https://github.com/user-attachments/assets/03bf64fc-99bd-43e9-a25a-53b0406ae9bd" />*(Thread 500 오버프로비저닝 시 에러율 폭증 vs Thread 200 최적화 시 0% 방어)*

| VUser | Peak TPS | Avg Response Time | Error Rate | 비고 |
| :--- | :--- | :--- | :--- | :--- |
| **700** | **1,036** | **714ms** | **0%** | **최종 안정화 완료** |
| 800+ | 0 (Fail) | - | - | nGrinder Agent OOM 발생 |

**🔎 최종 결과 해석**
* 단일 노드 환경에서 **VUser 700 트래픽까지 에러율 0%로 방어**하는 데 성공했습니다.
* VUser 800 이상 부하 주입 시, 타겟 애플리케이션의 한계가 아닌 **부하를 생성하는 인프라(nGrinder Agent JVM)의 메모리 한계(OOM)가 먼저 도달**함을 확인했습니다.

---

### 7️⃣ 핵심 인사이트 및 확장 전략 (Scale-Out)
* 캐시 도입과 튜닝을 통해 **단일 서버 기준 TPS를 약 5배 이상 향상**시켰습니다.
* 단일 머신의 자원(수직 확장)에는 명확한 물리적 한계가 존재함을 실험적으로 증명했습니다. 
* **Production 환경 적용 시 확장 계획:**
  * 단일 노드의 한계를 극복하기 위해 Nginx 기반 Load Balancing 및 다중 WAS 인스턴스(Scale-Out) 구성을 고려합니다.
  * 단일 Redis 장애에 대비하여 Redis Cluster 또는 Replication 구조를 도입합니다.
  * 병목 지점을 실시간으로 추적하기 위해 Prometheus + Grafana 기반 모니터링 파이프라인을 구축할 계획입니다.

<br>

## 💾 ERD 설계 (Database Design)
<img width="5386" height="4648" alt="Image" src="https://github.com/user-attachments/assets/e25eb7f9-888d-48ae-81d1-7dc195f82205" />

### 📐 데이터베이스 설계 특징
1.  **반려동물 중심 설계 (Pet-Centric):** `Users`와 `Pet`을 1:N 관계로 설계하여, 한 명의 회원이 여러 반려견의 프로필(품종, 나이, 성별 등)을 개별적으로 관리할 수 있도록 유연성을 확보했습니다.
2.  **확장 가능한 채팅 시스템:** `Conversation`(방)과 `Message`(메시지), `Participants`(참여자)를 분리하여 1:1 채팅뿐만 아니라 추후 그룹 채팅으로도 확장이 가능한 정규화된 구조를 채택했습니다.
3.  **위치 기반 서비스 데이터:** `Animal_Hospital`, `Playground` 테이블에 위도(`latitude`)와 경도(`longitude`) 인덱스를 고려하여 설계, 추후 거리순 조회 및 지도 API 연동에 최적화했습니다.
4.  **도메인별 명확한 분리 (Separation of Concerns):**
    * **쇼핑몰 (Commerce):** `Product` ↔ `Review` (상품 후기) 관계를 맺고, `Orders` ↔ `Payment`로 결제 무결성을 보장하는 구조입니다.
    * **커뮤니티 (Community):** `Post` ↔ `Comment` (대댓글 지원) 구조로 설계하여, 쇼핑몰 기능과 독립적인 소통 공간을 확보했습니다.
    * 이 두 도메인을 `Users` 테이블을 중심으로 유기적으로 연결했습니다.

<br>

## 🚀 주요 기능 (Key Features)

### 1️⃣ 사용자 편의 및 소셜 기능
* **OAuth 2.0 소셜 로그인:** 카카오, 네이버, 구글 로그인을 지원하여 접근성을 높였습니다.
* **멀티 펫 프로필:** 다견 가정 사용자를 고려하여 여러 반려견의 정보를 등록하고 맞춤형 정보를 제공받을 수 있습니다.
* **실시간 채팅/알림:** `WebSocket` 등을 활용한 채팅 시스템 및 활동 알림(`Notification`) 기능을 데이터베이스 구조 단계에서부터 고려하여 설계했습니다.

### 2️⃣ 커머스 및 결제 (E-Commerce)
* **결제 시스템:** PortOne API 연동 및 `Payment` 테이블을 통한 결제 이력 검증 시스템 구축.
* **상품 및 리뷰:** 상품 구매자만 리뷰를 작성할 수 있도록 로직을 제어하여 신뢰도 확보.
<br>

## 🧪 API 명세 (API Specification)

**Base URL:** `http://localhost:8080`  
**API Docs:** Swagger UI를 통해 상세 명세 확인 및 테스트가 가능합니다.

### 1️⃣ 인증 및 유저 (Auth & User)
| Method | URI | Description |
| :---: | :--- | :--- |
| `POST` | `/api/users/signup` | 회원 가입 |
| `POST` | `/api/users/login` | 일반 로그인 |
| `POST` | `/api/logout` | 로그아웃 |
| `GET` | `/api/profile` | 내 프로필 조회 |
| `PATCH` | `/api/profile` | 프로필 정보 수정 |
| `PATCH` | `/api/profile/password` | 비밀번호 변경 |
| `GET` | `/api/users/public-profile/{username}` | 타 유저 공개 프로필 조회 |
| `GET` | `/api/user/{provider}/callback` | 소셜 로그인 (Kakao, Naver, Google) |

### 2️⃣ 반려동물 및 AI (Pet & AI)
| Method | URI | Description |
| :---: | :--- | :--- |
| `GET` | `/api/pets` | 내 반려동물 목록 조회 |
| `POST` | `/api/pets` | 반려동물 등록 |
| `PUT` | `/api/pets/{petId}` | 반려동물 정보 수정 |
| `POST` | `/api/pets/{petId}/image` | 반려동물 프로필 사진 업로드 |
| `POST` | `/api/ai/classify-dog` | **AI 견종 분석 요청** (To FastAPI) |

### 3️⃣ 커뮤니티 (Community)
| Method | URI | Description |
| :---: | :--- | :--- |
| `GET` | `/api/posts` | 게시글 목록 조회 (검색 포함) |
| `POST` | `/api/posts` | 게시글 작성 |
| `GET` | `/api/posts/{id}` | 게시글 상세 조회 |
| `POST` | `/api/posts/{postId}/like` | 게시글 좋아요 |
| `POST` | `/api/posts/{postId}/comments` | 댓글 작성 |
| `POST` | `/api/comments/{parentId}/replies` | 대댓글 작성 |
| `GET` | `/api/mypage/posts` | 내가 쓴 게시글 조회 |

### 4️⃣ 쇼핑몰 - 상품 (Shop & Product)
| Method | URI | Description |
| :---: | :--- | :--- |
| `GET` | `/api/shop/products` | 상품 전체 목록 조회 |
| `GET` | `/api/shop/products/{productId}` | 상품 상세 조회 |
| `GET` | `/api/shop/recommend/breed` | 견종별 상품 추천 |
| `POST` | `/api/reviews` | 상품 리뷰 작성 |
| `GET` | `/api/reviews/product/{productId}` | 상품별 리뷰 조회 |

### 5️⃣ 쇼핑몰 - 주문/결제 (Order & Payment)
| Method | URI | Description |
| :---: | :--- | :--- |
| `POST` | `/api/shop/cart` | 장바구니 담기 |
| `GET` | `/api/shop/cart` | 장바구니 목록 조회 |
| `POST` | `/api/shop/order` | 주문 생성 |
| `POST` | `/api/shop/payment/verify` | **결제 검증 (PortOne)** |
| `POST` | `/api/shop/order/{orderId}/cancel` | 주문 취소 |
| `POST` | `/api/shop/order/{orderId}/refund` | 환불 요청 |

### 6️⃣ 편의 기능 (Chat & Map & Notification)
| Method | URI | Description |
| :---: | :--- | :--- |
| `GET` | `/api/messages` | 채팅방 목록 조회 |
| `POST` | `/api/messages/{receiver}` | 1:1 쪽지/채팅 보내기 |
| `GET` | `/api/notifications` | 알림 목록 조회 |
| `GET` | `/api/hospitals` | 동물병원 검색/조회 |
| `GET` | `/api/playgrounds` | 반려견 놀이터 검색/조회 |

### 👑 관리자 (Admin)
| Method | URI | Description |
| :---: | :--- | :--- |
| `GET` | `/admin/api/users` | 전체 회원 관리 |
| `PATCH` | `/admin/api/users/{userId}/status` | 회원 상태 변경 (정지 등) |
| `GET` | `/admin/api/orders` | 전체 주문 관리 |
| `POST` | `/admin/orders/{orderId}/status` | 주문 상태 변경 |
| `GET` | `/admin/api/stats` | 매출/가입자 통계 |

<br>

## 📂 디렉토리 구조 (Directory Structure)
```bash
puppytalk
├── src
│   ├── main
│   │   ├── java/com/puppytalk
│   │   │   ├── domain       # Entity & Repository
│   │   │   ├── controller   # API Controller
│   │   │   ├── service      # Business Logic
│   │   │   └── global       # Config, Exception, Security
│   │   └── resources
├── ai-server                # Python FastAPI Server
│   ├── main.py
│   ├── model.pth            # PyTorch Model
│   └── requirements.txt
└── build.gradle
```

<br>

## ⚙️ 실행 방법 (How to Run)

### 1. Main Server (Spring Boot)
```bash
# 1. Clone Repository
git clone https://github.com/pd8459/puppytalk.git
cd puppytalk

# 2. Build (Windows: gradlew.bat / Mac: ./gradlew)
gradlew clean build

# 3. Run
java -jar build/libs/*.jar
```

### 2. AI Server (FastAPI)
```bash
# 1. Move to AI directory
cd ai-server

# 2. Install Dependencies
pip install -r requirements.txt

# 3. Run Server
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```
