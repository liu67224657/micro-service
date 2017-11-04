package com.enjoyf.platform.contentservice.web.rest.util;

import com.enjoyf.platform.common.util.CollectionUtil;
import com.enjoyf.platform.common.util.md5.MD5Util;
import com.enjoyf.platform.contentservice.config.WebAppConfig;
import com.enjoyf.platform.contentservice.domain.Content;
import com.enjoyf.platform.contentservice.domain.FiledValue;
import com.enjoyf.platform.contentservice.domain.UserCollect;
import com.enjoyf.platform.contentservice.domain.enumeration.*;
import com.enjoyf.platform.contentservice.service.ContentService;
import com.enjoyf.platform.contentservice.service.GameService;
import com.enjoyf.platform.contentservice.service.dto.UserCollectDTO;
import com.enjoyf.platform.contentservice.service.profileservice.ProfileServiceFeignClient;
import com.enjoyf.platform.contentservice.service.userservice.UserProfileFeignClient;
import com.enjoyf.platform.contentservice.web.rest.vm.ContentVM;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by zhimingli on 2017/5/12.
 */
@Component
public class AskUtil {


    private static final String SEARCH_CORE_WIKIAPP = "wikiapp";
    private static final String SEARCH_KEY_PARAM_TITLE = "title";
    private static final String SEARCH_KEY_PARAM_TYPE = "type";
    private static final String SEARCH_KEY_PARAM_ID = "id";
    private static final String SEARCH_KEY_PARAM_CREATETIME = "createtime";

    private final static LocalDate date = LocalDate.now();

    @Autowired
    private WebAppConfig webAppConfig;

    @Autowired
    private UserProfileFeignClient userProfileFeignClient;

    @Autowired
    private ProfileServiceFeignClient profileServiceFeignClient;

    @Autowired
    private GameService gameService;

    @Autowired
    private RestUtil restUtil;


    public static String getContentLineKey(ContentLineOwn contentLineOwn, String gameid) {
        //今天的文章line
        if (contentLineOwn.getCode() == ContentLineOwn.TODAY_ALL_ARCHIVE.getCode()) {
            return MD5Util.Md5(contentLineOwn.getCode() + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ContentLineType.CONTENTLINE_ARCHIVE.getCode());
        } else if (contentLineOwn.getCode() == ContentLineOwn.GAME_ALL_ARCHIVE.getCode()) { //游戏对应的文章lien
            return MD5Util.Md5(contentLineOwn.getCode() + gameid + ContentLineType.CONTENTLINE_ARCHIVE.getCode());
        }
        return MD5Util.Md5(contentLineOwn.getCode() + "" + ContentLineType.CONTENTLINE_ARCHIVE.getCode());
    }

    public static String getWikiappSearchEntryId(String id, int type) {
        return MD5Util.Md5(id + type);
    }


    public static String getAdvertiseLinekey(String appkey, Integer platform, AdvertiseDomain advertiseDomain) {
        return MD5Util.Md5(appkey + platform + advertiseDomain.getCode());
    }


    public List<UserCollectDTO> buildCollect(List<UserCollect> list, ContentService contentService) throws ServiceException {
        List<UserCollectDTO> returnList = new ArrayList<>();
        Set<Long> contentIds = new HashSet<Long>();
        if (CollectionUtil.isEmpty(list)) {
            return returnList;
        }
        for (UserCollect userCollect : list) {
            contentIds.add(userCollect.getContentId());
        }

        //查询文章信息
        StringBuffer strBuff = new StringBuffer();
        Map<Long, Content> contentMap = contentService.queryContentByUserCollect(contentIds);
        for (Long contentId : contentMap.keySet()) {
            Content content = contentMap.get(contentId);
            if (!content.getSource().equals(ContentSource.WIKI)) {
                strBuff.append(content.getGameId() + ",");
            }
        }

        JSONObject gameJson = new JSONObject();
        if (!StringUtils.isEmpty(strBuff.toString())) {
            gameJson = queryGameById(strBuff.substring(0, strBuff.lastIndexOf(",")));
        }

        for (UserCollect userCollect : list) {
            Content content = contentMap.get(userCollect.getContentId());
            if (content == null) {
                continue;
            }
            UserCollectDTO collectDTO = new UserCollectDTO();
            collectDTO.setCtype(String.valueOf(content.getSource()));
            collectDTO.setTime(userCollect.getCreateTime().getTime());
            collectDTO.setGamename(content.getTitle());
            collectDTO.setId(userCollect.getId());
            collectDTO.setJt("-2");//wap页为负2
            collectDTO.setJi(content.getWebUrl());
            if (ContentSource.WIKI.equals(content.getSource())) {
                collectDTO.setDiscussion(content.getDescription());
            } else {
                collectDTO.setDiscussion(gameJson.get(content.getGameId()) == null ? "" : gameJson.getString(content.getGameId()));
            }
            returnList.add(collectDTO);
        }

        return returnList;
    }

