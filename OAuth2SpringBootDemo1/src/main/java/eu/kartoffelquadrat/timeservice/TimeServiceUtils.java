/**
 * @Author: Maximilian Schiedermeier
 * @Date: April 2019
 */
package eu.kartoffelquadrat.timeservice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Motivation of this class is to separate internal service functionality away from the REST controller.
 */
public class TimeServiceUtils {

    /**
     * Internal service functionality to generate a time string for the current time. Not directly exposed to others via REST.
     *
     * @return
     */
    public static String lookUpCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date());
    }
}
