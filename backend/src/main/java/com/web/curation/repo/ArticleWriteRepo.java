package com.web.curation.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ArticleWriteRepo {

	@Query(value="insert into article(email,writer,image,content,link) values(:email , :writer, :image,"
			+ " :content, :link )", nativeQuery=true)
	void insertArticle(String email, String writer, String image, String content, String link);
	
}
