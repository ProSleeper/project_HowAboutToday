-- ���
DROP TABLE T_MEMBER CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_MEMBER (
  memberNum number(10) PRIMARY KEY,
  email VARCHAR2(40) NOT NULL,
  pw VARCHAR2(20) NOT NULL,
  nickName VARCHAR2(50) NOT NULL,
  tel VARCHAR2(20) NOT NULL,
  memberPoint NUMBER(10) NOT NULL,
  grade NUMBER(10) NOT NULL -- ȸ���� ���� �Ϲ�, �Ǹ���, ������
);

-- ����
DROP TABLE T_COUPON CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_COUPON (
  couponNum NUMBER(10) PRIMARY KEY,
  couponName VARCHAR2(50) NOT NULL,
  discountPrice NUMBER(10) NOT NULL, -- ���� ���� �ݾ�
  startDate DATE NOT NULL,
  endDate DATE NOT NULL,
  useRules NUMBER(10) NOT NULL  -- ��� ����(ex. 40,000�� �̻�)
);

-- ����� �������� N:M ���踦 ���� ���̺�
DROP TABLE T_MEMBER_VIEW_COUPON CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_MEMBER_VIEW_COUPON (
  memberNum number(10),
  couponNum number(10),
  CONSTRAINTS FK_23 FOREIGN KEY (memberNum) REFERENCES T_MEMBER(memberNum),
  CONSTRAINTS FK_24 FOREIGN KEY (couponNum) REFERENCES T_COUPON(couponNum)
);

-- ����
DROP TABLE T_REGION CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_REGION (
  regionNum number(10) PRIMARY KEY,
  regionName VARCHAR2(20) NOT NULL,
  parentNum NUMBER(10) NOT NULL
);

-- ���� ī�װ�
DROP TABLE T_ACCO_CATEGORY CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_ACCO_CATEGORY (
  categoryNum number(10) PRIMARY KEY,
  categoryName VARCHAR2(50) NOT NULL
);

-- ����
DROP TABLE T_ACCOMMODATION CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_ACCOMMODATION (
  accommodationNum number(10) PRIMARY KEY,
  accommodationName VARCHAR2(50) NOT NULL,
  tel VARCHAR2(50) NOT NULL,
  categoryNum number(10) NOT NULL,   --ī�װ� (����, ȣ��, ���)
  regionNum NUMBER(10) NOT NULL,       --����
  accommodationAddress VARCHAR2(200) NOT NULL,
  rating NUMBER(2,1) NOT NULL,
  ratingCount NUMBER(10) NOT NULL,
  latitude decimal(16,14) NOT NULL,	--����
  longitude decimal(17,14) NOT NULL,	--�浵
  lowPrice number(10) NOT NULL,         --������ ��� �����߿��� ���� ���� ����
  reservationRange number(10) NOT NULL,      --���� ���� ����(���� �� 60���� �����صθ� ���÷κ��� +60�ϱ����� ������ �����մϴ�.)
  CONSTRAINT FK_9 FOREIGN KEY (regionNum) REFERENCES T_REGION(regionNum),
  CONSTRAINT FK_25 FOREIGN KEY (categoryNum) REFERENCES T_ACCO_CATEGORY (categoryNum)
);

-- ���� �̹���
DROP TABLE T_ACCO_IMAGE CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_ACCO_IMAGE (
  accommodationNum number(10),
  originFileName VARCHAR2(100) NOT NULL,
  saveFileName VARCHAR2(100) NOT NULL,
  CONSTRAINT FK_3 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum)
);

