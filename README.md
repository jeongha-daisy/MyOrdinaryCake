<p align="center"><img width="812" alt="image" src="https://github.com/user-attachments/assets/274124cc-157c-4756-9da2-22036dabd781" /></p>

# 🎂 My Ordinary Cake

> **주문 제작 케이크 예약 시스템**  
> 기획부터 개발, UI/UX 설계, 기능 구현까지 전반을 경험한 프로젝트입니다.

---

## 💭 프로젝트 개요

- **주요 기능**:
    - 회원 전용 주문서 작성
    - 사장님/회원 모드 분리
    - 매장별 옵션 관리 및 운영 시간 설정
    - Vue.js 기반 프론트, Spring Boot 백엔드
    - 이미지 업로드 및 예약 시간 제한 기능

- **개발 목적**:  
  기존의 주문 방식(카카오톡 오픈채팅방/분산된 예약 플랫폼)을 개선하고,  
  **고객과 점주 모두에게 더 편리한 예약 경험**을 제공하기 위해 기획

---

## 💭 시스템 흐름

### 유저 플로우
- 회원가입 → 로그인 → 주문서 작성 → 예약 관리
- 점주 모드일 경우 매장 등록 + 옵션 등록 + 예약 승인 관리

### 주요 페이지 구성 (Vue.js)
- 메인 페이지
- 사용 방법 안내
- 매장 목록 둘러보기
- 매장 상세 페이지
- 주문서 작성 페이지
- 예약 내역 확인 (회원 / 점주 각각)
- 가게 관리 페이지

---

## 💭 데이터베이스 설계 (ERD)

- 사용자(user): 회원/사장 모드 구분
- 매장(store): 1:1로 사장과 연결됨
- 주문(order): 회원(user)과 매장(store)에 각각 연결
- 옵션 (sheet/cream/size): 매장마다 등록된 케이크 옵션
- 매장 영업시간(store_business_hour): 요일별 운영시간
- ✅ 참조 무결성 보장을 위해 옵션은 삭제 대신 `is_available`로 관리


---

## 💭 기술 스택

| 분류 | 기술 |
|--|--|
| Frontend | Vue.js, Vuex, Figma|
| Backend | Spring Boot, JPA |
| DB | MySQL (MariaDB) |
| 배포 | Localhost 기준 (시연 중심) |

---

## 💭 주요 기능 설명

### 주문서 작성
- 가게 선택 → 옵션 선택 → 디자인 요청 작성 → 이미지 첨부
- 운영시간 외 시간 선택 시 알림 표시
- 주문 완료 후 "나의 예약"에서 확인 가능

### 사용자 역할 분리
- `user_mode`로 회원/사장 역할 구분
- 하나의 user 테이블에서 역할별 기능 분기

### 이미지 업로드
- `multipart/form-data` 형식으로 이미지 전송
- 서버 로컬 디렉토리에 저장 후 정적 리소스 경로 매핑

### 동적 라우팅
```js
router.push({ path: `/detail/${store_id}` });
```
- URL 파라미터 기반으로 매장 상세 페이지 접근 구현

---

## 💭 기능 확장 아이디어

- 리뷰 작성 기능 (픽업 후)
- 예약 시간 자동 마감 처리
- 주문 상태 변경 히스토리 관리
- 선입금/잔금 관리 기능


---

본 프로젝트는 사용자 경험 중심으로 기획/설계되었으며, 프론트부터 백엔드, DB 설계까지 직접 수행한 개인 프로젝트입니다.

