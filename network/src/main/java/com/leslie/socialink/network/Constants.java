package com.leslie.socialink.network;

public class Constants {

//    public static String BASE_URL = "http://182.92.84.79:8081/xiangyu";
    public static final String BASE_URL = "http://8.138.85.81:6000/xiangyu";

    public static final String TOKEN_HEADER = "XIANGYU-ACCESS-TOKEN";
    public static final int DEFAULT_PS = 10;

    public static String token;
    public static int uid = 1;

    public static int clubId;       //当前团队id
    public static int creatorId;    //团队创建者id
    public static boolean isJoin;   //是否加入该团队
    public static boolean isAdmin;  //在当前团队是否管理员
    public static String userName;

    /**
     * 存储键盘高度
     */
    public static int keyboardHeight = 0;

    /**
     * Emojis 感情符号 80 个 <br>
     * <a href="https://apps.timwhitlock.info/emoji/tables/unicode">Emoji Unicode Tables</a>
     * --> <a href="https://apps.timwhitlock.info/unicode/inspect/hex/1F600-1F64F">Unicode character inspector</a>
     */
    public static String[] emojis = {"{D83D}{DE00}", "{D83D}{DE01}", "{D83D}{DE02}", "{D83D}{DE03}",
            "{D83D}{DE04}", "{D83D}{DE05}", "{D83D}{DE06}", "{D83D}{DE07}", "{D83D}{DE08}", "{D83D}{DE09}",
            "{D83D}{DE0A}", "{D83D}{DE0B}", "{D83D}{DE0C}", "{D83D}{DE0D}", "{D83D}{DE0E}", "{D83D}{DE0F}",
            "{D83D}{DE10}", "{D83D}{DE11}", "{D83D}{DE12}", "{D83D}{DE13}", "{D83D}{DE14}", "{D83D}{DE15}",
            "{D83D}{DE16}", "{D83D}{DE17}", "{D83D}{DE18}", "{D83D}{DE19}", "{D83D}{DE1A}", "{D83D}{DE1B}",
            "{D83D}{DE1C}", "{D83D}{DE1D}", "{D83D}{DE1E}", "{D83D}{DE1F}", "{D83D}{DE20}", "{D83D}{DE21}",
            "{D83D}{DE22}", "{D83D}{DE23}", "{D83D}{DE24}", "{D83D}{DE25}", "{D83D}{DE26}", "{D83D}{DE27}",
            "{D83D}{DE28}", "{D83D}{DE29}", "{D83D}{DE2A}", "{D83D}{DE2B}", "{D83D}{DE2C}", "{D83D}{DE2D}",
            "{D83D}{DE2E}", "{D83D}{DE2F}", "{D83D}{DE30}", "{D83D}{DE31}", "{D83D}{DE32}", "{D83D}{DE33}",
            "{D83D}{DE34}", "{D83D}{DE35}", "{D83D}{DE36}", "{D83D}{DE37}", "{D83D}{DE38}", "{D83D}{DE39}",
            "{D83D}{DE3A}", "{D83D}{DE3B}", "{D83D}{DE3C}", "{D83D}{DE3D}", "{D83D}{DE3E}", "{D83D}{DE3F}",
            "{D83D}{DE40}", "{D83D}{DE45}", "{D83D}{DE46}", "{D83D}{DE47}", "{D83D}{DE48}", "{D83D}{DE49}",
            "{D83D}{DE4A}", "{D83D}{DE4B}", "{D83D}{DE4C}", "{D83D}{DE4D}", "{D83D}{DE4E}", "{D83D}{DE4F}"};

}