-- ����
DROP TABLE T_ROOM CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_ROOM (
  accommodationNum number(10),
  roomNum number(10) PRIMARY KEY,
  roomName VARCHAR2(50) NOT NULL,
  defaultGuest number(10) NOT NULL,
  maxGuest number(10) NOT NULL,
  stayStartDate DATE NOT NULL,      -- ���� ������
  stayEndDate DATE NOT NULL,       -- ���� ������
  weekdayPrice number(10) NOT NULL,
  weekdayDiscount number(10) NOT NULL,
  weekendPrice number(10) NOT NULL,
  weekendDiscount number(10) NOT NULL,
  restStartTime DATE NOT NULL,
  restEndTime DATE NOT NULL,
  info VARCHAR2(1000) NOT NULL,
  CONSTRAINT FK_26 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum)
);
--������ ���� ������ ���̺�� �����µ� ���� �ʿ䰡 ���ٴ� �Ǵ��� �� �������̺�� ���ƽ��ϴ�.

-- ���� ����
DROP TABLE T_ROOM_RESERVATION_INFO CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_ROOM_RESERVATION_INFO (
  accommodationNum number(10),
  roomNum number(10),
  reservationDate DATE NOT NULL,
  CONSTRAINT FK_1 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION (accommodationNum),
  CONSTRAINT FK_2 FOREIGN KEY (roomNum) REFERENCES T_ROOM (roomNum)
);
--�� ���̺��� ��¥�� �������� �� �ش� ������ ������ ������ ���������� �Ǵ��մϴ�.
--���� ��� ����A�� ����A�� ����X�� 10�� 1���� �����ߴٸ� �� INFO�� ���ҹ�ȣ�� ���ǹ�ȣ�� ��¥�� ����˴ϴ�.
--�׸��� ���� B�� ����A�� ���������� 10�� 1���� �����ϸ� INFO�� ������ �о�ͼ� 10�� 1�� ���� X�� ���� �������� ���Դϴ�.


-- ����-�̹���
DROP TABLE T_ROOM_IMAGE CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_ROOM_IMAGE (
  accommodationNum number(10),
  roomNum number(10),
  originFileName VARCHAR2(100) NOT NULL,
  saveFileName VARCHAR2(100) NOT NULL,
  CONSTRAINT FK_18 FOREIGN KEY (roomNum) REFERENCES T_ROOM(roomNum),
  CONSTRAINT FK_19 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum)
);

-- ��ٱ��� ����
--DROP TABLE T_CART_ACCO CASCADE CONSTRAINTS PURGE;
--CREATE TABLE T_CART_ACCO (
--  memberNum number(10),
--  accommodationNum number(10),
--  CONSTRAINT FK_10 FOREIGN KEY T_CART_ACCO(memberNum) REFERENCES T_MEMBER(memberNum),
--  CONSTRAINT FK_11 FOREIGN KEY T_CART_ACCO(accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum)
--);

-- ��ٱ���
DROP TABLE T_CART CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_CART (
  memberNum number(10),
  accommodationNum number(10),
  roomNum number(10),
  startDate DATE NOT NULL,
  endDate DATE NOT NULL,
  price number(10) NOT NULL,
  CONSTRAINT FK_11 FOREIGN KEY (memberNum) REFERENCES T_MEMBER(memberNum),
  CONSTRAINT FK_12 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum),
  CONSTRAINT FK_13 FOREIGN KEY (roomNum) REFERENCES T_ROOM(roomNum)
);

-- ���� �ü�
DROP TABLE T_FACILITIES CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_FACILITIES (
  facilityNum number(10) PRIMARY KEY,
  facilityName VARCHAR2(50) NOT NULL
);

-- ���ϱ�
DROP TABLE T_WISHLIST CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_WISHLIST (
  memberNum number(10),
  accommodationNum number(10),
  CONSTRAINT FK_14 FOREIGN KEY (memberNum) REFERENCES T_MEMBER(memberNum),
  CONSTRAINT FK_15 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum)
);

-- �ֹ�
--DROP TABLE T_ORDER CASCADE CONSTRAINTS PURGE;
--CREATE TABLE T_ORDER (
--  memberNum number(10),
--  orderNum number(10) PRIMARY KEY,
--  orderDate DATE NOT NULL,
--  CONSTRAINT FK_16 FOREIGN KEY T_ORDER(memberNum) REFERENCES T_MEMBER(memberNum)
--);

