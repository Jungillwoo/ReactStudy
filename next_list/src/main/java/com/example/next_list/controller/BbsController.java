package com.example.next_list.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.next_list.service.BbsService;
import com.example.next_list.util.Paging;
import com.example.next_list.vo.BbsVO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/notice")
public class BbsController {

    @Autowired
    private BbsService bbsService;

    @RequestMapping("/list") // http://loacalhost:8080/notice/list
    @ResponseBody
    public Map<String, Object> list(String bname, String searchType, String searchValue,
                            String cPage) {

        Map<String, Object> map = new HashMap<>();

        int nowPage = 1;
        if (cPage != null) {
            nowPage = Integer.parseInt(cPage);
        }
        // bname이 무조건 인자로 넘어와야 한다. 그런데 공백이면 안된다.
        if (bname == null || bname.trim().length() == 0)
            bname = "BBS";

        // 전체게시물의 수
        int totalRecord = bbsService.getTotalCount(searchType, searchValue, bname);
        
        // 페이징 객체 생성
        Paging page = new Paging(7, 5);
        page.setTotalRecord(totalRecord);
        page.setNowPage(nowPage);

        // 뷰페이지에서 표현할 목록을 가져올 때 사용하는 값(begin, end)
        int begin = page.getBegin();
        int end = page.getEnd();

        // 목록 가져오기
        BbsVO[] ar = bbsService.getList(searchType, searchValue, bname, begin, end);

        // 뷰페이지에서 표현할 정보들을 저장
        map.put("ar", ar);
        map.put("page", page);
        map.put("totalRecord", totalRecord);
        map.put("bname", bname);
        map.put("nowPage", nowPage);

        return map; // json형식으로 반환
    }
}
