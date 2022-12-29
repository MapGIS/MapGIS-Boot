package com.zondy.mapgis.common.captcha.service.impl;

import com.google.code.kaptcha.Producer;
import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.captcha.service.IValidateCodeService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.exception.user.CaptchaException;
import com.zondy.mapgis.common.core.exception.user.CaptchaExpireException;
import com.zondy.mapgis.common.core.exception.user.CaptchaNullException;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.sign.Base64;
import com.zondy.mapgis.common.core.utils.uuid.IdUtils;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码实现处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Service
public class ValidateCodeServiceImpl implements IValidateCodeService {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private ICacheService cacheService;

    /**
     * 生成验证码
     */
    @Override
    public AjaxResult createCaptcha(String captchaType) throws IOException, CaptchaException {
        AjaxResult ajax = AjaxResult.success();

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        cacheService.setCacheObject(verifyKey, code, CacheConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }

    /**
     * 校验验证码
     */
    @Override
    public void checkCaptcha(String code, String uuid) throws CaptchaException {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");

        String captcha = cacheService.getCacheObject(verifyKey);
        cacheService.deleteObject(verifyKey);
        if (StringUtils.isEmpty(code)) {
            throw new CaptchaNullException();
        }
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
