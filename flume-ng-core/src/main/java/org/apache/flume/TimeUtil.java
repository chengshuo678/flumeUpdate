/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.flume;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chengshuo on 2017/3/1.
 */
public class TimeUtil {

    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static String DEFAULT_DATETIME_FORMAT = DEFAULT_DATE_FORMAT + " "
            + DEFAULT_TIME_FORMAT;

    /**
     * 根据默认时间格式解析时间字符串
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long parseTime(String time) throws ParseException {
        return parseTime(time, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 根据日期格式生成时间
     *
     * @param time
     * @param formatStr
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("deprecation")
    public static long parseTime(String time, String formatStr)
            throws ParseException {
        DateFormat format = new java.text.SimpleDateFormat(formatStr);
        Date date = format.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, date.getHours());
        return calendar.getTime().getTime();
    }

}
