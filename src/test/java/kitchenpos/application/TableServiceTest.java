package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.application.dto.TableGroupRequest.OrderTableId;
import kitchenpos.application.dto.TableRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestWithProfiles
class TableServiceTest {
    private static final TableRequest CHANGE_EMPTY_TRUE = new TableRequest(null, true);
    private static final TableRequest CHANGE_GUESTS_TO_THREE = new TableRequest(3, null);
    private static final TableRequest TABLE_REQUEST_FOUR_NOT_EMPTY = new TableRequest(4, false);
    private static final TableRequest TABLE_REQUEST_FOUR_EMPTY = new TableRequest(4, true);

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 테이블 정상 생성")
    void create() {
        OrderTable saved = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("주문 테이블 목록 정상 조회")
    void list() {
        tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);

        assertThat(tableService.list()).hasSize(3);
    }

    @Test
    @DisplayName("주문 테이블 정상 Empty 상태 수정")
    void changeEmpty() {
        OrderTable orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);

        OrderTable changed = tableService.changeEmpty(orderTable.getId(), CHANGE_EMPTY_TRUE);
        assertThat(changed.isEmpty()).isEqualTo(CHANGE_EMPTY_TRUE.getEmpty());
    }

    @Test
    @DisplayName("주문 테이블 Empty 상태 수정 실패 :: 존재하지 않는 OrderTable")
    void changeEmptyNotExistingOrderTable() {
        Long notExistingOrderTableId = Long.MAX_VALUE;
        assertThatThrownBy(() -> tableService.changeEmpty(notExistingOrderTableId, CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 Empty 상태 수정 실패 :: 단체 지정된 테이블")
    void changeEmptyForTableWithTableGroupId() {
        OrderTable orderTable1 = tableService.create(TABLE_REQUEST_FOUR_EMPTY);
        OrderTable orderTable2 = tableService.create(TABLE_REQUEST_FOUR_EMPTY);

        tableGroupService.create(new TableGroupRequest(Arrays.asList(
                new OrderTableId(orderTable1.getId()),
                new OrderTableId(orderTable2.getId())
        )));
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "주문 테이블 Empty 상태 수정 실패 :: 주문 상태 {0}")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmptyForTableWithNotAllowedOrderStatus(OrderStatus orderStatus) {
        OrderTable orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        orderRepository.save(
                new Order(orderTable, orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), CHANGE_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 정상 수정")
    void changeNumberOfGuests() {
        OrderTable orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);

        OrderTable changed = tableService.changeNumberOfGuests(orderTable.getId(), CHANGE_GUESTS_TO_THREE);
        assertThat(changed.getNumberOfGuests()).isEqualTo(CHANGE_GUESTS_TO_THREE.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 음의 손님 수")
    void changeNumberOfGuestsWithNegativeValue() {
        OrderTable orderTable = tableService.create(TABLE_REQUEST_FOUR_NOT_EMPTY);
        TableRequest negativeNumberRequest = new TableRequest(-3, null);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), negativeNumberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 존재하지 않는 테이블 ID")
    void changeNumberOfGuestsNotExistingTableId() {
        Long notExistingTableId = Long.MAX_VALUE;

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistingTableId, CHANGE_GUESTS_TO_THREE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정 실패 :: 빈 테이블 상태")
    void changeNumberOfGuestsWithEmptyTableStatus() {
        OrderTable orderTable = tableService.create(new TableRequest(3, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), CHANGE_GUESTS_TO_THREE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        orderTableRepository.deleteAllInBatch();
        tableGroupRepository.deleteAllInBatch();
    }
}
