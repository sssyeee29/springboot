package com.example.firstproject.entity;


import com.example.firstproject.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity // 테이블을 생성, 생성과 동시에 기본키를 지정해야지 오류가 안남
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 자동으로 1씩 증가(시퀀스)
    private Long id;
    private String nickname;
    private String body;

                //@OneToMany
    @ManyToOne (fetch = FetchType.LAZY) // comment 엔티티와 Article 엔티티를 다대일 관계로 설정
    @JoinColumn(name = "article_id") // 이렇게 이름을 지정할수도 있음(지정 안하면 그냥 클래스를 소문자로 바꿔서 기본으로 생성해줌)
    private Article article; // => 이걸보고 외래키를 생성함. article_id

    public static Comment create(CommentDto dto, Article article) {
        //예외 발생
        if(dto.getId() != null) {
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        }
        if(dto.getArticleId() != article.getId()) {
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못됐습니다.");
        }
        //엔티티 생성 및 반환 //댓글 아이디, 댓글 닉네임, 댓글 본문, 부모 게시글
        return new Comment(dto.getId(), dto.getNickname(), dto.getBody(), article);
    }

    public void patch(CommentDto dto) {
        //예외 발생
        if(this.id != dto.getId()){
            throw new IllegalArgumentException("댓글 수정 실패!, 잘못된 id가 입력됐습니다.");
        }

        //객체 객신
        if(dto.getNickname() != null){ //수정할 nickname있니?
            this.nickname = dto.getNickname();
        }
        if(dto.getBody() != null){ //수정할 body있니?
            this.body = dto.getBody();
        }
    }
}
