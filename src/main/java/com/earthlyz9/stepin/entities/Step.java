package com.earthlyz9.stepin.entities;

import com.earthlyz9.stepin.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "step")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "프로젝트 페이지의 열에 해당하는 카테고리")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(JsonViews.Base.class)
    private int id;

    @Column(name = "name")
    @JsonView(JsonViews.Base.class)
    private String name;

    @Column(name = "number")
    @JsonView(JsonViews.Base.class)
    private int number;

    @ManyToOne
    @JoinColumn(name ="project_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonView(JsonViews.Retrieve.class)
    private Project project;

    @Column(name = "project_id")
    @JsonView(JsonViews.List.class)
    private int projectId;

    @Column(name = "created_at")
    @CreationTimestamp
    @JsonView(JsonViews.Base.class)
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonView(JsonViews.Base.class)
    private Date updatedAt;
}
