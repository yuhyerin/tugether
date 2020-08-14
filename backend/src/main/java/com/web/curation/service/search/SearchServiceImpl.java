package com.web.curation.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.curation.dto.article.Article;
import com.web.curation.dto.article.FrontArticle;
import com.web.curation.repo.ArticleRepo;
import com.web.curation.repo.ArticleTagRepo;
import com.web.curation.repo.LikeyRepo;
import com.web.curation.repo.ProfileRepo;
import com.web.curation.repo.TagRepo;
//1. 태그테이블에서 태그네임으로 태그아이디 찾고 
// search_cnt++ 해줘야해 ->얘는 검색버튼 눌렀을때!

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private ArticleRepo articleRepo;
	@Autowired
	private ArticleTagRepo articletagRepo;
	@Autowired
	private LikeyRepo likeRepo;
	@Autowired
	private ProfileRepo profileRepo;

	@Override	//키워드 포함한 태그이름 찾기
	public List<String> findTagNamesByTag(String keyword) {
		List<String> tagNames = tagRepo.findTagNameByTagNameContains(keyword);
		return tagNames;
	}

	@Override
	public List<FrontArticle> findByTagName(String email, String keyword) {
		//1. 키워드를 포함한 태그이름으로 태그아이디 가져오기
		List<Integer> tagIDs = tagRepo.findTagIdByTagNameContains(keyword);
		//2. 태그아이디를 가지고 있는 아티클아이디 가져오기 -> treeset 써서 중복 제거 또는 list.contains 써서 중복 제거
		List<Integer> articleIDs = new ArrayList<>();
		for(int i=0;i<tagIDs.size();i++) {
			List<Integer> temp = articletagRepo.findArticleIdByTagId(tagIDs.get(i));
			for(int j=0;j<temp.size();j++) {
				if(articleIDs.contains(temp.get(j)))
					continue;
				articleIDs.add(temp.get(j));
			}
		}
		List<FrontArticle> list = new ArrayList<>();
		for(int i=articleIDs.size()-1;i>=0;i--) {
			list.add(makeFront(email, articleIDs.get(i)));
		}
		return list;
	}
	
	@Override
	public List<String> findNickNamesByNickname(String keyword) {
		List<String> list = profileRepo.findNicknamesByNickname(keyword);
		return list;
	}
	
	@Override
	public List<FrontArticle> findArticlesByNickname(String email, String keyword) {
		List<String> list = profileRepo.findNicknamesByNickname(keyword);
		List<String> emails = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			List<String> temp = profileRepo.findEmailByNickname(list.get(i));
			for(int j=0;j<temp.size();j++) {
				if(emails.contains(temp.get(j)))
					continue;
				emails.add(temp.get(j));
			}
		}
		List<FrontArticle> result = new ArrayList<>();
		//이메일로 아티클데려옴
		TreeSet<Integer> articleIDs = new TreeSet<>();
		for(int i=0;i<emails.size();i++) {
			List<Integer> temp = articleRepo.findArticleIdByEmail(emails.get(i));
			for(int j=0;j<temp.size();j++)
				articleIDs.add(temp.get(j));
		}
		while(articleIDs.size()>0)
			result.add(makeFront(email, articleIDs.pollLast()));
		return result;
	}
	
	@Override // email = like 체크 / article_id = 태그리스트
	public FrontArticle makeFront(String email, int article_id) {

		Article now = articleRepo.findArticleByArticleId(article_id).get(0);
		List<Integer> taglist = articletagRepo.findTagIdByArticleId(now.getArticle_id()); // 아티클태그케이블에서 태그 가져와야 프론트에 줄 수
																							// 있음
		String[] temp = new String[taglist.size()]; // 태그 리스트를 태그 배열로 만들거임
		for (int j = 0; j < taglist.size(); j++) {
			temp[j] = tagRepo.findTagNameByTagId(taglist.get(j)); // 태그테이블에서 태그아이디로 태그네임 찾아서 배열 저장
		}

		boolean like = likeRepo.findLike(article_id, email).isPresent();
		String profile_photo = profileRepo.findProfilePhotoByEmail(email);
		FrontArticle ar = FrontArticle.builder()
				.article_id(article_id)
				.writer(now.getWriter())
				.reg_time(now.getReg_time())
				.image(now.getImage())
				.profile_photo(profile_photo)
				.content(now.getContent())
				.link(now.getLink())
				.like_cnt(now.getLike_cnt())
				.like(like)
				.comment_cnt(now.getComment_cnt())
				.scrap_cnt(now.getScrap_cnt())
				.tag_name(temp)
				.build();
				
		return ar;
	}

	

	

}