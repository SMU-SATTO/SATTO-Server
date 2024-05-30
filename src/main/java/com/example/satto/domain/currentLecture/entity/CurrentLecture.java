package com.example.satto.domain.currentLecture.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "test")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentLecture {
    @Id
    @Column(name = "SBJ_DIVCLS")
    private String sbjDivcls;

    @Column(name = "SBJ_NO")
    private String sbjNo;

    @Column(name = "SBJ_NM")
    private String sbjNm;

    @Column(name = "LECT_TIME_ROOM")
    @Nullable
    private String lectTimeRoom;

    @Column(name = "CMP_DIV_RCD")
    private String cmpDivRcd;

    @Column(name = "THEO_TIME")
    private int theoTime;

    @Column(name = "ATTC_FILE_NO")
    private String attcFileNo;

    @Column(name = "DIVCLS")
    private int divcls;

    @Column(name = "TLSN_RMK")
    private String tlsnRmk;

    @Column(name = "CDT")
    private int cdt;

    @Column(name = "SES_RCD")
    private String sesRcd;

    @Column(name = "CMP_DIV_NM")
    private String cmpDivNm;

    @Column(name = "CYBER_YN")
    private String cyberYn;

    @Column(name = "CYBER_B_YN")
    private String cyberBYn;

    @Column(name = "SCH_YEAR")
    private String schYear;

    @Column(name = "PRAC_TIME")
    private int pracTime;

    @Column(name = "CYBER_S_YN")
    private String cyberSYn;

    @Column(name = "FILE_PBY_YN")
    private String filePbyYn;

    @Column(name = "KIND_RCD")
    private String kindRcd;

    @Column(name = "STAFF_NM")
    private String staffNm;

    @Column(name = "DEPT_CD")
    private String deptCd;

    @Column(name = "RMK")
    private String rmk;

    @Column(name = "CYBER_E_YN")
    private String cyberEYn;

    @Column(name = "REP_STAFF_NO")
    private String repStaffNo;

    @Column(name = "EST_DEPT_INFO")
    private String estDeptInfo;

    @Column(name = "SMT_RCD")
    private String smtRcd;

    @Column(name = "CRS_SHYR")
    private int crsShyr;

    @Column(name = "KIND_NM")
    private String kindNm;

    @Column(name = "BEF_CTNT_02")
    private String befCtnt02;

    @Column(name = "BEF_CTNT_01")
    private String befCtnt01;
}

