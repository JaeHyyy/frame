# 데이터베이스 설정

## PostgreSQL 데이터베이스 생성

```sql
CREATE DATABASE saju;
```

## 테이블 생성

테이블은 이미 생성되어 있습니다. 참고용 스키마:

```sql
-- 사용자 테이블
CREATE TABLE users (
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   provider VARCHAR(10) NOT NULL,
   user_id VARCHAR(100) NOT NULL,
   nickname VARCHAR(50),
   CONSTRAINT uk_provider_user UNIQUE (provider, user_id)
);

-- 테이블 설명
COMMENT ON TABLE users IS '로그인 사용자 정보 테이블';
-- 컬럼 설명
COMMENT ON COLUMN users.id IS '사용자 식별자 (자동 증가 PK)';
COMMENT ON COLUMN users.provider IS '로그인 제공사 (KAKAO, GOOGLE, NAVER)';
COMMENT ON COLUMN users.user_id IS 'SNS 로그인 ID';
COMMENT ON COLUMN users.nickname IS '이름';
```
