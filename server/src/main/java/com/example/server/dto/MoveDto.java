package com.example.server.dto;

import lombok.Data;

@Data
public class MoveDto {
    private long id;

    private byte c;

    private byte r;

    private byte turnPlayer;
}
