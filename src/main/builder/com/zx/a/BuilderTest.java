package com.zx.a;

/**
 * 建造者模式测试类
 */
public class BuilderTest {
    public static void main(String[] args) {
        BuilderObject builderObject = BuilderObject
                .custom("aa", 1)
                .setC(false)
                .build();
    }
}

/**
 * 要建造的对象
 */
class BuilderObject {
    private String a;
    private Integer b;
    private Boolean c;
    private String d;


    //特殊的构造函数，需要传入构造者，并拷贝构造者的参数到自身
    public BuilderObject(Builder builder) {
        this.a = builder.a;
        this.b = builder.b;
        this.c = builder.c;
        this.d = builder.d;
    }
    //调用此方法获取建造者的实例，或者
    //new BuilderObject.Builder(a,b)
    public static Builder custom(String a,Integer b){
        return new Builder(a,b);
    }

    //静态内部类用来作建造者
    public static class Builder {
        //必须参数
        private String a;
        private Integer b;

        //可选参数
        private Boolean c = false;
        private String d = "aaa";

        /**
         * 最终生成的方法
         */
        public BuilderObject build(){
            //将自己传入
            return new BuilderObject(this);
        }

        //构造 建造者 时,传入必须参数
        public Builder(String a,Integer b) {
            this.a = a;
            this.b = b;
        }
        /**
         * set
         */
        public Builder setC(Boolean c) {
            this.c = c;
            return this;
        }
        public Builder setD(String d) {
            this.d = d;
            return this;
        }
    }

}
