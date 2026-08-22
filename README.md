## 프로젝트 소개

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-70.7%25-brightgreen)
![Tech Debt Ratio](https://img.shields.io/badge/tech%20debt%20ratio-62-blue)
![API Speed](https://img.shields.io/badge/API%20response-up%20to%2028.6x%20faster-success)

코드 품질 개선과 성능 최적화, 테스트 커버리지 향상을 목표로 진행한 리팩토링 프로젝트입니다.

- **조회 성능**: N+1 문제 해결 및 반정규화로 쿼리 개수 최대 1,200배 감소, API 응답속도 최대 28.6배 개선
- **코드 품질**: SonarQube 기술부채지수 87 → 62로 개선
- **테스트**: Service 계층 통합 테스트로 코드 커버리지 70.7% 달성

---

## 1. 조회 성능 개선

### 1. N+1 문제 해결
batch fetching 방식에서 join fetch 방식으로 변경하여 N+1 문제를 해결하였습니다.

**실측 결과**

| 지표 | Before | After | 개선 수치 |
|------|--------|-------|-----------|
| 쿼리 개수 | 약 1,200건 | 1건 | 약 1,200배, **O(N)→O(1)** |
| 응답 속도 | 약 300ms | 약 100ms | **약 3배** |

### 2. 상품 평점/리뷰 개수 반정규화
매 조회마다 `AVG`/`COUNT`로 집계하던 상품 평점/리뷰 개수를 `Product`의 `avgRating`, `reviewCount` 캐시 컬럼으로 반정규화하고, 리뷰 생성/수정/삭제 시점에 갱신하도록 변경하였습니다.

**실측 결과** (리뷰 1만 건 기준)

| 지표 | Before | After | 개선 수치 |
|------|--------|-------|-----------|
| DB 조회 비용 (1만 건) | 21.2ms | 0.000125ms | 170,000배, **O(n)→O(1)** |
| DB 쓰기 비용 (1만 건 평균) | 0.083ms | 0.178ms | 2.16배 악화 |
| API 응답 시간 | 127.5ms | 4.46ms | **28.6배** |
| API 응답 크기 | 2.3MB | 412B | **5.7배** |

---

## 2. 코드 스타일 및 구조 개선
| 항목 | 내용 |
|------|------|
| SonarQube 플러그인 추가 | 코드 품질 분석 도입 |
|  | → 기술부채지수: **약 29% 감소** (87 → 62) |
| 불필요한 코드 및 주석 제거 | 사용되지 않는 코드 정리 |
| 코드 스타일 통일 | Google Java Style Guide 적용 |
| 클래스명 정리 | `Handler` → `Exception` 으로 일관성 부여 |
| 서비스 리팩토링 | 단일 책임 원칙(SRP) 적용 |
| 반환 타입 통일 | 서비스 계층 반환 타입을 DTO로 통일 |
| 중복 로직 추출 | 공통 기능 메서드화 |
| 코드 가독성 향상 | indent 1 이하 유지 |
| 메서드 길이 제한 | 메서드 20라인 이하로 정리 |
| 중복 DTO 통합 | 필드 구성이 동일했던 `ReasonDTO`/`ErrorReasonDTO`를 `ReasonDTO`로 통합 |

---

## 3. 테스트 코드 작성

#### 테스트 환경 구성
- 테스트용 프로파일 추가: test 환경 분리
- Jacoco 플러그인 적용: 코드 커버리지 수집
- SonarQube 연동: 테스트 및 품질 메트릭 분석

#### 테스트 작성 범위
| 계층 | 내용 |
|------|------|
| Controller | 일부 클래스 단위 테스트 작성 |
| Repository | 일부 클래스 단위 테스트 작성 |
| Service | 모든 클래스 통합 테스트 작성 |
|  | → 코드 커버리지 **70.7%** 달성 |
