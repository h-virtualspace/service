package com.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.demo.service.mapper.TestMapper;
import com.here.framework.service.BaseService;
import com.here.framework.util.JSONUtil;

/**
 * service demo
 * @author koujp
 *
 */
public class DemoServiceImpl extends BaseService implements DemoService{

	@Override
	public String getDemoString() {
		return "this is an demo string.";
	}

	@Override
	public DemoBean getDemo() {
		DemoBean bean=new DemoBean();
		bean.setName("demo tu");
		bean.setCode(520);
		return bean;
	}

	@Override
	public DemoBean updateDemo(DemoBean bean) {
		bean.setName(bean.getName()+"---updated!"+(new Date()).toLocaleString());
		bean.setCode(bean.getCode()+1);
		return bean;
	}

	@Override
	public String test(Map<String, String> meta) {
		System.out.println(meta);
		return meta.toString();
	}

	@Override
	public List<Map<String, String>> getAll() {
		TestMapper mapper=getMapper(TestMapper.class);
		return mapper.getAll();
	}

	@Override
	public String addBean(DemoBean bean, List<PersonDemo> persons) {
		// TODO Auto-generated method stub
		System.out.println(JSONUtil.toJson(bean));
		return JSONUtil.toJson(persons);
	}


}
