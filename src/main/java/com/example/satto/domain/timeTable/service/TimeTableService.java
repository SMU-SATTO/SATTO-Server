package com.example.satto.domain.timeTable.service;

import com.example.satto.domain.currentLecture.converter.CurrentLectureConverter;
import com.example.satto.domain.currentLecture.dto.CurrentLectureListResponseDTO;
import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import com.example.satto.domain.currentLecture.entity.CurrentLecture;
import com.example.satto.domain.currentLecture.repository.CurrentLectureRepository;
import com.example.satto.domain.timeTable.dto.MajorCombinationResponseDTO;
import com.example.satto.domain.timeTable.dto.TimeTableRequestDTO;
import com.example.satto.domain.timeTable.dto.TimeTableResponseDTO;
import com.example.satto.domain.timeTable.repository.TimeTableRepository;
import com.example.satto.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final CurrentLectureRepository currentLectureRepository;
    private final TimeTableRepository timeTableRepository;


    //강의 시간 충돌 검사
    public static boolean isNotTimeConflict(List<CurrentLectureResponseDTO> lectList,
                                            CurrentLectureResponseDTO lect) {

        String[] lectTimeSegments = lect.lectTime().split(" "); // 한 번만 분리
        for (CurrentLectureResponseDTO existingLect : lectList) {
            for (String segment : lectTimeSegments) {
                if (existingLect.lectTime().contains(segment)) {
                    return false; // 충돌 발견 시 즉시 반환
                }
            }
        }
        return true; // 충돌 없음

    }
    //학수번호 충돌 검사
    public static boolean isNotLectNumberConfilct(List<CurrentLectureResponseDTO> lectList,
                                                  CurrentLectureResponseDTO lect) {

        for (CurrentLectureResponseDTO existingLect : lectList) {
            if (existingLect.code().equals(lect.code())) {
                return false; // 같은 강의 번호 발견 시 즉시 반환
            }
        }
        return true; // 충돌 없음
    }

    public static boolean isNotConflict(List<CurrentLectureResponseDTO> currentLect, CurrentLectureResponseDTO lect) {
        return currentLect.isEmpty() || (isNotTimeConflict(currentLect, lect) && isNotLectNumberConfilct(currentLect, lect));
    }

    //시간표 조합 알고리즘
    private void generateCombinations(List<CurrentLectureResponseDTO> majorLectures,
                                      int start,
                                      List<CurrentLectureResponseDTO> current,
                                      List<List<CurrentLectureResponseDTO>> result,
                                      int targetSize
    ) {
        if (current.size() == targetSize) {
            result.add((new ArrayList<>(current)));
            return;
        }

        for (int i = start; i < majorLectures.size(); i++) {
            CurrentLectureResponseDTO nextMajorLecture = majorLectures.get(i);
            if (isNotConflict(current, nextMajorLecture)) {
                current.add(nextMajorLecture);
                generateCombinations(majorLectures, i + 1, current, result, targetSize);
                current.remove(current.size() - 1);
            }
        }
    }

    public List<MajorCombinationResponseDTO> createMajorTimeTable(TimeTableRequestDTO createDTO, Users users) {

        //3학년 1학기 4전공 선택 기준
        List<CurrentLecture> majorLectList = currentLectureRepository.findCurrentLectureByDepartmentAndGrade(users.getDepartment(), users.getGrade());
        List<CurrentLectureResponseDTO> majorLectDetailList = CurrentLectureConverter.toCurrentLectureDtoList(majorLectList);

        List<CurrentLectureResponseDTO> lectDetailList = new ArrayList<>();
        List<List<CurrentLectureResponseDTO>> timeTable = new ArrayList<>();
        List<TimeTableResponseDTO> result;

        List<CurrentLecture> requiredLectList = new ArrayList<>();
        for( String lect : createDTO.requiredLect() ){
            requiredLectList.add(currentLectureRepository.findCurrentLectureByCodeSection(lect));
        }

        List<CurrentLectureResponseDTO> requiredLectDetailList = CurrentLectureConverter.toCurrentLectureDtoList(requiredLectList);

        majorLectDetailList = removeLecturesInImpossibleTimeZones(majorLectDetailList, createDTO.impossibleTimeZone());

        //필수로 들어야할 강의 우선 조합
        generateCombinations(requiredLectDetailList, 0, lectDetailList, timeTable, requiredLectDetailList.size());

        generateCombinations(majorLectDetailList, 0, lectDetailList, timeTable, createDTO.majorCount() + requiredLectDetailList.size());
        result = TimeTableResponseDTO.fromList(timeTable);
        System.out.println("전공 강의 조합 개수 : " + timeTable.size());

        List<TimeTableResponseDTO> result1 = createTimeTable(createDTO, result);

        return calculateMajorCombinations(result1);
    }

    //시간대 벤 로직
    public List<CurrentLectureResponseDTO> removeLecturesInImpossibleTimeZones(List<CurrentLectureResponseDTO> majorLectDetailList, String impossibleTimeZones) {

        if (impossibleTimeZones == null || impossibleTimeZones.trim().isEmpty()) {
            return majorLectDetailList;
        }

        String[] timeSegments = impossibleTimeZones.split(" ");
        List<Integer> toRemoveIndex = new ArrayList<>();
        for (String time : timeSegments) {
            for (int i = 0; i < majorLectDetailList.size(); i++) {
                if (majorLectDetailList.get(i).lectTime().contains(time)) {
                    toRemoveIndex.add(i);
                }
            }
        }
        Collections.reverse(toRemoveIndex);
        for (int index : toRemoveIndex) {
            majorLectDetailList.remove(index);
        }
        return majorLectDetailList;
    }

    //전공 조합의 개수 계산
    public List<MajorCombinationResponseDTO> calculateMajorCombinations(List<TimeTableResponseDTO> result) {
        Map<Set<String>, Integer> combinationCounts = new HashMap<>();

        for (TimeTableResponseDTO data : result) {
            Set<String> sbjNames = data.timeTable().stream()
                    .filter(lecture -> "1전심".equals(lecture.cmpDiv()) || "1전선".equals(lecture.cmpDiv()))
                    .map(CurrentLectureResponseDTO::lectName)
                    .collect(Collectors.toSet());

            combinationCounts.merge(sbjNames, 1, Integer::sum);
        }

        return combinationCounts.entrySet().stream()
                .map(entry -> new MajorCombinationResponseDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<TimeTableResponseDTO> createTimeTable(TimeTableRequestDTO createDTO, List<TimeTableResponseDTO> majorTimetable) {

        List<CurrentLecture> entireLect = currentLectureRepository.findLectByCmpDiv("교선");
        List<CurrentLectureResponseDTO> entireLectList = CurrentLectureConverter.toCurrentLectureDtoList(entireLect);

        List<CurrentLectureResponseDTO> lectDetailList = new ArrayList<>();
        List<List<CurrentLectureResponseDTO>> timeTable = new ArrayList<>();
        List<TimeTableResponseDTO> result;

        entireLectList = removeLecturesInImpossibleTimeZones(entireLectList, createDTO.impossibleTimeZone());
        Collections.shuffle(entireLectList);
        List<CurrentLectureResponseDTO> randomList = entireLectList.subList(0,10);

        for(TimeTableResponseDTO lect : majorTimetable){
            generateCombinations1212(randomList, 0, lect.timeTable(), timeTable, createDTO.maxGPA());
        }

        result = TimeTableResponseDTO.fromList(timeTable);
        optimizationTimeTable(result);
        optimizationTimeTable2(result);
        optimizationTimeTable3(result);
        optimizationTimeTable4(result);
//        optimizationTimeTable5(result);
//        optimizationTimeTable6(result);

        return  result;
    }

    //시간표 조합 알고리즘
    private void generateCombinations1212(List<CurrentLectureResponseDTO> majorLectures,
                                          int start,
                                          List<CurrentLectureResponseDTO> current,
                                          List<List<CurrentLectureResponseDTO>> result,
                                          int targetSize
    ) {
        int currentCredits = current.stream().mapToInt(lecture -> lecture.credit()).sum();
        if ((currentCredits == targetSize) && !current.isEmpty()) {
            result.add((new ArrayList<>(current)));
            return;
        }

        for (int i = start; i < majorLectures.size(); i++) {
            CurrentLectureResponseDTO nextMajorLecture = majorLectures.get(i);
            int nextCredits = currentCredits + nextMajorLecture.credit();
            if (nextCredits <= targetSize && isNotConflict(current, nextMajorLecture)) {
                current.add(nextMajorLecture);
                generateCombinations1212(majorLectures, i + 1, current, result, targetSize);
                current.remove(current.size() - 1);
            }
        }
    }
    //공강이 3시간 이상 존재하는 경우 제거
    public List<TimeTableResponseDTO> optimizationTimeTable(List<TimeTableResponseDTO> timeTables){

        List<Integer> toRemoveIndexes = new ArrayList<>();
        for(int i = 0; i < timeTables.size(); i++){

            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).totalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet이 없으면 생성
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                // 시간 추가
                scheduleMap.get(day).add(time);
            }

            boolean hasThreeHourGap = false;
            // 각 요일별로 시간대 확인
            for (TreeSet<Integer> times : scheduleMap.values()) {
                // 이전 시간대 저장
                Integer prevTime = null;
                for (Integer currentTime : times) {
                    if (prevTime != null && (currentTime - prevTime > 3)) {
                        // 3교시 이상 비어있으면 다음 시간표로 넘어감
                        hasThreeHourGap = true;
                        break;
                    }
                    prevTime = currentTime;
                }
                if (!hasThreeHourGap) {
                    break; // 현재 시간표에서 3교시 이상 비어있지 않는 부분을 찾았으면 더 이상 확인하지 않음
                }
            }
            if (hasThreeHourGap) {
                // 3교시 이상 비어있는 경우 리스트에서 제거
                toRemoveIndexes.add(i);
            }
        }
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }

    //하루에 강의가 1시간만 있는 경우 제거
    public List<TimeTableResponseDTO> optimizationTimeTable2(List<TimeTableResponseDTO> timeTables) {
        // 제거하기 위한 인덱스 리스트
        List<Integer> toRemoveIndexes = new ArrayList<>();

        for (int i = 0; i < timeTables.size(); i++) {
            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).totalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet 생성 또는 시간 추가
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                scheduleMap.get(day).add(time);
            }

            // 하루에 강의가 1시간만 있는 경우를 찾기
            boolean hasOneHourClassOnly = scheduleMap.values().stream()
                    .anyMatch(times -> times.size() == 1); // 하루에 시간이 정확히 1개만 있는 경우 확인

            if (hasOneHourClassOnly) {
                // 하루에 강의가 1시간만 있는 경우 인덱스 저장
                toRemoveIndexes.add(i);
            }
        }

        // 뒤에서부터 제거하여 인덱스 변화를 방지
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }

    //공강일이 없는 경우 제거
    public List<TimeTableResponseDTO> optimizationTimeTable3(List<TimeTableResponseDTO> timeTables) {
        // 제거하기 위한 인덱스 리스트
        List<Integer> toRemoveIndexes = new ArrayList<>();

        for (int i = 0; i < timeTables.size(); i++) {
            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).totalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet 생성 또는 시간 추가
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                scheduleMap.get(day).add(time);
            }

            // 모든 요일에 강의가 있는지 확인
            boolean hasNoEmptyDay = scheduleMap.keySet().size() == 5; // 월화수목금을 기준으로 5일 모두 강의가 있는지 확인

            if (hasNoEmptyDay) {
                // 모든 요일에 강의가 있어 공강이 없는 경우 인덱스 저장
                toRemoveIndexes.add(i);
            }
        }

        // 뒤에서부터 제거하여 인덱스 변화를 방지
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }

    //7시간 이상 연속으로 강의가 존재하는 시간표 제외
    public List<TimeTableResponseDTO> optimizationTimeTable4(List<TimeTableResponseDTO> timeTables){

        List<Integer> toRemoveIndexes = new ArrayList<>();
        for(int i = 0; i < timeTables.size(); i++){

            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).totalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet이 없으면 생성
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                // 시간 추가
                scheduleMap.get(day).add(time);
            }

            boolean hasSevenHourContinuous = false;
            // 각 요일별로 시간대 확인
            for (TreeSet<Integer> times : scheduleMap.values()) {
                // 연속 시간 확인
                Integer firstTime = null;
                Integer lastTime = null;
                for (Integer currentTime : times) {
                    if (firstTime == null) {
                        firstTime = currentTime;
                        lastTime = currentTime;
                    } else if (currentTime - lastTime == 1) {
                        lastTime = currentTime;
                        // 연속되는 시간이 7시간 이상인지 확인
                        if (lastTime - firstTime >= 6) {
                            hasSevenHourContinuous = true;
                            break;
                        }
                    } else {
                        firstTime = currentTime;
                        lastTime = currentTime;
                    }
                }
                if (hasSevenHourContinuous) {
                    break; // 7시간 이상 연속되는 경우 찾았으면 더 이상 확인하지 않음
                }
            }
            if (hasSevenHourContinuous) {
                // 7시간 이상 연속된 경우 리스트에서 제거
                toRemoveIndexes.add(i);
            }
        }
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }

    //0교시, 1교시, 2교시 강의가 존재하는 시간표 제외
    public List<TimeTableResponseDTO> optimizationTimeTable5(List<TimeTableResponseDTO> timeTables) {

        List<Integer> toRemoveIndexes = new ArrayList<>();
        for (int i = 0; i < timeTables.size(); i++) {
            if (timeTables.get(i).totalTime().contains("1")) {
                toRemoveIndexes.add(i);
            } else if (timeTables.get(i).totalTime().contains("2")) {
                toRemoveIndexes.add(i);
            }
        }

        // 뒤에서부터 제거하여 인덱스 변화를 방지
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }

    //9시~11시 중 강의가 있고 12시 이후에도 강의가 있는 경우 점심시간(11시~13시중 1시간)이 확보되지 않은 시간표 제외
    public List<TimeTableResponseDTO> optimizationTimeTable6(List<TimeTableResponseDTO> timeTables){

        List<Integer> toRemoveIndexes = new ArrayList<>();
        for(int i = 0; i < timeTables.size(); i++){

            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).totalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet이 없으면 생성
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                // 시간 추가
                scheduleMap.get(day).add(time);
            }

            boolean isValidTimeTable = false;
            for (TreeSet<Integer> times : scheduleMap.values()) {
                // 1~3교시 중 강의가 존재하는지 확인
                boolean hasEarlyClass = times.stream().anyMatch(time -> time >= 1 && time <= 3);
                // 4교시 이후 강의가 존재하는지 확인
                boolean hasLateClass = times.stream().anyMatch(time -> time >= 4);

                if (hasEarlyClass && hasLateClass) {
                    // 3교시, 4교시, 5교시 중 비어있는 시간이 1시간 이상 있는지 확인
                    boolean hasGap = false;
                    for (int j = 3; j <= 5; j++) {
                        if (!times.contains(j)) {
                            hasGap = true;
                            break;
                        }
                    }
                    // 조건을 만족하지 않는 시간표는 제외
                    if (!hasGap) {
                        isValidTimeTable = true;
                        break;
                    }
                }
            }

            if (isValidTimeTable) {
                toRemoveIndexes.add(i);
            }
        }
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }

}
