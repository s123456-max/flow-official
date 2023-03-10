package com.alexmisko.util;

import com.alexmisko.config.ConditionException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;

import java.util.Random;

public class SendSmsCodeUtil {
    public static String sendSmsCode(String phoneNumber, String code){
        SendSmsResponse resp = null;
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("AKIDoSyHNert2zqX0GKAmBLIuNiqkJgpjOoz", "FH6lDQK0ThhU2LRJ4bv40mNo271jEVaa");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {phoneNumber};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId("1400655704");
            req.setSignName("春禾计划公众号");
            req.setTemplateId("1356328");

            String[] templateParamSet1 = {code, "10"};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            throw new ConditionException(e.toString());
        }
        return "OK";
    }

    //生成验证码
    public static String generateCode(){
        String str = "0123456789";
        //将字符串转换为一个新的字符数组。
        char[] VerificationCodeArray = str.toCharArray();
        Random random = new Random();
        int count = 0;//计数器
        StringBuilder stringBuilder = new StringBuilder();
        do {
            //随机生成一个随机数
            int index = random.nextInt(VerificationCodeArray.length);
            char c = VerificationCodeArray[index];
            //限制四位不重复数字
            if (stringBuilder.indexOf(c + "") == -1) {
                stringBuilder.append(c);
                //计数器加1
                count++;
            }
            //当count等于4时结束，随机生成四位数的验证码
        } while (count != 4);
        return stringBuilder.toString();
    }
}