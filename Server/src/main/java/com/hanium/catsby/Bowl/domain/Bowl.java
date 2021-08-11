package com.hanium.catsby.Bowl.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanium.catsby.Bowl.domain.BowlUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Bowl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bowl_id")
    private Long id;

    @Lob
    private String info;
    private String name;

    //찾아보기
    private String address;

    @Lob
    private byte[] image;

    @Column(name = "created_time")
    private LocalDateTime createDate;

    @Column(name = "updated_time")
    private LocalDateTime updateDate;

    @JsonIgnore
    @OneToMany(mappedBy = "bowl", cascade = CascadeType.ALL)
    private List<BowlCommunity> bowlCommunities = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bowl", cascade = CascadeType.ALL)
    private List<BowlUser> bowlUsers = new ArrayList<>();

}
