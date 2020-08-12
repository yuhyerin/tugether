package com.web.curation.repo;

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

	@Query(value="update profile set nickname= :nickname , profile_photo= :profile_photo where email= :email",nativeQuery=true)
<<<<<<< HEAD
	void updateProfile(String email, String nickname, String profile_photo);
=======
	void updateProfilewithImage(String email, String nickname, String profile_photo);
	
	@Query(value="update profile set nickname= :nickname where email= :email",nativeQuery=true)
	void updateProfile(String email, String nickname);
>>>>>>> 9e3c10da12ddc8bae955035bfadc90b134c0d77f

	@Query(value = "select profile_photo from profile p where p.email=:email", nativeQuery=true)
	String findProfilePhoto(String email);
}
