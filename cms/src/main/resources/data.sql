-- 사이트 등록
INSERT INTO SITE (SITE_ID, SITE_NAME, DESCRIPTION, USE_YN, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY) VALUES (1, '관리자', '관리자 사이트', 'Y', NOW(), NOW(), 'admin', 'admin');
INSERT INTO SITE (SITE_ID, SITE_NAME, DESCRIPTION, USE_YN, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY) VALUES (2, '블로그', '블로그 사이트', 'Y', NOW(), NOW(), 'admin', 'admin');

-- 코드 등록
INSERT INTO CODE (CODE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, CODE, DESCRIPTION, LEVEL, NAME, ORD, USE_YN, PARENT_ID, TOP_ID)
VALUES (1, NOW(), NOW(), 'admin', 'admin', 'SYSTEM', '시스템 코드 그룹', 1, 'SYSTEM', 1, 'Y', NULL, NULL);
INSERT INTO CODE (CODE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, CODE, DESCRIPTION, LEVEL, NAME, ORD, USE_YN, PARENT_ID, TOP_ID)
VALUES (2, NOW(), NOW(), 'admin', 'admin', 'MENU_TYPE', '메뉴 타입', 2, 'MENU_TYPE', 1, 'Y', 1, 1);
INSERT INTO CODE (CODE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, CODE, DESCRIPTION, LEVEL, NAME, ORD, USE_YN, PARENT_ID, TOP_ID)
VALUES (3, NOW(), NOW(), 'admin', 'admin', 'MT_MENU', '메뉴', 3, '메뉴', 1, 'Y', 2, 1);
INSERT INTO CODE (CODE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, CODE, DESCRIPTION, LEVEL, NAME, ORD, USE_YN, PARENT_ID, TOP_ID)
VALUES (4, NOW(), NOW(), 'admin', 'admin', 'MT_BOARD', '게시판', 3, '게시판', 2, 'Y', 2, 1);
INSERT INTO CODE (CODE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, CODE, DESCRIPTION, LEVEL, NAME, ORD, USE_YN, PARENT_ID, TOP_ID)
VALUES (5, NOW(), NOW(), 'admin', 'admin', 'MT_CONTENTS', '컨텐츠', 3, '컨텐츠', 3, 'Y', 2, 1);
INSERT INTO CODE (CODE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, CODE, DESCRIPTION, LEVEL, NAME, ORD, USE_YN, PARENT_ID, TOP_ID)
VALUES (6, NOW(), NOW(), 'admin', 'admin', 'MT_LINK', '바로가기 링크', 3, '바로가기 링크', 4, 'Y', 2, 1);

