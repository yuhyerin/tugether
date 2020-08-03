package com.web.curation.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.curation.dto.article.Article;

@Repository
@Transactional
public interface ArticleWriteRepo extends JpaRepository<Article, String>{

	@Query(value="insert into article(email,writer,image,content,link) values(:email , :writer, :image, :content, :link )", nativeQuery=true)
	void insertArticle(String email, String writer, String image, String content, String link);
	
}
