package HealthNote.healthnote.service;

import HealthNote.healthnote.community_dto.CommunitySaveDto;
import HealthNote.healthnote.community_dto.Community_ID_Boolean;
import HealthNote.healthnote.community_dto.EncodingImageDto;
import HealthNote.healthnote.domain.Community;
import HealthNote.healthnote.domain.Member;
import HealthNote.healthnote.repository.CommunityRepository;
import HealthNote.healthnote.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    //게시글 저장(dto 데이터를 엔티티로 변환 후 저장)
    public Community_ID_Boolean contentSave(CommunitySaveDto csd) throws IOException {
        Community_ID_Boolean cib = new Community_ID_Boolean();
        Community community = new Community();
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();

        community.setGoodCount(0);
        community.setTitle(csd.getTitle());
        community.setDate(date);

        //커뮤니티 엔티티에 해당 member 엔티티 연관관계 맵핑 해주기
        Member findMember = memberRepository.findOne(csd.getId());
        if (findMember != null) {
            community.setMember(findMember);
        }else{
            cib.setSuccess(false);
            return cib;
        }
        //커뮤니티에 사진 저장하기
        if(csd.getCommunityPicture()!=null){
            MultipartFile picture = csd.getCommunityPicture();
            byte[] pictureBytes = picture.getBytes();
            String encodingPictureString = Base64.getEncoder().encodeToString(pictureBytes);
            community.setCommunityPicture(encodingPictureString);
        }

        //커뮤니티 저장
        communityRepository.save(community);
        cib.setSuccess(true); cib.setId(community.getId());
        return cib;
    }




    //좋아요 더하기 기능
    public int GoodCountAdd(Long id){
        Community findCommunity = communityRepository.findCommunity(id);
        int goodCount = findCommunity.getGoodCount();
        findCommunity.setGoodCount(goodCount+1);
        return findCommunity.getGoodCount();
    }



    //해당 user의 게시판 이미지들 넘기기 기능
    public List<EncodingImageDto> UserCommunityImages(Long id){
        List<Community> communities = communityRepository.findMemberCommunity(id);
        //해당 유저가 게시글이 하나도 없을 경우.(게시글 이미지가 없을 경우)
        if(communities.isEmpty()){
            return null;
        }
        List<EncodingImageDto> communityImages = new ArrayList<>();

        for (Community community : communities) {
            String encodingImage = community.getCommunityPicture();
            if(encodingImage.isEmpty()){
                return null;
            }
            int goodCount = community.getGoodCount();
            EncodingImageDto image = new EncodingImageDto(encodingImage,goodCount);
            communityImages.add(image);
        }
        return communityImages;

    }


}
