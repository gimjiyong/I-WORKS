package com.example.iworks.domain.schedule.controller;

import com.example.iworks.domain.schedule.dto.schedule.request.ScheduleCreateRequestDto;
import com.example.iworks.domain.schedule.dto.schedule.request.ScheduleUpdateRequestDto;
import com.example.iworks.domain.schedule.dto.scheduleAssign.request.ScheduleAssignSearchParameter;
import com.example.iworks.domain.schedule.service.schedule.ScheduleService;
import com.example.iworks.domain.schedule.service.scheduleAssign.ScheduleAssignService;
import com.example.iworks.global.dto.DateCondition;
import com.example.iworks.global.util.JwtProvider;
import com.example.iworks.global.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleAssignService scheduleAssignService;
    private final Response response;
    private final JwtProvider jwtProvider;

    /**
     * 할일 등록
     * @param authorizationToken
     * @param scheduleCreateRequestDto
     * @return 할일 등록 성공
     */
    @PostMapping
    public ResponseEntity<Map<String,Object>> createSchedule(@RequestHeader("Authorization") String authorizationToken, @Validated @RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        int userId = jwtProvider.getUserId(authorizationToken);
        scheduleService.createSchedule(userId, scheduleCreateRequestDto);
        return response.handleSuccess("할일 등록 성공");
    }

    /**
     * 할일 조회
     * @param scheduleId
     * @return ScheduleResponseDto
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Map<String,Object>> getSchedule(@PathVariable(name = "scheduleId") Integer scheduleId){
        return response.handleSuccess(scheduleService.getSchedule(scheduleId));
    }

    /**
     * 키워드로 할일 검색
     * @param keyword
     * @return List<ScheduleResponseDto>
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> searchScheduleByKeyword(@RequestParam("keyword") String keyword) {
        return response.handleSuccess(scheduleService.searchByKeyword(keyword));
    }

    /**
     * 할일 수정
     * @param scheduleId
     * @param scheduleUpdateRequestDto
     * @return 할일 수정 성공
     */
    @PostMapping("/{scheduleId}/update")
    public ResponseEntity<Map<String,Object>> updateSchedule(@PathVariable(name = "scheduleId") int scheduleId, @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto){
        scheduleService.updateSchedule(scheduleId, scheduleUpdateRequestDto);
        return response.handleSuccess("할일 수정 성공");
    }

    /**
     * 할일 완료 여부 업데이트
     * @param scheduleId
     * @param isFinish
     * @return 할일 완료 여부 업데이트 성공
     */
    @PostMapping("/{scheduleId}/isFinish")
    public ResponseEntity<Map<String,Object>> updateIsFinishStatus(@PathVariable(name = "scheduleId") int scheduleId, @RequestBody Boolean isFinish){
        scheduleService.isFinishedSchedule(scheduleId, isFinish);
        return response.handleSuccess("할일 완료 여부 업데이트 성공");
    }

    /**
     * 할일 삭제
     * @param scheduleId
     * @return 할일 삭제 성공
     */
    @PostMapping("/{scheduleId}/delete")
    public ResponseEntity<Map<String,Object>> deleteSchedule(@PathVariable(name = "scheduleId") int scheduleId){
        scheduleService.deleteSchedule(scheduleId);
        return response.handleSuccess("할일 삭제 성공");
    }

    /**
     * 담당자 할일 조회
     * @param searchParameterAndDate
     * @return ?
     */
    @PostMapping("/get-by-assignees-and-date")
    public ResponseEntity<Map<String,Object>> getAllByAssigneesAndDate(@RequestBody @Validated ScheduleAssignSearchParameter searchParameterAndDate){
        return response.handleSuccess(scheduleAssignService.findByAssignees(searchParameterAndDate.getAssigneeInfos(), searchParameterAndDate.getDateCondition()));
    }

    /**
     * 유저의 모든 소속에 대한 할일 배정 검색 조건 조회
     * @param authorizationToken
     * @return ?
     */
    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> getAllByUser(@RequestHeader("Authorization") String authorizationToken){
        int userId = jwtProvider.getUserId(authorizationToken);
        return response.handleSuccess(scheduleAssignService.findByUser(userId, null));
    }

    /**
     * 유저의 모든 소속에 대한 할일(업무) 배정 검색 조건 조회
     * @param authorizationToken
     * @param dateCondition
     * @return ?
     */
    @PostMapping("/task/date")
    public ResponseEntity<Map<String,Object>> getAllByUserAndDate(@RequestHeader("Authorization") String authorizationToken, @RequestBody @Validated DateCondition dateCondition){
        int userId = jwtProvider.getUserId(authorizationToken);
        return response.handleSuccess(scheduleAssignService.findTaskByUser(userId, dateCondition));
    }
}
