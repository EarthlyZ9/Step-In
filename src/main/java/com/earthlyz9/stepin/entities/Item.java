package com.earthlyz9.stepin.entities;

import com.earthlyz9.stepin.exceptions.PermissionDeniedException;
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
public class Item implements NeedsPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "memo")
    private String memo;

    @ManyToOne
    @JoinColumn(name ="step_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Step step;

    @Column(name = "step_id")
    private int stepId;

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

    @Override
    public void checkPermission(int requestUserId) throws PermissionDeniedException {
        if (requestUserId != ownerId) throw new PermissionDeniedException("Only resource owner has access");
    }
}
