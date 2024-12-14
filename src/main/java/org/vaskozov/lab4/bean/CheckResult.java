package org.vaskozov.lab4.bean;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "CHECK_RESULTS")
@Data
@NoArgsConstructor
public class CheckResult implements Serializable {
    @Builder
    public CheckResult(double x, double y, double r, boolean inArea, long executionTimeNs) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.inArea = inArea;
        this.executionTimeNs = executionTimeNs;
    }

    @Builder(builderMethodName = "builderWithUserId", builderClassName = "BuilderWithUserId")
    public CheckResult(long userId, double x, double y, double r, boolean inArea, long executionTimeNs) {
        this(x, y, r, inArea, executionTimeNs);
        this.userId = userId;
    }

    @Id
    @JsonbTransient
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonbTransient
    @Column(name = "user_id")
    private long userId;

    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private double y;

    @Column(name = "r")
    private double r;

    @Column(name = "in_area")
    private boolean inArea;

    @Column(name = "execution_time_ns")
    private long executionTimeNs;
}
