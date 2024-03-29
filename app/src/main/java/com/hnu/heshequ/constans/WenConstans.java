package com.hnu.heshequ.constans;


import com.hnu.heshequ.network.Constants;

public class WenConstans {
    public static String BaseUrl = Constants.base_url;
    //    public static String BaseUrl="http://rxcpe2.natappfree.cc";
    public static String UserInfo = BaseUrl + "/api/user/info.do";
    public static String SendQuestion = BaseUrl + "/api/ask/base/save.do";
    public static String WenwenList = BaseUrl + "/api/ask/base/pglist.do";
    public static String WenwenDetail = BaseUrl + "/api/ask/base/askInfo.do";
    public static String Advantice = BaseUrl + "/api/pub/category/advertisement.do";
    public static String WwLike = BaseUrl + "/api/ask/base/likes.do";
    public static String WwFirst = BaseUrl + "/api/ask/base/pgfirst.do";
    public static String WwSecond = BaseUrl + "/api/ask/base/pgsecond.do";
    public static String WwDisscuss = BaseUrl + "/api/ask/base/comment.do";
    public static String WwDing = BaseUrl + "/api/ask/base/top.do";
    public static String WwDSave = BaseUrl + "/api/ask/base/favorite.do";
    public static String WwJuBao = BaseUrl + "/api/ask/base/inform.do";
    public static String ZcList = BaseUrl + "/api/ask/special/pglist.do";
    public static String ZcDisscuss = BaseUrl + "/api/ask/special/comment.do";
    public static String ZcZan = BaseUrl + "/api/ask/special/likes.do";
    public static String ZcWenZanList = BaseUrl + "/api/ask/special/pgComment.do";
    public static String ZcProblemsList = BaseUrl + "/api/ask/special/pgHotReview.do";
    public static String ZcSendDisscuss = BaseUrl + "/api/ask/special/HotReviewComment.do";
    public static String ZcDeleteDisscuss = BaseUrl + "/api/ask/special/deleteComment.do";
    public static String MyProblems = BaseUrl + "/api/ask/base/myPglist.do";
    public static String MyFoots = BaseUrl + "/api/user/myfootprint.do";
    public static String MySaves = BaseUrl + "/api/user/myfavorite.do";
    public static String InitUserinfo = BaseUrl + "/api/user/info.do";
    //发送私聊消息,查询私聊，删除私聊消息
    public static String SendMessage = BaseUrl + "/api/chat/base/save.do";
    public static String SearchMessage = BaseUrl + "/api/chat/base/pglist.do";
    public static String DeleteMessage = BaseUrl + "/api/chat/base/deleteMychat.do";
    public static String DeleteMessageBox = BaseUrl + "/api/chat/base/deleteMyChatBox.do";
    public static String SearchMessageBox = BaseUrl + "/api/chat/base/myChatBox.do";


    public static String CheckAttention = BaseUrl + "/api/follow/check.do";
    public static String SetAttention = BaseUrl + "/api/follow/save.do";
    public static String SearchAttention = BaseUrl + "/api/follow/pglist.do";
    public static String DelecteAttention = BaseUrl + "/api/follow/deleteMyFollow.do";

    //拉黑
    public static String SetPullTheBlack = BaseUrl + "/api/chat/base/black.do";
    public static String SearchPullTheBlack = BaseUrl + "/api/chat/base/myBlack.do";
    public static String CheckPullTheBlack = BaseUrl + "/api/chat/base/check.do";
    public static String DeletePullTheBlack = BaseUrl + "/api/chat/base/deleteMyBlack.do";


    //密码加团
    public static String CHECKJOINSECRET = BaseUrl + "/api/club/base/judge.do";
    public static String SETJOINSECRET = BaseUrl + "/api/club/base/setpwd.do";
    public static String DELETEJOINSECRET = BaseUrl + "/api/club/base/delpwd.do";
    public static String TEAMJOINSECRET = BaseUrl + "/api/club/base/joinpwd.do";
    public static String CLOSEJOINSECRET = BaseUrl + "/api/club/base/pwdswitch.do";
    public static String JOINWITHNOSECRET = BaseUrl + "/api/club/base/joinjudge.do";
//    public static String token="faa3ff1e-ffcf-413e-b2f6-29b4c78592ec944921d1-12da-4b83-80fe-5ae7a8ecb0dc";

