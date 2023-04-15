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
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "스텝 하위에 들어가는 아이템")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(JsonViews.Base.class)
    private int id;

    @Column(name = "content")
    @JsonView(JsonViews.Base.class)
    private String content;

    @ManyToOne
    @JoinColumn(name ="step_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonView(JsonViews.Retrieve.class)
    private Step step;

    @Column(name = "step_id")
    @JsonView(JsonViews.List.class)
    private int stepId;

    // TODO: ADD MEMO FIELD

    @Column(name = "created_at")
    @CreationTimestamp
    @JsonView(JsonViews.Base.class)
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonView(JsonViews.Base.class)
    private Date updatedAt;
}
