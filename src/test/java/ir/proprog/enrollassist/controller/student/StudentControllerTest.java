package ir.proprog.enrollassist.controller.student;

import com.google.common.collect.Iterators;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

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


    Student student1;
    Student student2;
    Student student3;

    StudentView studentView1;
    StudentView studentView2;
    StudentView studentView3;

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

        student1 = mock(Student.class);
        student2 = mock(Student.class);
        student3 = mock(Student.class);

        studentView1 = mock(StudentView.class);
        studentView2 = mock(StudentView.class);
        studentView3 = mock(StudentView.class);
    }

    @AfterEach
    public void cleanUp() {
        studentController = null;

        studentRepository = null;
        courseRepository = null;
        sectionRepository = null;
        enrollmentListRepository = null;
        userRepository = null;

        student1 = null;
        student2 = null;
        student3 = null;

        studentView1 = null;
        studentView2 = null;
        studentView3 = null;
    }

    @Test
    public void getAll_empty() {
        Stream<Student> stream = Stream.empty();
        when(studentRepository.findAll()).thenReturn(stream::iterator);
        assertEquals(0, Iterators.size(studentController.all().iterator()));
    }

    @Test
    public void getAll_not_empty() {
        when(studentRepository.findAll()).thenReturn(() -> Stream.of(student1, student2, student3).iterator());
        assertEquals(3, Iterators.size(studentController.all().iterator()));
    }


/*    @Test
    public void addStudent_no_duplicate(){
        StudentView ss = studentController.addStudent(studentView1);

        System.out.println(ss);
        System.out.println(studentView1);
//        assertEquals(studentView1, ss);
    }*/

}
