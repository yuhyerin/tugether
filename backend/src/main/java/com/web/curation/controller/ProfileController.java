package com.web.curation.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.curation.dto.BasicResponse;
import com.web.curation.dto.profile.Profile;
import com.web.curation.jwt.service.JwtService;
import com.web.curation.service.profile.ProfileService;
import com.web.curation.service.tag.FavtagService;
import com.web.curation.service.tag.TagService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/tugether")
public class ProfileController {

//	@Value("${ubuntu.profile.upload.directory}")
	@Value("${window.profile.upload.directory}")
	String upload_FILE_PATH;
	
	@Autowired
	private ProfileService profileSerivce;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private FavtagService favtagService;
	
	@Autowired
	private JwtService jwtService;
	
	@GetMapping("/profile")
	@ApiOperation(value = "회원의 프로필정보 가져오기 ")
	public ResponseEntity<Map<String,Object>> getProfile(HttpServletRequest request) {
	
			String token = request.getHeader("jwt-auth-token");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Jws<Claims> claims = jwtService.getDecodeToken(token);
			Map<String, Object> Userinfo = new HashMap<String, Object>();
			Userinfo = (Map<String, Object>) claims.getBody().get("AuthenticationResponse");
			String email = Userinfo.get("email").toString();
			
			 //이메일 보내서 프로필 가져오기
			Profile profile = profileSerivce.getProfile(email);
			resultMap.put("profile", profile);
			System.out.println("프로필 전달 합니다!!!");
			
			 //이메일 보내서 관심태그 가져오기
			 ArrayList<String> favtaglist = favtagService.getFavtagList(email);
			 for(int i=0; i<favtaglist.size();i++) {
				 System.out.println(favtaglist.get(i));
			 }
			 resultMap.put("favtaglist", favtaglist);

			return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
	
	}
	
	@PostMapping("/profilechange")
	@ApiOperation(value = "회원의 프로필 수정하기 ")
	public ResponseEntity<Map<String,Object>> updateProfile(
			@RequestParam("profile_photo") MultipartFile mFile,
			@RequestParam("nickname") String nickname,
    		@RequestParam("taglist") ArrayList<String> favtaglist,
			HttpServletRequest request) {
	
			HttpStatus status = null;
			String token = request.getHeader("jwt-auth-token");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Jws<Claims> claims = jwtService.getDecodeToken(token);
			Map<String, Object> Userinfo = new HashMap<String, Object>();
			Userinfo = (Map<String, Object>) claims.getBody().get("AuthenticationResponse");
			String email = Userinfo.get("email").toString();
			// DB에 프로필사진 저장할 때 이미지는 이메일+파일명 만 !!!
			System.out.println("여긴 옴~!!!~!~");
			System.out.println(mFile.getOriginalFilename());
			String profile_photo = email+mFile.getOriginalFilename();
			System.out.println("사진이름?? "+profile_photo);
			// 이메일로 해당 유저 프로필 수정하기 
			profileSerivce.updateProfile(email, nickname, profile_photo);
			System.out.println("프로필 수정이 완료되었습니다!");
			
			// 이전 관심태그 지우기
			profileSerivce.resetFavtag(email);
			System.out.println("이전 관심태그 목록을 지웠습니다. ");
					
			//이메일로 해당 유저 관심태그 수정하기
			ArrayList<Integer> new_favtaglist  = new ArrayList<Integer>();
			
			for(int i=0; i<favtaglist.size(); i++) {
				int tag_id;
				try {
					tag_id = tagService.getTagByTagName(favtaglist.get(i));
					
				}catch(NullPointerException e) {
					// 해당태그명이 테이블에 없다면, 태그 테이블에 등록하고 
					tagService.addTag(favtaglist.get(i));
					tag_id = tagService.getTagByTagName(favtaglist.get(i));
					
				}
				// 관심태그에 등록하기 
				new_favtaglist.add(tag_id);
				
			}
			profileSerivce.updateFavtag(email, new_favtaglist);
			System.out.println("프로필 컨트롤러 - 관심태그 수정 완료했습니다.");

			// 프로필사진 업로드
			try {
				// 파일업로드 할때 => 경로 + (작성자 이메일 + 파일명) 
				mFile.transferTo(new File(upload_FILE_PATH+email+profile_photo));
				status = HttpStatus.OK;
				System.out.println("파일을 업로드 했습니다.");
				
			}catch(IllegalStateException | IOException e) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				e.printStackTrace();
			}

			return new ResponseEntity<Map<String,Object>>(resultMap, status);
	
	}

}
