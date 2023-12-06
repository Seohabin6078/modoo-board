package com.modooboard.post.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostImage { // 값 타입 컬렉션으로 변경하는거 고려해보기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long postImageId;

    @Column(name = "image_address", nullable = false)
    private String imageAddress;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
