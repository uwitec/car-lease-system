/**  
 * @Filename:    AlarmSettingConverter.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-6 上午11:03:23  
 *
 */  

package com.org.dqgb.conversion;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ognl.DefaultTypeConverter;

import com.org.dqgb.bean.AlarmConfiguration;
import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.AlarmParam;

@SuppressWarnings("rawtypes")
public class AlarmSettingConverter extends DefaultTypeConverter {

	/**
	 * 
	 * @TODO	自定义类型转换器，实现参数数据和实体类之间的转换
	 * 
	 */
	@Override
	public Object convertValue(Map context, Object target, Member member,
			String propertyName, Object value, Class toType) {
		String[] v = (String[]) value;
		if(null == v[0] || "".equals(v[0])){
			return null;
		}
		JSONObject json = JSONObject.fromObject(v[0]);
		JSONArray confArr = json.getJSONArray(Constant.CONFIGURATION);
		List<AlarmConfiguration> list = new ArrayList<AlarmConfiguration>();
		for(int i = 0; i < confArr.size(); i++){
			JSONObject jO = (JSONObject) confArr.get(i);							//获取单一配置
			AlarmConfiguration acf = new AlarmConfiguration();
			list.add(acf);
			acf.setType(jO.getInt(Constant.TYPE));
			JSONArray apl = null;
			try{
				apl = jO.getJSONArray(Constant.APL);
			}catch(Exception e){
				continue;
			}
			List<AlarmParam> lap = new ArrayList<AlarmParam>();
			for(int j = 0; j < apl.size(); j++){
				AlarmParam ap = (AlarmParam) JSONObject.toBean(apl.getJSONObject(j),AlarmParam.class);
				lap.add(ap);
			}
			acf.setApl(lap);
		}
		return list;
	}
}
