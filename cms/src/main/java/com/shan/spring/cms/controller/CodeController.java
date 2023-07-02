package com.shan.spring.cms.controller;

import com.shan.spring.cms.dto.CodeDto;
import com.shan.spring.cms.repository.code.dto.CodeQueryDto;
import com.shan.spring.cms.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX)
@RequiredArgsConstructor
public class CodeController extends RestControllerBase {

    private final CodeService codeService;


    @PostMapping("/codes")
    public ResponseEntity<?> createCode(@RequestBody @Valid CodeDto.Create create) {
        log.info("Request Param [{}]", create);

        codeService.createCode(create);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/codes")
    public ResponseEntity<?> getCodes() {
        return ResponseEntity.ok(codeService.getAllCodesOpti());
    }

    @GetMapping("/codes/{codeId}")
    public ResponseEntity<?> getCode(@PathVariable Long codeId) {
        return ResponseEntity.ok(codeService.getCode(codeId));
    }

    @GetMapping("/codes/top-code/{topCode}")
    public ResponseEntity<?> getTopCodeGroup(@PathVariable String topCode) {
        List<CodeQueryDto.AllCodesResponseQuery> topCodeGroup = codeService.getTopCodeGroup(topCode);
        return ResponseEntity.ok(topCodeGroup);
    }

    @GetMapping("/codes/top-code/{topCode}/parent-code/{parentCode}")
    public ResponseEntity<?> getTopCodeGroup(@PathVariable String topCode, @PathVariable String parentCode) {
        List<CodeQueryDto.AllCodesResponseQuery> topCodeGroup = codeService.getParentCodeGroup(topCode, parentCode);
        return ResponseEntity.ok(topCodeGroup);
    }

    @PutMapping("/codes/{codeId}")
    public ResponseEntity<?> updateCode(@PathVariable Long codeId, @RequestBody @Valid CodeDto.Update update) {
        log.info("Request Param [{}]", update);

        codeService.updateCode(codeId, update);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/codes/{codeId}")
    public ResponseEntity<?> deleteCode(@PathVariable Long codeId) {
        codeService.deleteCode(codeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
