package com.enjoyf.platform.contentservice.service.search;

import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameOperStatus;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Created by ericliu on 2017/8/21.
 */
public interface GameSearchService {

    Page<Long> searchGame(String name, Set<String> gameTag, GameType gameType,
                          GameOperStatus gameOperStatus,
                          Boolean isPc, Boolean isAndroid, Boolean isIOS,
                          Pageable pageable);

    boolean saveGame(Game game);

    boolean deleteGame(Long id);
}
