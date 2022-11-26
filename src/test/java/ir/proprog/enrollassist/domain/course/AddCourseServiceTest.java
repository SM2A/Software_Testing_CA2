package ir.proprog.enrollassist.domain.course;

import antlr.Utils;
import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.controller.course.CourseMajorView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AddCourseServiceTest {
    @Mock
    public  Course course1;
    @Mock
    public  Course course2;

    @Mock
    public  CourseMajorView courseMajorView;
    @InjectMocks
    public AddCourseService addCourseService;

    private Method getCheckLoopMethod() throws NoSuchMethodException {
        Method method = AddCourseService.class.getDeclaredMethod("checkLoop", Course.class, ExceptionList.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    void checkLoopTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ExceptionList exceptionList = new ExceptionList();
        when(course1.getPrerequisites()).thenReturn(Sets.newSet(course2));
        when(course2.getPrerequisites()).thenReturn(Sets.newSet(course1));

        getCheckLoopMethod().invoke(addCourseService, course1, exceptionList);

        verify(course1, times(1)).getPrerequisites();
        verify(course2, times(1)).getPrerequisites();

        Assertions.assertEquals(1, exceptionList.getExceptions().size());
    }

}
