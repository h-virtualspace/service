package com.virtualspace.service.social.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.here.framework.service.BaseService;
import com.here.framework.service.ServiceException;
import com.virtualspace.database.dao.PositionMapper;
import com.virtualspace.database.dao.StoryMapper;
import com.virtualspace.database.model.PosQueryCondition;
import com.virtualspace.database.model.Position;
import com.virtualspace.database.model.Story;
import com.virtualspace.service.common.UserService;
import com.virtualspace.service.social.SocialAllItem;
import com.virtualspace.service.social.SocialService;
import com.virtualspace.util.DistanceUtil;

public class SocialServiceImp extends BaseService implements SocialService
{
	@Override
	public List<SocialAllItem> socialAllNoUser(double lon, double lat, int distance, int limit, String sortType) throws ServiceException 
	{
		List<SocialAllItem> allItems = new ArrayList<>();
		if(limit == 0) 
		{
			limit = 50;
		}
		String vis = Story.STORY_VISI_STRANGER;
		int distancePerMix = 111000;
		
		PositionMapper positionMapper = this.getMapper(PositionMapper.class);
		double maxDisByl = (double)distance/distancePerMix;
		PosQueryCondition condition = new PosQueryCondition();
		condition.setLon(lon);
		condition.setLat(lat);
		condition.setMaxDisByl(maxDisByl);
		condition.setLimit(limit);
		condition.setSortType(sortType);
		List<Position> positions = positionMapper.getPositions(condition);
		List<String> pks = new ArrayList<>();
		Map<String, Position> cache = new HashMap<>();
		
		for(Position position : positions)
		{
			pks.add(position.getPk());
			cache.put(position.getPk(), position);
		}
		if(pks.size() == 0) return allItems;
		
		StoryMapper storyMapper = this.getMapper(StoryMapper.class);
		List<Story> stories = storyMapper.selectStoriedByVisiType(pks,vis);
		
		if(stories.size() == 0) return allItems;
		
		List<String> userPks = new ArrayList<>();
		for(Story story : stories)
		{
			userPks.add(story.getCreator());
		}
		UserService userService = this.getService(UserService.class);
		Map<String, String> userNameMap = userService.getUserNamesByPks(userPks);
		
		for (Story story : stories) 
		{
			SocialAllItem item = new SocialAllItem();
			item.setPk(story.getPk());
			item.setTag(story.getTag());
			item.setTitle(story.getTitle());
			item.setType(story.getType());
			item.setCreateTime(story.getCreateTime());
			item.setCreator(userNameMap.get(story.getCreator()));
			
			Position position = cache.get(story.getCreateLocation());
			double realDistance  = DistanceUtil.getDistance(lon, lat, position.getLongitude(), position.getLatitude());
			item.setDistance(String.valueOf(realDistance));
			
			allItems.add(item);
		}
		return allItems;
	}
}