-- 권한 등록
INSERT INTO AUTHORITY (AUTHORITY, AUTHORITY_NAME, AUTHORITY_TYPE, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('ROLE_ADMIN', '최고관리자', 'MANAGER', 'admin', 'admin', NOW(), NOW());
INSERT INTO AUTHORITY (AUTHORITY, AUTHORITY_NAME, AUTHORITY_TYPE, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('ROLE_MANAGER', '관리자', 'MANAGER', 'admin', 'admin', NOW(), NOW());
INSERT INTO AUTHORITY (AUTHORITY, AUTHORITY_NAME, AUTHORITY_TYPE, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('ROLE_USER_1', '권한1', 'MEMBER', 'admin', 'admin', NOW(), NOW());
INSERT INTO AUTHORITY (AUTHORITY, AUTHORITY_NAME, AUTHORITY_TYPE, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('ROLE_USER_2', '권한2', 'MEMBER', 'admin', 'admin', NOW(), NOW());
INSERT INTO AUTHORITY (AUTHORITY, AUTHORITY_NAME, AUTHORITY_TYPE, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('ROLE_USER_3', '권한3', 'MEMBER', 'admin', 'admin', NOW(), NOW());

-- 관리자 등록(비번 1234)
INSERT INTO MANAGER (USERNAME, PASSWORD, NAME, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE, AUTHORITY_ID) VALUES ('admin', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', '최고관리자', 'admin@naver.com', '010-1111-2222', 'ACTIVITY', 'admin', 'admin', NOW(), NOW(), 1);
INSERT INTO MANAGER (USERNAME, PASSWORD, NAME, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE, AUTHORITY_ID) VALUES ('manager', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', '관리자', 'manager@naver.com', '010-1234-5678', 'ACTIVITY', 'admin', 'admin', NOW(), NOW(), 2);

-- 관리자 권한 등록
INSERT INTO MANAGER_AUTHORITY (MANAGER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO MANAGER_AUTHORITY (MANAGER_ID, AUTHORITY_ID) VALUES (2, 2);

-- 테스트 회원 등록(비번 1234)
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원1', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member1@naver.com', '010-1111-2222', 'ACTIVITY', 'member1@naver.com', 'member1@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원2', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member2@naver.com', '010-1234-5678', 'ACTIVITY', 'member2@naver.com', 'member2@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원3', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member3@naver.com', '010-1234-5678', 'ACTIVITY', 'member3@naver.com', 'member3@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원4', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member4@naver.com', '010-1234-5678', 'ACTIVITY', 'member4@naver.com', 'member4@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원5', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member5@naver.com', '010-1234-5678', 'ACTIVITY', 'member5@naver.com', 'member5@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원6', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member6@naver.com', '010-1234-5678', 'ACTIVITY', 'member6@naver.com', 'member6@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원7', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member7@naver.com', '010-1234-5678', 'ACTIVITY', 'member7@naver.com', 'member7@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원8', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member8@naver.com', '010-1234-5678', 'ACTIVITY', 'member8@naver.com', 'member8@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원9', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member9@naver.com', '010-1234-5678', 'ACTIVITY', 'member9@naver.com', 'member9@naver.com', NOW(), NOW());
INSERT INTO MEMBER (NAME, PASSWORD, EMAIL, HP, STATUS, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('회원10', '$2a$10$is4cfHcYBgNPhcTMW2e8c.gd0EdqYShhhicWddNX28zfQUQkDJrq2', 'member10@naver.com', '010-1234-5678', 'ACTIVITY', 'member10@naver.com', 'member10@naver.com', NOW(), NOW());

-- 테스트 회원 권한 등록
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (1, 3);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (2, 4);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (3, 5);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (4, 3);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (5, 4);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (6, 5);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (7, 3);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (8, 4);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (9, 5);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (10, 3);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (10, 4);
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_ID) VALUES (10, 5);

-- 테스트 게시판 등록
INSERT INTO BOARD_MANAGER (NAME, DESCRIPTION, BOARD_TYPE, BOARD_USE_YN, COMMENT_USE_YN, CREATE_BY, LAST_MODIFIED_BY, CREATED_DATE, LAST_MODIFIED_DATE) VALUES ('공지사항', '공지사항게시판', 'NOTICE', 'Y', 'Y', 'admin', 'admin', NOW(), NOW());

-- 메뉴 그룹 등록
INSERT INTO MENU_GROUP (MENU_GROUP_ID, GROUP_NAME, SITE_ID, USE_YN, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY) VALUES (1, '관리자', 1, 'Y', NOW(), NOW(), 'admin', 'admin');
INSERT INTO MENU_GROUP (MENU_GROUP_ID, GROUP_NAME, SITE_ID, USE_YN, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY) VALUES (2, '블로그', 2, 'Y', NOW(), NOW(), 'admin', 'admin');

-- 테스트 메뉴 등록
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (1, 1, NOW(), NOW(), 'admin', 'admin', '시스템', 0, 'MT_MENU', '시스템', 9, 'Y', null, null, 'laptop');
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (2, 1, NOW(), NOW(), 'admin', 'admin', '코드설정', 1, 'MT_MENU', '코드설정', 1, 'Y', 1, 1, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (3, 1, NOW(), NOW(), 'admin', 'admin', '메뉴설정', 1, 'MT_MENU', '메뉴설정', 2, 'Y', 1, 1, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (100, 1, NOW(), NOW(), 'admin', 'admin', '서울소식', 0, 'MT_MENU', '서울소식', 1, 'Y', null, null, 'google-downasaur');
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (101, 1, NOW(), NOW(), 'admin', 'admin', '새소식', 1, 'MT_MENU', '새소식', 1, 'Y', 100, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (102, 1, NOW(), NOW(), 'admin', 'admin', '서울시 정책 뉴스', 1, 'MT_BOARD', '서울시 정책 뉴스', 2, 'Y', 100, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (103, 1, NOW(), NOW(), 'admin', 'admin', '보도해명자료', 1, 'MT_BOARD', '보도해명자료', 3, 'Y', 100, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (104, 1, NOW(), NOW(), 'admin', 'admin', '보도자료', 2, 'MT_BOARD', '보도자료', 1, 'Y', 103, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (105, 1, NOW(), NOW(), 'admin', 'admin', '해명자료', 2, 'MT_BOARD', '해명자료', 2, 'Y', 103, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (106, 1, NOW(), NOW(), 'admin', 'admin', '이벤트 신청', 2, 'MT_BOARD', '이벤트 신청', 3, 'Y', 101, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (107, 1, NOW(), NOW(), 'admin', 'admin', '분야별 새소식', 2, 'MT_BOARD', '분야별 새소식', 1, 'Y', 101, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (108, 1, NOW(), NOW(), 'admin', 'admin', '이달의 행사 및 축제', 2, 'MT_BOARD', '이달의 행사 및 축제', 2, 'Y', 101, 100, null);
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (109, 1, NOW(), NOW(), 'admin', 'admin', '시민참여', 0, 'MT_BOARD', '시민참여', 2, 'Y', null, null, 'github');
INSERT INTO MENU (MENU_ID, MENU_GROUP_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, DESCRIPTION, LEVEL, MENU_TYPE, NAME, ORD, USE_YN, PARENT_ID, TOP_ID, MATERIAL_ICON) VALUES (110, 1, NOW(), NOW(), 'admin', 'admin', '서울시민과의 대화', 1, 'MT_CONTENTS', '서울시민과의 대화', 1, 'Y', 109, 109, null);

-- 테스트 메뉴 권한 등록
INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 100);
INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 101);
INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 102);
-- INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
-- VALUES (NOW(), NOW(), 'admin', 'admin', 2, 103);
INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 104);
INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 105);
INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 106);
INSERT INTO MENU_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, MENU_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 107);


-- 테스트 시큐리티 리소스 등록
INSERT INTO SECURED_RESOURCE (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, ORD, RESOURCE_NAME, RESOURCE_PATTERN, RESOURCE_TYPE)
VALUES (NOW(), NOW(), 'admin', 'admin', 1, '코드', '/api/codes/**', 'URL');
INSERT INTO SECURED_RESOURCE (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, ORD, RESOURCE_NAME, RESOURCE_PATTERN, RESOURCE_TYPE)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, '코드', '/api/members/**', 'URL');

-- 테스트 시큐리티 리소스 권한 등록
INSERT INTO SECURED_RESOURCE_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, RESOURCE_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 1, 1);
INSERT INTO SECURED_RESOURCE_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, RESOURCE_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 1, 2);
INSERT INTO SECURED_RESOURCE_AUTHORITY (CREATED_DATE, LAST_MODIFIED_DATE, CREATE_BY, LAST_MODIFIED_BY, AUTHORITY_ID, RESOURCE_ID)
VALUES (NOW(), NOW(), 'admin', 'admin', 2, 1);
