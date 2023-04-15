package com.earthlyz9.stepin.entities;

import com.earthlyz9.stepin.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "project")
@NoArgsConstructor
@Getter
@Setter
@Relation(collectionRelation = "projects", itemRelation = "project")
@Schema(description = "프로젝트")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(JsonViews.Base.class)
    private int id;

    @Column(name = "name")
    @JsonView(JsonViews.Base.class)
    private String name;

    @ManyToOne
    @JoinColumn(name ="owner_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonView(JsonViews.Retrieve.class)
    private User owner;

    @Column(name = "owner_id")
    @JsonView(JsonViews.List.class)
    private int ownerId;

    @Column(name = "created_at")
    @CreationTimestamp
    @JsonView(JsonViews.Base.class)
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonView(JsonViews.Base.class)
    private Date updatedAt;
}
