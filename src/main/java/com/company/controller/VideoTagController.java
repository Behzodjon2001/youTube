package com.company.controller;

import com.company.dto.video.VideoTagDTO;
import com.company.service.VideoTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Video Tag")
@Slf4j
@RestController
@RequestMapping("/videoTag")
public class VideoTagController {
    @Autowired
    private VideoTagService videoTagService;

    @PostMapping("/adm/create")
    public ResponseEntity<?> create(@RequestBody VideoTagDTO dto) {
        log.info("Request for create {}" , dto);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        VideoTagDTO categoryDTO = videoTagService.create(dto);
        return ResponseEntity.ok().body(categoryDTO);
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> list() {
        log.info("Request for list {}");
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<VideoTagDTO> list = videoTagService.list();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/adm/update/{key}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody VideoTagDTO dto) {
        log.info("Request for update {}" , dto, id);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        videoTagService.update(dto,id);
        return ResponseEntity.ok().body("Successfully updated");
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        log.info("Request for delete {}" , id);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        videoTagService.delete(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }

//    @GetMapping("/public")
//    public ResponseEntity<?> getVideoTagList(@RequestHeader(value = "Accept-Language", defaultValue = "uz") LangEnum lang) {
//        log.info("Request for get article {}" , lang);
//        List<VideoTagDTO> list = videoTagService.getList(lang);
//        return ResponseEntity.ok().body(list);
//    }
}
