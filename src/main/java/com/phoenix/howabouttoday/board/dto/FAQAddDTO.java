package com.phoenix.howabouttoday.board.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FAQAddDTO {

    // 게시판 작성 페이지 : FAQ

    private Long memberNum; // 회원번호
    private Long boardCategoryNum; // 게시글 카테고리 번호

    @NotBlank(message = "카테고리를 선택하세요.")
    private String boardCategoryName; // 게시글 카테고리 이름

    @NotBlank(message = "제목을 입력하세요.")
    private String boardTitle; // 게시글 제목

    @NotBlank(message = "내용을 입력하세요.")
    private String boardContent; // 게시글 내용

}