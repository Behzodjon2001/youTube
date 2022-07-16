package com.company.repository;

import com.company.entity.PlaylistEntity;
import com.company.mapper.PlaylistFullInfo;
import com.company.mapper.PlaylistShortInfo;
import com.company.mapper.PlaylistVideoLimit2;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends PagingAndSortingRepository<PlaylistEntity, Integer> {

    boolean existsByIdAndVisible(Integer id, Boolean visible);

    @Transactional
    @Modifying
    @Query("update PlaylistEntity set visible = false where id = ?1")
    void changeStatus(Integer id);

    Optional<PlaylistEntity> findById(Integer cId);

    Optional<PlaylistEntity> findByVisible(boolean b);

    @Query(value = "SELECT p.uuid as playlist_id, p.name as playListName, p.created_date as playListCreatedDate, \n" +
            "      c.uuid as channleId, c.name as channelName, " +
            " (select count(*) from playlist_video  as pv where pv.playlist_id = p.uuid ) " +
            "     from  playlist as p " +
            " inner JOIN channel as c on p.channel_id = c.uuid " +
            "     where c.profile_id =1 " +
            "     and c.visible = true and p.visible = true ", nativeQuery = true)
    List<PlaylistShortInfo> getPlayLists(@Param("profileId") Integer profileId);


    @Query(value = "SELECT p.uuid as playlistId, p.name as playListName, p.created_date as playListCreatedDate, " +
            "                       c.uuid as channelId, c.name as channelName, " +
            "                       (select cast(count(*) as int) from playlist_video  as pv where pv.playlist_id = p.uuid ) as countVideo " +
            "                   from  playlist as p " +
            "                   inner JOIN channel as c on p.channel_id = c.uuid " +
            "                   where c.uuid =:channelId " +
            "                   and c.visible and p.visible", nativeQuery = true)
    List<PlaylistShortInfo> getPlayListByChannelId(@Param("channelId") String channelId);

    @Query(value = "select pv.video_id as videoId, v.name as videoName " +
            "from playlist_video as pv " +
            "inner join video as v on pv.video_id = v.uuid " +
            "where pv.playlist_id =:plId " +
            "and pv.visible " +
            "and v.visible " +
            "order by pv.order_number , pv.created_date " +
            "limit 2", nativeQuery = true)
    List<PlaylistVideoLimit2> playlistShortInfoLimit2(@Param("plId") String playlistId);

    // id,name,video_count, total_view_count (shu play listdagi videolarni ko'rilganlar soni last_update_date

    @Query(value = "select pv.playlist_id as playlistId, pv.playlist_name, as playlistName " +
            "v.name as videoName, v.uuid as videoId," +
            " v.review_id as reviewId, " +
            "(select cast(count(pwv.*) as int) as viewCount " +
            "from profile_watch_video as pwv " +
            "where pwv.video_id = pv.video_id) " +
            "inner join video as v on pv.video_id = v.uuid " +
            "where pv.playlist_id = :plId "+
            "and pv.visible " +
            "and v.visible " +
            "order by pv.order_number , pv.created_date ",nativeQuery = true)
    List<PlaylistFullInfo> playlistFullInfoList(@Param("plId") String playlistId);


}
