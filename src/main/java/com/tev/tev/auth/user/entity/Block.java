package com.tev.tev.auth.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_block")
@Builder
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Integer blockId;

    @Comment("차단 사유")
    @Column(name = "block_reason")
    private String reason;

    @Comment("차단일")
    @Column(name = "block_at")
    private LocalDateTime blockAt;

    @Comment("차단 만료일")
    @Column(name = "block_expiry_at")
    private LocalDateTime expiryAt;

    @Comment("유저 식별자")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
