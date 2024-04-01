package com.leslie.socialink.network;

public class Constants {

    public static String BASE_URL = "http://182.92.84.79:8081/xiangyu";
//    public static final String BASE_URL = "http://8.138.85.81:6000/xiangyu";

    public static final String TOKEN_HEADER = "XIANGYU-ACCESS-TOKEN";

    //    public static String BASE_URL="http://rxcpe2.natappfree.cc";
    public static String USER_INFO = BASE_URL + "/api/user/info.do";
    public static String SEND_QUESTION = BASE_URL + "/api/ask/base/save.do";
    public static String QUESTIONS = BASE_URL + "/api/ask/base/pglist.do";
    public static String QUESTION_DETAIL = BASE_URL + "/api/ask/base/askInfo.do";
    public static String ADVERTISEMENT = BASE_URL + "/api/pub/category/advertisement.do";
    public static String QUESTION_LIKES = BASE_URL + "/api/ask/base/likes.do";
    public static String QUESTION_FIRST = BASE_URL + "/api/ask/base/pgfirst.do";
    public static String QUESTION_SECOND = BASE_URL + "/api/ask/base/pgsecond.do";
    public static String QUESTION_COMMENT = BASE_URL + "/api/ask/base/comment.do";
    public static String QUESTIONS_TOP = BASE_URL + "/api/ask/base/top.do";
    public static String QUESTION_SAVE = BASE_URL + "/api/ask/base/favorite.do";
    public static String QUESTION_INFO = BASE_URL + "/api/ask/base/inform.do";
    public static String ZC_LIST = BASE_URL + "/api/ask/special/pglist.do";
    public static String ZC_COMMENT = BASE_URL + "/api/ask/special/comment.do";
    public static String ZC_LIKES = BASE_URL + "/api/ask/special/likes.do";
    public static String ZC_PG_COMMENT = BASE_URL + "/api/ask/special/pgComment.do";
    public static String ZC_PG_HOT_REVIEW = BASE_URL + "/api/ask/special/pgHotReview.do";
    public static String ZC_HOT_REVIEW_COMMENT = BASE_URL + "/api/ask/special/HotReviewComment.do";
    public static String ZC_DELETE_COMMENT = BASE_URL + "/api/ask/special/deleteComment.do";
    public static String MY_QUESTIONS = BASE_URL + "/api/ask/base/myPglist.do";
    public static String MY_FOOT_PRINT = BASE_URL + "/api/user/myfootprint.do";
    public static String MY_FAVORITE = BASE_URL + "/api/user/myfavorite.do";

    //发送私聊消息,查询私聊，删除私聊消息
    public static String SEND_MESSAGE = BASE_URL + "/api/chat/base/save.do";
    public static String SEARCH_MESSAGE = BASE_URL + "/api/chat/base/pglist.do";
    public static String DELETE_MESSAGE = BASE_URL + "/api/chat/base/deleteMychat.do";
    public static String DELETE_MESSAGE_BOX = BASE_URL + "/api/chat/base/deleteMyChatBox.do";
    public static String SEARCH_MESSAGE_BOX = BASE_URL + "/api/chat/base/myChatBox.do";


    public static String CHECK_ATTENTION = BASE_URL + "/api/follow/check.do";
    public static String SET_ATTENTION = BASE_URL + "/api/follow/save.do";
    public static String SEARCH_ATTENTION = BASE_URL + "/api/follow/pglist.do";
    public static String DELETE_ATTENTION = BASE_URL + "/api/follow/deleteMyFollow.do";

    //拉黑
    public static String SetPullTheBlack = BASE_URL + "/api/chat/base/black.do";
    public static String SearchPullTheBlack = BASE_URL + "/api/chat/base/myBlack.do";
    public static String CheckPullTheBlack = BASE_URL + "/api/chat/base/check.do";
    public static String DeletePullTheBlack = BASE_URL + "/api/chat/base/deleteMyBlack.do";


