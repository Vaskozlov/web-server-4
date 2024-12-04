package org.vaskozov.lab4.bean;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "RESULTS")
@Data
@NoArgsConstructor
public class RequestResults implements Serializable {
    @Builder
    RequestResults(double x, double y, double r, boolean inArea, long executionTimeNs) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.inArea = inArea;
        this.executionTimeNs = executionTimeNs;
    }

    @Builder(builderMethodName = "builderWithUserId", builderClassName = "BuilderWithUserId")
    RequestResults(long userId, double x, double y, double r, boolean inArea, long executionTimeNs) {
        this(x, y, r, inArea, executionTimeNs);
        this.userId = userId;
    }

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
