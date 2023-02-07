package com.example.firstProject.service;

import com.example.firstProject.dto.ArticleForm;
import com.example.firstProject.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 해당 클래스는 스프링부트와 연동되어 테스트
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;
    @Test
    void index() {
        //예상
        Article a = new Article(1L,"가가가가","나나나나");
        Article b = new Article(2L,"가가가가","나나나나");
        Article c = new Article(3L,"가가가가","나나나나");
        List<Article> expected = new ArrayList<Article>(Arrays.asList(a,b,c));

        //실제
        List<Article> articles = articleService.index();

        //비교
        assertEquals(expected.toString(),articles.toString());
    }

    @Test
    void show_성공___존재하는_id_입력() {
        //예상
        Long id = 1L;
        Article expected = new Article(id,"가가가가","나나나나");

        //실제
        Article article = articleService.show(id);

        //비교
        assertEquals(expected.toString(),article.toString());
    }

    @Test
    void show_실패__존재하지_않는_id_입력() {
        //예상
        Long id = -1L;
        Article expected = null;

        //실제
        Article article = articleService.show(id);

        //비교
        assertEquals(expected,article);
    }

    @Test
    @Transactional
    void create__성공__title과_content만_입력() {
        //예상
        String title = "사사사사";
        String content = "자자자자";
        ArticleForm dto = new ArticleForm(null,title,content);
        Article expected = new Article(4L,title,content);

        //실제
        Article article = articleService.create(dto);

        //비교
        assertEquals(expected.toString(),article.toString());
    }

    @Test
    void create__실패___id가_포함된_dto_입력() {
        //예상
        String title = "사사사사";
        String content = "자자자자";
        ArticleForm dto = new ArticleForm(4L,title,content);
        Article expected = null;

        //실제
        Article article = articleService.create(dto);

        //비교
        assertEquals(expected,article);
    }
}