    //二手市场所用到的接口
    // 删除自己商品
    public static String SecondhandGoodDele = BaseUrl + "/api/goods/base/deleteMyGoods.do";
    // 商品详情
    public static String SecondhandGoodsInfo = BaseUrl + "/api/goods/base/goodsInfo.do";
    // 商品推荐
    public static String SecondhandRecommend = BaseUrl + "/api/recommend/recommendateProduct.do";
    // 商品二级分类
    public static String SecondhandClassify = BaseUrl + "/api/catagory1/listCategory";
    //首页商品展示 分类商品展示
    public static String Secondhand = BaseUrl + "/api/goods/base/pglist.do";
    //发布商品接口
    public static String Sendgoods = BaseUrl + "/api/goods/base/save.do";
    //查询标签接口
    public static String SecondhandLabel = BaseUrl + "/api/pub/category/list.do";
    //商品点赞
    public static String Secondgoodlike = BaseUrl + "/api/goods/base/likes.do";
    //商品评论
    public static String Secondgooddiscuss = BaseUrl + "/api/goods/base/comment.do";
    //商品一级评论
    public static String SecondgooddiscussFirst = BaseUrl + "/api/goods/base/pgfirst.do";
    //商品二级评论
    public static String SecondgooddiscussSecond = BaseUrl + "/api/goods/base/pgsecond.do";
    //商品评论点赞
    public static String SecondgooddiscussDing = BaseUrl + "/api/goods/base/top.do";
    //商品收藏
    public static String Secondgoodcollect = BaseUrl + "/api/goods/base/favorite.do";
    //我收藏的商品
    public static String Mycollectgood = BaseUrl + "/api/user/myfavoriteGoods.do";
    //我发布的商品
    public static String Mygood = BaseUrl + "/api/goods/base/myPglist.do";

    //交友
    //交友附近接口
    public static String FriendNear = BaseUrl + "/api/social/nearby.do";
    //交友好友列表接口
    public static String FriendList = BaseUrl + "/api/social/showFriends.do";
    //交友关注接口
    public static String FriendAttention = BaseUrl + "/api/social/follow.do";
    //交友动态接口
    public static String FriendNew = BaseUrl + "/api/social/getdynamic.do";
    //交友我的动态接口
    public static String MyFriendNew = BaseUrl + "/api/social/getFriendsDynamic.do";
    //交友好友动态接口
    public static String FriendsNew = BaseUrl + "/api/social/getConcernedDynamic.do";
    //交友发布动态接口
    public static String SendNew = BaseUrl + "/api/social/publishdynamic.do";
    //交友查看用户信息接口
    public static String FriendInfo = BaseUrl + "/api/social/getinfo.do";
    //交友展示好友接口
    public static String FriendShow = BaseUrl + "/api/social/showFriends.do";
    //交友筛选接口
    public static String FriendFritrate = BaseUrl + "/api/social/screen.do";
    //交友动态评论
    public static String FriendDiscuss = BaseUrl + "/api/social/saveComment.do";
    //交友保存答案
    public static String AnswerSave = BaseUrl + "/api/social/saveAnswer.do";
    //交友展示好友请求列表
    public static String ShowFriendAdd = BaseUrl + "/api/user/news/pglist.do";
    //交友删除好友
    public static String DeleteFriend = BaseUrl + "/api/social/deleteFriend.do";
    //交友通过好友请求
    public static String AcceptFriend = BaseUrl + "/api/social/becomeFriend.do";
    //交友拒绝好友请求
    public static String RejectFriend = BaseUrl + "/api/social/deleteAnswer.do";
    //交友动态点赞
    public static String FriendNewZan = BaseUrl + "/api/social/likedynamic.do";
    //判断用户是否设置问题
    public static String JudgeSetques = BaseUrl + "/api/social/judgeSetques.do";
    //交友获取特定好友的动态
    public static String GetonesDynamic = BaseUrl + "/api/social/getonesdynamic.do";
    //交友查看单个动态
    public static String GetSingleDynamic = BaseUrl + "/api/social/getSingleDynamic.do";
    //交友获取所有好友的动态
    public static String GetFriendDynamic = BaseUrl + "/api/social/getFriendsDynamic.do";
    //课程表相关接口
    //中南课程表获取验证码
    public static String ZhongnanGetTimetable = BaseUrl + "/api/account/getSch.do";
    //中南登录验证码验证
    public static String ZhongnanGetYanzheng = BaseUrl + "/api/account/getCourseVerification.do";
    //中南获取课程表
    public static String ZhongnanTimetable = BaseUrl + "/api/account/getCourse.do";
    //通过周数获取课程表
    public static String WeekTimetable = BaseUrl + "/api/account/getWeekCourse.do";

    //获取知识付费推荐列表
    public static String getKnowledgeRecommend = BaseUrl + "/api/payforkownledge/passage/feed.do";
    public static String getArticleDetail = BaseUrl + "/api/payforkownledge/passage/get.do";
    public static String getSubscription = BaseUrl + "/api/payforkownledge/subscription/getMy.do";
    public static String getColumnDetail = BaseUrl + "/api/payforkownledge/specialColumn/get.do";
    public static String unsubscribe = BaseUrl + "/api/payforkownledge/subscription/unSub.do";
    public static String getMyArticle = BaseUrl + "/api/payforkownledge/passage/listMy.do";
    public static String getMyArticleDetail = BaseUrl + "/api/payforkownledge/passage/get.do";
    public static String getMyColumnList = BaseUrl + "/api/payforkownledge/specialColumn/listMy.do";
    public static String midifyArticle = BaseUrl + "/api/payforkownledge/passage/put.do";
    public static String newArticle = BaseUrl + "/api/payforkownledge/passage/addNew.do";
    public static String token;
    public static String Timetabletoken;
    public static boolean hasChanged;
    public static int id;
    public static int Timetableid;
}
