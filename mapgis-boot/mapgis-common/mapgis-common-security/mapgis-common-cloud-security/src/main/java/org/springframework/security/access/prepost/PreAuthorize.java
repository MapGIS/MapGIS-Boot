package org.springframework.security.access.prepost;

import java.lang.annotation.*;

/**
 * 在微服务模式下进行模拟
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuthorize {
    String value();
}
