package com.simpleblogapi.simpleblogapi.models;

import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_title",length = 200, nullable = false)
    private String postTitle;

    @Column(name = "post_thumbnail",length = 200, nullable = false)
    private String postThumbnail;

    @Column(name = "post_content",columnDefinition = "TEXT")
    private String postContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status")
    private PostStatus postStatus;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
