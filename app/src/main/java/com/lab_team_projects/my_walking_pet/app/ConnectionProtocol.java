package com.lab_team_projects.my_walking_pet.app;

/**
 * The type Connection protocol.
 */
public class ConnectionProtocol {
    /**
     * The constant SUCCESS.
     */
// 리턴 코드
    public static final String SUCCESS = "S00"; // 성공
    /**
     * The constant NOT_FOUND_EMAIL.
     */
    public static final String NOT_FOUND_EMAIL = "A01"; // DB에서 이메일을 찾을 수 없을 때
    /**
     * The constant NOT_AUTH_EMAIL.
     */
    public static final String NOT_AUTH_EMAIL = "A02"; // 인증받지 않은 이메일인 경우
    /**
     * The constant NOT_CORRECT_PASSWORD.
     */
    public static final String NOT_CORRECT_PASSWORD = "A03"; // 올바르지 않은 비밀번호 일 때
    /**
     * The constant EMAIL_IS_DUPLICATION.
     */
    public static final String EMAIL_IS_DUPLICATION = "A04"; // 이메일이 중복되었을 때
    /**
     * The constant EMAIL_SEND_FAIL.
     */
    public static final String EMAIL_SEND_FAIL = "A05"; // 인증 메일 전송 실패
    /**
     * The constant INVALID_ACCESS_TOKEN.
     */
    public static final String INVALID_ACCESS_TOKEN = "A06"; // 엑세스토큰이 유효하지 않은 경우

    /**
     * The constant INVALID_IMAGE_DATA.
     */
    public static final String INVALID_IMAGE_DATA = "D01"; // 잘못된 이미지 파일
    /**
     * The constant UNCORRECTABLE_DATA.
     */
    public static final String UNCORRECTABLE_DATA = "D02"; // 수정할 수 없는 데이터
    /**
     * The constant INVALUD_DATE.
     */
    public static final String INVALUD_DATE = "D03"; // 유효하지 않은 날짜
    /**
     * The constant INVALUD_DATE_RANGE.
     */
    public static final String INVALUD_DATE_RANGE = "D04"; // 잘못된 날짜 범위
    /**
     * The constant NOT_USER.
     */
    public static final String NOT_USER = "D05"; // 없는 유저
    /**
     * The constant NOT_FOUND_PROFILE.
     */
    public static final String NOT_FOUND_PROFILE = "D06"; // 프로필 사진 없음
    /**
     * The constant MYSELF_AN_ETERNAL_FRIEND.
     */
    public static final String MYSELF_AN_ETERNAL_FRIEND = "D07"; // 자기 자신 친구추가

    /**
     * The constant NOT_MY_PET.
     */
    public static final String NOT_MY_PET = "P01"; // 자기 자신의 동물이 아님

    /**
     * The constant FAIL.
     */
    public static final String FAIL = "F00"; // 걍 실패

    /**
     * The constant SIGN_UP.
     */
// 요청 url
    public static final String SIGN_UP = "sign-up";
    /**
     * The constant CHECK_EMAIL_DUPLICATION.
     */
    public static final String CHECK_EMAIL_DUPLICATION = "check-email-duplication";
    /**
     * The constant LOGIN.
     */
    public static final String LOGIN = "login";
    /**
     * The constant PROFILE_SETTING.
     */
    public static final String PROFILE_SETTING = "users/profile/edit";
    /**
     * The constant PASSWORD_RESET_REQUEST.
     */
    public static final String PASSWORD_RESET_REQUEST = "password-reset-request";
    /**
     * The constant PASSWORD_RESET.
     */
    public static final String PASSWORD_RESET = "password-reset";
    /**
     * The constant PROFILE_EDIT.
     */
    public static final String PROFILE_EDIT = "user/profile/edit";
    /**
     * The constant PROFILE_VIEW.
     */
    public static final String PROFILE_VIEW = "user/profile/view";
    /**
     * The constant PROFILE_PHOTO_UPLOAD.
     */
    public static final String PROFILE_PHOTO_UPLOAD = "user/profile_photo/upload";
    /**
     * The constant PROFILE_PHOTO_VIEW.
     */
    public static final String PROFILE_PHOTO_VIEW = "user/profile_photo/view";
    /**
     * The constant GAME_DATA_VIEW.
     */
    public static final String GET_PETS = "/users/pets";


    /**
     * The constant LOGIN_TEST.
     */
    public static final String LOGIN_TEST = "/login-test"; // 로그인 테스트

}
