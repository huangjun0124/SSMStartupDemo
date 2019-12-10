package com.wishuok.controller;

import com.wishuok.config.KeyConfig;
import com.wishuok.dto.ApiResult;
import com.wishuok.dto.EnDecodeModel;
import com.wishuok.utils.EncodeDecodeHelper;
import com.wishuok.utils.JsonHelper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api
public class EnDecodeController {

    @RequestMapping(value = "/encode", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult encode(@RequestBody EnDecodeModel inParam){
        if(inParam == null) {
            return ApiResult.Fail(-1,"输入参数不正确");
        }
        inParam.setUserkey(KeyConfig.EnDecKey);
        String inStr = JsonHelper.toJson(inParam);
        String encoded = EncodeDecodeHelper.encode(inStr);
        return ApiResult.Success("加密成功", encoded);
    }

    @RequestMapping(value = "/decode", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult decode(@RequestBody String encodedStr){
        if(StringUtils.isBlank(encodedStr)){
            return ApiResult.Fail(-1,"输入参数不正确");
        }
        String decodeStr = EncodeDecodeHelper.decode(encodedStr);
        if(StringUtils.isBlank(decodeStr)){
            return ApiResult.Fail(-1,"输入参数不正确");
        }
        EnDecodeModel decode = JsonHelper.toObject(decodeStr, EnDecodeModel.class);
        return ApiResult.Success("解密成功", decode.getCommand());
    }
}
