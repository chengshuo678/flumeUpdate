import org.apache.flume.ip.IpHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengshuo on 2017/3/9.
 */
public class TestIp {
    @Test
    public void test() throws Exception {

        List<String> stringList = new ArrayList<String>();
        stringList.add("114.113.197.131");
        stringList.add("114.112.197.131");
        stringList.add("114.113.197.131");
        stringList.add("114.114.197.131");
        stringList.add("114.115.197.131");
        stringList.add("114.116.197.131");
        stringList.add("114.117.197.131");
        stringList.add("114.119.197.131");
        stringList.add("114.113.197.131");
        stringList.add("114.110.197.131");
        stringList.add("114.144.197.131");
        stringList.add("114.113.195.131");
        stringList.add("114.113.196.131");
        stringList.add("114.113.197.131");
        stringList.add("114.113.194.131");
        stringList.add("114.113.193.131");
        stringList.add("114.113.192.131");
        stringList.add("114.113.191.131");
        stringList.add("114.113.197.134");
        stringList.add("114.113.197.135");
        stringList.add("114.113.197.136");
        stringList.add("114.113.197.137");
        stringList.add("114.113.197.138");
        stringList.add("114.113.197.139");
        stringList.add("114.113.197.131");

        for (String ip:stringList) {
            String region = IpHelper.findRegionByIp(ip);
            System.out.println(region);
        }
    }
}
