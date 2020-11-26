package jp.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();
        Properties properties = new Properties();
        // 图片边框
        properties.setProperty("kaptcha.border", "no");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 图片宽
        properties.setProperty("kaptcha.image.width", "118");
        // 图片高
        properties.setProperty("kaptcha.image.height", "36");

        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial, Courier, 楷体, Comic Sans MS");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "33");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "red");

        // 图片样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        // 去干扰线
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        // session key
        properties.setProperty("kaptcha.session.key", "KAPTCHA_SESSION_KEY");
        // 文字间隔
        properties.setProperty("kaptcha.textproducer.char.space", "5");


        //properties.setProperty("kaptcha.noise.color", "35,37,38");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
