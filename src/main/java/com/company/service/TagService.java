package com.company.service;

import com.company.entity.TagEntity;
import com.company.entity.video.VideoEntity;
import com.company.enums.TagStatus;
import com.company.exception.ItemNotFoundException;
import com.company.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public TagEntity create(String name) {
        TagEntity tag = new TagEntity();
        tag.setName(name);
        tagRepository.save(tag);
        return tag;
    }

    public TagEntity createIfNotExists(String tagName) {
//        Optional<TagEntity> tagOptional = tagRepository.findByName(tagName);
//        if (tagOptional.isEmpty()) {
//            return create(tagName);
//        }
//        return tagOptional.get();
        return tagRepository.findByName(tagName).orElse(create(tagName));
    }

    public TagEntity get(Integer id) {
        return tagRepository.findById(id).orElseThrow(() -> {
            log.error("article  not found {}" , id);
            throw new ItemNotFoundException("Article not found");
        });
    }
}
