/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flume.interceptor;

import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.DEFAULT_EXCLUDE_EVENTS;
import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.DEFAULT_REGEX;
import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.EXCLUDE_EVENTS;
import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.REGEX;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.IntercepterConstant;
import org.apache.flume.TimeUtil;
import org.apache.flume.ip.IpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Interceptor that filters events selectively based on a configured regular
 * expression matching against the event body.
 *
 * This supports either include- or exclude-based filtering. A given
 * interceptor can only perform one of these functions, but multiple
 * interceptor can be chained together to create more complex
 * inclusion/exclusion patterns. If include-based filtering is configured, then
 * all events matching the supplied regular expression will be passed through
 * and all events not matching will be ignored. If exclude-based filtering is
 * configured, than all events matching will be ignored, and all other events
 * will pass through.
 *
 * Note that all regular expression matching occurs through Java's built in
 * java.util.regex package.
 *
 * Properties:<p>
 *
 *   regex: Regular expression for matching excluded events.
 *          (default is ".*")<p>
 *
 *   excludeEvents: If true, a regex match determines events to exclude,
 *                  otherwise a regex determines events to include
 *                  (default is false)<p>
 *
 * Sample config:<p>
 *
 * <code>
 *   agent.sources.r1.channels = c1<p>
 *   agent.sources.r1.type = SEQ<p>
 *   agent.sources.r1.interceptors = i1<p>
 *   agent.sources.r1.interceptors.i1.type = REGEX<p>
 *   agent.sources.r1.interceptors.i1.regex = (WARNING)|(ERROR)|(FATAL)<p>
 * </code>
 *
 */
public class RegexFilteringInterceptor implements Interceptor {

  private static final Logger logger = LoggerFactory
      .getLogger(StaticInterceptor.class);

  private final Pattern regex;
  private final boolean excludeEvents;

  /**
   * Only {@link RegexFilteringInterceptor.Builder} can build me
   */
  private RegexFilteringInterceptor(Pattern regex, boolean excludeEvents) {
    this.regex = regex;
    this.excludeEvents = excludeEvents;
  }

  @Override
  public void initialize() {
    // no-op
  }


  @Override
  /**
   * Returns the event if it passes the regular expression filter and null
   * otherwise.
   */
  public Event intercept(Event event) {
    // We've already ensured here that at most one of includeRegex and
    // excludeRegex are defined.

    if (!excludeEvents) {

      byte[] newBody = converJson(event.getBody());

      if (newBody == null) {
        return null;
      } else {
        event.setBody(newBody);
        return event;
      }
    }else {
      return event;
    }
  }

