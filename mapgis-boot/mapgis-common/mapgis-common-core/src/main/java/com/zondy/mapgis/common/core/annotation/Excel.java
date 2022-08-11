package com.zondy.mapgis.common.core.annotation;

import com.zondy.mapgis.common.core.utils.poi.ExcelHandlerAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

/**
 * 自定义导出Excel数据注解
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {
    /**
     * 导出时在excel中排序
     */
    public int sort() default Integer.MAX_VALUE;

    /**
     * 导出到Excel中的名字.
     */
    public String name() default "";

    /**
     * 日期格式, 如: yyyy-MM-dd
     */
    public String dateFormat() default "";

    /**
     * 如果是字典类型，请设置字典的type值 (如: sys_user_sex)
     */
    public String dictType() default "";

    /**
     * 读取内容转表达式 (如: 0=男,1=女,2=未知)
     */
    public String readConverterExp() default "";

    /**
     * 分隔符，读取字符串组内容
     */
    public String separator() default ",";

    /**
     * BigDecimal 精度 默认:-1(默认不开启BigDecimal格式化)
     */
    public int scale() default -1;

    /**
     * BigDecimal 舍入规则 默认:BigDecimal.ROUND_HALF_EVEN
     */
    public int roundingMode() default BigDecimal.ROUND_HALF_EVEN;

    /**
     * 导出时在excel中每个列的高度 单位为字符
     */
    public double height() default 14;

    /**
     * 导出时在excel中每个列的宽 单位为字符
     */
    public double width() default 16;

    /**
     * 文字后缀,如% 90 变成90%
     */
    public String suffix() default "";

    /**
     * 当值为空时,字段的默认值
     */
    public String defaultValue() default "";

    /**
     * 提示信息
     */
    public String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容.
     */
    public String[] combo() default {};

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写.
     */
    public boolean isExport() default true;

    /**
     * 另一个类中的属性名称,支持多级获取,以小数点隔开
     */
    public String targetAttr() default "";

    /**
     * 是否自动统计数据,在最后追加一行统计数据总和
     */
    public boolean isStatistics() default false;

    /**
     * 导出类型（0数字 1字符串）
     */
    public ColumnType cellType() default ColumnType.STRING;

    /**
     * 导出字体颜色
     */
    public IndexedColors color() default IndexedColors.BLACK;

    /**
     * 导出字段对齐方式
     */
    public HorizontalAlignment align() default HorizontalAlignment.CENTER;

    /**
     * 自定义数据处理器
     */
    public Class<?> handler() default ExcelHandlerAdapter.class;

    /**
     * 自定义数据处理器参数
     */
    public String[] args() default {};

    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     */
    Type type() default Type.ALL;

    public enum Type {
        ALL(0), EXPORT(1), IMPORT(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    public enum ColumnType {
        NUMERIC(0), STRING(1), IMAGE(2);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    /**
     * copy from org.apache.poi.ss.usermodel,解除该注解对poi的强依赖
     */
    public enum HorizontalAlignment {
        GENERAL,
        LEFT,
        CENTER,
        RIGHT,
        FILL,
        JUSTIFY,
        CENTER_SELECTION,
        DISTRIBUTED;

        private HorizontalAlignment() {
        }

        public short getCode() {
            return (short) this.ordinal();
        }

        public static HorizontalAlignment forInt(int code) {
            if (code >= 0 && code < values().length) {
                return values()[code];
            } else {
                throw new IllegalArgumentException("Invalid HorizontalAlignment code: " + code);
            }
        }
    }

    /**
     * copy from org.apache.poi.ss.usermodel,解除该注解对poi的强依赖
     */
    public enum IndexedColors {
        BLACK1(0),
        WHITE1(1),
        RED1(2),
        BRIGHT_GREEN1(3),
        BLUE1(4),
        YELLOW1(5),
        PINK1(6),
        TURQUOISE1(7),
        BLACK(8),
        WHITE(9),
        RED(10),
        BRIGHT_GREEN(11),
        BLUE(12),
        YELLOW(13),
        PINK(14),
        TURQUOISE(15),
        DARK_RED(16),
        GREEN(17),
        DARK_BLUE(18),
        DARK_YELLOW(19),
        VIOLET(20),
        TEAL(21),
        GREY_25_PERCENT(22),
        GREY_50_PERCENT(23),
        CORNFLOWER_BLUE(24),
        MAROON(25),
        LEMON_CHIFFON(26),
        LIGHT_TURQUOISE1(27),
        ORCHID(28),
        CORAL(29),
        ROYAL_BLUE(30),
        LIGHT_CORNFLOWER_BLUE(31),
        SKY_BLUE(40),
        LIGHT_TURQUOISE(41),
        LIGHT_GREEN(42),
        LIGHT_YELLOW(43),
        PALE_BLUE(44),
        ROSE(45),
        LAVENDER(46),
        TAN(47),
        LIGHT_BLUE(48),
        AQUA(49),
        LIME(50),
        GOLD(51),
        LIGHT_ORANGE(52),
        ORANGE(53),
        BLUE_GREY(54),
        GREY_40_PERCENT(55),
        DARK_TEAL(56),
        SEA_GREEN(57),
        DARK_GREEN(58),
        OLIVE_GREEN(59),
        BROWN(60),
        PLUM(61),
        INDIGO(62),
        GREY_80_PERCENT(63),
        AUTOMATIC(64);

        private static final IndexedColors[] _values = new IndexedColors[65];
        public final short index;

        private IndexedColors(int idx) {
            this.index = (short) idx;
        }

        public short getIndex() {
            return this.index;
        }

        public static IndexedColors fromInt(int index) {
            if (index >= 0 && index < _values.length) {
                IndexedColors color = _values[index];
                if (color == null) {
                    throw new IllegalArgumentException("Illegal IndexedColor index: " + index);
                } else {
                    return color;
                }
            } else {
                throw new IllegalArgumentException("Illegal IndexedColor index: " + index);
            }
        }

        static {
            IndexedColors[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                IndexedColors color = var0[var2];
                _values[color.index] = color;
            }

        }
    }
}
