package com.enjoyf.platform.contentservice.service.search;

import com.enjoyf.platform.contentservice.config.WebAppConfig;
import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameOperStatus;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameType;
import com.enjoyf.platform.contentservice.service.dto.search.GameSearchDTO;
import com.enjoyf.platform.contentservice.service.mapper.SearchMapper;
import com.google.common.base.Strings;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;


/**
 * 搜索服务
 * Created by ericliu on 2017/8/21.
 */
@Service
public class GameSearchServiceImpl implements GameSearchService {
    private static final String GAME_CORE = "contentservice-game";
    private static final String FIELD_CREATETIME = "createTime";
    //
//    private final static LocalDate date = LocalDate.now();

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ALIASNAME = "aliasName";
    private static final String FIELD_GAMETAG = "gameTags";
    private static final String FIELD_ANDROID = "android";
    private static final String FIELD_OPERSTATUS = "operStatus";
    private static final String FIELD_GAMETYPE = "gameType";
    private static final String FIELD_IOS = "ios";
    private static final String FIELD_PC = "pc";


    private WebAppConfig webAppConfig;

    private RestTemplate restTemplate;

    public GameSearchServiceImpl(WebAppConfig webAppConfig, RestTemplate restTemplate) {
        this.webAppConfig = webAppConfig;
        this.restTemplate = restTemplate;
    }


    @Override
    public Page<Long> searchGame(String name, Set<String> gameTag, GameType gameType,
                                 GameOperStatus gameOperStatus,
                                 Boolean isPc, Boolean isAndroid, Boolean isIOS,
                                 Pageable pageable) {
        String searchCondition = buildCondition(name, gameTag, gameType, gameOperStatus, isPc, isAndroid, isIOS);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("c", GAME_CORE);
        paramMap.add("q", searchCondition);
        paramMap.add("p", String.valueOf(pageable.getPageNumber() + 1));
        paramMap.add("ps", String.valueOf(pageable.getPageSize()));
        paramMap.add("rule", "solr");
        JSONObject jsonObject = restTemplate.postForEntity(webAppConfig.getSolr_query(), buildFormRequest(paramMap), JSONObject.class).getBody();
//
        Page<Long> result = new PageImpl<>(new ArrayList<>(), pageable, 0);
        //返回值是错误
        if (jsonObject.getInt("rs") != 1) {
            return result;
        }
        JSONObject pageObject = jsonObject.getJSONObject("page");
        int total = pageObject.getInt("total");
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        //返回值为空
        if (jsonArray == null) {
            return result;
        }
        Object[] resultObject = jsonArray.toArray();
        List<Long> returnList = new ArrayList<>();
        for (Object object : resultObject) {
            JSONObject jsonobj = (JSONObject) object;
            try {
                returnList.add(jsonobj.getLong(FIELD_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result = new PageImpl<>(returnList, pageable, total);
        return result;
    }

    private String buildCondition(String name, Set<String> gameTags, GameType gameType, GameOperStatus gameOperStatus
        , Boolean isPc, Boolean isAndroid, Boolean isIOS) {
        StringBuffer condition = new StringBuffer();
        if (!Strings.isNullOrEmpty(name)) {
            condition.append("(").append(FIELD_NAME).append(":").append(name).append(" OR ").append(FIELD_ALIASNAME).append(":").append(name).append(")");
        }

        if (!CollectionUtils.isEmpty(gameTags)) {
            condition.append("(");
            int i = 0;
            for (String gameTag : gameTags) {
                condition.append(FIELD_GAMETAG).append(":").append(gameTag);

                if (i < gameTags.size() - 1) {
                    condition.append(" OR ");
                }
                i++;
            }
            condition.append(")").append(" AND ");
//            .append(FIELD_GAMETAG).append(":").append(String.join(" ", gameTag)).append(")");
        }

        if (gameType != null) {
            condition.append("(").append(FIELD_GAMETYPE).append(":").append(gameType).append(")").append(" AND ");;
        }
        if (gameOperStatus != null) {
            condition.append("(").append(FIELD_OPERSTATUS).append(":").append(gameOperStatus).append(")").append(" AND ");;
        }
        if (isPc != null) {
            condition.append("(").append(FIELD_PC).append(":").append(isPc).append(")").append(" AND ");;
        }
        if (isIOS != null) {
            condition.append("(").append(FIELD_IOS).append(":").append(isIOS).append(")").append(" AND ");;
        }
        if (isAndroid != null) {
            condition.append("(").append(FIELD_ANDROID).append(":").append(isAndroid).append(")").append(" AND ");;
        }

        return condition.substring(0,condition.lastIndexOf(" AND "));
    }

    public boolean saveGame(Game game) {
        try {
            GameSearchDTO searchDTO = SearchMapper.MAPPER.game2SearchDTO(game);
            if (searchDTO.getGameType() == null) {
                searchDTO.setGameType(GameType.UNKNOWN);
            }

            Set<String> gameTags = new HashSet<>();
            if (!Strings.isNullOrEmpty(game.getGameTag())) {
                for (String gameTagString : game.getGameTag().split(",")) {
                    gameTags.add(gameTagString);
                }
            }
            searchDTO.setGameTags(gameTags);

            String json = Jackson2ObjectMapperBuilder.json().build().writeValueAsString(searchDTO);

            MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("c", GAME_CORE);
            paramMap.add("field", json);

            JSONObject jsonObject = restTemplate.postForEntity(webAppConfig.getSolr_save(), buildFormRequest(paramMap), JSONObject.class).getBody();
            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteGame(Long id) {
        try {
            String deletQuery = "(id:" + id + ")";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("c", GAME_CORE);
            paramMap.put("q", deletQuery);
            String responseBody = restTemplate.postForObject(webAppConfig.getSolr_delete() + "?c={c}&q={q}", null, String.class, paramMap);
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private HttpEntity buildFormRequest(MultiValueMap<String, String> paramMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "*/*");

        return new HttpEntity<>(paramMap, headers);
    }
}
