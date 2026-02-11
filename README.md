# 🐶 PuppyTalk (퍼피톡)
> **AI 기술을 접목한 반려견 정보 공유 커뮤니티 & 쇼핑 플랫폼& 딥러닝(Deep Learning) 기반의 견종 분석/챗봇 기능과 **안정적인 커머스(결제/재고 관리) 시스템**을 결합한 올인원 서비스입니다.

<br>

## 📅 프로젝트 개요 (Overview)
* **진행 기간:** 2025.07.28 ~ 2026.02.1
* **개발 인원:** 개인 프로젝트 (Full Stack)
* **기획 의도:**
    1.  **소통:** 반려인 간의 정보 교류를 위한 커뮤니티 제공
    2.  **AI 기술:** PyTorch 모델을 활용한 견종 분석 및 양육 상담 챗봇
    3.  **커머스:** 커뮤니티와 연계된 굿즈 및 용품 판매 (결제/주문 시스템 구현)

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

<br>

## 💾 ERD 설계 (Database Design)
<img width="5386" height="4648" alt="Image" src="https://github.com/user-attachments/assets/e25eb7f9-888d-48ae-81d1-7dc195f82205" />

### 📐 데이터베이스 설계 특징
1.  **반려동물 중심 설계 (Pet-Centric):** `Users`와 `Pet`을 1:N 관계로 설계하여, 한 명의 회원이 여러 반려견의 프로필(품종, 나이, 성별 등)을 개별적으로 관리할 수 있도록 유연성을 확보했습니다.
2.  **확장 가능한 채팅 시스템:** `Conversation`(방)과 `Message`(메시지), `Participants`(참여자)를 분리하여 1:1 채팅뿐만 아니라 추후 그룹 채팅으로도 확장이 가능한 정규화된 구조를 채택했습니다.
3.  **위치 기반 서비스 데이터:** `Animal_Hospital`, `Playground` 테이블에 위도(`latitude`)와 경도(`longitude`) 인덱스를 고려하여 설계, 추후 거리순 조회 및 지도 API 연동에 최적화했습니다.
4.  **커머스와 커뮤니티의 분리 및 통합:**
    * **쇼핑:** `Product` - `Orders` - `Payment` (결제 검증 및 재고 관리 최적화)
    * **커뮤니티:** `Post` - `Comment` (계층형 구조) - `Review` (상품 연동)
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

<br>

## ⚙️ 실행 방법 (How to Run)

### 1. Main Server (Spring Boot)
```bash
# 1. Clone Repository
git clone [https://github.com/pd8459/puppytalk.git](https://github.com/pd8459/puppytalk.git)
cd puppytalk

# 2. Build (Windows: gradlew.bat / Mac: ./gradlew)
gradlew clean build

# 3. Run
java -jar build/libs/*.jar

cd ai-server

# 1. Install Dependencies
pip install -r requirements.txt

# 2. Run Server
uvicorn main:app --reload --host 0.0.0.0 --port 8000
