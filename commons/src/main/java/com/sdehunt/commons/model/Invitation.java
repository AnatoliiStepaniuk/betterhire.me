package com.sdehunt.commons.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Invitation {

    private String inviter;

    private String invitee;

    private Instant when;

    private int terms;

    private InvitationStatus status;

}
