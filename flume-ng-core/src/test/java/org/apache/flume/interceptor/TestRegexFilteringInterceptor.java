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

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.RegexFilteringInterceptor.Constants;
import org.apache.flume.ip.IpHelper;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;

public class TestRegexFilteringInterceptor {

  @Test
  /** By default, we should pass through any event. */
  public void testDefaultBehavior() throws ClassNotFoundException,
      InstantiationException, IllegalAccessException {
    Interceptor.Builder builder = InterceptorBuilderFactory.newInstance(
        InterceptorType.REGEX_FILTER.toString());
    builder.configure(new Context());
    Interceptor interceptor = builder.build();

    Event event = EventBuilder.withBody("test", Charsets.UTF_8);

    Event filteredEvent = interceptor.intercept(event);
    Assert.assertNotNull(filteredEvent);
    Assert.assertEquals(event, filteredEvent);
  }

  @Test
  public void testInclusion() throws ClassNotFoundException,
      InstantiationException, IllegalAccessException {
    Interceptor.Builder builder = InterceptorBuilderFactory.newInstance(
        InterceptorType.REGEX_FILTER.toString());

    Context ctx = new Context();
    ctx.put(Constants.REGEX, "(INFO.*)|(WARNING.*)");
    ctx.put(Constants.EXCLUDE_EVENTS, "false");

    builder.configure(ctx);
    Interceptor interceptor = builder.build();

    Event shouldPass1 = EventBuilder.withBody("INFO: some message",
        Charsets.UTF_8);
    Assert.assertNotNull(interceptor.intercept(shouldPass1));

    Event shouldPass2 = EventBuilder.withBody("WARNING: some message",
        Charsets.UTF_8);
    Assert.assertNotNull(interceptor.intercept(shouldPass2));

    Event shouldNotPass = EventBuilder.withBody("DEBUG: some message",
        Charsets.UTF_8);
    Assert.assertNull(interceptor.intercept(shouldNotPass));

    builder.configure(ctx);
  }

  @Test
  public void testExclusion() throws ClassNotFoundException,
      InstantiationException, IllegalAccessException {
    Interceptor.Builder builder = InterceptorBuilderFactory.newInstance(
        InterceptorType.REGEX_FILTER.toString());

    Context ctx = new Context();
    ctx.put(Constants.REGEX, ".*DEBUG.*");
    ctx.put(Constants.EXCLUDE_EVENTS, "true");

    builder.configure(ctx);
    Interceptor interceptor = builder.build();

    Event shouldPass1 = EventBuilder.withBody("INFO: some message",
        Charsets.UTF_8);
    Assert.assertNotNull(interceptor.intercept(shouldPass1));

    Event shouldPass2 = EventBuilder.withBody("WARNING: some message",
        Charsets.UTF_8);
    Assert.assertNotNull(interceptor.intercept(shouldPass2));

    Event shouldNotPass = EventBuilder.withBody("this message has DEBUG in it",
        Charsets.UTF_8);
    Assert.assertNull(interceptor.intercept(shouldNotPass));

    builder.configure(ctx);
  }

  @Test
  public void testLogBehavior() throws ClassNotFoundException,
          InstantiationException, IllegalAccessException {

    IpHelper.buildTrain();

    Interceptor.Builder builder = InterceptorBuilderFactory.newInstance(
            InterceptorType.REGEX_FILTER.toString());
    builder.configure(new Context());
    Interceptor interceptor = builder.build();

    Event event = EventBuilder.withBody("2017-02-20 20:38:36 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]", Charsets.UTF_8);

    Event filteredEvent = interceptor.intercept(event);
    Event filteredEvent1 = interceptor.intercept(EventBuilder.withBody("2017-02-20 20:38:36 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]", Charsets.UTF_8));
    Event filteredEvent2 = interceptor.intercept(EventBuilder.withBody("2017-02-20 20:38:36 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]", Charsets.UTF_8));
    Event filteredEvent3 = interceptor.intercept(EventBuilder.withBody("2017-02-20 20:38:36 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]", Charsets.UTF_8));
    Event filteredEvent4 = interceptor.intercept(EventBuilder.withBody("2017-02-20 20:38:36 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]", Charsets.UTF_8));
    Event filteredEvent5 = interceptor.intercept(EventBuilder.withBody("2017-02-20 20:38:36 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]", Charsets.UTF_8));
    Event filteredEvent6 = interceptor.intercept(EventBuilder.withBody("2017-02-20 20:38:36 [[opt:getImg,spreadId:4,demandId:10,platform:null,ip:114.113.197.131,ua:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36]]", Charsets.UTF_8));
    Assert.assertNotNull(filteredEvent);
    Assert.assertEquals(event, filteredEvent);
  }
}