package huangzehai.util;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class DateTimeUtilsTest {

    @Test
    public void randomDate() {
        LocalDateTime localDateTime = DateTimeUtils.randomDate();
        System.out.println(localDateTime);
    }
}