package ir.proprog.enrollassist.controller.student;

import com.google.common.collect.Iterators;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.student.StudentNumber;
import ir.proprog.enrollassist.domain.user.User;
import ir.proprog.enrollassist.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    /*
    * studentController : Mock
    * Sate Verification
    * Classical
    * */
    @Test
    public void getAll_empty() {
        assertEquals(0, Iterators.size(studentController.all().iterator()));
    }

    /*
     * studentController : Mock
     * studentRepository : Stub
     * student1 : Dummy Object
     * student2 : Dummy Object
     * student3 : Dummy Object
     * Sate Verification
     * Classical
     * */
    @Test
    public void getAll_not_empty() {
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        Student student3 = mock(Student.class);

        when(studentRepository.findAll()).thenReturn(() -> Stream.of(student1, student2, student3).iterator());
        assertEquals(3, Iterators.size(studentController.all().iterator()));
    }

    /*
     * studentController : Mock
     * Sate Verification
     * Classical
     * */
    @Test
    public void get_one_student_not_found() {
        assertThrows(ResponseStatusException.class, () -> studentController.one(StudentNumber.DEFAULT.getNumber()));
    }

    /*
     * studentController : Mock
     * studentRepository : Stub
     * Sate Verification
     * Classical
     * */
    @Test
    public void get_one_student_student_found() {
        String stdNum = "1";
        when(studentRepository.findByStudentNumber(new StudentNumber(stdNum))).thenReturn(Optional.of(new Student(stdNum)));
        assertEquals(stdNum, studentController.one(stdNum).getStudentNo().getNumber());
    }

    /*
     * studentController : Mock
     * Sate Verification
     * Classical
     * */
    @Test
    public void addStudent_no_user_found() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentController.addStudent(new StudentView(new Student("1"))));
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("was not found."));
    }

    /*
     * studentController : Mock
     * studentRepository : Stub
     * userRepository : Stub
     * studentView : Fake Object
     * Sate Verification
     * Classical
     * */
    @Test
    public void addStudent_student_exist() {
        String stdNum = "1";
        StudentView studentView = new StudentView(new Student(stdNum));
        studentView.setUserId(stdNum);
        when(userRepository.findByUserId(stdNum)).thenReturn(Optional.of(mock(User.class)));
        when(studentRepository.findByStudentNumber(new StudentNumber(stdNum))).thenReturn(Optional.of(mock(Student.class)));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentController.addStudent(studentView));
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("This student already exists."));
    }

    /*
     * studentController : Mock
     * userRepository : Stub
     * studentView : Fake Object
     * Sate Verification
     * Classical
     * */
    @Test
    public void addStudent_student_not_exist() throws ExceptionList {
        String stdNum = "1";
        StudentView studentView = new StudentView(new Student(stdNum, GraduateLevel.PHD.name()));
        studentView.setUserId(stdNum);

        when(userRepository.findByUserId(stdNum)).thenReturn(Optional.of(mock(User.class)));
        assertEquals(stdNum, studentController.addStudent(studentView).getStudentNo().getNumber());
    }

    /*
     * studentController : Mock
     * Sate Verification
     * Classical
     * */
    @Test
    public void findTakeableSectionsByMajor_no_student_found() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentController.findTakeableSectionsByMajor("1"));
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("Student not found."));
    }

    /*
     * studentController : Mock
     * studentRepository : Stub
     * Sate Verification
     * Classical
     * */
    @Test
    public void findTakeableSectionsByMajor_no_course() {
        String stdNum = "1";
        when(studentRepository.findByStudentNumber(new StudentNumber(stdNum))).thenReturn(Optional.of(mock(Student.class)));
        assertEquals(0, Iterators.size(studentController.findTakeableSectionsByMajor(stdNum).iterator()));
    }

    /*
     * studentController : Mock
     * studentRepository : Stub
     * sectionRepository : Stub
     * student : Stub
     * section1 : Fake Object
     * section2 : Fake Object
     * section3 : Fake Object
     * Sate Verification
     * Classical
     * */
    @Test
    public void findTakeableSectionsByMajor_some_course() throws ExceptionList {
        String stdNum = "1";
        Student student = mock(Student.class);
        when(student.getStudentNumber()).thenReturn(new StudentNumber(stdNum));
        when(studentRepository.findByStudentNumber(new StudentNumber(stdNum))).thenReturn(Optional.of(student));

        Section section1 = new Section(new Course("0000001", "a", 3, "Masters"), "1");
        Section section2 = new Section(new Course("0000002", "b", 3, "Masters"), "2");
        Section section3 = new Section(new Course("0000003", "c", 3, "Masters"), "3");

        when(sectionRepository.findAll()).thenReturn(() -> Stream.of(section1, section2, section3).iterator());
        when(student.getTakeableSections(sectionRepository.findAll())).thenReturn(Arrays.asList(section1, section2));

        assertEquals(2, Iterators.size(studentController.findTakeableSectionsByMajor(stdNum).iterator()));
    }
}
