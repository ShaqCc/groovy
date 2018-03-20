package com.bunny.groovy.api;

import com.bunny.groovy.model.GlobalModel;
import com.bunny.groovy.model.GoogleMapLoc;
import com.bunny.groovy.model.MusicianDetailModel;
import com.bunny.groovy.model.MusicianModel;
import com.bunny.groovy.model.OpportunityModel;
import com.bunny.groovy.model.PerformDetail;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.model.UserMainModel;
import com.bunny.groovy.model.VenueApplyModel;
import com.bunny.groovy.model.VenueInViteModel;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.model.VenueOpportunityModel;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.model.WalletBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/6.
 */

public interface ApiService {

    //检查邮箱是否被占用
    @FormUrlEncoded
    @POST("FrontUserController/checkUserAccountRegister")
    Observable<ResultResponse<Object>> checkAccountUsed(@Field("userAccount") String userAccount);

    //发送邮箱验证码
    //userType:角色类型（0-普通用户 1-表演者 2-演出厅）
    @FormUrlEncoded
    @POST("FrontUserController/getChekCodeRegister")
    Observable<ResultResponse> getEmailCheckCode(@Field("userAccount") String userAccount, @Field("userType") String userType);

    //检验邮箱验证码
    @FormUrlEncoded
    @POST("FrontUserController/chekEmailCodeRegister")
    Observable<ResultResponse> chekEmailCodeRegister(@Field("checkCode") String checkCode, @Field("userAccount") String userAccount);

    //表演者用户注册
    @FormUrlEncoded
    @POST("FrontUserController/performerFrontUserRegister")
    Observable<ResultResponse> performerRegister(@Field("userName") String userAccount, @Field("userPwd") String userPwd,
                                                 @Field("telephone") String phone, @Field("userEmail") String email);

    //表演者登录
    @FormUrlEncoded
    @POST("FrontUserController/login")
    Observable<ResultResponse<PerformerUserModel>> performerLogin(@Field("userAccount") String userAccount, @Field("userPwd") String password,
                                                                  @Field("UserType") String userType, @Field("userZone") String userZone);

    //表演者完善信息,上传文件
    @POST("FrontUserController/updatePerformerInfoFirstLogin")
    Observable<ResultResponse<Object>> updatePerformerInfo(@Body RequestBody body);


    //获取表演者个人信息
    @POST("PerformerMeController/getPerformerMeList")
    Observable<ResultResponse<PerformerUserModel>> getPerformerInfo();


    //获取表演者下一个演出
    @POST("PerformerOverviewController/getNextScheduledShow")
    Observable<ResultResponse<List<ShowModel>>> getNextShow();

    //获取表演类型
    @POST("PerformerBasicsController/getPerformTypeListNotLogin")
    Observable<ResultResponse<List<StyleModel>>> getPerformStyle();


    //获取演出厅列表-关键字查询
    @FormUrlEncoded
    @POST("PerformerOverviewController/getVenueListBykeyword")
    Observable<ResultResponse<List<VenueModel>>> getVenueList(@Field("keyword") String keyword);

    //获取表演者列表-关键字查询
    @FormUrlEncoded
    @POST("VenueBookingsController/getPerformerList")
    Observable<ResultResponse<List<PerformerUserModel>>> getPerformerList(@Field("keyword") String keyword);


    //Release Show: 发布演出-待验证演出
    @FormUrlEncoded
    @POST("PerformerOverviewController/addPerformApply")
    Observable<ResultResponse<Object>> releaseShow(@FieldMap Map<String, String> map);

    //Release Show: （演出厅用户）发布演出
    @FormUrlEncoded
    @POST("VenueBookingsController/addPerform")
    Observable<ResultResponse<Object>> releaseVenueShow(@FieldMap Map<String, String> map);


    //获取收藏演出厅记录
    @POST("PerformerMeController/getPerformerMeMyFavoriteV")
    Observable<ResultResponse<List<VenueModel>>> getMyFavorite();


    //获取演出记录
    @POST("PerformerMeController/getPerformerMeShowHistory")
    Observable<ResultResponse<List<ShowModel>>> getHistoryList();


    //登出
    @FormUrlEncoded
    @POST("FrontUserController/loginOut")
    Observable<ResultResponse<Object>> loginOut(@Field("userID") String userId);


    //根据邮编获取经纬度
    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    Observable<GoogleMapLoc> getLocation(@Query("address") String address, @Query("key") String apiKey);


