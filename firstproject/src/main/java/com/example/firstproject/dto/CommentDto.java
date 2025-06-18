package com.example.firstproject.dto;

import com.example.firstproject.entity.Comment;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class CommentDto {

    private Long id; // 댓글 id
    private Long articleId; // 부모 댓글

    private String nickname;
    private String body;

    public static CommentDto createCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .articleId(comment.getArticle().getId())
                .nickname(comment.getNickname())
                .body(comment.getBody())
                .build();
    }
}
