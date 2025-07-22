package com.test.todoapi.domain;

import com.henheang.securityapi.domain.User;
import com.test.todoapi.enums.PermissionLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "list_shares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_id")
    private Long shareId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    private TodoList listId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false)
    private PermissionLevel permissionLevel = PermissionLevel.VIEW; // The default permission level is View


    @Column(name = "share_date", nullable = false)
    private LocalDateTime shareDate; // Date when the list was shared, in ISO 8601 format (e.g., "2023-10-01T12:00:00Z")

    @PrePersist
    protected void onCreate() {
        this.shareDate = LocalDateTime.now();
    }

}
