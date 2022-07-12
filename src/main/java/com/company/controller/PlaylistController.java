package com.company.controller;

import com.company.dto.PlaylistDTO;
import com.company.entity.PlaylistEntity;
import com.company.service.PlaylistService;
import com.company.util.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(tags = "Playlist")
@Slf4j
@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @ApiOperation(value = "Playlist create", notes="Method for Playlist create")
    @PostMapping("/user/create")
    public ResponseEntity<?> create(@RequestBody @Valid PlaylistDTO dto) {
        log.info("Request for create {}" , dto);
//        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        PlaylistDTO articleDTO = playlistService.create(dto, CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body(articleDTO);
    }

    @ApiOperation(value = "Playlist update", notes="Method for Playlist update")
    @PutMapping("/ownUser/fullUpdate{id}")
    public ResponseEntity<?> fullUpdate(@PathVariable Integer id, @RequestBody PlaylistDTO dto) {
        log.info("Request for update {}" , dto);
//        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        PlaylistEntity channel = playlistService.fullUpdate(id, dto);
        return ResponseEntity.ok().body("\tSuccessfully updated \n\n" + channel);
    }

    @ApiOperation(value = "Playlist update", notes="Method for Playlist update")
    @PutMapping("/AOU/changStatus{id}")
    public ResponseEntity<?> changStatus(@PathVariable Integer id) {
        log.info("Request for update {}" , id);
//        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        PlaylistEntity channel = playlistService.changStatus(id);
        return ResponseEntity.ok().body("\tSuccessfully updated \n\n" + channel);
    }

//    @ApiOperation(value = "list By Playlist", notes="Method for list By Playlist")
//    @GetMapping("/public/byId/{id}")
//    public ResponseEntity<?> listById(@PathVariable("id") String id) {
//        log.info("Request for listByCategory {}" , id);
//        Optional<PlaylistDTO> list = playlistService.listById(id);
//        return ResponseEntity.ok().body(list);
//    }
//
//    @ApiOperation(value = "list By Type", notes="Method for list By Type")
//    @GetMapping("/user/list")
//    public ResponseEntity<?> listByType() {
//        log.info("Request for listByType {}"  );
//        List<PlaylistDTO> list = playlistService.list(CurrentUser.getCurrentUser().getProfile().getId());
//        return ResponseEntity.ok().body(list);
//    }

    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestHeader("Content-ID") Integer id) {
        log.info("Request for delete {}" , id);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        playlistService.delete(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }



    @GetMapping("/admin/pagination")
    public ResponseEntity<?> getPagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "2") int size) {
        log.info("Request for getPagination {}" , page);
        PageImpl<PlaylistDTO> response = playlistService.pagination(page, size);
        return ResponseEntity.ok().body(response);
    }
}