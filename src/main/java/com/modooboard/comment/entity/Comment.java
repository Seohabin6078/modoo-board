package com.modooboard.comment.entity;

import com.modooboard.audit.Auditable;
import com.modooboard.member.entity.Member;
import com.modooboard.post.entity.Post;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "root_comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
