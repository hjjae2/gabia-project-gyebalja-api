package com.gabia.gyebalja.controller;

import com.gabia.gyebalja.common.CommonJsonFormat;
import com.gabia.gyebalja.common.StatusCode;
import com.gabia.gyebalja.dto.education.EducationAllResponseDto;
import com.gabia.gyebalja.dto.education.EducationDetailResponseDto;
import com.gabia.gyebalja.dto.education.EducationRequestDto;
import com.gabia.gyebalja.service.EducationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author : 정태균
 * Part : All
 */

@RequiredArgsConstructor  //생성자 주입방식을 사용하기 위해 사용
@Api(value = "EducationApiController V1")
@RestController
public class EducationApiController {

    //생성자 DI
    private final EducationService educationService;

    /** 등록 - education 한 건 (게시글 등록) */
    @ApiOperation(value = "postOneEducation : 등록 - education 한 건 (게시글 등록)", notes = "교육 한 건 등록")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD_REQUEST"),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
    })
    @PostMapping("/api/v1/educations")
    public CommonJsonFormat postOneEducation(@RequestBody EducationRequestDto educationRequestDto) {
        Long eduId = educationService.postOneEducation(educationRequestDto);

        return new CommonJsonFormat(StatusCode.OK.getCode(),StatusCode.OK.getMessage(),eduId);
    }

    /** 조회 - education 한 건 (상세페이지) */
    @ApiOperation(value = "getOneEducation : 조회 - education 한 건 (상세페이지)", notes = "교육 한 건에 대한 데이터 요청")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD_REQUEST"),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
    })
    @GetMapping("/api/v1/educations/{id}")
    public CommonJsonFormat getOneEducation(@PathVariable("id") Long id) {
        EducationDetailResponseDto educationResponseDto = educationService.getOneEducation(id);

        return new CommonJsonFormat(StatusCode.OK.getCode(),StatusCode.OK.getMessage(),educationResponseDto);
    }

    /** 수정 - education 한 건 (상세페이지) */
    @ApiOperation(value = "putOneEducation : 수정 - education 한 건 (상세페이지)", notes = "교육 한 건에 대한 수정 요청")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD_REQUEST"),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
    })
    @PutMapping("/api/v1/educations/{id}")
    public CommonJsonFormat putOneEducation(@PathVariable("id") Long id, @RequestBody EducationRequestDto educationRequestDto) {
        Long eduId = educationService.putOneEducation(id, educationRequestDto);

        return new CommonJsonFormat(StatusCode.OK.getCode(),StatusCode.OK.getMessage(),eduId);
    }

    /** 삭제 - education 한 건 (상세페이지) */
    @ApiOperation(value = "deleteOneEducation : 삭제 - education 한 건 (상세페이지)", notes = "교육 한 건에 대한 삭제 요청")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD_REQUEST"),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
    })
    @DeleteMapping("/api/v1/educations/{id}")
    public CommonJsonFormat deleteOneEducation(@PathVariable("id") Long id) {
        Long eduId = educationService.deleteOneEducation(id);

        return new CommonJsonFormat(StatusCode.OK.getCode(),StatusCode.OK.getMessage(), eduId);
    }

    /** 조회 - education 전체 (페이징) */
    @ApiOperation(value = "getAllEducationByUserId : 조회 - education 전체 (페이징)", notes = "사용자에 대한 전체 교육 리스트 요청")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD_REQUEST"),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
    })
    @GetMapping("/api/v1/users/{id}/educations")
    public CommonJsonFormat getAllEducationByUserId(@PathVariable("id") Long id, @PageableDefault(size=10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<EducationAllResponseDto> educationDtoPage = educationService.getAllEducationByUserId(id, pageable);

        return new CommonJsonFormat(StatusCode.OK.getCode(),StatusCode.OK.getMessage(), educationDtoPage);
    }
}

/**
 *  검토 1. 유효성 검증 로직을 클라이언트 사이드와 서버 사이드중 어디에 해야할까?
 *          => 브라우저의 개발자 도구에서 값 변조를 통해 회피가 가능하므로
 *             반드시 서버사이드에도 검증 로직이 필요하다.
 *  두가지 방법
 *      1. 프론트와 백엔드 양쪽에 모두 검증 코드를 작성한다.
 *      2. 백엔드에 검증 코드를 작성 후, 백엔드 결과에 따라 프론트는 메세지만 노출.
 *      => 오류 혹은 결과값을 통일시켜 프론트에서 처리하도록 하는 방식
 */
