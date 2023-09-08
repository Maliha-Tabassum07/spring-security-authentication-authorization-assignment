package com.maliha.securitypractice.constants;

public class AppConstants {
    public static final String TOKEN_SECRET = "MySecretMySecretMySecretMySecretMySecretMySecretMySecretMySecret";
    public static final long EXPIRATION_TIME = 864000000; //10 days

    public static final String SIGN_IN = "/login";
    public static final String SIGN_UP = "/users/registration";
    public static final String HEADER_STRING = "token";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String DEFAULT_ROLE = "ROLE_USER";
    public static final String[] ADMIN_ACCESS = {"ROLE_ADMIN", "ROLE_MODERATOR"};
    public static final String[] MODERATOR_ACCESS = {"ROLE_MODERATOR"};
}
