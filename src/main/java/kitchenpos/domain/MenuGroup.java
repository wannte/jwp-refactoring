package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this.id = null;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
