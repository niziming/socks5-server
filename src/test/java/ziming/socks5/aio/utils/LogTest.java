package ziming.socks5.aio.utils;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:bean.xml"})
public class LogTest {
    @Autowired
    static Log log;
    @Test
    public void log() {
        int count = 0;
        for (int i = 0; i <= 40; i++) {
            count += i;
        }
        log.log("开启port[%s]", 12414);
        log.log("1-40累加[%s]", count);
    }
}