    //    performerID
//    longitude
//    latitude
//    distance
//    performStartDate
//    performEndDate
    //获取表演机会列表-带条件
    @FormUrlEncoded
    @POST("PerformerOverviewController/findPerformerOverviewOpportunityList")
    Observable<ResultResponse<List<OpportunityModel>>> findOpportunityList(@FieldMap Map<String, String> map);


    //检查表演者是否已经申请过这个机会
    @FormUrlEncoded
    @POST("PerformerOverviewController/checkPerformerOpportunity")
    Observable<ResultResponse<Object>> checkPerformerApply(@Field("applyID") String applyID);

    //   venueID
//   performType
//   performStartDate
//   performEndDate
//   performDesc
//   performerID
//   opportunityID
    //申请演出机会
    @FormUrlEncoded
    @POST("PerformerOverviewController/addPerformOpportunityApply")
    Observable<ResultResponse<Observable>> applyOpportunity(@FieldMap Map<String, String> map);

    //获取演出机会申请列表
    @POST("PerformerOverviewController/getPerformerOverviewOpportunityList")
    Observable<ResultResponse<List<ShowModel>>> getApplyOpportunityList();

    //演出厅-获取演出机会申请列表
    @POST("VenueBookingsController/getVenuePerformOpportunityList")
    Observable<ResultResponse<List<VenueOpportunityModel>>> getVenueApplyOpportunityList();

    //获取邀请的列表
    @POST("PerformerOverviewController/getPerformerOverviewInviteList")
    Observable<ResultResponse<List<ShowModel>>> getInviteList();

    //演出厅-获取邀请的列表
    @POST("VenueBookingsController/getVenuePerformInviteList")
    Observable<ResultResponse<List<VenueInViteModel>>> getVenueInviteList();

    //获取待验证演出申请列表
    @POST("PerformerOverviewController/getPerformerOverviewApplyList")
    Observable<ResultResponse<List<ShowModel>>> getReleaseShowList();

    //获取待验证演出申请列表
    @POST("VenueBookingsController/getVenuePerformApplyList")
    Observable<ResultResponse<List<VenueApplyModel>>> getVenueReleaseShowList();

    //获取选中周期内的表演统计
    @FormUrlEncoded
    @POST("PerformerScheduleController/getPerformerScheduleList")
    Observable<String> getScheduleList(@Field("performStartDate") String performStartDate,
                                       @Field("performEndDate") String performEndDate);

    //演出厅用户-获取选中周期内的表演统计
    @FormUrlEncoded
    @POST("VenueScheduleController/getVenuePerformScheduleList")
    Observable<String> getVenuePerformScheduleList(@Field("startDate") String startDate,
                                                   @Field("endDate") String endDate);

    //演出厅个人主页：获取演出厅详细信息
    @FormUrlEncoded
    @POST("PerformerBasicsController/getVenueScheduleList")
    Observable<ResultResponse<VenueModel>> getVenueDetail(@Field("venueID") String venueID);


    //收藏演出厅
    @FormUrlEncoded
    @POST("PerformerBasicsController/collectionVenue")
    Observable<ResultResponse<Object>> collectionVenue(@Field("venueID") String venueID);

    //取消收藏演出厅
    @FormUrlEncoded
    @POST("PerformerBasicsController/cancelCollectionVenue")
    Observable<ResultResponse<Object>> cancelCollectionVenue(@Field("venueID") String venueID);


    //Notification：Invite-同意邀请
    @FormUrlEncoded
    @POST("PerformerOverviewController/agreePerformerOverviewInvite")
    Observable<ResultResponse<Object>> agreePerformerInvite(@Field("inviteID") String inviteID,
                                                            @Field("performType") String performType, @Field("performDesc") String performDesc);

    //Notification：Invite-拒绝邀请
    @FormUrlEncoded
    @POST("PerformerOverviewController/refusePerformerOverviewInvite")
    Observable<ResultResponse<Object>> rejectPerformInvite(@Field("inviteID") String inviteID);

    //Notification：演出厅用户--Invite-拒绝邀请
    @FormUrlEncoded
    @POST("VenueBookingsController/refusePerformApply")
    Observable<ResultResponse<Object>> refusePerformApply(@Field("performID") String performID);

    //Notification：演出厅用户--Invite-同意邀请
    @FormUrlEncoded
    @POST("VenueBookingsController/agreePerformApply")
    Observable<ResultResponse<Object>> agreePerformApply(@Field("performID") String performID);


    //修改用户信息
    @POST("PerformerMeController/updatePerformerMeInfo")
    Observable<ResultResponse<Object>> updateUserProfile(@Body RequestBody body);


