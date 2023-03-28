package com.lab_team_projects.my_walking_pet.app;

public class ConnectionProtocol {
    // 리턴 코드
    public static final String SUCCESS = "S00"; // 성공
    public static final String NOT_FOUND_EMAIL = "A01"; // DB에서 이메일을 찾을 수 없을 때
    public static final String NOT_AUTH_EMAIL = "A02"; // 인증받지 않은 이메일인 경우
    public static final String NOT_CORRECT_PASSWORD = "A03"; // 올바르지 않은 비밀번호 일 때
    public static final String EMAIL_IS_DUPLICATION = "A04"; // 이메일이 중복되었을 때
    public static final String EMAIL_SEND_FAIL = "A05"; // 인증 메일 전송 실패
    public static final String INVALID_ACCESS_TOKEN = "A06"; // 엑세스토큰이 유효하지 않은 경우

    public static final String INVALID_IMAGE_DATA = "D01"; // 잘못된 이미지 파일


    // 요청 url
    public static final String SIGN_UP = "sign-up";
    public static final String CHECK_EMAIL_DUPLICATION = "check-email-duplication";
    public static final String LOGIN = "login";
    public static final String PROFILE_SETTING = "users/profile/edit";
    public static final String PASSWORD_RESET_REQUEST = "password-reset-request";
    public static final String PASSWORD_RESET = "password-reset";
    public static final String PROFILE_EDIT = "user/profile/edit";
    public static final String PROFILE_VIEW = "user/profile/view";
    public static final String PROFILE_PHOTO_UPLOAD = "user/profile_photo/upload";
    public static final String PROFILE_PHOTO_VIEW = "user/profile_photo/view";




    public static final String LOGIN_TEST = "/login-test"; // 로그인 테스트

}