    //密码加团
    public static String CHECKJOINSECRET = BASE_URL + "/api/club/base/judge.do";
    public static String SETJOINSECRET = BASE_URL + "/api/club/base/setpwd.do";
    public static String DELETEJOINSECRET = BASE_URL + "/api/club/base/delpwd.do";
    public static String TEAMJOINSECRET = BASE_URL + "/api/club/base/joinpwd.do";
    public static String CLOSEJOINSECRET = BASE_URL + "/api/club/base/pwdswitch.do";
    public static String JOINWITHNOSECRET = BASE_URL + "/api/club/base/joinjudge.do";
//    public static String token="faa3ff1e-ffcf-413e-b2f6-29b4c78592ec944921d1-12da-4b83-80fe-5ae7a8ecb0dc";

    //------------二手市场所用到的接口-----------
    // 删除自己商品
    public static String SECOND_GOOD_DELETE_MY_GOODS = BASE_URL + "/api/goods/base/deleteMyGoods.do";
    // 商品详情
    public static String SECOND_GOOD_GOODS_INFO = BASE_URL + "/api/goods/base/goodsInfo.do";
    // 商品推荐
    public static String SECOND_GOOD_RECOMMEND = BASE_URL + "/api/recommend/recommendateProduct.do";
    // 商品二级分类
    public static String SECOND_GOOD_CLASSIFICATIONS = BASE_URL + "/api/catagory1/listCategory";
    //首页商品展示 分类商品展示
    public static String SECOND_GOODS = BASE_URL + "/api/goods/base/pglist.do";
    //发布商品接口
    public static String SECOND_GOOD_SAVE = BASE_URL + "/api/goods/base/save.do";
    //查询标签接口
    public static String SECOND_GOOD_LABEL = BASE_URL + "/api/pub/category/list.do";
    //商品点赞
    public static String SECOND_GOOD_LIKES = BASE_URL + "/api/goods/base/likes.do";
    //商品评论
    public static String SECOND_GOOD_DISCUSS = BASE_URL + "/api/goods/base/comment.do";
    //商品一级评论
    public static String SECOND_GOOD_DISCUSS_FIRST = BASE_URL + "/api/goods/base/pgfirst.do";
    //商品二级评论
    public static String SECOND_GOOD_DISCUSS_SECOND = BASE_URL + "/api/goods/base/pgsecond.do";
    //商品评论点赞
    public static String SECOND_GOOD_DISCUSS_DING = BASE_URL + "/api/goods/base/top.do";
    //商品收藏
    public static String SECOND_GOOD_FAVORITE = BASE_URL + "/api/goods/base/favorite.do";
    //我收藏的商品
    public static String MY_FAVORITE_GOODS = BASE_URL + "/api/user/myfavoriteGoods.do";
    //我发布的商品
    public static String MY_GOOD = BASE_URL + "/api/goods/base/myPglist.do";

