package com.bimbiya.server.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@MappedSuperclass
public class CommonEntity
{

    @Column(name = "CREATED_USER", nullable = false, length = 64)
    private String createdUser;
    @Column(name = "LAST_UPDATED_USER", nullable = false, length = 64)
    private String lastUpdatedUser;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false, length = 23)
    private Date createdTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED_TIME", nullable = false, length = 23)
    private Date lastUpdatedTime;


}
