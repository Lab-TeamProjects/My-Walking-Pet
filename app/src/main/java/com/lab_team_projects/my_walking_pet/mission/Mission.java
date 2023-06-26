package com.lab_team_projects.my_walking_pet.mission;

/**
 * The type Mission.
 */
public class Mission {
    private String experience;
    private Integer maxValue;
    private Integer nowValue;
    private Integer reward;
    private enum kind {
        /**
         * Today kind.
         */
        TODAY,
        /**
         * Week kind.
         */
        WEEK
    }
}
