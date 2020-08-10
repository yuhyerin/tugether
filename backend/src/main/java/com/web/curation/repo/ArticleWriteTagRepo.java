package com.web.curation.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.curation.dto.tag.Tag;


@Repository
@Transactional
public interface ArticleWriteTagRepo extends JpaRepository<Tag, String>{
	
	@Query(value="select * from tag t where t.tag_name=:tagname",nativeQuery=true)
	Tag findTagByTagName(String tagname);
	
	@Query(value="insert into tag(tag_name) values(:tagname )", nativeQuery=true)
	void insertTag(String tagname);
}
