package com.web.curation.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.curation.dto.profile.Profile;

@Repository
@Transactional
public interface ProfileRepo extends JpaRepository<Profile, String>{
	
	@Query(value="select * from profile p where p.email=:email",nativeQuery=true)
	Profile findProfileByEmail(String email);

	@Query(value="update profile set article_cnt = :article_cnt where email= :email ",nativeQuery=true)
	void countMyArticle(String email, int article_cnt);
	
	@Query(value="select nickname from profile p where p.email=:email", nativeQuery=true)
	String findNicknameByEmail(String email);
	
	@Query(value="select profile_photo from profile p where p.email=:email", nativeQuery=true)
	String findProfilePhotoByEmail(String email);
	
	@Query(value="select nickname from profile p where p.nickname like CONCAT('%',:keyword,'%')")
	List<String> findNicknamesByNickname(String keyword);
	
	@Query(value="select email from profile p where p.nickname like  CONCAT('%',:nickname,'%')")
	List<String> findEmailByNickname(String nickname);

	@Query(value="update profile set nickname= :nickname , profile_photo= :profile_photo where email= :email",nativeQuery=true)
	void updateProfile(String email, String nickname, String profile_photo);

	@Query(value = "select profile_photo from profile p where p.email=:email", nativeQuery=true)
	String findProfilePhoto(String email);
}
