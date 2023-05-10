package com.earthlyz9.stepin.entities;

import io.swagger.v3.oas.annotations.Hidden;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
@Hidden
@Builder
public class Step extends NeedsPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "number")
    private int number;

    @ManyToOne
    @JoinColumn(name ="project_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @Column(name = "project_id")
    private int projectId;

    @ManyToOne
    @JoinColumn(name ="owner_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
}
