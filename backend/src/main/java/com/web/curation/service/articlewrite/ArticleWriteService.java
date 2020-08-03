package com.web.curation.service.articlewrite;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.web.curation.dto.article.Article;

public interface ArticleWriteService {
	
		// favtag 테이블에 insert 하는 문장 
		public ResponseEntity<Object> addArticle(Article article);
}
