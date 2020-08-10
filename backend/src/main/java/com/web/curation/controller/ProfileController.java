package com.web.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.dto.BasicResponse;
import com.web.curation.dto.profile.Profile;
import com.web.curation.jwt.service.JwtService;
import com.web.curation.service.profile.ProfileService;
import com.web.curation.service.tag.FavtagService;

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

	@Autowired
	private ProfileService profileSerivce;
	
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
			 resultMap.put("favtaglist", favtaglist);

			return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
	
	}

}
