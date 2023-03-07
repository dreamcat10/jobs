package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.Floor;
import com.yyoung.jobs.entity.Room;
import lombok.Data;

import java.util.List;

@Data
public class Remark{
    private Floor floor;
    private List<Room> rooms;
}