package com.web.curation.controller;

import java.util.HashMap;
import java.util.List;
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
import com.web.curation.dto.comment.FrontComment;
import com.web.curation.jwt.service.JwtService;
import com.web.curation.service.comment.CommentService;

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
public class CommentController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/mainfeed/comment")
	@ApiOperation(value = "댓글페이지")
	public ResponseEntity<Map<String,Object>> commentPage(HttpServletRequest request) {
		System.out.println("Controller");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String token = request.getHeader("jwt-auth-token");	//토큰 가져와서
		Jws<Claims> claims = jwtService.getDecodeToken(token);	//복호화해서
		Map<String, Object> Userinfo = (Map<String, Object>) claims.getBody().get("AuthenticationResponse");
//		String email = Userinfo.get("email").toString();
		int article_id = Integer.parseInt(request.getHeader("article_id"));
		
		List<FrontComment> comments = commentService.findComments(article_id);
		resultMap.put("comments", comments);
		System.out.println("result : "+comments.toString());
		return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
	}
}
