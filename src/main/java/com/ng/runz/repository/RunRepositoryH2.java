package com.ng.runz.repository;

import com.ng.runz.model.Coordinate;
import com.ng.runz.model.Runs;
import com.ng.runz.model.Users;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class RunRepositoryH2 {

    private final JdbcClient jdbcClient;
    public RunRepositoryH2(JdbcClient jdbcClient){
        this.jdbcClient=jdbcClient;
    }


    public boolean createNewRun(Runs runs, Users user){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO coordinate(longitude,latitude) VALUES(:startPointLong,:startPointLat)")
                .param("startPointLong",runs.getStartPoint().getLongitude())
                .param("startPointLat",runs.getStartPoint().getLatitude())
                .update(keyHolder);
        Long startPointId = keyHolder.getKey().longValue();

        jdbcClient.sql("INSERT INTO coordinate(longitude,latitude) VALUES(:endPointLong,:endPointLat)")
                .param("endPointLong",runs.getEndPoint().getLongitude())
                .param("endPointLat",runs.getEndPoint().getLatitude())
                .update(keyHolder);
        Long endPointId = keyHolder.getKey().longValue();

        int effectedRow = jdbcClient.sql("INSERT INTO runs(start_time,end_time,miles,user_id,start_point_id,end_point_id) VALUES(:startTime,:endTime,:miles,:userId,:startPointId,:endPointId)")
                .param("startTime",runs.getStartTime())
                .param("endTime",runs.getEndTime())
                .param("miles",runs.getMiles())
                .param("userId",user.getId())
                .param("startPointId",startPointId)
                .param("endPointId",endPointId)
                .update();

        return effectedRow>0;
    }


    public List<Runs> findAllRunByUserId(Long userId){

        return jdbcClient.sql("SELECT r.id AS id, r.start_time AS start_time, r.end_time AS end_time,r.miles AS miles, c1.longitude AS start_point_longitude, c1.latitude AS start_point_latitude, c2.longitude AS end_point_longitude, c2.latitude AS end_point_latitude FROM runs r INNER JOIN coordinate c1 ON r.start_point_id=c1.id INNER JOIN coordinate c2 ON r.end_point_id=c2.id WHERE r.user_id=:userId").param("userId",userId).query((rs,rsNum)->{
            Runs runsFound = new Runs();
            runsFound.setId(rs.getLong("id"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            runsFound.setStartTime(LocalDateTime.parse(rs.getString("start_time"),dateTimeFormatter));
            runsFound.setEndTime(LocalDateTime.parse(rs.getString("end_time"),dateTimeFormatter));
            runsFound.setMiles(rs.getDouble("miles"));
            Coordinate startPoint = new Coordinate(rs.getDouble("start_point_longitude"),rs.getDouble("start_point_latitude"));
            Coordinate endPoint = new Coordinate(rs.getDouble("end_point_longitude"),rs.getDouble("end_point_latitude"));
            runsFound.setStartPoint(startPoint);
            runsFound.setEndPoint(endPoint);
            return runsFound;
        }).list();
    }

    public Optional<Runs> findRunById(Long runId){
        return jdbcClient.sql("SELECT start_time,end_time,miles,user_id,start_point_id,end_point_id FROM runs WHERE id=:runId").param("runId",runId).query(Runs.class).stream().findFirst();
    }
}
