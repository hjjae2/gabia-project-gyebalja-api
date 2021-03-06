package com.gabia.gyebalja.user;

import com.gabia.gyebalja.domain.Department;
import com.gabia.gyebalja.domain.GenderType;
import com.gabia.gyebalja.domain.User;
import com.gabia.gyebalja.repository.DepartmentRepository;
import com.gabia.gyebalja.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author : 정태균
 * Part : All
 */

@Transactional
@SpringBootTest(properties = "spring.config.location=classpath:application-test.yml")
public class UserRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("User 저장 테스트(save)")
    public void saveTest() throws Exception {
        //given
        Department department = Department.builder()
                .name("Team1")
                .depth(2)
                .parentDepartment(null)
                .build();

        Department saveDepartment = departmentRepository.save(department);

        User user = User.builder()
                .email("test@gabia.com")
                .name("User1")
                .gender(GenderType.MALE)
                .phone("000-0000-0000")
                .tel("111-1111-1111")
                .positionId(23L)
                .positionName("인턴")
                .department(saveDepartment)
                .profileImg("src/img")
                .build();

        //when
        User saveUser = userRepository.save(user);
        User findUser = userRepository.findById(saveUser.getId()).get();

        //then
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getName()).isEqualTo(user.getName());
        assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(findUser).isEqualTo(user); //JPA 엔티티 동일성 보장하는지 검증
    }

    @Test
    @DisplayName("User 단건조회 테스트(findById)")
    public void findByIdTest() throws Exception {
        //given
        Department department = Department.builder()
                .name("Team1")
                .depth(2)
                .parentDepartment(null)
                .build();

        Department saveDepartment = departmentRepository.save(department);

        User user = User.builder()
                .email("test@gabia.com")
                .name("User1")
                .gender(GenderType.MALE)
                .phone("000-0000-0000")
                .tel("111-1111-1111")
                .positionId(23L)
                .positionName("인턴")
                .department(saveDepartment)
                .profileImg("src/img")
                .build();

        User saveUser = userRepository.save(user);

        /**
         * 영속성 컨텍스트를 초기화해줘야 findById를 할때 직접 select 쿼리가 날라감
         * 안해주면 그냥 존재하는 1차캐시에서 조회해오므로 디비에 select쿼리가 날라가지않음.
         */
        em.flush();
        em.clear();

        //when
        Optional<User> findUserById = userRepository.findById(saveUser.getId());

        //then
        assertThat(findUserById.get().getId()).isEqualTo(saveUser.getId());
        assertThat(findUserById.get().getName()).isEqualTo(saveUser.getName());
        assertThat(findUserById.get().getEmail()).isEqualTo(saveUser.getEmail());
    }

    @Test
    @DisplayName("User 전체 조회 테스트(findAll)")
    public void findAllTest() throws Exception {
        //given
        for (int i = 1; i <= 2; i++) {
            Department department = Department.builder()
                    .name("Team" + i)
                    .depth(2 + i)
                    .parentDepartment(null)
                    .build();


            Department saveDepartment = departmentRepository.save(department);

            User user = User.builder()
                    .email("test@gabia.com")
                    .name("User" + i)
                    .gender(GenderType.MALE)
                    .phone("000-0000-0000")
                    .tel("111-1111-1111")
                    .positionId(23L)
                    .positionName("인턴")
                    .department(saveDepartment)
                    .profileImg("src/img")
                    .build();

            User saveUser = userRepository.save(user);
        }

        em.flush();
        em.clear();

        //when
        List<User> userList = userRepository.findAll();

        //then
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("User 갯수 테스트(count)")
    public void countTest() throws Exception {
        //given
        for (int i = 1; i <= 3; i++) {
            Department department = Department.builder()
                    .name("Team"+i)
                    .depth(2+i)
                    .parentDepartment(null)
                    .build();


            Department saveDepartment = departmentRepository.save(department);

            User user = User.builder()
                    .email("test@gabia.com")
                    .name("User"+i)
                    .gender(GenderType.MALE)
                    .phone("000-0000-0000")
                    .tel("111-1111-1111")
                    .positionId(23L)
                    .positionName("인턴")
                    .department(saveDepartment)
                    .profileImg("src/img")
                    .build();

            User saveUser = userRepository.save(user);
        }

        em.flush();
        em.clear();

        //when
        long count = userRepository.count();

        //then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("User 삭제 테스트(delete)")
    public void deleteTest() throws Exception {
        //given
        Department department = Department.builder()
                .name("Team1")
                .depth(2)
                .parentDepartment(null)
                .build();

        Department saveDepartment = departmentRepository.save(department);

        User user = User.builder()
                .email("test@gabia.com")
                .name("User1")
                .gender(GenderType.MALE)
                .phone("000-0000-0000")
                .tel("111-1111-1111")
                .positionId(23L)
                .positionName("인턴")
                .department(saveDepartment)
                .profileImg("src/img")
                .build();

        User saveUser = userRepository.save(user);

        em.flush();
        em.clear();
        long beforeDeleteNum = userRepository.count();

        //when
        userRepository.delete(user);

        //then
        assertThat(userRepository.count()).isEqualTo(beforeDeleteNum-1);
    }

    @Test
    @DisplayName("User 정보 변경 테스트(update)")
    public void updateTest() throws Exception {
        //given
        String updateEngName = "EngName";

        Department department = Department.builder()
                .name("Team1")
                .depth(2)
                .parentDepartment(null)
                .build();

        Department saveDepartment = departmentRepository.save(department);

        User user = User.builder()
                .email("test@gabia.com")
                .name("User1")
                .engName("Gabia")
                .gender(GenderType.MALE)
                .phone("000-0000-0000")
                .tel("111-1111-1111")
                .positionId(23L)
                .positionName("인턴")
                .department(saveDepartment)
                .profileImg("src/img")
                .build();

        User saveUser = userRepository.save(user);

        //when
        saveUser.changeUser(12345L, "test@gabia.com", "User1", updateEngName, GenderType.MALE, "000-0000-0000", "111-1111-1111", 23L, "인턴", "src/img", saveDepartment);
        em.flush();
        em.clear();
        User findUser = userRepository.findById(saveUser.getId()).orElseThrow(() -> new IllegalArgumentException("해당 데이터가 없습니다."));
        //then
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getEngName()).isEqualTo(updateEngName);
    }

    @Test
    @DisplayName("User 가비아 고유 넘버로 조회")
    public void findUserByGabiaUserNo() throws Exception {
        //given
        Long gabiaUserNo = 12345L;

        Department department = Department.builder()
                .name("Team1")
                .depth(2)
                .parentDepartment(null)
                .build();

        Department saveDepartment = departmentRepository.save(department);

        User user = User.builder()
                .gabiaUserNo(gabiaUserNo)
                .email("test@gabia.com")
                .name("User1")
                .engName("Ted")
                .gender(GenderType.MALE)
                .phone("000-0000-0000")
                .tel("111-1111-1111")
                .positionId(23L)
                .positionName("인턴")
                .department(saveDepartment)
                .profileImg("src/img")
                .build();

        User saveUser = userRepository.save(user);

        /**
         * 영속성 컨텍스트를 초기화해줘야 findById를 할때 직접 select 쿼리가 날라감
         * 안해주면 그냥 존재하는 1차캐시에서 조회해오므로 디비에 select쿼리가 날라가지않음.
         */
        em.flush();
        em.clear();

        //when
        Optional<User> findUserByGabiaUserNo = userRepository.findUserByGabiaUserNo(gabiaUserNo);

        //then
        assertThat(findUserByGabiaUserNo.get().getGabiaUserNo()).isEqualTo(gabiaUserNo);
        assertThat(findUserByGabiaUserNo.get().getId()).isEqualTo(saveUser.getId());
    }
}
