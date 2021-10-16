# 키친포스

## 요구 사항

### 상품
- [x] 상품을 등록할 수 있다.
  - [x] 상품의 가격이 BigDecimal로 정상 입력되어야 한다.
  - [x] 상품의 가격은 0원 이상이어야 한다.
- [x] 상품 목록을 조회할 수 있다.

### 메뉴
- [x] 메뉴를 등록할 수 있다.
  - [x] 메뉴의 가격은 BigDeciaml로 정상 입력되어야 한다.
  - [x] 메뉴의 가격은 0원 이상이어야 한다.
  - [x] 메뉴 가격은 메뉴를 구성하는 총 상품의 가격 합보다 작아야 한다.
  - [x] 해당하는 메뉴 그룹이 존재해야 한다.
  - [x] 메뉴를 구성하는 상품이 모두 존재해야 한다.
- [x] 메뉴 목록을 조회할 수 있다.

### 메뉴 그룹
- [x] 메뉴 그룹을 등록할 수 있다.
- [x] 메뉴 그룹의 목록을 조회할 수 있다.

### 테이블
- [x] 주문 테이블을 등록할 수 있다.
- [x] 주문 테이블 목록을 조회할 수 있다.
- [x] 주문 테이블을 빈 테이블 상태를 수정할 수 있다.
  - [x] 존재하는 주문 테이블이어야 한다.
  - [x] 테이블이 단체 지정되어 있으면 불가하다.
  - [x] 조리 중이거나, 식사중인 경우 불가하다.
- [x] 주문 테이블의 손님 수를 수정한다.
  - [x] 손님 수는 0명 이상이어야 한다.
  - [x] 존재하는 주문 테이블이어야 한다.
  - [x] 빈 테이블 상태이면 안 된다.

## 테이블 단체 지정
- [x] 단체 지정을 등록할 수 있다.
  - [x] 단체 지정할 주문 테이블의 수가 2개 이상이어야 한다.
  - [x] 입력 받은 주문 테이블이 모두 저장된 테이블이어야 한다.
  - [x] 저장된 주문 테이블들 각각은 빈 테이블이어야 한다.
  - [x] 저장된 주문 테이블들 각각은 단체 지정되어 있으면 안된다.
  - [x] 현재 시간을 생성 시간에 추가해 준다.
  - [x] 주문 테이블을 순회하며, 각각에 단체 지정을 해주고, 주문을 할 수 있는 테이블 상태로 수정한다.

- [x] 단체 지정을 취소할 수 있다.
  - [x] 조리중이거나 식사중인 테이블이 포함되어 있으면 안 된다.
  - [x] 각 주문 테이블의 단체 지정을 해지하고, 주문을 할 수 있는 테이블 상태로 수정한다.
  
### 주문
- 주문을 등록할 수 있다.
  - 주문에 포함된 메뉴들이 존재해야 한다.(not Empty)
  - 저장된 메뉴와 요청한 메뉴들의 종류가 같은지 검증한다.
  - 존재하는 주문 테이블이어야 한다.
  - 주문 테이블은 빈 테이블 상태가 아니여야 한다.
  - 주문의 상태를 조리중으로 바꾼다.
  - 주문 시간을 추가로 저장한다.
  - 각각의 주문 항목을 저장한다.
- 주문 목록을 조회할 수 있다.
  - 각각의 주문에 대해 주문 항목을 함께 조회한다.
- 주문의 상태를 바꿀 수 있다.
  - 존재하는 주문이어야 한다.
  - 주문이 완료된 상태이면 불가하다.
  
##용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |


## ISSUE
- 다른 Service에 의존적인 테스트 코드를 작성해도 될까?
  TableService 중 TableGroup에 포함됬는지 여부에 따라 Empty상태 변경의 예외 조건이 있다. Service간의 의존이 명확하게 되지 않은 지금 시점에서는 우선 Dao를 호출하여 진행해 보도록 한다.
