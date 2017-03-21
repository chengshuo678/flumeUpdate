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
package org.apache.flume.ip;

import org.apache.flume.fileConfig.FileUtil;
import org.apache.flume.fileConfig.PoiUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对ip进行处理
 */
public class IpHelper {

    private static IpTree ipTree = IpTree.getInstance();

    private static final String ipFile = "ipDatabase.csv";

    private static final String regionFile = "ipRegion.xlsx";
    private static List<IpRelation> ipRelationList = null;

    public static void buildTrain() {

        if (ipRelationList!=null){
            return;
        }

        try {
            ipRelationList = IpHelper.getIpRelation();
            int count = 0;
            for (IpRelation ipRelation : ipRelationList) {
                ipTree.train(ipRelation.getIpStart(), ipRelation.getIpEnd(), ipRelation.getProvince());
                if(count > 10){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 静态方法，传入ip地址，返回ip地址所在城市或地区
     * @param ip    IP地址，例：58.30.15.255
     * @return  返回IP地址所在城市或地区，例：北京市
     */
    public static String findRegionByIp(String ip){
        return ipTree.findIp(ip);
    }

    public static String findRegionByIp(StringBuilder ip){
        return ipTree.findIp(ip.toString());
    }

    public static List<IpRelation> getIpRelation() throws Exception {

        // <ipCode, province>
        Map<Integer, String> regionRelationMap = getRegionRelationMap();
//        String file =  IpHelper.class.getClassLoader().getResource(ipFile).getFile();
//        BufferedReader ipRelationReader = FileUtil.readFile(file);

//        InputStream fileInputStream =  IpHelper.class.getClassLoader().getResourceAsStream(ipFile);
//        BufferedReader ipRelationReader = FileUtil.readInputStream(fileInputStream);

        BufferedReader ipRelationReader = FileUtil.readFile("/home/logs/"+ipFile);

        String line;
        List<IpRelation> list = new ArrayList<IpRelation>();

        while((line = ipRelationReader.readLine()) != null){

            StringBuilder[] split = getSplitItem(line,',');

            Integer ipCode = Integer.valueOf(split[2].toString());

            String province = regionRelationMap.get(ipCode);
            IpRelation ipRelation = new IpRelation();
            ipRelation.setIpStart(split[0].toString());
            ipRelation.setIpEnd(split[1].toString());
            ipRelation.setProvince(province);
            list.add(ipRelation);
        }
        return list;

    }

    /**
     * @return Map<ipCode, province>
     * @throws Exception
     */
    public static Map<Integer, String> getRegionRelationMap() throws Exception {
//        String file =  IpHelper.class.getClassLoader().getResource(regionFile).getFile();

//        Workbook workbook = PoiUtil.getWorkbook(file);
        Workbook workbook = PoiUtil.getWorkbook("/home/logs/"+regionFile);

        Sheet sheet = workbook.getSheetAt(0);
        Map<Integer, String> map = new HashMap<Integer, String>();
        int rowLen = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rowLen; i++) {
            Row row = sheet.getRow(i);
            String province = row.getCell(0).getStringCellValue();
            Double a = row.getCell(2).getNumericCellValue();
            Integer ipCode = a.intValue();
            map.put(ipCode, province);
        }

        return map;
    }

    /**
     * 获取key:value,中的value
     * @param str
     * @param split
     * @return
     */
    private static StringBuilder[] getSplitItem(String str,char split){

        StringBuilder[] stringBuilder = new StringBuilder[3];

        int index = str.indexOf(split);
        stringBuilder[0] = new StringBuilder(str.substring(0,index));

        int index1 = str.substring(index+1).indexOf(split);

        stringBuilder[1] = new StringBuilder(str.substring(index+1,index+1+index1));

        int index2 = str.substring(index+1+index1+1).indexOf(split);

        stringBuilder[2] = new StringBuilder(str.substring(index+1+index1+1,index+1+index1+1+index2));

        return stringBuilder;

    }
}
