package ir.proprog.enrollassist.controller.student;

import com.google.common.collect.Iterators;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.student.StudentNumber;
import ir.proprog.enrollassist.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentControllerTest {

    @Spy
    StudentController studentController;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    SectionRepository sectionRepository;

    @Mock
    EnrollmentListRepository enrollmentListRepository;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void initialize() {
        studentRepository = mock(StudentRepository.class);
        courseRepository = mock(CourseRepository.class);
        sectionRepository = mock(SectionRepository.class);
        enrollmentListRepository = mock(EnrollmentListRepository.class);
        userRepository = mock(UserRepository.class);

        studentController = new StudentController(
                studentRepository,
                courseRepository,
                sectionRepository,
                enrollmentListRepository,
                userRepository
        );
    }

    @AfterEach
    public void cleanUp() {
        studentController = null;

        studentRepository = null;
        courseRepository = null;
        sectionRepository = null;
        enrollmentListRepository = null;
        userRepository = null;
    }

    @Test
    public void getAll_empty() {
        assertEquals(0, Iterators.size(studentController.all().iterator()));
    }

    @Test
    public void getAll_not_empty() {
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        Student student3 = mock(Student.class);

        when(studentRepository.findAll()).thenReturn(() -> Stream.of(student1, student2, student3).iterator());
        assertEquals(3, Iterators.size(studentController.all().iterator()));
    }

    @Test
    public void get_one_student_not_found(){
        assertThrows(ResponseStatusException.class, () -> studentController.one(StudentNumber.DEFAULT.getNumber()));
    }

    @Test
    public void get_one_student_student_found(){
        when(studentRepository.findByStudentNumber(new StudentNumber("1"))).thenReturn(Optional.of(new Student("1")));
        assertEquals("1", studentController.one("1").getStudentNo().getNumber());
    }

    /*@Test
    public void get_one_student_many_student_found(){
        assertThrows(ResponseStatusException.class, () -> studentController.one(StudentNumber.DEFAULT.getNumber()));
    }*/


/*    @Test
    public void addStudent_no_duplicate(){
        StudentView ss = studentController.addStudent(studentView1);

        System.out.println(ss);
        System.out.println(studentView1);
//        assertEquals(studentView1, ss);
    }*/

}