--���� ����
DROP TABLE T_RESERVATION_STATE CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_RESERVATION_STATE (
  reservationStatus number(10) PRIMARY KEY,
  statusName VARCHAR2(20)
);
--���´� �Ƹ� 3������? ���̰����� �̷��� ���� ���� �� ���Ŀ� ���°� �߰� �Ǹ� �� ������ ���� �� ���Ƽ� ���̺�� ��������ϴ�.

-- ����
DROP TABLE T_RESERVATION CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_RESERVATION (
  memberNum number(10),
  accommodationNum number(10),
  reservationStatus number(10),
  orderNum number(10) NOT NULL,
  orderDate DATE NOT NULL,
  reservationNum number(10) PRIMARY KEY,
  roomName VARCHAR2(50) NOT NULL,
  useStartDate DATE NOT NULL,
  useEndDate DATE NOT NULL,
  vehicle VARCHAR2(10) NOT NULL,
  price number(10) NOT NULL,
  CONSTRAINT FK_16 FOREIGN KEY (memberNum) REFERENCES T_MEMBER(memberNum),
  CONSTRAINT FK_5 FOREIGN KEY (reservationStatus) REFERENCES T_RESERVATION_STATE(reservationStatus),
  CONSTRAINT FK_17 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum)
);

-- ���ҿ� ���ǽü����� N:M ���踦 ���� ���̺�
DROP TABLE T_ACCO_VIEW_FACILITIES CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_ACCO_VIEW_FACILITIES (
  accommodationNum number(10),
  facilities number(10),
  CONSTRAINT FK_21 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum),
  CONSTRAINT FK_22 FOREIGN KEY (facilities) REFERENCES T_FACILITIES(facilityNum)
);

-- ����
DROP TABLE T_REVIEW CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_REVIEW (
  memberNum number(10),
  accommodationNum number(10),
  roomNum number(10),
  reservationNum number(10),
  reviewNum number(10) PRIMARY KEY,
  rating NUMBER(2,1) NOT NULL,      --����
  reviewContent VARCHAR2(1000) NOT NULL,    --����.
  CONSTRAINT FK_4 FOREIGN KEY (reservationNum) REFERENCES T_RESERVATION(reservationNum),
  CONSTRAINT FK_6 FOREIGN KEY (memberNum) REFERENCES T_MEMBER(memberNum),
  CONSTRAINT FK_7 FOREIGN KEY (accommodationNum) REFERENCES T_ACCOMMODATION(accommodationNum),
  CONSTRAINT FK_8 FOREIGN KEY (roomNum) REFERENCES T_ROOM(roomNum)
);

-- �����̹���   
DROP TABLE T_REVIEW_IMAGE CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_REVIEW_IMAGE (
  reviewNum number(10),
  originFileName VARCHAR2(100) NOT NULL,
  saveFileName VARCHAR2(100) NOT NULL,
  CONSTRAINT FK_20 FOREIGN KEY (reviewNum) REFERENCES T_REVIEW(reviewNum)
);

--�Խ���
DROP TABLE T_BOARD CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_BOARD (
	boardNum NUMBER PRIMARY KEY ,
	categoryNum NUMBER NOT NULL,
	boardTitle VARCHAR2(200) NOT NULL,
	boardContent VARCHAR2(3096) NOT NULL,
	boardCreate DATE NOT NULL,
	boardModify DATE NOT NULL,
	boardHits NUMBER NOT NULL,
CONSTRAINT FK_27 FOREIGN KEY (categoryNum) REFERENCES T_BOARD_CATEGORY(categoryNum)
);

--�Խ��� ī�װ�
DROP TABLE T_BOARD_CATEGORY CASCADE CONSTRAINTS PURGE;
CREATE TABLE T_BOARD_CATEGORY(
	categoryNum NUMBER(10) PRIMARY KEY,
	categoryName VARCHAR2(20) NOT NULL,
	parentNum NUMBER(10) NOT NULL
);