    /**
     * @param ids gameId  【,】分隔 例如： 10001,10002
     * @return
     */
    public JSONObject queryGameById(String ids) {
        JSONObject gameJson = new JSONObject();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gameids", ids);
        String responseBody = restUtil.query(queryGameNameByIdsURI(), paramMap);
        JSONObject jsonObject = JSONObject.fromObject(responseBody);
        if (jsonObject.containsKey("rs") && "1".equals(jsonObject.get("rs")) && jsonObject.containsKey("result")) {
            gameJson = jsonObject.getJSONObject("result");
        }
        return gameJson;
    }

    private String queryGameNameByIdsURI() {
        return webAppConfig.getUrl_wikiser() + "/api/wiki/game/querygame?gameids={gameids}";
    }

    private String checkUserForbidStatusURL() {
        return webAppConfig.getUrl_api() + "/jsoncomment/reply/checkForbidStatus?pid={pid}";
    }

    public boolean checkUserForbidStatus(String profileId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pid", profileId);
        String responseBody = restUtil.query(checkUserForbidStatusURL(), paramMap);
        JSONObject jsonObject = JSONObject.fromObject(responseBody);
        if (jsonObject.containsKey("rs") && "1".equals(jsonObject.get("rs"))) {
            return true;
        }
        return false;
    }


    public Map<String, CommentBeanDTO> queryCommentbeanById(String ids) {
        Map<String, CommentBeanDTO> returnMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("commentids", ids);
        String responseBody = restUtil.query(queryCommentbeanByIdsURI(), paramMap);
        JSONObject jsonObject = JSONObject.fromObject(responseBody);
        if (jsonObject.containsKey("rs") && "1".equals(jsonObject.get("rs")) && jsonObject.containsKey("result")) {
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                String commentid = jsonArray.getJSONObject(i).getString("commentid");
                returnMap.put(commentid, new CommentBeanDTO(commentid, jsonArray.getJSONObject(i).getInt("totalRows")));
            }
        }
        return returnMap;
    }


    private String queryCommentbeanByIdsURI() {
        return webAppConfig.getUrl_api() + "/comment/api/commentbean?commentids={commentids}";
    }


    public Page<FiledValue> searchByType(WikiappSearchType type, Pageable pageable, String... texts) {
        Page<FiledValue> pageRows = new PageImpl<FiledValue>(new ArrayList<>(), pageable, 0);
        if (texts == null || texts.length == 0) {
            return pageRows;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String text : texts) {
            stringBuffer.append("(").append(SEARCH_KEY_PARAM_TITLE).append(":").append(text).append(")");
        }
        if (type != null) {
            stringBuffer.append("(").append(SEARCH_KEY_PARAM_TYPE).append(":").append(type.getCode()).append(")");
        }
        try {
            JSONObject gameJson = new JSONObject();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("c", SEARCH_CORE_WIKIAPP);
            paramMap.put("q", stringBuffer.toString());
            paramMap.put("p", pageable.getPageNumber());
            paramMap.put("ps", pageable.getPageSize());
            String responseBody = restUtil.query(webAppConfig.getSolr_query() + "?c={c}&q={q}&p={p}&ps={ps}", paramMap);
            JSONObject jsonObject = new JSONObject().fromObject(responseBody);
            //返回值是错误
            if (jsonObject.getInt("rs") != 1) {
                return pageRows;
            }
            JSONObject pageObject = jsonObject.getJSONObject("page");
            int total = pageObject.getInt("total");
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //返回值为空
            if (jsonArray == null) {
                return pageRows;
            }
            Object[] resultObject = jsonArray.toArray();
            List<FiledValue> returnList = new ArrayList<FiledValue>();
            for (Object object : resultObject) {
                JSONObject jsonobj = (JSONObject) object;
                try {
                    FiledValue filedValue = new FiledValue(jsonobj.getString(SEARCH_KEY_PARAM_ID), jsonobj.getString(SEARCH_KEY_PARAM_CREATETIME));
                    returnList.add(filedValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pageRows = new PageImpl<FiledValue>(returnList, pageable, total);
            return pageRows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageRows;
    }


    public class CommentBeanDTO implements Serializable {
        private String commentid;
        private int totalRows;

        public String getCommentid() {
            return commentid;
        }

        public void setCommentid(String commentid) {
            this.commentid = commentid;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public CommentBeanDTO(String commentid, int totalRows) {
            this.commentid = commentid;
            this.totalRows = totalRows;
        }

        public CommentBeanDTO() {
        }
    }

    /**
     * 获取主站的轮播图
     *
     * @param platform
     * @return
     */
    public List<ContentVM> getSlideshow(Integer platform) {
        List<ContentVM> returnList = new ArrayList<ContentVM>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("platform", platform);
        try {
            String responseBody = restUtil.query(webAppConfig.getUrl_api() + "/api/wiki/content/slideshow?platform={platform}", paramMap);
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            if (jsonObject.containsKey("rs") && "1".equals(jsonObject.get("rs")) && jsonObject.containsKey("result")) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                String returnStr = jsonArray.toString();
                if (!StringUtils.isEmpty(returnStr)) {
                    JsonArray Jarray = new JsonParser().parse(returnStr).getAsJsonArray();
                    for (JsonElement obj : Jarray) {
                        returnList.add(new Gson().fromJson(obj, ContentVM.class));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnList;

    }
}