  /**
   * 将日志内容统一转换为json格式
   * 2017-02-20 20:45:33 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]
   * @param body
   * @return
   */
  private byte[] converJson(byte[] body) {

      try {

        String logInfo = new String(body);

        String time = logInfo.substring(0, 19);

        int length = logInfo.length();
        String info = logInfo.substring(22, length - 2);


        StringBuilder[] contents = splitNew(info, ',', 6);


        //以下为拼接字符串
        StringBuilder stringBuilderResult = new StringBuilder(IntercepterConstant.leftBrace);

        //时间
        stringBuilderResult.append(IntercepterConstant.Item.TIMEStr);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(IntercepterConstant.quatation);
        stringBuilderResult.append(time);
        stringBuilderResult.append(IntercepterConstant.quatation);

        stringBuilderResult.append(IntercepterConstant.comma);

        //时间毫秒数
        stringBuilderResult.append(IntercepterConstant.Item.TIMENum);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(TimeUtil.parseTime(time));

        stringBuilderResult.append(IntercepterConstant.comma);

        //OPT
        stringBuilderResult.append(IntercepterConstant.Item.OPT);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(IntercepterConstant.quatation);
        stringBuilderResult.append(contents[0]);
        stringBuilderResult.append(IntercepterConstant.quatation);

        stringBuilderResult.append(IntercepterConstant.comma);

        //spreadId
        stringBuilderResult.append(IntercepterConstant.Item.SPREAD_ID);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(contents[1]);

        stringBuilderResult.append(IntercepterConstant.comma);

        //demandId
        stringBuilderResult.append(IntercepterConstant.Item.DEMAND_ID);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(contents[2]);

        stringBuilderResult.append(IntercepterConstant.comma);

        //platform
        stringBuilderResult.append(IntercepterConstant.Item.PLATFORM);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(IntercepterConstant.quatation);
        stringBuilderResult.append(contents[3]);
        stringBuilderResult.append(IntercepterConstant.quatation);

        stringBuilderResult.append(IntercepterConstant.comma);

        //ip
        stringBuilderResult.append(IntercepterConstant.Item.IP);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(IntercepterConstant.quatation);
        stringBuilderResult.append(contents[4]);
        stringBuilderResult.append(IntercepterConstant.quatation);

        stringBuilderResult.append(IntercepterConstant.comma);

        //ua
        stringBuilderResult.append(IntercepterConstant.Item.UA);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(IntercepterConstant.quatation);
        stringBuilderResult.append(contents[5]);
        stringBuilderResult.append(IntercepterConstant.quatation);

        stringBuilderResult.append(IntercepterConstant.comma);

        //region
        stringBuilderResult.append(IntercepterConstant.Item.REGION);
        stringBuilderResult.append(IntercepterConstant.coloa);
        stringBuilderResult.append(IntercepterConstant.quatation);
        stringBuilderResult.append(IpHelper.findRegionByIp(contents[4]));
        stringBuilderResult.append(IntercepterConstant.quatation);

        stringBuilderResult.append(IntercepterConstant.rightBrace);

        logger.info("converJson lastResult:" + new String(stringBuilderResult));

        return stringBuilderResult.toString().getBytes();
      } catch (Exception e) {
        logger.info("converJson body:" + new String(body));
        logger.error("converJson e:" + e);
        return null;
      }
  }

    /**
     * 为了提高效率，减少string的对象。新写了一个split的方法。
     * @param str 待分割的字符串
     * @param split 分割字符
     * @param limit 要分割成几部分，所给字符串必须可以分割成至少limit部分，否则exception。
     * @return
     */
    private static StringBuilder[] splitNew(String str,char split,int limit){
      StringBuilder[] stringReturn = new StringBuilder[limit];

      for (int i=0;i<limit;i++){
        int index = str.indexOf(split);

        if (index<0 && i!=limit-1){
          throw new StringIndexOutOfBoundsException();
        }

        if (i==limit-1){ //分割最后一个需要特别注意

          stringReturn[i] = getSplitItem(str,':');

        }else {
          stringReturn[i] = getSplitItem(str.substring(0, index),':');
          str = str.substring(index + 1);
        }
      }

      return stringReturn;
    }

    /**
     * 获取key:value,中的value
     * @param str
     * @param split
     * @return
     */
    private static StringBuilder getSplitItem(String str,char split){
      int index = str.indexOf(split);
      return new StringBuilder(str.substring(index+1));
    }

  /**
   * Returns the set of events which pass filters, according to
   * {@link #intercept(Event)}.
   * @param events
   * @return
   */
  @Override
  public List<Event> intercept(List<Event> events) {
    List<Event> out = Lists.newArrayList();
    for (Event event : events) {
      Event outEvent = intercept(event);
      if (outEvent != null) {
        out.add(outEvent);
      }
    }
    return out;
  }

  @Override
  public void close() {
    // no-op
  }

  /**
   * Builder which builds new instance of the StaticInterceptor.
   */
  public static class Builder implements Interceptor.Builder {

    private Pattern regex;
    private boolean excludeEvents;

    @Override
    public void configure(Context context) {
      String regexString = context.getString(REGEX, DEFAULT_REGEX);
      regex = Pattern.compile(regexString);
      excludeEvents = context.getBoolean(EXCLUDE_EVENTS,
          DEFAULT_EXCLUDE_EVENTS);
    }

    @Override
    public Interceptor build() {
      logger.info(String.format(
          "Creating RegexFilteringInterceptor: regex=%s,excludeEvents=%s",
          regex, excludeEvents));
      return new RegexFilteringInterceptor(regex, excludeEvents);
    }
  }

  public static class Constants {

    public static final String REGEX = "regex";
    public static final String DEFAULT_REGEX = ".*";

    public static final String EXCLUDE_EVENTS = "excludeEvents";
    public static final boolean DEFAULT_EXCLUDE_EVENTS = false;
  }

}