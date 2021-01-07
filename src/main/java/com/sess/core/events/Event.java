package com.sess.core.events;

import com.sess.core.groups.Group;
import com.sess.core.running.RunningType;
import com.sess.core.users.User;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * sport event of user
 */
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "events_id_seq"
    )
    @SequenceGenerator(
            name = "events_id_seq",
            sequenceName = "events_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @NotNull(message = "Не указана группа для события")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @NotNull(message = "Не указан создатель события")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "running_type_id")
    private RunningType runningType;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "distance", precision = 8, scale = 2)
    @Min(value = 1, message = "Дистанция не может быть меньше 1")
    private float distance;

    @Column(name = "place_start", nullable = false)
    @NotBlank(message = "Не указано место начала события")
    private String placeStart;

    @Column(name = "place_end", nullable = false)
    @NotBlank(message = "Не указано место окончания события")
    private String placeEnd;

    @Column(name = "planned_dt_start", nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @NotNull(message = "Не указано запланированное время начала события")
    private LocalDateTime plannedDtStart;

    @Column(name = "planned_dt_end", nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @NotNull(message = "Не указано время окончания события")
    private LocalDateTime plannedDtEnt;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "factual_dt_start")
    private LocalDateTime factualDtStart;

    @Column(name = "factual_dt_end")
    private LocalDateTime factualDtEnd;

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public RunningType getRunningType() {
        return runningType;
    }

    public void setRunningType(RunningType runningType) {
        this.runningType = runningType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getPlaceStart() {
        return placeStart;
    }

    public void setPlaceStart(String placeStart) {
        this.placeStart = placeStart;
    }

    public String getPlaceEnd() {
        return placeEnd;
    }

    public void setPlaceEnd(String placeEnd) {
        this.placeEnd = placeEnd;
    }

    public LocalDateTime getPlannedDtStart() {
        return plannedDtStart;
    }

    public void setPlannedDtStart(LocalDateTime plannedDtStart) {
        this.plannedDtStart = plannedDtStart;
    }

    public LocalDateTime getPlannedDtEnt() {
        return plannedDtEnt;
    }

    public void setPlannedDtEnt(LocalDateTime plannedDtEnt) {
        this.plannedDtEnt = plannedDtEnt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getFactualDtStart() {
        return factualDtStart;
    }

    public void setFactualDtStart(LocalDateTime factualDtStart) {
        this.factualDtStart = factualDtStart;
    }

    public LocalDateTime getFactualDtEnd() {
        return factualDtEnd;
    }

    public void setFactualDtEnd(LocalDateTime factualDtEnd) {
        this.factualDtEnd = factualDtEnd;
    }
}
