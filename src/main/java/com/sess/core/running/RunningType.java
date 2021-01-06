package com.sess.core.running;

import com.sess.core.groups.Group;

import javax.persistence.*;

@Entity
@Table(name = "running_types")
public class RunningType {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "running_types_id_seq"
    )
    @SequenceGenerator(
            name = "running_types_id_seq",
            sequenceName = "running_types_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false, updatable = false)
    private Group group;

    @Column(name = "caption", length = 100, nullable = false)
    private String caption;

    @Column(name = "deleted")
    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