    //下载音乐文件
    @GET
    Observable<ResponseBody> downLoadMusic(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> downloadMusicAsync(@Url String url);

    //Wallet：绑定Paypal
    @FormUrlEncoded
    @POST("VenueMeController/bindPaypal")
    Observable<ResultResponse<String>> bindPaypal(@Field("userID") String userID, @Field("paypalAccount") String paypalAccount);

    //Wallet：充值）Paypal
    @FormUrlEncoded
    @POST("PaypalController/userRecharge")
    Observable<ResultResponse<String>> paypalRecharge(@Field("userID") String userID, @Field("amount") String amount,
                                                      @Field("payment_method_nonce") String desc);

    //Wallet：提现申请）
    @FormUrlEncoded
    @POST("PerformerMeController/getWithdrawals")
    Observable<ResultResponse<String>> getWithdrawals(@Field("balance") String balance);

    //交易记录
    @POST("PerformerMeController/getWalletList")
    Observable<ResultResponse<List<WalletBean>>> getWalletList();

    //演出厅用户--获取账单明细记录
    @POST("VenueMeController/getUserTransactionRecord")
    Observable<ResultResponse<List<WalletBean>>> getUserTransactionRecord();


    //获取PayPal 的token
    @FormUrlEncoded
    @POST("PaypalController/getToken")
    Observable<ResultResponse<String>> getPaypalToken(@Field("userID") String userID);


    //推广
    //@Field("userID") String userID, @Field("num") String num,
    //@Field("amount") String amount, @Field("payMethod") String payMethod,
    //@Field("payment_method_nonce") String nouce
    //支付方式（0-paypal 1-余额）
    @FormUrlEncoded
    @POST("PaypalController/buySpotlight")
    Observable<ResultResponse<String>> buySpotlight(@FieldMap Map<String, String> map);

    //演出厅用户--Spotlight-推广表演(已付费)
    @FormUrlEncoded
    @POST("VenueScheduleController/spotlightPerform")
    Observable<ResultResponse<Object>> spotlightPerform(@Field("performID") String performID, @Field("userID") String userID);


    //获取配置参数
    @POST("PaypalController/getGlobalParam")
    Observable<ResultResponse<GlobalModel>> getGlobalParam();


    /**
     * google登录
     **/
    //判断第三方ID是否已经绑定到账户
    @FormUrlEncoded
    @POST("FrontUserController/checkUidNotLogin")
    Observable<ResultResponse<PerformerUserModel>> checkUidNotLogin(@Field("loginType") String loginType,
                                                                    @Field("userType") String userType,
                                                                    @Field("uid") String uid,
                                                                    @Field("userZone") String userZone);

    //第三方登陆
    @FormUrlEncoded
    @POST("FrontUserController/checkAccountNotLogin")
    Observable<ResultResponse<PerformerUserModel>> socialAccountLogin(@Field("loginType") String loginType,
                                                                      @Field("uid") String uid,
                                                                      @Field("userName") String userName,
                                                                      @Field("userAccount") String userAccount,
                                                                      @Field("userType") String userType,
                                                                      @Field("userZone") String userZone);

    //第三方登陆：发送邮箱验证码
    @FormUrlEncoded
    @POST("FrontUserController/sendLoginEmailCodeNotLogin")
    Observable<ResultResponse<Object>> socialSendEmailCode(@Field("email") String email);

    //检查第三方的邮箱验证码
    @FormUrlEncoded
    @POST("FrontUserController/chekLoginEmailCodeNotLogin")
    Observable<ResultResponse<String>> checkSocialEmailCode(@Field("chekCode") String code);

    //反馈
    @FormUrlEncoded
    @POST("PerformerMeController/addFeedbackList")
    Observable<ResultResponse<Object>> addFeedback(@Field("content") String content, @Field("deviceType") String deviceType);


    //发送邮箱验证码
    @FormUrlEncoded
    @POST("VenueMeController/sendEmailCode")
    Observable<ResultResponse<Object>> sendEmailCode(@Field("userID") String userID, @Field("email") String email);

    //Setting：绑定手机号
    @FormUrlEncoded
    @POST("VenueMeController/bindPhone")
    Observable<ResultResponse<Object>> bindPhone(@Field("userID") String userID, @Field("telephone") String telephone);


    //Setting：绑定邮箱）
    @FormUrlEncoded
    @POST("VenueMeController/bindEmail")
    Observable<ResultResponse<Object>> bindEmail(@Field("userID") String userID, @Field("email") String email, @Field("checkCode") String checkCode);


    //编辑演出

    /*
     * @Field("performStartDate") String performStartDate,
     @Field("performEndDate") String performEndDate,
     @Field("performDesc") String performDesc,
     @Field("performType") String performType
     */
    @FormUrlEncoded
    @POST("VenueBookingsController/updatePerform")
    Observable<ResultResponse<Object>> updatePerform(@FieldMap Map<String, String> map);

    //修改是否屏蔽搜索
    @FormUrlEncoded
    @POST("PerformerMeController/updateMaskedSearch")
    Observable<ResultResponse<Object>> updateDiscover(@Field("state") String state);


    //忘记密码发送验证码
    @FormUrlEncoded
    @POST("FrontUserController/sendEmailCodeNotLogin")
    Observable<ResultResponse<Object>> forgetPwdSendCode(@Field("email") String email);


    //忘记密码-检查邮箱验证码
    @FormUrlEncoded
    @POST("FrontUserController/chekForgetPwdEmailCodeNotLogin")
    Observable<ResultResponse<Object>> checkMailCode(@Field("chekCode") String chekCode);


    //忘记密码-新密码保存
    @FormUrlEncoded
    @POST("FrontUserController/updatePasswordNotLogin")
    Observable<ResultResponse<Object>> updatePwd(@Field("userAccount") String userAccount,
                                                 @Field("accountType") String accountType,//账户类型（0-手机号 1-邮箱）
                                                 @Field("newPassword") String newPassword);

    //获取表演者个人信息
    @POST("VenueMeController/getVenueDetailInfo")
    Observable<ResultResponse<PerformerUserModel>> getVenueDetailInfo();

    //注册：演出厅用户注册
    @POST("FrontUserController/venueFrontUserRegister")
    Observable<ResultResponse<Object>> venueRegister(@Body RequestBody body);

    //演出厅：第三方登陆后首次登陆完善表演者信息
    @POST("FrontUserController/updatePerformerInfoFirstLogin")
    Observable<ResultResponse<Object>> updatePerformerInfoFirstLogin(@Body RequestBody body);

    //获取下一场演出信息，包含演出详情
    @POST("VenueBookingsController/getNextPerformInfo")
    Observable<ResultResponse<VenueShowModel>> getNextPerformInfo();

    //发布演出机会
    @FormUrlEncoded
    @POST("VenueBookingsController/addPerformOpportunity")
    Observable<ResultResponse<Object>> addPerformOpportunity(@Field("startDate") String startDate,
                                                             @Field("endDate") String endDate,
                                                             @Field("performDesc") String performDesc);

    //获取表演者列表
    @FormUrlEncoded
    @POST("VenueBookingsController/getSearchPerformerList")
    Observable<ResultResponse<List<PerformerUserModel>>> getSearchPerformerList(@Field("keyword") String keyword,
                                                                                @Field("orderType") String orderType,
                                                                                @Field("performType") String performType);

    //邀请表演者
    @FormUrlEncoded
    @POST("VenueBookingsController/addPerformInvitation")
    Observable<ResultResponse<Object>> addPerformInvitation(@Field("performerID") String performerID,
                                                            @Field("performStartDate") String performStartDate,
                                                            @Field("performEndDate") String performEndDate,
                                                            @Field("invitationDesc") String invitationDesc);

    //表演者个人主页：获取表演者主页信息
    @FormUrlEncoded
    @POST("VenueBasicsController/getSingPerformerDetail")
    Observable<ResultResponse<MusicianDetailModel>> getSingPerformerDetail(@Field("performerID") String performerID,
                                                                           @Field("userID") String userID);
    //表演者个人主页：获取表演者主页信息
    @FormUrlEncoded
    @POST("VenueBasicsController/getSingPerformerDetail")
    Observable<ResultResponse<MusicianDetailModel>> getSingPerformerDetail(@Field("performerID") String performerID);
    //表演者个人主页：举报表演者
    @FormUrlEncoded
    @POST("VenueBasicsController/reportPerformer")
    Observable<ResultResponse<Object>> reportPerformer(@Field("beReportedID") String beReportedID,
                                                       @Field("reporterID") String reporterID,
                                                       @Field("reportContent") String reportContent);

    //表演者个人主页：收藏表演者
    @FormUrlEncoded
    @POST("VenueBasicsController/collectionPerformer")
    Observable<ResultResponse<Object>> collectionPerformer(@Field("performerID") String performerID,
                                                           @Field("userID") String userID);

    //表演者个人主页：取消收藏表演者
    @FormUrlEncoded
    @POST("VenueBasicsController/cancelCollectionPerformer")
    Observable<ResultResponse<MusicianDetailModel>> cancelCollectionPerformer(@Field("performerID") String performerID);

    //表演者个人主页：收藏表演者
    @FormUrlEncoded
    @POST("VenueBasicsController/collectionPerformer")
    Observable<ResultResponse<Object>> collectionPerformer(@Field("performerID") String performerID);

    //表演者个人主页：取消收藏表演者
    @FormUrlEncoded
    @POST("VenueBasicsController/cancelCollectionPerformer")
    Observable<ResultResponse<MusicianDetailModel>> cancelCollectionPerformer(@Field("performerID") String performerID,
                                                                              @Field("userID") String userID);

    //演出厅用户: 表演机会中选择表演者
    @FormUrlEncoded
    @POST("VenueBookingsController/choosePerformer")
    Observable<ResultResponse<Object>> choosePerformer(@Field("applyID") String applyID);

    //查看演出厅营业时间安排
    @POST("VenueMeController/getVenueSchedule")
    Observable<ResultResponse<List<VenueModel.ScheduleListBean>>> getVenueSchedule();

    //查看演出厅营业时间安排
    @FormUrlEncoded
    @POST("VenueMeController/updateScheduleDate")
    Observable<ResultResponse<Object>> updateScheduleDate(@Field("scheduleID") String scheduleID,
                                                          @Field("startDate") String startDate, @Field("endDate") String endDate);

    //修改演出厅是否有服务费状态
    @FormUrlEncoded
    @POST("VenueMeController/updateScheduleIsHaveCharges")
    Observable<ResultResponse<Object>> updateScheduleIsHaveCharges(@Field("scheduleID") String scheduleID,
                                                                   @Field("isHaveCharges") String isHaveCharges);

    //获取历史表演记录
    @POST("VenueMeController/getPerformHistoryList")
    Observable<ResultResponse<List<VenueShowModel>>> getPerformHistoryList();

    //获取收藏的表演者列表
    @FormUrlEncoded
    @POST("VenueMeController/getCollectionPerformerList")
    Observable<ResultResponse<List<PerformerUserModel>>> getCollectionPerformerList(@Field("userID") String userID);

    @POST("VenueMeController/getCollectionPerformerList")
    Observable<ResultResponse<List<MusicianModel>>> getCollectionPerformerList();

    //演播厅个人资料-编辑个人资料
    @POST("VenueMeController/updateVenueInfo")
    Observable<ResultResponse<Object>> updateVenueInfo(@Body RequestBody body);

    //获取表演信息列表
    @POST("UserCenterController/getUserTransactionRecord")
    Observable<ResultResponse<List<WalletBean>>> getNormalUserTransactionRecord();

    //获取表演者个人信息
    @POST("UserCenterController/getUserInfo")
    Observable<ResultResponse<PerformerUserModel>> getUserInfo();

    //获取演出列表-条件查询
    @FormUrlEncoded
    @POST("UserHomeController/getPerformList")
    Observable<ResultResponse<UserMainModel>> getPerformList(@FieldMap Map<String, String> map);

    //获取表演信息列表
    @POST("UserCenterController/getPerformViewList")
    Observable<ResultResponse<List<PerformDetail>>> getPerformViewList();

    //个人资料-编辑个人资料
    @POST("UserCenterController/updateUserInfo")
    Observable<ResultResponse<Object>> updateUserInfo(@Body RequestBody body);

    //评价表演者
    @FormUrlEncoded
    @POST("UserCenterController/evaluatePerformer")
    Observable<ResultResponse<Object>> evaluatePerformer(@Field("performID") String performID,
                                                         @Field("performerStarLevel") String performerStarLevel,
                                                         @Field("evaluateContent") String evaluateContent);

    //打赏表演者
    @FormUrlEncoded
    @POST("PaypalController/rewardPerformer")
    Observable<ResultResponse<Object>> rewardPerformer(@FieldMap Map<String, String> map);

    //获取打赏记录
    @FormUrlEncoded
    @POST("VenueBasicsController/getrewardPerformerRecord")
    Observable<ResultResponse<List<MusicianDetailModel.TransactionRecord>>> getrewardPerformerRecord(@FieldMap Map<String, String> map);

    //普通用户注册
    @FormUrlEncoded
    @POST("FrontUserController/ordinaryFrontUserRegister")
    Observable<ResultResponse> ordinaryFrontUserRegister(@FieldMap Map<String, String> map);
}