    //---------------------交友--------------------
    //交友附近接口
    public static String FRIEND_NEARBY = BASE_URL + "/api/social/nearby.do";
    //交友好友列表接口
    public static String FRIENDS = BASE_URL + "/api/social/showFriends.do";
    //交友关注接口
    public static String FRIENDS_FOLLOW = BASE_URL + "/api/social/follow.do";
    //交友动态接口
    public static String FRIEND_NEW = BASE_URL + "/api/social/getdynamic.do";
    //交友我的动态接口
    public static String MY_FRIEND_NEW = BASE_URL + "/api/social/getFriendsDynamic.do";
    //交友好友动态接口
    public static String FRIENDS_NEW = BASE_URL + "/api/social/getConcernedDynamic.do";
    //交友发布动态接口
    public static String SEND_NEW = BASE_URL + "/api/social/publishdynamic.do";
    //交友查看用户信息接口
    public static String FRIEND_INFO = BASE_URL + "/api/social/getinfo.do";
    //交友展示好友接口
    public static String FRIEND_SHOW = BASE_URL + "/api/social/showFriends.do";
    //交友筛选接口
    public static String FRIEND_FRITRATE = BASE_URL + "/api/social/screen.do";
    //交友动态评论
    public static String FRIEND_COMMENT = BASE_URL + "/api/social/saveComment.do";
    //交友保存答案
    public static String ANSWER_SAVE = BASE_URL + "/api/social/saveAnswer.do";
    //交友展示好友请求列表
    public static String SHOW_FRIEND_ADD = BASE_URL + "/api/user/news/pglist.do";
    //交友删除好友
    public static String DELETE_FRIEND = BASE_URL + "/api/social/deleteFriend.do";
    //交友通过好友请求
    public static String ACCEPT_FRIEND = BASE_URL + "/api/social/becomeFriend.do";
    //交友拒绝好友请求
    public static String REJECT_FRIEND = BASE_URL + "/api/social/deleteAnswer.do";
    //交友动态点赞
    public static String FRIEND_NEW_ZAN = BASE_URL + "/api/social/likedynamic.do";
    //判断用户是否设置问题
    public static String JUDGE_SET_QUESTIONS = BASE_URL + "/api/social/judgeSetques.do";
    //交友获取特定好友的动态
    public static String GET_ONES_DYNAMIC = BASE_URL + "/api/social/getonesdynamic.do";
    //交友查看单个动态
    public static String GET_SINGLE_DYNAMIC = BASE_URL + "/api/social/getSingleDynamic.do";
    //交友获取所有好友的动态
    public static String GET_FRIENDS_DYNAMIC = BASE_URL + "/api/social/getFriendsDynamic.do";
    
    //------------------课程表相关接口--------------------
    //中南课程表获取验证码
    public static String ZHONGNAN_VERIFICATION_CODE = BASE_URL + "/api/account/getSch.do";
    //中南登录验证码验证
    public static String ZHONGNAN_VERIFICATION = BASE_URL + "/api/account/getCourseVerification.do";
    //中南获取课程表
    public static String ZHONGNAN_TIME_TABLE = BASE_URL + "/api/account/getCourse.do";
    //通过周数获取课程表
    public static String WEEK_TIME_TABLE = BASE_URL + "/api/account/getWeekCourse.do";

    //获取知识付费推荐列表
    public static String KNOWLEDGE_RECOMMEND = BASE_URL + "/api/payforkownledge/passage/feed.do";
    public static String ARTICLE_DETAIL = BASE_URL + "/api/payforkownledge/passage/get.do";
    public static String GET_SUBSCRIPTION = BASE_URL + "/api/payforkownledge/subscription/getMy.do";
    public static String COLUMN_DETAIL = BASE_URL + "/api/payforkownledge/specialColumn/get.do";
    public static String UNSUBSCRIBE = BASE_URL + "/api/payforkownledge/subscription/unSub.do";
    public static String MY_ARTICLES = BASE_URL + "/api/payforkownledge/passage/listMy.do";
    public static String MY_ARTICLE_DETAIL = BASE_URL + "/api/payforkownledge/passage/get.do";
    public static String MY_COLUMN_LIST = BASE_URL + "/api/payforkownledge/specialColumn/listMy.do";
    public static String MODIFY_ARTICLE = BASE_URL + "/api/payforkownledge/passage/put.do";
    public static String NEW_ARTICLE = BASE_URL + "/api/payforkownledge/passage/addNew.do";
    
    public static final int DEFAULT_PS = 10;

    public static String token;
    public static int uid = 1;

    public static int clubId;       //当前团队id
    public static int creatorId;    //团队创建者id
    public static boolean isJoin;   //是否加入该团队
    public static boolean isAdmin;  //在当前团队是否管理员
    public static String userName;

    public static String timetableToken;

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
