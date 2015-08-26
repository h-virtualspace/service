package com.demo.service.mapper;

import java.util.List;
import java.util.Map;
/**
 * 测试mapper
 * @author koujp
 *
 */
public interface TestMapper {
	public List<Map<String,String>> getAll();
	public boolean add(Map<String,String> map);
}
