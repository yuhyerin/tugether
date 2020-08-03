package com.web.curation.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.web.curation.dto.BasicResponse;
import com.web.curation.dto.article.Article;
import com.web.curation.jwt.service.JwtService;
import com.web.curation.service.articlewrite.ArticleWriteService;
import com.web.curation.service.fileupload.FileUploadService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/tugether")
public class ArticleWriteController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ArticleWriteService articleService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@PostMapping("/articlewrite")
    public ResponseEntity<Map<String,Object>> addArticle(@RequestBody Map<String, Object> map, HttpServletRequest request) {
    	
//		ArrayList<Integer> taglist = (ArrayList)map.get("taglist");
//		System.out.println(taglist.toString());
//		
//		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = null;
//		
		String token = request.getHeader("jwt-auth-token");
		Jws<Claims> claims = jwtService.getDecodeToken(token);
		Map<String, Object> Userinfo = new HashMap<String, Object>();
		Userinfo = (Map<String, Object>) claims.getBody().get("AuthenticationResponse");
		String email = Userinfo.get("email").toString(); //이메일 
		String writer = Userinfo.get("nickname").toString(); //닉네임 
		
//		String content = map.get("content").toString(); //글내용
		
		String link = map.get("urlLink").toString();
		System.out.println("링크??? "+link);

		try {
			
			String baseDir ="C:\\tugetherimg";
			System.out.println(map.get("myFiles"));
			ArrayList<MultipartFile> files = (ArrayList<MultipartFile>) map.get("myFiles");
//        	MultipartFile files = (MultipartFile)map.get("myFiles"); // 파일 받아오기 
			MultipartFile onefile = files.get(0);
        	String fileName = onefile.getOriginalFilename();
        	System.out.println("파일명은??? "+fileName);
			String filePath = baseDir + "\\" + fileName;
			onefile.transferTo(new File(filePath));
//			Article article = new Article();
//			article.setEmail(email);
//			article.setWriter(writer);
//			article.setImage(filePath);
//			article.setContent(content);
//			article.setLink(link);
//			articleService.addArticle(article); // Article 테이블에 article 넣기.    // ArticleTag 테이블에  해당글에 달린 태그도 넣어줘야 합니다! 
//			resultMap.put("status", true);
//    		status = HttpStatus.OK;
//    		System.out.println("입력한 태그를 디비에 넣었습니다!!!!! ");
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("이미지 첨부 실패  ");
    		resultMap.put("message", e.getMessage());
    		status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
		
    }
	
//	@RequestParam("myFiles") MultipartFile files, @RequestParam("urlLink") String link,
	@PostMapping("/articletest")
    public ResponseEntity<Map<String,Object>> addArticleTest( @RequestBody Map<String, Object> map, HttpServletRequest request
    		, HttpServletResponse response) throws IOException{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = null;
		String token = request.getHeader("jwt-auth-token");
		Jws<Claims> claims = jwtService.getDecodeToken(token);
		Map<String, Object> Userinfo = new HashMap<String, Object>();
		Userinfo = (Map<String, Object>) claims.getBody().get("AuthenticationResponse");
		String email = Userinfo.get("email").toString(); //이메일 
		String writer = Userinfo.get("nickname").toString(); //닉네임 
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest)request;
		Iterator<String> itr = multipart.getFileNames();
		MultipartFile mfile = null;
		while(itr.hasNext()) {
			String filename = itr.next();
			System.out.println("파일: "+filename);
			mfile = multipart.getFile(filename);
			String origName;
			origName = new String(mfile.getOriginalFilename());
			if("".equals(origName)) {
				continue;
			}
			String path="c://";
			File serverFile = new File(path+ origName);
			mfile.transferTo(serverFile);
			
		}
		
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
		
    }
	
	@PostMapping("/articletesty")
    public ResponseEntity<Map<String,Object>> addArticleTesty( @RequestParam("articleimg") MultipartFile mFile, 
    		@RequestParam("contents") String contents,
    		@RequestParam("link") String link,
    		HttpServletRequest request) {
//		@RequestParam MultipartFile mFile
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = null;
		String token = request.getHeader("jwt-auth-token");
		Jws<Claims> claims = jwtService.getDecodeToken(token);
		Map<String, Object> Userinfo = new HashMap<String, Object>();
		Userinfo = (Map<String, Object>) claims.getBody().get("AuthenticationResponse");
		String email = Userinfo.get("email").toString(); //이메일 
		String writer = Userinfo.get("nickname").toString(); //닉네임 
		
		String articleimg = mFile.getOriginalFilename();
		System.out.println("파일명: "+articleimg);
		System.out.println("글내용: "+contents);
		System.out.println("링크: "+link);
		
		try {
			mFile.transferTo(new File("c:/tugetherimg/"+mFile.getOriginalFilename()));
			System.out.println("파일업로드 성공!");
			status = HttpStatus.OK;
		}catch(IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
		
    }
	
	
	
	

}
