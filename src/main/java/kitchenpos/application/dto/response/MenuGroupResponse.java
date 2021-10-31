package kitchenpos.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }

    public static List<MenuGroupResponse> listOf(List<MenuGroup> menuGroups) {
        return menuGroups.stream().map(MenuGroupResponse::of).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
