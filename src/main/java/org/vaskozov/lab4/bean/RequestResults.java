package org.vaskozov.lab4.bean;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "RESULTS")
@Data
public class RequestResults implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Expose
    @Column(name = "x")
    private double x;

    @Expose
    @Column(name = "y")
    private double y;

    @Expose
    @Column(name = "r")
    private double r;

    @Expose
    @Column(name = "in_area")
    private boolean inArea;

    @Expose
    @Column(name = "execution_time_ns")
    private long executionTimeNs;
}
